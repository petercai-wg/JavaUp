package com.springboot.mq;

import jakarta.jms.TextMessage;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import jakarta.jms.Message;

@Component
public class MQConsumerService {

    @JmsListener(destination = "${project.mq.queue-name}")
    public void receiveMessage(Message message) {
        try{
            int deliveryCount =  message.getIntProperty("JMSXDeliveryCount");
            System.out.println("Delivery Count: " + deliveryCount);
            int msgId = message.getIntProperty("JMSXMessageID");
            System.out.println("Message ID: " + msgId);

            System.out.println("Received message from IBM MQ: " + message);

            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                System.out.println("Received text: " + textMessage.getText());
            }

        } catch (Exception e) {
            System.err.println("Error occurred while processing message: " + e.getMessage());
        }
    }
}