package main.java.test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.mqtt.MqttConnection;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.ColorsOut;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.ConnectionFactory;

public class Test_Scale_Activation {
	private static Interaction connSupport;
	private static Process procScale;
	private static Process procBroker;

	//Metodo di supporto per mostrare l'output dei messaggi a colori
	public static void showOutput(Process proc, String color){
		new Thread(){
			public void run(){
				try {
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
				ColorsOut.outappl("Here is the standard output of the command:\n", color);
				while (true){
					String s = stdInput.readLine();
					if ( s != null ) ColorsOut.outappl( s, color );
				}
				}catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	//Metodo per attivare la scale
	public static void activateScale() {
		Thread th = new Thread(){
			public void run(){
				CommUtils.outmagenta("TestWIS activateWIS");
				try {
					//procWIS = Runtime.getRuntime().exec("./src/main/java/test/WISexec.bat");
					procScale = Runtime.getRuntime().exec("./gradlew run");
					showOutput(procScale,ColorsOut.GREEN);
				} catch ( Exception e) {
					CommUtils.outred("TestScale ActivateScale ERROR " + e.getMessage());
				}
			}
		};
		th.start();
	}
	public static void activateBroker() {
		Thread th = new Thread(){
			public void run(){
				CommUtils.outmagenta("TestScale ActivateBroker");
				try {
					//procBroker = Runtime.getRuntime().exec("mosquitto -p 8081");
					procBroker = Runtime.getRuntime().exec("/usr/sbin/mosquitto -p 8081");
					showOutput(procBroker,ColorsOut.GREEN);
				} catch ( Exception e) {
					CommUtils.outred("TestScale ActivateBroker ERROR " + e.getMessage());
				}
			}
		};
		th.start();
	}

	@BeforeClass
	//Metodo per attivare il sistema complessivo, prima di partire con i veri test
	public static void activate() {
		CommUtils.outmagenta("TestScale activate ");
		/*activateBasicRobot();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		activateBroker();
		activateScale();
	}
	/*
	 * After execution
	 */
	@AfterClass
	//Metodo per disattivare tutto, una volta completati i vari test
	public static void down() {
		CommUtils.outmagenta("end of test ");
		procScale.destroy();
		procBroker.destroy();
		/*try {
			CommUtils.outcyan("end of test - taskkill /F /IM java.exe");
			Runtime.getRuntime().exec("taskkill /F /IM java.exe");
		} catch (IOException e) {
				e.printStackTrace();
		}*/
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
				
				((MqttConnection)connSupport).subscribe("wisinfo");
				CommUtils.outcyan("CONNECTED to mqtt broker " + connSupport+" and subscribed to wisinfo");
	 			String message = "";
	 			while(!(message.contains("num_RP"))) {
	 				message = connSupport.receiveMsg();
	 				CommUtils.outred(message);
	 			}
			} catch (Exception e) {
				CommUtils.outred("testWIS ERROR " + e.getMessage());
				fail("testRequest " + e.getMessage());
			}
		}
}