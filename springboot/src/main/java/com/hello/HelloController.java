package com.hello;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 
 * 
 * log4j vulnerabilty , search for ${java:version}, ${env:JAVA_HOME},, ${jndi:ldap://localhost:8088/dc=example,dc=com}
 *
 */

//https://docs.spring.io/spring-boot/docs/current/reference/html/howto-logging.html
@Controller
public class HelloController {

    private static final Logger logger = LogManager.getLogger(HelloController.class);

    @RequestMapping("/searchName")
    public  String searchName(@RequestParam(value="name", required=true)String name, Model model) {
    	logger.info("searchName ..." + name);
    	

        model.addAttribute("name", name);

        return "welcome"; //view
    }  

    @GetMapping("/")
    public String main(@RequestParam(value="name", required = false, defaultValue = "testdefault")String name, Model model) {

      
            logger.info("logging input name = " +  name);
     

       
        model.addAttribute("name", name);

        return "welcome"; //view
    }

    

}
