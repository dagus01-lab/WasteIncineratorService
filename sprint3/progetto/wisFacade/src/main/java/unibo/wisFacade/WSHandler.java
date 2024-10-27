package unibo.wisFacade;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.msg.ApplMessage;
import unibo.basicomm23.utils.CommUtils;

/*
Gestisce la websocket avendo come riferimento applicativo ApplguiCore
 */


public class WSHandler extends AbstractWebSocketHandler {
    private final List<WebSocketSession> sessions               = new ArrayList<>();
    private final Map<String, WebSocketSession> pendingRequests = new HashMap<>();
    private final Map<String, WebSocketSession> curSessions     = new HashMap<>();
    private int statoAshStorage = 0;
    private int statoWasteStorage = 0;
    private String statoIncinerator = "-";
    private String statoOpRobot = "-";
    private String opRobotJob = "-";

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        CommUtils.outyellow("WSH | Added client session id=" + session.getId() + " " + session.getRemoteAddress());
        //curSessions.put(session.getId(),session);
        super.afterConnectionEstablished(session);
        session.sendMessage(new TextMessage("statoAshStorage(0,"+statoAshStorage+")"));
        session.sendMessage(new TextMessage("num_RP("+statoWasteStorage+")"));
        session.sendMessage(new TextMessage("opRobotState("+statoOpRobot+")"));
        session.sendMessage(new TextMessage("statoIncinerator("+statoIncinerator+")"));
        session.sendMessage(new TextMessage("opRobotJob("+opRobotJob+")"));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus status) throws Exception {
        sessions.remove(session);
        pendingRequests.remove(session);
        curSessions.remove( session.getId() );
        System.out.println("WSH | Removed " + session);
        super.afterConnectionClosed(session, status);
    }

    protected  void sendToOne(String msg) {
        //CommUtils.outcyan("WSH | sendToOne " + msg  );
        IApplMessage msgqak      = new ApplMessage(msg);
        CommUtils.outcyan("WSH | sendToOne " + msgqak  );
        WebSocketSession session = curSessions.get( msgqak.msgReceiver() );
        if( session != null ) {
            try {
                session.sendMessage( new TextMessage(msgqak.msgContent()) );
            } catch (IOException e) {
                CommUtils.outred("WSH | sendToOne " + msg + " ERROR " + e.getMessage());
            }
        }else{
            CommUtils.outred("WSH | sendToOne " + msg + " session not found "  );
        }
    }

    protected  void sendToOne(IApplMessage msg) {
        CommUtils.outred("WSH | sendToOne " + msg  + " SENDER=" + msg.msgSender()  );
        WebSocketSession session = curSessions.get( msg.msgReceiver() );
        if( session != null ) {
            try {
                session.sendMessage( new TextMessage(msg.msgContent()) );
            } catch (IOException e) {
                CommUtils.outred("WSH | sendToOne " + msg + " ERROR " + e.getMessage());
            }
        }else{
            CommUtils.outred("WSH | sendToOne " + msg + " session not found "  );
        }

    }
    
    protected synchronized void updateStoredValues(String message) {
    	try {
    		if(message.contains("statoAshStorage")){
        		int stato = Integer.parseInt(message.split(",")[1].split(")")[0]);
        		if(stato>=0) {
        			statoAshStorage = stato;
        		}
        	}
        	else if(message.contains("statoIncinerator")){
        		statoIncinerator = message.split("(")[1].split(")")[0];

        	}
        	else if(message.contains("opRobotState")){
        		statoOpRobot  = message.split("(")[1].split(")")[0];
        	}
        	else if(message.contains("opRobotJob")){
        		opRobotJob = message.split("(")[1].split(")")[0];
        	}
        	else if(message.contains("num_RP")){
        		int state = Integer.parseInt(message.split("(")[1].split(")")[0]);
        		if(state>=0) {
        			statoWasteStorage = state;
        		}
        	}
    	} catch(Exception e) {
    	}
    	
    }

    protected synchronized  void sendToAll(String message) { 
        
        updateStoredValues(message);
        CommUtils.outcyan("WSH | Sending to all " + message);
        try {
	        if( sessions.size() > 0 ){
	            for (WebSocketSession session : sessions) {
	                session.sendMessage(new TextMessage(message));
	                //CommUtils.outcyan("WSH | sent on current session " + session.getRemoteAddress());
	            }
	        }
            else{
                    //CommUtils.outred("WSH | Sorry: no session yet ...");
            }
     
        } catch (Exception e) {
                CommUtils.outred("WSH | sendToAll " + message + " ERROR " + e.getMessage() );
        }
    }
 
}
