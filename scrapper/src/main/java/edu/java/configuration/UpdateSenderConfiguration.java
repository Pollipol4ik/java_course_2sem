package edu.java.configuration;

import edu.java.client.BotClient;
import edu.java.dto.UpdateLink;
import edu.java.sender.BotUpdateSender;
import edu.java.sender.KafkaUpdateSender;
import edu.java.sender.UpdateSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class UpdateSenderConfiguration {
    @Bean
    public UpdateSender updateSender(
        ApplicationConfig config,
        KafkaTemplate<String, UpdateLink> kafka,
        BotClient client
    ) {
        return (config.useQueue())
            ? new KafkaUpdateSender(kafka, config)
            : new BotUpdateSender(client);
    }
}
