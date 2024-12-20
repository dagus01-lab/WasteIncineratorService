package main.java.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.ColorsOut;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.ConnectionFactory;

public class TestWISOutput {
	private static Interaction connSupport;
	private static Process procBasicRobot;
	private static Process procWIS;

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

	//Metodo per attivare il BasicRobot
	public static void activateBasicRobot() {
		Thread th = new Thread(){
			public void run(){
				CommUtils.outmagenta("TestWIS activateBasicRobot");
				try {
					//procBasicRobot = Runtime.getRuntime().exec("./src/main/java/test/basicRobotexec.bat");
					procBasicRobot = Runtime.getRuntime().exec("../unibo.basicrobot24/gradlew run");
					showOutput(procBasicRobot,ColorsOut.BLUE);
				} catch ( Exception e) {
					CommUtils.outred("TestWIS activateBasicRobot ERROR " + e.getMessage());
				}
			}
		};
		th.start();
	}

	//Metodo per attivare il WIS
	public static void activateWIS() {
		Thread th = new Thread(){
			public void run(){
				CommUtils.outmagenta("TestWIS activateWIS");
				try {
					//procWIS = Runtime.getRuntime().exec("./src/main/java/test/WISexec.bat");
					procWIS = Runtime.getRuntime().exec("./gradlew run");
					showOutput(procWIS,ColorsOut.GREEN);
				} catch ( Exception e) {
					CommUtils.outred("TestWIS activateWIS ERROR " + e.getMessage());
				}
			}
		};
		th.start();
	}

	@BeforeClass
	//Metodo per attivare il sistema complessivo, prima di partire con i veri test
	public static void activate() {
		CommUtils.outmagenta("TestWIS activate ");
		/*activateBasicRobot();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		activateWIS();
	}
	/*
	 * After execution
	 */
	@AfterClass
	//Metodo per disattivare tutto, una volta completati i vari test
	public static void down() {
		CommUtils.outmagenta("end of test ");
		//procBasicRobot.destroy();
		procWIS.destroy();
		/*try {
			CommUtils.outcyan("end of test - taskkill /F /IM java.exe");
			Runtime.getRuntime().exec("taskkill /F /IM java.exe");
		} catch (IOException e) {
				e.printStackTrace();
		}*/
	}

	/*
	 */
		@Test
		public void testWISSystemOutput() {
			IApplMessage req  = CommUtils.buildRequest( "tester", "info", "info(X)", "wistester");
	 		try {
	 			Thread.sleep(20000);
	  			 CommUtils.outmagenta("testWISSystemOutput ======================================= ");
				while( connSupport == null ) {
	 				connSupport = ConnectionFactory.createClientSupport(ProtocolType.tcp, "localhost", "8125");
	 				CommUtils.outcyan("testWISSystem another connect attempt ");
	 				Thread.sleep(1000);
	 			}
	 			CommUtils.outcyan("CONNECTED to wis " + connSupport);
				IApplMessage reply = connSupport.request(req);
				CommUtils.outmagenta("test burn 1 RP reply="+reply);
				String answer = reply.msgContent();
				assertEquals(answer, "inforeply(1)");
			} catch (Exception e) {
				CommUtils.outred("testWIS ERROR " + e.getMessage());
				fail("testRequest " + e.getMessage());
			}
		}
		@Test
		public void testWISAshStorageFull() {
			IApplMessage reqAshStorageFull  = CommUtils.buildDispatch( "tester", "statoAshStorage", "statoAshStorage(1)", "wis");
			IApplMessage reqNewRPArrived  = CommUtils.buildRequest( "tester", "info", "info(X)", "wistester");

	 		try {
	  			 CommUtils.outmagenta("testWISSystemAshStorageFull ======================================= ");
				while( connSupport == null ) {
	 				connSupport = ConnectionFactory.createClientSupport(ProtocolType.tcp, "localhost", "8125");
	 				CommUtils.outcyan("testWISSystem another connect attempt ");
	 				Thread.sleep(1000);
	 			}
	 			CommUtils.outcyan("CONNECTED to wis " + connSupport);
				connSupport.forward(reqAshStorageFull);
				IApplMessage reply = connSupport.request(reqNewRPArrived);
				CommUtils.outmagenta("test burn 1 RP reply="+reply);
				String answer = reply.msgContent();
				assertEquals(answer, "inforeply(0)");
			} catch (Exception e) {
				CommUtils.outred("testWIS ERROR " + e.getMessage());
				fail("testRequest " + e.getMessage());
			}
		}
}
