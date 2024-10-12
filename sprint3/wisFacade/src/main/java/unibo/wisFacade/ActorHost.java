package unibo.wisFacade;

import unibo.basicomm23.coap.CoapConnection;

public class ActorHost {
	public  String qakSysHost;
    public  String qakSysCtx;
    public  String applActorName;
    public  String ctxportStr;
    public  int ctxport;
    public  String facadeportStr;
    public  int facadeport;
    public  String appName;
    public CoapObserver obs;
    public CoapConnection coapConn;
	public ActorHost(String qakSysHost, String qakSysCtx, String applActorName, String ctxportStr, int ctxport,
			String facadeportStr, int facadeport, String appName) {
		super();
		this.qakSysHost = qakSysHost;
		this.qakSysCtx = qakSysCtx;
		this.applActorName = applActorName;
		this.ctxportStr = ctxportStr;
		this.ctxport = ctxport;
		this.facadeportStr = facadeportStr;
		this.facadeport = facadeport;
		this.appName = appName;
	}
	
	
}
