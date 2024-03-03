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
@RequestMapping("/telegram/chat")
@RequiredArgsConstructor
@Log4j2
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/{chatId}")
    public void registerChat(@PathVariable("chatId") Long chatId) {
        log.info("Registering chat with ID: {}", chatId);
        chatService.registerChat(chatId);
    }

    @DeleteMapping("/{chatId}")
    public void deleteChat(@PathVariable("chatId") Long chatId) {
        log.info("Deleting chat with ID: {}", chatId);
        chatService.deleteChat(chatId);
    }
}
