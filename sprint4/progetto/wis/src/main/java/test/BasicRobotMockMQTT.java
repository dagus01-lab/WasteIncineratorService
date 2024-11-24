package main.java.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.mqtt.MqttConnection;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.ConnectionFactory;

public class BasicRobotMockMQTT {
	private Interaction connSupport ; 
	public BasicRobotMockMQTT() {
		try {
			while( connSupport == null ) {
					connSupport = ConnectionFactory.createClientSupport(ProtocolType.mqtt, "tcp://localhost:1883", "basicrobot");
					CommUtils.outcyan("testWISSystem another connect attempt ");
					Thread.sleep(1000);
				}
				CommUtils.outcyan("CONNECTED to mqtt broker " + connSupport);
				((MqttConnection)connSupport).trace=true;
				((MqttConnection)connSupport).setupConnectionForAnswer("robotevents");
				((MqttConnection)connSupport).subscribe("robotevents");
				((MqttConnection)connSupport).subscribe("basicrobot", "robotevents");
				String message = "";
		        while((message = connSupport.receiveMsg())!= null) {
		        	if ( message.contains("engage")) {               
		                IApplMessage response = CommUtils.buildReply("basicrobot", "engagedone", "engagedone(0)", "oprobot"); //oprobot does not receive it
		                //IApplMessage response = CommUtils.buildDispatch("basicrobot", "engagedone", "engagedone(1)", "oprobot"); //this actually works!!
		     			CommUtils.outgreen("basicrobotmock | engaged!");
		                ((MqttConnection)connSupport).publish("answ_engage_oprobot", response.toString());
		     			CommUtils.delay(1000);
		        	}
		        	else if(message.contains("moverobot")){
		                IApplMessage response = CommUtils.buildReply("basicrobot", "moverobotdone", "moverobotdone(1)", "oprobot");
		     			((MqttConnection)connSupport).publish("robotevents", response.toString());
		        	}
		        }
			
		} catch(Exception e) {
			CommUtils.outred("Exception occurred: "+e);
		}
		
	}
}
