package Controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import Model.Greeting;

@RestController
public class HelloController {
    
    @RequestMapping("/")
    public String index() {
        //return "Greetings from Spring Boot!";
    	Greeting greeting = new Greeting("test");
    	return greeting.getMsg();
    }
    
}
