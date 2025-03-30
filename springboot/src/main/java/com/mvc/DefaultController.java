package com.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DefaultController {

    @GetMapping("/")
    public String home1() {
        return "/home";
    }

    @GetMapping("/home")
    public String home() {
        return "/home";
    }

    @GetMapping("/admin")
    public String admin() {
        return "/admin";
    }

    @GetMapping("/user")
    public String user() {
        return "/user";
    }

    @GetMapping("/about")
    public String about() {
        return "/about";
    }

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public  String login(){
    	System.out.println("login GET");
        return "/login";
    }    
    
    @RequestMapping(value="/login", method = RequestMethod.POST)
    public  String authenticate(){
    	System.out.println("login POST");
        return "/login";
    }      

    @GetMapping("/403")
    public String error403() {
        return "/error/403";
    }

}
