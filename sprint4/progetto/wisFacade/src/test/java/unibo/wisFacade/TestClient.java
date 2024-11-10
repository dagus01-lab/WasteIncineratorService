package unibo.wisFacade;

import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ProtocolType;
import unibo.basicomm23.utils.ApplAbstractObserver;
import unibo.basicomm23.utils.CommUtils;
import unibo.basicomm23.utils.ConnectionFactory;
import unibo.basicomm23.ws.WsConnection;

public class TestClient extends ApplAbstractObserver {
    private Interaction clientConn;

    public TestClient(){
        clientConn = ConnectionFactory.createClientSupport(
                ProtocolType.ws, "localhost:8080", "/accessgui");
        ((WsConnection)clientConn ).addObserver(this);
    }

    @Override
    public void update(String s) {
        CommUtils.outblue("TestClient update=" + s);
    }
}
