package unibo.wisFacade;
import java.util.List;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.utils.CommUtils;

/*
Logica applicativa (domain core) della gui
Creata da ServiceFacadeController usando FacadeBuilder
 */
public class ApplguiCore {
    private   WSHandler wsHandler;

    public ApplguiCore( WSHandler wsHandler ) {
        this.wsHandler = wsHandler;
    }

    public void handleMsgFromMqttBroker(MqttMessage msg) {
    	String message = new String(msg.getPayload());
    	String payload = CommUtils.getContent(message);
    	wsHandler.sendToAll(payload);
    }
    
}