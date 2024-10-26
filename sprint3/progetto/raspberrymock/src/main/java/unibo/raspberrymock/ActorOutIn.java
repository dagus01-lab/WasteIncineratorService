package unibo.raspberrymock;


import unibo.basicomm23.coap.CoapConnection;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.msg.ApplMessage;
import unibo.basicomm23.tcp.TcpConnection;
import unibo.basicomm23.utils.CommUtils;


/*
Adapter verso il QActor che fa da facade
Usa il file facadeConfig.json
 */
public class ActorOutIn {
    private ApplguiCore guiCore;
    private WSHandler wsHandler;
    private  Interaction tcpConn;


    public ActorOutIn( WSHandler wsHandler ) {
        try {
            this.wsHandler        = wsHandler;
            tcpConn                = new TcpConnection(ApplSystemInfo.qakSysHost, ApplSystemInfo.ctxport); //(ApplSystemInfo.qakSysHost+":"+ApplSystemInfo.qakSysPort, ApplSystemInfo.qakSysCtx+"/"+ApplSystemInfo.applActorName);
            CommUtils.outyellow("OUTIN | tcp connection established with "+ApplSystemInfo.qakSysHost+":"+ApplSystemInfo.ctxport);
        } catch (Exception e) {
            tcpConn = null;
            CommUtils.outred("OUTIN | creation WARNING: " + e.getMessage());
        }
     }

     //Injection
     public void setGuiCore(ApplguiCore guiCore){
         this.guiCore = guiCore;
     }

     
     public void sendToActor(IApplMessage message ) {
    	try {
    		CommUtils.outyellow("OUTIN | sent dispatch message "+message);
			tcpConn.forward(message);
		} catch (Exception e) {
			CommUtils.outred("OUTIN | Could not send message to actor: "+e.getMessage());
		}
     }
    public void sendToOne(IApplMessage msg){
        wsHandler.sendToOne( msg  );
    }
    public void sendToOne(String msg){ wsHandler.sendToOne( msg  ); }

    public void sendToAll(String msg){ wsHandler.sendToAll( msg  ); }
}