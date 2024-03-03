package edu.java.controller;

import edu.java.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-chat")
@RequiredArgsConstructor
@Log4j2
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/{id}")
    public void registerChat(@PathVariable("id") long chatId) {
        log.info("Registering chat with id {}", chatId);
        chatService.registerChat(chatId);
    }

    @DeleteMapping("/{id}")
    public void deleteChat(@PathVariable("id") long chatId) {
        log.info("Deleting chat with id {}", chatId);
        chatService.deleteChat(chatId);
    }
}
