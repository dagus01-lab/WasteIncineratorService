package unibo.raspberrymock;
import org.json.JSONObject;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.msg.ApplMessage;
import unibo.basicomm23.utils.CommUtils;
import java.util.List;


/*
Logica applicativa (domain core) della gui
Creata da ServiceFacadeController usando FacadeBuilder
 */
public class ApplguiCore {
    private   ActorOutIn outinadapter;
    private String reqid      = "dofibo";           
    private String reqcontent = "dofibo(X)";      
    private  String destActor = "";
    private String statoAshStorage = "";
    private String statoWasteStorage = "";

    public ApplguiCore( ActorOutIn outinadapter ) {
        this.outinadapter = outinadapter;
//        ApplSystemInfo.setup();  //MAY2024  al momento, non si vede PARCHE' 
        destActor         = ApplSystemInfo.applActorName;
    }

    //Chiamato da CoapObserver
    public void handleMsgFromActor(String msg, String requestId) {
        CommUtils.outcyan("AGC | hanldeMsgFromActor " + msg + " requestId=" + requestId) ;
        updateMsg( msg );
    }
    public void handleReplyMsg( String msg) { //IApplMessage msg
        CommUtils.outcyan("AGC | handleReplyMsg " + msg  ) ;
        //Mando la risppsta alla ws-conn del browser che ha fatto la richiesta
        //updateMsg( msg  );
        outinadapter.sendToOne(msg);
    }
    public void handleReplyMsg( IApplMessage msg) {
        CommUtils.outcyan("AGC | handleReplyMsg " + msg  ) ;
        //Mando la risppsta alla ws-conn del browser che ha fatto la richiesta
        outinadapter.sendToOne(msg);
    }
    public void updateMsg( String msg ) {
        CommUtils.outblue("AGC updateMsg " + msg);
        outinadapter.sendToAll(msg);
        //potrei mandare a M2M ... che poi manda la risposta a REST POST
        //M2MController.m2mCtrl.setAnswer(msg);
    }

    public void handleWsMsg(String id, String msg) {
        CommUtils.outcyan("AGC | handleWsMsg msg " + msg  );
        JSONObject jsonMsg = new JSONObject(msg);
        boolean isAshStorageStatus = jsonMsg.has("ashStorageState");
        boolean isWasteStorageStatus = jsonMsg.has("wasteStorageState");
        if( isAshStorageStatus ){
        	String msgPayload = jsonMsg.get("ashStorageState").toString();
        	if(msgPayload != statoAshStorage) {
        		statoAshStorage = msgPayload;
                IApplMessage message = CommUtils.buildDispatch("gui", "ashStorageState", "ashStorageState("+statoAshStorage+")" , ApplSystemInfo.applActorName);
                outinadapter.sendToActor( message );
                outinadapter.sendToAll(jsonMsg.toString());
        	}
            return;
        } else if( isWasteStorageStatus ){
        	String msgPayload = jsonMsg.get("wasteStorageState").toString();
        	if(msgPayload!=statoWasteStorage) {
        		statoWasteStorage = msgPayload;
        	    IApplMessage message = CommUtils.buildDispatch("gui", "wasteStorageState", "wasteStorageState("+statoWasteStorage+")" , ApplSystemInfo.applActorName);
        	    outinadapter.sendToActor( message );
        	    outinadapter.sendToAll(jsonMsg.toString());
        	}
            return;
        }
    }
}
