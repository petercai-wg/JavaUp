package com.hello;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

@RestController
//@EnableAutoConfiguration
public class HelloWorldRest {
	@RequestMapping("/")
	public String sayHello() {
		return JsonService.jsonString();
	}

//	public static void main(String[] args) throws Exception {
//		SpringApplication.run(HelloWorldRest.class, args);
//	}
}
