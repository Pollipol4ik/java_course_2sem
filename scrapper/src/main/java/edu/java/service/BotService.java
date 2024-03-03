package edu.java.service;

import edu.java.dto.UpdateLink;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface BotService {

    @PostExchange("/updates")
    void sendUpdate(@RequestBody UpdateLink linkUpdate);
}
