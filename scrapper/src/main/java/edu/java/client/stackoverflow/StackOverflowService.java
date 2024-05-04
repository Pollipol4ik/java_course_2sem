package edu.java.client.stackoverflow;

import edu.java.client.dto.stackoverflow.QuestionResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface StackOverflowService {

    @GetExchange("/questions/{question_id}?site=stackoverflow")
    QuestionResponse getQuestion(@PathVariable("question_id") String questionId);
}
