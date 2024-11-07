package unibo.wisFacade;

import org.springframework.stereotype.Component;

import unibo.basicomm23.utils.CommUtils;

import org.eclipse.paho.mqttv5.client.IMqttDeliveryToken;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttClientException;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.springframework.beans.factory.annotation.Autowired;

public class MqttFacadeClient {

    private MqttClient client;
    private String mytopic;
    private int MAX_RETRIES = 10;

    public MqttFacadeClient(ApplguiCore guiCore, String brokerURL, String clientID, String topic) throws MqttException {
    	for(int i = 0; i<MAX_RETRIES; i++) {
    		try {
    			MqttClient client = new MqttClient(brokerURL, clientID);
                MqttConnectionOptions options = new MqttConnectionOptions();

                client.setCallback(new MqttCallback() {
                    public void connectComplete(boolean reconnect, String serverURI) {
                        CommUtils.outgreen("connected to: " + serverURI);
                    }

                    public void disconnected(MqttDisconnectResponse disconnectResponse) {
                        CommUtils.outred("disconnected: " + disconnectResponse.getReasonString());
                    }

                    public void deliveryComplete(IMqttToken token) {
                        CommUtils.outgreen("deliveryComplete: " + token.isComplete());
                    }

                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                    	CommUtils.outcyan("Message arrived. Topic: " + topic + " Message: " + new String(message.getPayload()));
                        guiCore.handleMsgFromMqttBroker(message);
                    }

                    public void mqttErrorOccurred(MqttException exception) {
                        CommUtils.outred("mqttErrorOccurred: " + exception.getMessage());
                    }
                    public void authPacketArrived(int reasonCode, MqttProperties properties) {
                        CommUtils.outcyan("authPacketArrived");
                    }

                });

                client.connect(options);

                client.subscribe(topic, 1);
    		} catch(MqttException e) {
    			continue;
    		}
    	}
    }
    public void sendMessage(String messageContent) throws MqttException {
        MqttMessage message = new MqttMessage(messageContent.getBytes());
        message.setQos(1); 
        client.publish(mytopic, message);
        System.out.println("Message published: " + messageContent);
    }
}
