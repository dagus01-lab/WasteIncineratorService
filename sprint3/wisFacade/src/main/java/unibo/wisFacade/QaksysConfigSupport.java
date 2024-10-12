package unibo.wisFacade;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import unibo.basicomm23.utils.CommUtils;

public class QaksysConfigSupport {

    public static List<List<String>> readConfig( String fName )  {
        try{
            Path p = Paths.get(fName);
            String data = new String( Files.readAllBytes(Paths.get(fName)));
            return readTheContent(data);
        }catch( Exception e){
            try {
                CommUtils.outmagenta("QaksysConfigSupport | readConfig ERROR:" + e.getMessage());
                Path p = Paths.get("../"+fName);
                String data = null;
                data = new String( Files.readAllBytes(Paths.get("../"+fName)));
                return readTheContent(data);
            } catch (Exception e1) {
                CommUtils.outred("QaksysConfigSupport | readConfig ERROR AGAIN:" + e.getMessage());
                //e1.printStackTrace();
            }
        }
        return null;

        /*
        try{
            String data = new String( Files.readAllBytes(Paths.get(fName)));
            return readTheContent(data);
        }catch( Exception e){
            CommUtils.outred("QaksysConfigSupport | readConfig ERROR:" + e.getMessage());
        }
        return null;

         */
    }
    protected static List<List<String>> readTheContent(String config) throws JSONException, ParseException{
    	List<List<String>> content = new ArrayList<List<String>>();
    	CommUtils.outyellow("qaksysConfigSupport | readTheContent " + config);
    	JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(config);
        for (Object o : jsonArray) {
        	content.add(readHost((JSONObject)o));
        }
        return content;
    }

   protected static List<String> readHost( JSONObject jsonObject ) throws JSONException {
        //jsonParser.parse(config) ;
       String host = jsonObject.get("host").toString();
       String port = jsonObject.get("port").toString();
       String context = jsonObject.get("context").toString();
       String actorfacade = jsonObject.get("facade").toString();
       String facadeport = jsonObject.get("facadeport").toString();
       String sysname = jsonObject.get("sysdescr").toString();

       List<String> outS = new ArrayList<String>();
       outS.add(host);
       outS.add(port);
       outS.add(context);
       outS.add(actorfacade);
       outS.add(facadeport);
       outS.add(sysname);
       CommUtils.outyellow("qaksysConfigSupport | readTheContent " + outS.toString());
       return outS;
    }

}
