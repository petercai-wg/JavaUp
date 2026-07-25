package com.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {org.springframework.boot.jms.autoconfigure.JmsAutoConfiguration.class, com.ibm.mq.spring.boot.MQAutoConfiguration.class})
public class MqApplication {
    public static void main(String[] args) {
//        System.setProperty("com.ibm.mq.cfg.jmqi.useMQCSPauthentication", "false");
        SpringApplication.run(MqApplication.class, args);
    }
}
