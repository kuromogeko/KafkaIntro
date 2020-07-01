import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SomeClassConsumer {
    public static void main(String[] args) {
        Properties config = new Properties();
        config.put("client.id", "nnkm");
        config.put("group.id", "nnkm-group");
        config.put("bootstrap.servers", "192.168.10.39:9092");
        config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        config.put("enable.auto.commit", "false");
        KafkaConsumer consumer = new KafkaConsumer<String, String>(config);
        List<String> topics = new ArrayList<String>();
        topics.add("test");

        consumer.subscribe(topics);

        boolean running = true;
        while (running) {
            ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
            for (ConsumerRecord record : records) {
                System.out.println(record.value());
                consumer.commitSync();
            }
        }
    }
}
