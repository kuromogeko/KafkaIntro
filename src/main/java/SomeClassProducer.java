import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SomeClassProducer {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Properties config = new Properties();
        config.put("client.id", "nnkm");
        config.put("group.id", "nnkm-group");
        config.put("bootstrap.servers", "192.168.10.39:9092");
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
//        config.put("request.timeout.ms", "3000");
//        config.put("timeout.ms", "3000");
        config.put("max.block.ms", "3000");

        String topic = "test";

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(config);
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, "value ohne key");
        ProducerRecord<String, String> recordWithKey = new ProducerRecord<>(topic, "key-nnkm", "Inhalt");

        Future<RecordMetadata> recordFuture = producer.send(record);
        Future<RecordMetadata> recordFuture2 = producer.send(record, (recordMetadata, e) -> {
            if (e != null) {
                //rollback magic
                System.out.println("Totally doing rollback magic");
            } else {
                System.out.println("Message ended up on partition: " + recordMetadata.partition());
            }
        });
        Future<RecordMetadata> recordFutureWithKey = producer.send(recordWithKey);


        recordFuture.get();
        recordFuture2.get();
        recordFutureWithKey.get();
    }
}
