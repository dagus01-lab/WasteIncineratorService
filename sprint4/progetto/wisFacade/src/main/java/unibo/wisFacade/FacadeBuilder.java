package unibo.wisFacade;
import org.eclipse.paho.client.mqttv3.MqttException;

import unibo.basicomm23.coap.CoapConnection;
import unibo.basicomm23.utils.CommUtils;

public class FacadeBuilder {
    public static  WSHandler wsHandler;
    public static  ApplguiCore guiCore  ;

    public FacadeBuilder( ){
        create();
    }

    public void create(){
        //C
        wsHandler    = new WSHandler();
        guiCore      = new ApplguiCore(wsHandler); //Injection

        try {
			MqttFacadeClient mqttClient = new MqttFacadeClient(guiCore, ApplSystemInfo.brokerURL, ApplSystemInfo.clientID, ApplSystemInfo.topic);
		} catch (MqttException e) {
			System.out.println("MqttClient: Connection to server failed: "+ e.getMessage());
		} catch(Exception e) {
			System.out.println("Other exception occurred: "+ e.getMessage());
		}
        
    }
}
