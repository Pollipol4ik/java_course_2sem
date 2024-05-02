package edu.java.bot.controller;


import edu.java.bot.dto.UpdateLink;
import edu.java.bot.service.UpdateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
@RequiredArgsConstructor
public class UpdateController {
    private final UpdateService updateService;

    @PostMapping
    public void update(@RequestBody @Valid UpdateLink linkUpdate) {
        updateService.updateLink(linkUpdate);
    }
}
