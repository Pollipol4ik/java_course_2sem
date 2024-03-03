package edu.java.bot.controller;

import edu.java.bot.dto.UpdateLink;
import edu.java.bot.service.UpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
@RequiredArgsConstructor
@Log4j2
public class UpdateController {

    private final UpdateService updateService;

    @PostMapping
    public void sendUpdate(@RequestBody UpdateLink linkUpdate) {
        log.info("Received update: {}", linkUpdate);
        updateService.sendUpdate(linkUpdate);
    }
}
