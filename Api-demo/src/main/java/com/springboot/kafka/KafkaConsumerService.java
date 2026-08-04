package com.springboot.kafka;

import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Profile("kafka")
public class KafkaConsumerService {

    @KafkaListener(topics = "my-new-topic", groupId = "my-debug-group",
            containerFactory = "customListenerFactory")
    public void listen(String message) {

        System.out.println("Received message: " + message);
    }
}