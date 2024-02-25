package edu.java.client.dto.stackoverflow;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

public record QuestionResponse(List<GetQuestionResponseItem> items) {

    public record GetQuestionResponseItem(@JsonProperty("last_activity_date") OffsetDateTime lastUpdate) {

    }
}
