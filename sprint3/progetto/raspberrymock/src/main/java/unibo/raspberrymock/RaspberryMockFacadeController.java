package unibo.raspberrymock;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.msg.ApplMessage;
import unibo.basicomm23.utils.CommUtils;

 

@Controller
public class RaspberryMockFacadeController {
    protected String sysName = "unknown";
    String protocol="tcp";
    
    @Value("${spring.application.name}")
    String appName;  //vedi application.properties

    protected String mainPage     = "RaspberryMockGui";
    protected ApplguiCore guiCore ;

    public RaspberryMockFacadeController(){
        CommUtils.outgreen (" --- RaspberryMockFacadeController | STARTS ");
        new FacadeBuilder( ) ;
        guiCore = FacadeBuilder.guiCore;
    }

    protected String buildThePage(Model viewmodel) {
        return mainPage;
    }
 

    @GetMapping("/")
    public String homePage(Model viewmodel) {
        viewmodel.addAttribute("appname", ApplSystemInfo.appName);
        String dir = System.getProperty("user.dir");
        CommUtils.outgreen (" --- RaspberryMockFacadeController | entry dir= "+dir  );
        return buildThePage(viewmodel);
    }

    @ExceptionHandler
    public ResponseEntity handle(Exception ex) {
        HttpHeaders responseHeaders = new HttpHeaders();
        MultiValueMap map;
        return new ResponseEntity(
             "BaseController ERROR " + ex.getMessage(),
             responseHeaders, HttpStatus.CREATED);
    }

}
/*
 * curl --location --request POST 'http://localhost:8080/move' --header 'Content-Type: text/plain' --form 'move=l'
 * curl -d move=r localhost:8080/move
 */