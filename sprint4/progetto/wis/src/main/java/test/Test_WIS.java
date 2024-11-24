package main.java.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.ColorsOut;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.ConnectionFactory;
import unibo.basicomm23.mqtt.MqttConnection;

public class Test_WIS {
	private static Interaction connSupport;
	private static Process procBroker;
	private static Process procWIS;
	private static BasicRobotMockMQTT basicRobotMock;

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


	//Metodo per attivare il BasicRobot
	public static void activateBasicRobot() {
		Thread th = new Thread(){
			public void run(){
				CommUtils.outmagenta("TestWIS activateBasicRobotMock");
				try {
					basicRobotMock = new BasicRobotMockMQTT();
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
	
	public static void activateBroker() {
		Thread th = new Thread(){
			public void run(){
				CommUtils.outmagenta("TestWIS activateBroker");
				try {
					//procWIS = Runtime.getRuntime().exec("./src/main/java/test/WISexec.bat");
					procBroker = Runtime.getRuntime().exec("/usr/sbin/mosquitto -p 1883");
					showOutput(procBroker,ColorsOut.GREEN);
				} catch ( Exception e) {
					CommUtils.outred("TestWIS activateBroker ERROR " + e.getMessage());
				}
			}
		};
		th.start();
	}

	@BeforeClass
	public static void activateSetup() {
		CommUtils.outyellow("== SETTING UP TEST ENVIRONMENT ==============");
//		activateBroker();
//		CommUtils.delay(1000);
	}
	@Before
	//Metodo per attivare il sistema complessivo, prima di partire con i veri test
	public void activate() {
		CommUtils.outmagenta("TestWIS activate ");
		/*activateBasicRobot();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		activateBroker();
		CommUtils.delay(1000);
		activateBasicRobot();
		CommUtils.delay(2000);
		CommUtils.outyellow("== SETTING UP WIS ==============");
		activateWIS();
		CommUtils.delay(8000);
	}
	/*
	 * After execution
	 */
	@After
	//Metodo per disattivare tutto, una volta completati i vari test
	public void down() {
		CommUtils.outmagenta("end of test ");
		procWIS.destroy();
		procBroker.destroy();
		CommUtils.delay(2000);
		/*try {
			CommUtils.outcyan("end of test - taskkill /F /IM java.exe");
			Runtime.getRuntime().exec("taskkill /F /IM java.exe");
		} catch (IOException e) {
				e.printStackTrace();
		}*/
	}
	
	@AfterClass
	public static void downAll() {
//		procBroker.destroy();
//		basicRobotMock.shutdown();
	}

	
	@Test
	public void test_ArrivedRP_MDfull() {
		boolean testok = false;
		try {
 			Thread.sleep(6000);
  			 CommUtils.outmagenta("test_ArrivedRP_MDfull ======================================= ");
			while( connSupport == null ) {
 				connSupport = ConnectionFactory.createClientSupport(ProtocolType.mqtt, "tcp://localhost:1883", "wistester");
 				CommUtils.outcyan("testWISSystem another connect attempt ");
 				Thread.sleep(1000);
 			}
 			CommUtils.outcyan("CONNECTED to mqtt broker " + connSupport);
 			((MqttConnection)connSupport).trace=true;
 			((MqttConnection)connSupport).setupConnectionForAnswer("wisinfo");
 			((MqttConnection)connSupport).subscribe("wisinfo");
 			IApplMessage monitoringDeviceFull = CommUtils.buildDispatch("wistester", "statoAshStorage", "statoAshStorage(1, 0)", "raspberryinfocontroller");
 			IApplMessage new_RP = CommUtils.buildDispatch("wistester", "num_RP", "num_RP(1)", "raspberryinfocontroller");
 			((MqttConnection)connSupport).publish("wisinfo", monitoringDeviceFull.toString());
 			CommUtils.delay(1000);
 			((MqttConnection)connSupport).publish("wisinfo", new_RP.toString());
 			
 			String message = "";
 			//if I do not receive any message with opRobotState(Going_WASTEIN) the test is successful
 			long startTime = System.currentTimeMillis();
 			long timeout = 5000;
 			while(!message.toLowerCase().contains("going_to_wastein")) {
 				message = connSupport.receiveMsg();
 				CommUtils.outred("wistester | received "+message);
 				
 				if(System.currentTimeMillis()-startTime >= timeout) {
 					testok = true;
 					break;
 				} else if (message.toLowerCase().contains("going_to_wastein")) {
 					break;
 				}
 			}
 			if(testok) {
 				assertTrue(true);
 			} else {
 				fail("wistester | robot has taken RP even though ash storage was full!: "+message.toString());
 			}
 			
		} catch (Exception e) {
			CommUtils.outred("testWIS ERROR " + e.getMessage());
			fail("testRequest " + e.getMessage());
		}
	}
	
	/*
	 */
		@Test(timeout=25000)
		public void test_ArrivedRP_MDok() {
	 		try {
	 			Thread.sleep(6000);
	  			 CommUtils.outmagenta("test_ArrivedRP_MDok ======================================= ");
				while( connSupport == null ) {
	 				connSupport = ConnectionFactory.createClientSupport(ProtocolType.mqtt, "tcp://localhost:1883", "wistester");
	 				CommUtils.outcyan("testWISSystem another connect attempt ");
	 				Thread.sleep(1000);
	 			}
	 			CommUtils.outcyan("CONNECTED to mqtt broker " + connSupport);
	 			((MqttConnection)connSupport).trace=true;
	 			((MqttConnection)connSupport).setupConnectionForAnswer("wisinfo");
	 			((MqttConnection)connSupport).subscribe("wisinfo");
	 			CommUtils.delay(1000);
	 			IApplMessage new_RP = CommUtils.buildDispatch("wistester", "num_RP", "num_RP(1)", "raspberryinfocontroller");
	 			IApplMessage monitoringDeviceEmpty = CommUtils.buildDispatch("wistester", "statoAshStorage", "statoAshStorage(0, 100)", "raspberryinfocontroller");
	 			((MqttConnection)connSupport).publish("wisinfo", monitoringDeviceEmpty.toString());
	 			((MqttConnection)connSupport).publish("wisinfo", new_RP.toString());
	 			
	 			String message = "";
	 			while((message = connSupport.receiveMsg())!=null) {
	 				CommUtils.outgreen("wistester | received "+message);
	 				if(!message.toLowerCase().contains("waiting_for_an_rp")) {
	 					break;
	 				}
	 			}
	 			assertTrue(true);
	 			
			} catch (Exception e) {
				CommUtils.outred("testWIS ERROR " + e.getMessage());
				fail("testRequest " + e.getMessage());
			}
		}
		@Test(timeout=40000)
		public void test_ArrivedRP_MDoff_after_one_round() {
			boolean testok = false;
			try {
	 			Thread.sleep(6000);
	  			 CommUtils.outmagenta("test_ArrivedRP_MDoff_after_one_round ======================================= ");
				while( connSupport == null ) {
	 				connSupport = ConnectionFactory.createClientSupport(ProtocolType.mqtt, "tcp://localhost:1883", "wistester");
	 				CommUtils.outcyan("testWISSystem another connect attempt ");
	 				Thread.sleep(1000);
	 			}
	 			((MqttConnection)connSupport).trace=true;
	 			((MqttConnection)connSupport).setupConnectionForAnswer("wisinfo");
	 			((MqttConnection)connSupport).subscribe("wisinfo");
	 			CommUtils.delay(1000);
	 			CommUtils.outcyan("CONNECTED to mqtt broker " + connSupport);
	 			IApplMessage monitoringDeviceFull = CommUtils.buildDispatch("wistester", "statoAshStorage", "statoAshStorage(1, 0)", "raspberryinfocontroller");
	 			IApplMessage monitoringDeviceEmpty = CommUtils.buildDispatch("wistester", "statoAshStorage", "statoAshStorage(0, 100)", "raspberryinfocontroller");
	 			IApplMessage new_RP = CommUtils.buildDispatch("wistester", "num_RP", "num_RP(1)", "raspberryinfocontroller");
	 			((MqttConnection)connSupport).publish("wisinfo", monitoringDeviceEmpty.toString());
	 			CommUtils.delay(1000);
	 			((MqttConnection)connSupport).publish("wisinfo", new_RP.toString());
	 			String message = "";
	 			while((message = connSupport.receiveMsg())!=null) {
	 				CommUtils.outred("wistester | received "+message);
	 				
	 				if (!message.toLowerCase().contains("waiting_for_an_rp")) {
	 					break;
	 				}
	 			}
	 			CommUtils.outred("wistester | oprobot has done one round");
	 			//if I do not receive any message with opRobotState(Going_WASTEIN) the test is successful
	 			long startTime = System.currentTimeMillis();
	 			long timeout = 2000;
	 			message = "";
	 			while((message = connSupport.receiveMsg())!=null) {
	 				CommUtils.outred("wistester | received "+message);
	 				if(System.currentTimeMillis()-startTime >= timeout) {
	 					testok = true;
	 					break;
	 				} else if (message.toLowerCase().contains("going_to_wastein")) {
	 					break;
	 				}
	 			}
	 			if(testok) {
	 				assertTrue(true);
	 			} else {
	 				fail("wistester | robot has taken RP even though ash storage was full!: "+message.toString());
	 			}
	 			
			} catch (Exception e) {
				CommUtils.outred("testWIS ERROR " + e.getMessage());
				fail("testRequest " + e.getMessage());
			}
		}
		@Test(timeout=60000)
		public void test_ArrivedRP_MDfull_after_one_round() {
			boolean testok = false;
			try {
	 			Thread.sleep(6000);
	  			 CommUtils.outmagenta("test_ArrivedRP_MDfull_after_one_round ======================================= ");
				while( connSupport == null ) {
	 				connSupport = ConnectionFactory.createClientSupport(ProtocolType.mqtt, "tcp://localhost:1883", "wistester");
	 				CommUtils.outcyan("testWISSystem another connect attempt ");
	 				Thread.sleep(1000);
	 			}
	 			((MqttConnection)connSupport).trace=true;
	 			((MqttConnection)connSupport).setupConnectionForAnswer("wisinfo");
	 			((MqttConnection)connSupport).subscribe("wisinfo");
	 			CommUtils.delay(1000);
	 			CommUtils.outcyan("CONNECTED to mqtt broker " + connSupport);
	 			IApplMessage monitoringDeviceFull = CommUtils.buildDispatch("wistester", "statoAshStorage", "statoAshStorage(1, 0)", "raspberryinfocontroller");
	 			IApplMessage monitoringDeviceEmpty = CommUtils.buildDispatch("wistester", "statoAshStorage", "statoAshStorage(0, 100)", "raspberryinfocontroller");
	 			IApplMessage new_RP = CommUtils.buildDispatch("wistester", "num_RP", "num_RP(1)", "raspberryinfocontroller");
	 			((MqttConnection)connSupport).publish("wisinfo", monitoringDeviceEmpty.toString());
	 			CommUtils.delay(1000);
	 			((MqttConnection)connSupport).publish("wisinfo", new_RP.toString());
	 			String message = "";
	 			while((message = connSupport.receiveMsg())!=null) {
	 				CommUtils.outred("wistester | received "+message);
	 				
	 				if  (!message.toLowerCase().contains("waiting_for_an_rp")) {
	 					break;
	 				}
	 			}
	 			CommUtils.delay(15000);
	 			CommUtils.outred("wistester | oprobot has done one round");
	 			((MqttConnection)connSupport).publish("wisinfo", monitoringDeviceFull.toString());
	 			//if I do not receive any message with opRobotState(Going_WASTEIN) the test is successful
	 			long startTime = System.currentTimeMillis();
	 			long timeout = 2000;
	 			message = "";
	 			while((message = connSupport.receiveMsg())!=null) {
	 				CommUtils.outred("wistester | received "+message);
	 				if(System.currentTimeMillis()-startTime >= timeout) {
	 					testok = true;
	 					break;
	 				} else if (message.toLowerCase().contains("going_to_wastein")) {
	 					break;
	 				}
	 			}
	 			if(testok) {
	 				assertTrue(true);
	 			} else {
	 				fail("wistester | robot has taken RP even though ash storage was full!: "+message.toString());
	 			}
	 			
			} catch (Exception e) {
				CommUtils.outred("testWIS ERROR " + e.getMessage());
				fail("testRequest " + e.getMessage());
			}
		}
		@Test(timeout=60000)
		public void test_ArrivedRP_MDok_after_one_round() {
			boolean testok = false;
			try {
	 			Thread.sleep(6000);
	  			 CommUtils.outmagenta("test_ArrivedRP_MDfull_after_one_round ======================================= ");
				while( connSupport == null ) {
	 				connSupport = ConnectionFactory.createClientSupport(ProtocolType.mqtt, "tcp://localhost:1883", "wistester");
	 				CommUtils.outcyan("testWISSystem another connect attempt ");
	 				Thread.sleep(1000);
	 			}
	 			((MqttConnection)connSupport).trace=true;
	 			((MqttConnection)connSupport).setupConnectionForAnswer("wisinfo");
	 			((MqttConnection)connSupport).subscribe("wisinfo");
	 			CommUtils.delay(1000);
	 			CommUtils.outcyan("CONNECTED to mqtt broker " + connSupport);
	 			IApplMessage monitoringDeviceFull = CommUtils.buildDispatch("wistester", "statoAshStorage", "statoAshStorage(1, 0)", "raspberryinfocontroller");
	 			IApplMessage monitoringDeviceEmpty = CommUtils.buildDispatch("wistester", "statoAshStorage", "statoAshStorage(0, 100)", "raspberryinfocontroller");
	 			IApplMessage new_RP = CommUtils.buildDispatch("wistester", "num_RP", "num_RP(1)", "raspberryinfocontroller");
	 			CommUtils.delay(2000);
	 			((MqttConnection)connSupport).publish("wisinfo", monitoringDeviceEmpty.toString());
	 			CommUtils.delay(1000);
	 			((MqttConnection)connSupport).publish("wisinfo", new_RP.toString());
	 			String message = "";
	 			while((message = connSupport.receiveMsg())!=null) {
	 				CommUtils.outred("wistester | received "+message);
	 				
	 				if  (!message.toLowerCase().contains("waiting_for_an_rp")) {
	 					break;
	 				}
	 			}
	 			CommUtils.outred("wistester | oprobot has done one round");
	 			CommUtils.delay(20000);
	 			((MqttConnection)connSupport).publish("wisinfo", monitoringDeviceFull.toString());
	 			((MqttConnection)connSupport).publish("wisinfo", new_RP.toString());
	 			((MqttConnection)connSupport).publish("wisinfo", monitoringDeviceEmpty.toString());
	 			message = "";
	 			while((message = connSupport.receiveMsg())!=null) {
	 				CommUtils.outred("wistester | received "+message);
	 				 if (!message.toLowerCase().contains("waiting_for_an_rp")) {
	 					break;
	 				}
	 			}
	 			
			} catch (Exception e) {
				CommUtils.outred("testWIS ERROR " + e.getMessage());
				fail("testRequest " + e.getMessage());
			}
		}
}
