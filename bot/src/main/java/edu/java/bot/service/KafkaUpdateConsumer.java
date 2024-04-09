package edu.java.bot.service;

import edu.java.bot.dto.UpdateLink;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
@ConditionalOnProperty(name = "app.use-queue", havingValue = "true")
public class KafkaUpdateConsumer {
    private final UpdateService updateService;

    @SneakyThrows
    @RetryableTopic(
        attempts = "1",
        dltStrategy = DltStrategy.FAIL_ON_ERROR,
        dltTopicSuffix = "_dlq")
    @KafkaListener(topics = "${app.topic-name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(UpdateLink update) {
        log.info(String.format("#### -> Consumed message -> %s", update));
        updateService.sendUpdate(update);
    }

    @DltHandler
    public void handleDltPayment(
        UpdateLink linkUpdate, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic
    ) {
        log.info("Event on dlt topic={}, payload={}", topic, linkUpdate);
    }
}
