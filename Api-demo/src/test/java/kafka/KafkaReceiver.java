package kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class KafkaReceiver {
    public static void main(String[] args) {
        String bootstrapServers = "localhost:9092";
        String groupId = "standalone-group";
        String topic = "my-new-topic";

        // 1. Create Consumer Configuration Properties
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // 2. Create the Consumer instance
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties)) {

            // 3. Subscribe to the specific topic list
            consumer.subscribe(Collections.singletonList(topic));
            System.out.println("Consumer started. Waiting for messages...");

            // 4. Polling loop infrastructure
            while (true) {
                // Poll Kafka brokers for records. Blocks for up to 1000ms if no data is present.
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("Consumed Record! Key: %s | Value: %s | Partition: %d | Offset: %d%n",
                            record.key(), record.value(), record.partition(), record.offset());
                }
            }
        } catch (Exception e) {
            System.err.println("Consumer exception encountered: " + e.getMessage());
        }
    }
}
