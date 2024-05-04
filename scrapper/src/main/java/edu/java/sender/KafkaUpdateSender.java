package edu.java.sender;

import edu.java.configuration.ApplicationConfig;
import edu.java.dto.UpdateLink;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
public class KafkaUpdateSender implements UpdateSender {
    private final KafkaTemplate<String, UpdateLink> kafkaTemplate;
    private final ApplicationConfig config;

    @Override
    public void sendUpdate(UpdateLink update) {
        kafkaTemplate.send(config.topic(), update);
    }
}
