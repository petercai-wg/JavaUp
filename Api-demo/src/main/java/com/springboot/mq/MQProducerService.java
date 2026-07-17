package com.springboot.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class MQProducerService {
    @Autowired
    private final JmsTemplate jmsTemplate;

    @Value("${project.mq.queue-name}")
    private String defaultQueue;

    public MQProducerService(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;

    }
    /**
     * 1. Publish a standard text message to the default queue.
     */
    public void publishText(String payload) {
        jmsTemplate.convertAndSend(defaultQueue, payload);
    }

}