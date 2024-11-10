package unibo.wisFacade;
import java.util.List;

import org.eclipse.paho.mqttv5.common.MqttMessage;

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

    public void handleMsgFromMqttBroker(MqttMessage messagemqtt) {
    	String message = new String(messagemqtt.getPayload());
    	String payload = CommUtils.getContent(message);
    	wsHandler.sendToAll(payload);
    }
    
}