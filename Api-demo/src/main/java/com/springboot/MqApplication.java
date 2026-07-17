package com.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class MqApplication {
    public static void main(String[] args) {
        System.setProperty("com.ibm.mq.cfg.jmqi.useMQCSPauthentication", "false");
        SpringApplication.run(MqApplication.class, args);
    }
}
