package unibo.wisFacade;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach; 
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.mqtt.MqttConnection;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.ApplAbstractObserver;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.ConnectionFactory;
import unibo.basicomm23.ws.WsConnection;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestExecutionListeners(
	    listeners = {MQTTBrokerSetup.class, DependencyInjectionTestExecutionListener.class},
	    mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class WebSocketMqttIntegrationTest{

	private String websocketEndpoint = "accessgui"; // WebSocket endpoint
	private Interaction websocketConnSupport=null;
	private CountDownLatch latch = new CountDownLatch(1);
	
//	@BeforeClass
//	public void setup() throws Exception {
//
//		while (websocketConnSupport == null) {
//			websocketConnSupport = ConnectionFactory.createClientSupport(ProtocolType.ws,
//					"ws://localhost:8080/accessgui", "wistester");
//			CommUtils.outcyan("testWISFacade | another connect attempt ");
//			Thread.sleep(1000);
//		}
//		CommUtils.outgreen("testWISFacade | connected to facade");
//	}

	@Test
	public void testWebSocketReceiveMessageFromMqtt() throws InterruptedException {

		try {
			while (websocketConnSupport == null) {
				websocketConnSupport = ConnectionFactory.createClientSupport(ProtocolType.ws, "localhost:8080", "accessgui");
				CommUtils.outcyan("testWISFacade | another connect attempt ");
				WebsocketObserver obs = new WebsocketObserver(latch, websocketConnSupport);
				Thread.sleep(1000);
			}
			CommUtils.outgreen("testWISFacade | connected to facade");
			Interaction connSupport = null;
			Thread.sleep(2000);
			CommUtils.outmagenta("test_WISFacade_MQTT ======================================= ");
			while (connSupport == null) {
				connSupport = ConnectionFactory.createClientSupport(ProtocolType.mqtt, "tcp://localhost:8081",
						"wistester");
				CommUtils.outcyan("testWISSystem another connect attempt ");
				Thread.sleep(1000);
			}
			CommUtils.outcyan("CONNECTED to mqtt broker " + connSupport);
			((MqttConnection) connSupport).trace = true;
			((MqttConnection) connSupport).setupConnectionForAnswer("wisinfo");
			((MqttConnection) connSupport).subscribe("wisinfo");
			IApplMessage new_RP = CommUtils.buildEvent("wistester", "num_RP", "num_RP(1)");
			((MqttConnection) connSupport).publish("wisinfo", new_RP.toString());

			latch.await(5, TimeUnit.SECONDS);
		} catch( InterruptedException ie) {
			fail("websocket client did not receive message");
		} catch (Exception e) {
			CommUtils.outred("testWISFacade ERROR " + e.getMessage());
			fail("testRequest " + e.getMessage());
		}
	}

	@AfterEach
	public void cleanup() throws Exception {
		websocketConnSupport.close();
	}

	public class WebsocketObserver extends ApplAbstractObserver{
		CountDownLatch latch;
		public WebsocketObserver(CountDownLatch latch, Interaction clientConn) {
	        ((WsConnection)clientConn ).addObserver(this);
			this.latch = latch;
		}
		@Override
		public void update(String value) {
			CommUtils.outblue("WebsocketClientMock | received "+value);
			if(value.contains("num_RP(1)")) {
				latch.countDown();
			}
		}
	}
}

