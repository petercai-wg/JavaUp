package kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaPublisher {

    public static void main(String[] args) {
        String bootstrapServers = "localhost:9092";
        String topic = "my-new-topic";

        // 1. Create Producer Configuration Properties
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // 2. Create the Producer instance (wrapped in try-with-resources for automatic closing)
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {

            String messageValue = "Hello from Java Client!";

            // Create a record (No explicit routing key is defined here)
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, messageValue);

            System.out.println("Sending message: " + messageValue);

            // 3. Send data asynchronously
            producer.send(record, (metadata, exception) -> {
                if (exception == null) {
                    System.out.printf("Message sent successfully! Topic: %s | Partition: %d | Offset: %d%n",
                            metadata.topic(), metadata.partition(), metadata.offset());
                } else {
                    System.err.println("Error while producing: " + exception.getMessage());
                }
            });

            // Flush forces buffered data to immediately send before the block ends
            producer.flush();
        }
    }
}
