package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ref: https://spring.io/guides/tutorials/react-and-spring-data-rest/
 * code:  https://github.com/spring-guides/tut-react-and-spring-data-rest
 * 
 *
 */



@SpringBootApplication
public class SpringRestApplication {

	public static void main(String[] args) {
		//  log4j vulnerability demo
		System.setProperty("com.sun.jndi.ldap.object.trustURLCodebase",  "true");
		
		
		SpringApplication.run(SpringRestApplication.class, args);
		
		
		
	}

}
