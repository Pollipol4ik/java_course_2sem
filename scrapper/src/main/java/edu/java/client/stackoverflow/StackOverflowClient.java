package edu.java.client.stackoverflow;

import edu.java.client.AbstractClient;
import edu.java.client.dto.stackoverflow.QuestionResponse;
import edu.java.client.link_information.LastUpdateTime;
import edu.java.client.link_information.LinkInfoReceiver;
import edu.java.link_type_resolver.LinkType;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StackOverflowClient extends AbstractClient<StackOverflowService> implements LinkInfoReceiver {

    private static final String DEFAULT_BASE_URL = "https://api.stackexchange.com/2.3/";
    private static final Pattern STACKOVERFLOW_LINK_PATTERN =
        Pattern.compile("https://stackoverflow.com/questions/(\\d+).*");

    public StackOverflowClient() {
        this(DEFAULT_BASE_URL);
    }

    public StackOverflowClient(String baseUrl) {
        super(baseUrl);
    }

    @Override
    public LinkType getLinkType() {
        return LinkType.STACKOVERFLOW;
    }

    @Override
    public LastUpdateTime receiveLastUpdateTime(URI url) {
        Matcher matcher = STACKOVERFLOW_LINK_PATTERN.matcher(url.toString());
        if (!matcher.find()) {
            return null;
        }
        String questionId = matcher.group(1);
        QuestionResponse response = service.getQuestion(questionId);
        return new LastUpdateTime(response.items().get(0).lastUpdate());
    }

    @Override
    public boolean isValidate(URI url) {
        return STACKOVERFLOW_LINK_PATTERN.matcher(url.toString()).matches();
    }
}
