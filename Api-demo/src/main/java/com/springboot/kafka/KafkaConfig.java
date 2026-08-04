package com.springboot.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;


@EnableKafka // Required when manually building your own listener containers
@Configuration
@Profile("kafka")
public class KafkaConfig {
    /**
     KafkaListener configuration:
     a ConsumerFactory (which dictates how to connect and deserialize messages)
     a ConcurrentKafkaListenerContainerFactory (which manages the background polling threads).
     */
    @Bean
    public ConsumerFactory<String, String> customConsumerFactory() {
        Map<String, Object> props = new HashMap<>();

        // 1. Connection settings
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "my-debug-group");

        // 2. Deserialization settings (Key and Value)
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // 3. Offset Management
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // If you want to use JSON objects instead of strings, uncomment below:
        // return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(YourClass.class));

        return new DefaultKafkaConsumerFactory<>(props);
    }

    /**
        @KafkaListener to bind this specific configuration instance
     */
    @Bean("customListenerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String> customListenerFactory() {
        System.out.println("init customListenerFactory ...");
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        // Link the consumer factory configured above
        factory.setConsumerFactory(customConsumerFactory());

        // Concurrency level (Spins up 3 concurrent consumer threads for parallel processing)
        // Note: Your topic must have at least 3 partitions to utilize all 3 threads!
        factory.setConcurrency(3);

        // Turn on/off auto-startup behavior
        factory.setAutoStartup(true);

        return factory;
    }

//    @Bean
//    public NewTopic myTopic() {
//        return TopicBuilder.name("my-topic")
//                .partitions(3)
//                .replicas(1)
//                .build();
//    }

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configMap = new HashMap<>();
        configMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configMap);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}