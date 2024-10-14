package unibo.wisFacade;

import org.springframework.stereotype.Component;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class MqttFacadeClient {

    private MqttClient client;
    private String mytopic;
    private int MAX_RETRIES = 10;

    @Autowired
    public MqttFacadeClient(ApplguiCore guiCore, String brokerURL, String clientID, String topic) throws MqttException {
    	for(int i = 0; i<MAX_RETRIES; i++) {
    		try {
    			client = new MqttClient(brokerURL, clientID);
                this.mytopic = topic;
                // Set a callback handler, similar to CoapHandler in CoAP
                client.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {
                        System.out.println("Connection lost: " + cause.getMessage());
                        //guiCore.updateMsg("MqttObserver ERROR");
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        System.out.println("Message arrived. Topic: " + topic + " Message: " + new String(message.getPayload()));
                        guiCore.handleMsgFromMqttBroker(message);

                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {
                        System.out.println("Message delivery complete for token: " + token.isComplete());
                    }
                });
                client.connect();
                client.subscribe(topic);
                break;
    		}
    		catch(MqttException e) {
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
