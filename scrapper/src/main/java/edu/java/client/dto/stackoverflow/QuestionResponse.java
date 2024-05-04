package edu.java.client.dto.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record QuestionResponse(StackOverflowInfo[] items) {

    public record StackOverflowInfo(
        String title,
        @JsonProperty("last_activity_date") OffsetDateTime lastUpdate) {

    }
}
