package com.handson.sentiment.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;
import reactor.kafka.sender.SenderResult;

import java.util.UUID;

@Component
public class AppKafkaSender {

    @Autowired
    @Qualifier("simpleProducer")
    private KafkaSender<String, String> kafkaSender;

    //This function sends a message to a Kafka topic using the KafkaSender and returns true when the operation completes.
    public boolean send(String msg, String topic) {
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, null, UUID.randomUUID().toString(),
                msg);
        // sends a mono (1 element instead of a flux) to the Kafka topic
        Mono<SenderRecord<String, String, String>> mono = Mono.just(SenderRecord.create(record, null));
        Flux<SenderResult<String>> res = kafkaSender.send(mono);
        res.collectList().subscribe();

        return true;
    }
}
