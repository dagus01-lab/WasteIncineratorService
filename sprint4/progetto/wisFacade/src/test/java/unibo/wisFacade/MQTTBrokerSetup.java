package unibo.wisFacade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import unibo.basicomm23.utils.ColorsOut;
import unibo.basicomm23.utils.CommUtils;

@TestConfiguration
public class MQTTBrokerSetup extends AbstractTestExecutionListener {
    @Override
    public void beforeTestClass(TestContext testContext) {
        activateBroker();
    }

	private static Process procBroker;

	private static void showOutput(Process proc, String color){
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
	public static void activateBroker() {
		Thread th = new Thread() {
			public void run() {
				CommUtils.outmagenta("TestWIS activateBroker");
				try {
					// procWIS = Runtime.getRuntime().exec("./src/main/java/test/WISexec.bat");
					procBroker = Runtime.getRuntime().exec("/usr/sbin/mosquitto -p 8081");
					showOutput(procBroker, ColorsOut.RED);
					CommUtils.outred("MQTTBroker | STARTED");
				} catch (Exception e) {
					CommUtils.outred("TestWIS activateBroker ERROR " + e.getMessage());
				}
			}
		};
		th.start();
	}
}
