package edu.java.client.stackoverflow;

import edu.java.client.AbstractWebClient;
import edu.java.client.dto.stackoverflow.QuestionResponse;
import edu.java.client.link_information.LastUpdateTime;
import edu.java.link_type_resolver.LinkType;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StackOverflowClient extends AbstractWebClient {

    private static final String BASE_URL = "https://api.stackexchange.com/2.3/";
    private static final Pattern STACKOVERFLOW_LINK_PATTERN =
        Pattern.compile("https://stackoverflow.com/questions/(\\d+).*");

    private final StackOverflowService service;

    public StackOverflowClient() {
        this(BASE_URL);
    }

    public StackOverflowClient(String baseUrl) {
        super(baseUrl);
        service = factory.createClient(StackOverflowService.class);
    }

    @Override
    public LinkType getLinkType() {
        return LinkType.STACKOVERFLOW;
    }

    @Override
    public LastUpdateTime receiveLastUpdateTime(String link) {
        Matcher matcher = STACKOVERFLOW_LINK_PATTERN.matcher(link);
        if (!matcher.find()) {
            return null;
        }
        QuestionResponse response = service.getQuestion(matcher.group(1));
        return new LastUpdateTime(response.lastUpdate());
    }
}
