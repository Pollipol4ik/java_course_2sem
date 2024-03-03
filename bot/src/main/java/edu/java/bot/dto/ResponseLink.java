package edu.java.bot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResponseLink(@JsonProperty("id") long linkId, @JsonProperty("url") String link) {

}
