package main.java;

import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.MsgUtil;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import unibo.basicomm23.utils.CommUtils;
import java.util.Vector;
import javafx.scene.layout.HBox;

public class ScaleGui  extends Application{

    //Variabili statiche per consentire accesso entro start
	//POSESSORE DEL DISPOSITIVO
    private  static ActorBasic displayOwnerActor ;
    //DICHIRAZIONE RELATIVE ALLA GRIGLIA
    private static Rectangle[][]  cells   ;
    private static int rows;
    private static int cols;
    private static int cellsize;
    private static boolean gameStarted = false;
    private static int RPs = 0;
    //DICHIRAZIONE COMPONENTI DELLA SCENA
        private Stage stage;
        private VBox vbox;
        private Scene scene;
        
        
    public ScaleGui(     ){
        super();
    }

    public  void initialize(ActorBasic caller){
        //GridSupport.INSTANCE.setDisplayOwnerActor(caller);
        //conway.GridSupport.readGridConfig("gridConfig.json");
        displayOwnerActor = caller;
        
        launch(new String[] {});
    }
    
//    public void activate() {
//    	CommUtils.outmagenta("GoLGUI | activate: rows="+rows);
//        launch(new String[] {});
//    	
//    }

	@Override
	public void start(Stage primaryStage) throws Exception {
        stage     = primaryStage;
		createScene( );
	}
	@Override
	public void stop(){
	    CommUtils.outmagenta("ScaleGUI | stop: Stage is closing");
	    //displayOwnerActor.emit("gameover", "gameover(master)", null);   //per finire l'appendice remota
	    System.exit(0);  
	}	
	
	protected void setGameInput() {

		Label label = new Label("Press the button + and - to update the number of RPs");
		Label countRPs = new Label("Number of RPs: "+RPs);
        Button newRP = new Button("+");
        Button lessRP = new Button("-");
        label.setWrapText(true);
        countRPs.setWrapText(true);
        HBox buttonRow = new HBox(10); // 10 is the spacing between buttons
        buttonRow.getChildren().addAll(newRP, lessRP);
        buttonRow.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(label, countRPs, buttonRow);
        vbox.setAlignment(Pos.CENTER);
		EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
               String evstr = e.getSource().toString();
               if(evstr.contains("+")){
            	   RPs++;
                  sendMsgToOwner("guicmd", "guicmd("+RPs+")");
                  countRPs.setText("Number of RPs: "+RPs);
               }
               else if(evstr.contains("-")){
            	   if(RPs>0) {
            		   RPs--;
                       sendMsgToOwner("guicmd", "guicmd("+RPs+")");
                       countRPs.setText("Number of RPs: "+RPs);
            	   }
            
                }
            }
         };		
         newRP.setOnAction(event);
         lessRP.setOnAction(event);
	}

    protected void createScene( ){
        vbox = new VBox(10);
        scene = new Scene( vbox, 300, 100);

        stage.setTitle("Scale Mock");
        stage.setScene(scene);
        stage.setAlwaysOnTop(true);
        
        setGameInput();
        stage.show();

    }

    public void sendMsgToOwner(String msgId, String msgPayload) {
    	CommUtils.outcyan("MonitoringDeviceGUI sendMsgToOwner:" + msgId + " " + msgPayload + " " + displayOwnerActor);
    	try {
        MsgUtil.sendMsg("gui",msgId, msgPayload,displayOwnerActor,null);
    	//val m = MsgUtil.buildDispatch( displayOwnerActor.getName(), msgId, msgPayload, displayOwnerActor.getName() );
    	//GlobalScope.launch{ displayOwnerActor.send( m ); };
    	}catch(Exception e) {
    		CommUtils.outred("ScaleGUi sendMsgToOwner ERROR:" +e.getMessage());
    	}
 
    }
    


}
