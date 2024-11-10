package unibo.wisFacade;

import java.util.concurrent.CountDownLatch;

import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.utils.ApplAbstractObserver;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.ws.WsConnection;

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
