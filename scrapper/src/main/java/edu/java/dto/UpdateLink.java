package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record UpdateLink(@JsonProperty("id") long linkId, @JsonProperty("url") java.net.URI link, String description,
                         List<Long> tgChatIds) {

}
