package unibo.wisFacade;
import unibo.basicomm23.coap.CoapConnection;
import unibo.basicomm23.utils.CommUtils;

public class FacadeBuilder {
    public static  WSHandler wsHandler;
    public static  ApplguiCore guiCore  ;
    protected   ActorOutIn outinadapter;

    public FacadeBuilder( ){
        create();
    }

    public void create(){
        //C
        wsHandler    = new WSHandler();
        outinadapter = new ActorOutIn( wsHandler );
        guiCore      = new ApplguiCore(outinadapter);
        outinadapter.setGuiCore(guiCore); //Injection
        wsHandler.setGuiCore(guiCore); //Injection

        //CommUtils.outred("FacadeBuilder create wsHandler=" + wsHandler);
//        List<String> config = QaksysConfigSupport.readConfig("facadeConfig.json");
//        if( config != null ) {
        for(ActorHost a: ApplSystemInfo.observedHosts) {
        	String qakSysHost    = a.qakSysHost;
            String ctxportStr    = a.ctxportStr;
            String qakSysCtx     = a.qakSysCtx;
            String applActorName = a.applActorName;

            a.obs = new CoapObserver(guiCore, applActorName);
            String saddr   = qakSysHost + ":" + ctxportStr;
            String resource = qakSysCtx + "/" + applActorName;
            CommUtils.outblue("FacadeBuilder | coapConn : " + saddr + " " + resource);
            a.coapConn = new CoapConnection(qakSysHost + ":" + ctxportStr,
                    qakSysCtx + "/" + applActorName);
            CommUtils.outblue("FacadeBuilder | Stabilita coapConn : " + a.coapConn);
            a.coapConn.observeResource(a.obs);
        }
            
        //}
    }
}
