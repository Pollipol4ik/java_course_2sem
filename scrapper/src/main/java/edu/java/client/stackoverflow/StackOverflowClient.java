package edu.java.client.stackoverflow;

import edu.java.client.AbstractClient;
import edu.java.client.dto.stackoverflow.QuestionResponse;
import edu.java.client.link_information.LinkInfo;
import edu.java.exception.UnsupportedLinkTypeException;
import edu.java.link_type_resolver.LinkType;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Value;

public class StackOverflowClient extends AbstractClient {

    private static final String DEFAULT_BASE_URL = "https://api.stackexchange.com/2.3/";
    private static final Pattern STACKOVERFLOW_LINK_PATTERN =
        Pattern.compile("https://stackoverflow.com/questions/(\\d+).*");
    @Value("${client.stackoverflow.access-token}")
    private String accessToken;
    @Value("${client.stackoverflow.key}")
    private String key;

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
    public List<LinkInfo> receiveLastUpdateTime(URI url) {
        Matcher matcher = STACKOVERFLOW_LINK_PATTERN.matcher(url.toString());
        if (!matcher.matches()) {
            return null;
        }
        String questionId = matcher.group(1);
        QuestionResponse info = webClient
            .get()
            .uri("/questions/" + questionId
                + "?site=stackoverflow&access_token=" + accessToken + "&key=" + key)
            .retrieve()
            .bodyToMono(QuestionResponse.class)
            .onErrorReturn(new QuestionResponse(null))
            .block();
        if (info == null || info.equals(new QuestionResponse(null)) || info.items().length == 0) {
            throw new UnsupportedLinkTypeException(url.toString());
        }
        List<LinkInfo> listInfo = new ArrayList<>();
        listInfo.add(new LinkInfo(
            url,
            "Был обновлён вопрос \"" + info.items()[0].title() + "\": ",
            info.items()[0].lastUpdate()
        ));
        return listInfo;
    }

    @Override
    public boolean isValidate(URI url) {
        return STACKOVERFLOW_LINK_PATTERN.matcher(url.toString()).matches();
    }
}
