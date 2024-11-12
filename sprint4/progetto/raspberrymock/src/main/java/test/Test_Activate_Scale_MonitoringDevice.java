package main.java.test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.mqtt.MqttConnection;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.ColorsOut;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.ConnectionFactory;

public class Test_Activate_Scale_MonitoringDevice {

	private static Interaction connSupport;
	private static Process procRaspberry;
	private static Process procBroker;

	//Metodo di supporto per mostrare l'output dei messaggi a colori
	public static void showOutput(Process proc, String color){
		new Thread(){
			public void run(){
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
				try {
					ColorsOut.outappl("Here is the standard output of the command:\n", color);
					while (true){
						String s = stdInput.readLine();
						if ( s != null ) ColorsOut.outappl( s, color );
					}
				}catch (IOException e) {
//					e.printStackTrace();
					try {
						stdInput.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}.start();
	}

	//Metodo per attivare la scale
	public static void activateRaspberryMock() {
		Thread th = new Thread(){
			public void run(){
				CommUtils.outmagenta("TestMonitoringDevice ActivateMonitoringDevice");
				try {
					//procWIS = Runtime.getRuntime().exec("./src/main/java/test/WISexec.bat");
					procRaspberry= Runtime.getRuntime().exec("./gradlew run");
					showOutput(procRaspberry,ColorsOut.GREEN);
				} catch ( Exception e) {
					CommUtils.outred("TestRaspberry ActivateRaspberry ERROR " + e.getMessage());
				}
			}
		};
		th.start();
	}
	public static void activateBroker() {
		Thread th = new Thread(){
			public void run(){
				CommUtils.outmagenta("TestRaspberry ActivateBroker");
				try {
					//procBroker = Runtime.getRuntime().exec("mosquitto -p 8081");
					procBroker = Runtime.getRuntime().exec("/usr/sbin/mosquitto -p 8081");
					showOutput(procBroker,ColorsOut.GREEN);
				} catch ( Exception e) {
					CommUtils.outred("TestRaspberry ActivateBroker ERROR " + e.getMessage());
				}
			}
		};
		th.start();
	}

	@BeforeClass
	//Metodo per attivare il sistema complessivo, prima di partire con i veri test
	public static void activate() {
		CommUtils.outmagenta("TestRaspberry activate ");
		/*activateBasicRobot();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		activateBroker();
		activateRaspberryMock();
		CommUtils.delay(2000);
	}
	/*
	 * After execution
	 */
	@AfterClass
	//Metodo per disattivare tutto, una volta completati i vari test
	public static void down() {
		CommUtils.outmagenta("end of test ");
		//procBasicRobot.destroy();
		procRaspberry.destroy();
		procBroker.destroy();
		/*try {
			CommUtils.outcyan("end of test - taskkill /F /IM java.exe");
			Runtime.getRuntime().exec("taskkill /F /IM java.exe");
		} catch (IOException e) {
				e.printStackTrace();
		}*/
	}

		@Test(timeout=10000)
		public void testMonitoringDeviceActivation() {
			try {
	 			Thread.sleep(2000);
	  			 CommUtils.outmagenta("testMonitoringDeviceActivation ======================================= ");
				while( connSupport == null ) {
	 				connSupport = ConnectionFactory.createClientSupport(ProtocolType.mqtt, "tcp://localhost:8081", "wistester");
	 				CommUtils.outcyan("testRaspberry | another connect attempt ");
	 				Thread.sleep(1000);
	 			}
	 			CommUtils.outcyan("CONNECTED to mqtt broker " + connSupport);
	 			((MqttConnection)connSupport).trace=true;
	 			((MqttConnection)connSupport).setupConnectionForAnswer("wisinfo");
	 			((MqttConnection)connSupport).subscribe("wisinfo");
	 			
	 			Interaction clientConnSupport = null;
	 			while( clientConnSupport == null ) {
	 				clientConnSupport = ConnectionFactory.createClientSupport(ProtocolType.tcp, "localhost", "8100");
	 				CommUtils.outcyan("testRaspberry | another connect attempt ");
	 				Thread.sleep(1000);
	 			}
	 			IApplMessage msg = CommUtils.buildDispatch("raspberrymocktester", "ashStorageState", "ashStorageState(EMPTY)", "raspberrymockproxy");
	 			clientConnSupport.forward(msg);
	 			String message = "";
	 			while(!(message.contains("statoAshStorage"))) {
	 				message = connSupport.receive().msgContent();
	 			}
	 			assertEquals(message, "statoAshStorage(0,100)");
			} catch (Exception e) {
				CommUtils.outred("testWIS ERROR " + e.getMessage());
				fail("testRequest " + e.getMessage());
			}
		}
		@Test(timeout=10000)
		public void testScaleActivation() {
			try {
	  			 CommUtils.outmagenta("testScaleActivation ======================================= ");
	  			while( connSupport == null ) {
					connSupport = ConnectionFactory.createClientSupport(ProtocolType.mqtt, "tcp://localhost:8081", "wistester");
					CommUtils.outcyan("testWISSystem another connect attempt ");
					Thread.sleep(1000);
				}
	 			((MqttConnection)connSupport).trace=true;
	 			((MqttConnection)connSupport).setupConnectionForAnswer("wisinfo");
	 			((MqttConnection)connSupport).subscribe("wisinfo");
				((MqttConnection)connSupport).subscribe("wisinfo");
				CommUtils.outcyan("CONNECTED to mqtt broker " + connSupport+" and subscribed to wisinfo");
				Interaction clientConnSupport = null;
	 			while( clientConnSupport == null ) {
	 				clientConnSupport = ConnectionFactory.createClientSupport(ProtocolType.tcp, "localhost", "8100");
	 				CommUtils.outcyan("testRaspberry | another connect attempt ");
	 				Thread.sleep(1000);
	 			}
	 			IApplMessage msg = CommUtils.buildDispatch("raspberrymocktester", "wasteStorageState", "wasteStorageState(1)", "raspberrymockproxy");
	 			clientConnSupport.forward(msg);
	 			String message = "";
	 			while(!(message.contains("num_RP"))) {
	 				message = connSupport.receive().msgContent();
	 				CommUtils.outred(message);
	 			}
	 			assertEquals(message, "num_RP(1)");
			} catch (Exception e) {
				CommUtils.outred("testWIS ERROR " + e.getMessage());
				fail("testRequest " + e.getMessage());
			}
		}

}
