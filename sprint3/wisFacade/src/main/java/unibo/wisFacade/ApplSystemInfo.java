package unibo.wisFacade;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Theory;
import unibo.basicomm23.utils.CommUtils;

/*
readConfig invocato da CustomContainer

setup() e getActorNamesInApplCtx() invocati da
  ApplguiCore create da FacadeBuilder
 */
public class ApplSystemInfo {

    private static Prolog pengine;
    public static String appName;
    public static List<ActorHost> observedHosts;
    /*
    facadeConfig.json

    {"host":"<qakSysHost>", "port":"<ctxportStr>", "context":"<qakSysCtx>",
      "facade": "<applActorName>", "facadeport":"<facadeportStr>",
      "sysdescr":"<appName>" }
     */
    public static void readConfig(){
        List<List<String>> config = QaksysConfigSupport.readConfig("facadeConfig.json");
        if( config != null ) {
        	String qakSysHost, ctxportStr, qakSysCtx, applActorName, facadeportStr, appName;
			int ctxport, facadeport;
        	for(List<String> hostConf : config) {
        		qakSysHost    = hostConf.get(0);
                ctxportStr    = hostConf.get(1);
                qakSysCtx     = hostConf.get(2);
                applActorName = hostConf.get(3);
                facadeportStr = hostConf.get(4);
                appName       = hostConf.get(5);
                ctxport       = Integer.parseInt(ctxportStr);
                facadeport    = Integer.parseInt(facadeportStr);
                observedHosts.add(new ActorHost(applActorName, qakSysCtx, applActorName, ctxportStr, ctxport, facadeportStr, facadeport, appName));
        	}
        }

        //setup();
        //getActorNamesInApplCtx( );
    }

    public  static List<String> getActorNamesInApplCtx( ) {
        //CommUtils.outcyan( "ApplSystemInfo | getActorNames ctx=" + ctx  );
        List<String> actors = getAllActorNames(observedHosts.get(0).qakSysCtx);
        CommUtils.outcyan( "ApplSystemInfo ACTORS ON THE localhost  "  );
        actors.forEach( a -> CommUtils.outcyan( a) );

        return actors;
    }

    public static List<String> getAllActorNames(String ctxName )  {
        try {
            SolveInfo actorNamesSol = pengine.solve("getActorNames(A," + ctxName + ")."  );
            String actorNames = actorNamesSol.getVarValue("A").toString();
            return  Arrays.asList(actorNames.replace("[", "")
                    .replace("]", "").split(","));
        } catch (Exception e) {
            CommUtils.outred("ApplSystemInfo | getAllActorNames");
            return new ArrayList<String>();
        }
    }

    public static void setup() {
        try {
            pengine = new Prolog();
            Theory systemTh = new Theory(new FileInputStream(appName + ".pl"));
            Theory rulesTh  = new Theory(new FileInputStream("sysRules.pl"));
            //CommUtils.outblue("ApplSystemInfo | setup systemTh:\n" + systemTh);
            CommUtils.outblue("" + systemTh);
            pengine.addTheory(systemTh);
            pengine.addTheory(rulesTh);
        } catch (Exception e) {
            CommUtils.outred("ApplSystemInfo | setup ERROR:" + e.getMessage());
        }
    }
}
