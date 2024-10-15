package unibo.wisFacade;

import com.google.common.base.Charsets;
import org.json.JSONException;

import org.json.JSONObject;
import unibo.basicomm23.utils.CommUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MQTTConfigSupport {

    public static List<String> readConfig( String fName )  {
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

   protected static List<String> readTheContent( String config ) throws JSONException {
       CommUtils.outyellow("qaksysConfigSupport | readTheContent " + config);
       JSONObject jsonObject = new JSONObject(config); //jsonParser.parse(config) ;
       String protocol = jsonObject.get("brokerProto").toString();
       String host = jsonObject.get("brokerHost").toString();
       String port = jsonObject.get("brokerPort").toString();
       String clientID = jsonObject.get("clientID").toString();
       String topic = jsonObject.get("topic").toString();

       List<String> outS = new ArrayList<String>();
       outS.add(protocol);
       outS.add(host);
       outS.add(port);
       outS.add(clientID);
       outS.add(topic);
       CommUtils.outyellow("MQTTConfigSupport | readTheContent " + outS.toString());
       return outS;
    }

}
