package unibo.wisFacade;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import unibo.basicomm23.utils.CommUtils;
 

 

@Controller
public class FacadeController {
    String protocol="tcp";

    @Value("${spring.application.name}")
    String appNameOld;  //vedi application.properties
     
    protected String mainPage = "WISGui"; //TODO: "WebRobot24Gui";  

    public FacadeController(){
        CommUtils.outgreen (" --- FacadeController | STARTS " );
        new FacadeBuilder( ) ;
    }
 

    protected String buildThePage(Model viewmodel) {
        setConfigParams(viewmodel);
        return mainPage;
    }
    protected void setConfigParams(Model viewmodel){
 
    }
    @GetMapping("/")
    public String homePage(Model viewmodel) {
        //CommUtils.outcyan("FacadeController homePage appNameOld=" + appNameOld);
        viewmodel.addAttribute("appname", ApplSystemInfo.appName);
        String dir = System.getProperty("user.dir");
        CommUtils.outgreen (" --- FacadeController | entry dir= "+dir  );
        return buildThePage(viewmodel); //"qakFacadeGUI";
    } 
}
