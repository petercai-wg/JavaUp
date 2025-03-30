package com.ldap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.annotation.PostConstruct;
import java.util.List;

///@SpringBootApplication
public class CommandApplication {
	private static Logger log = LoggerFactory.getLogger(CommandApplication.class);

    @Autowired
    private LDAPRepository personRepository;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(CommandApplication.class, args);
    }

    @PostConstruct
    public void setup(){
        log.info("Spring LDAP + Spring Boot Configuration Example");

        List<String> names = personRepository.getAllPersonNames();
        log.info("names: " + names);

        
    }
}
