package unibo.wisFacade;

import alice.tuprolog.Prolog;
import alice.tuprolog.SolveInfo;
import alice.tuprolog.Theory;
import unibo.basicomm23.utils.CommUtils;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
readConfig invocato da CustomContainer

setup() e getActorNamesInApplCtx() invocati da
  ApplguiCore create da FacadeBuilder
 */
public class ApplSystemInfo {
	public static String brokerProto;
	public static String brokerHost;
	public static String brokerPortStr;
	public static int brokerPort;
	public static String brokerURL;
	public static String clientID;
	public static String topic;
	public static String appName = "wisFacade";
	public static String facadePortStr;
	public static int facadeport;

    /*
    facadeConfig.json

    {"brokerProto":<proto>, "brokerHost":<host>, "brokerPort":<port>, "clientID":<cliID>, "topic":<topic> }
     */
    public static void readConfig(){
        List<String> config = MQTTConfigSupport.readConfig("facadeConfig.json");
        if( config != null ) {
            brokerProto    = config.get(0);
            brokerHost    = config.get(1);
            brokerPortStr     = config.get(2);
            clientID = config.get(3);
            topic = config.get(4);
            facadePortStr = config.get(5);
            facadeport = Integer.parseInt(facadePortStr);
            brokerPort       = Integer.parseInt(brokerPortStr);
            brokerURL = brokerProto+"://"+brokerHost+":"+brokerPortStr;
            CommUtils.outmagenta("ApplConfig: {url:"+brokerURL+", clientID:"+clientID+", topic:"+topic+"}");
        }
    }
}
