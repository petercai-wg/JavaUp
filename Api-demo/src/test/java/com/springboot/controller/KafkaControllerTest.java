package com.springboot.controller;

import com.springboot.kafka.KafkaProducerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class KafkaControllerTest {
    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private KafkaProducerService kafkaProducer;

    @Test
    void testPublishMessage() {
        String message = "Hello, Kafka!";
        String topic = "my-new-topic";
        when(kafkaTemplate.send(anyString(), eq(message))).thenReturn(null);

        kafkaProducer.sendMessage(message);

        verify(kafkaTemplate, Mockito.times(1)).send(topic, message);


    }


}