package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;

public record ResponseLink(@JsonProperty("id") long linkId, @JsonProperty("url") URI link) {

}
