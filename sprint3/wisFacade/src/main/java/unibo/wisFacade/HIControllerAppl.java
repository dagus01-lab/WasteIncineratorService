package unibo.wisFacade;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@RequestMapping("/Api")
public class HIControllerAppl {
    @Value("${spring.application.name}")
    String appName;
    int ashStorageLevel;
    int wasteStoragePackets;
    String opRobotStatus;
    boolean incineratorStatus;

	@GetMapping("/")
	public String homePage(Model model) {
	    model.addAttribute("arg", appName);
	    model.addAttribute("ashStorageLevel", ashStorageLevel);
	    model.addAttribute("wasteStoragePackets", wasteStoragePackets);
	    model.addAttribute("opRobotStatus", opRobotStatus);
	    model.addAttribute("incineratorStatus", incineratorStatus);
	    return "Gui";
	}
}
