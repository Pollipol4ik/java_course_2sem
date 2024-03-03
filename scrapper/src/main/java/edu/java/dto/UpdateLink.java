package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record UpdateLink(@JsonProperty("id") long linkId, @JsonProperty("url") String link, String description,
                         List<Long> tgChatIds) {

}
