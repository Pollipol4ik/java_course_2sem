package edu.java.scrapper;

import edu.java.client.github.GithubClient;
import edu.java.client.github.events.EventProvider;
import edu.java.client.github.events.GitHubEvent;
import edu.java.client.link_information.LinkInfoReceiver;
import edu.java.client.link_information.LinkInformationReceiverProvider;
import edu.java.link_type_resolver.LinkType;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class LinkInformationReceiverProviderTest {

    @Test
    @DisplayName("LinkInformationReceiverProvider#getReceiver basic test")
    public void getReceiver_shouldReturnCorrectReceiver() {
        EventProvider mockEventProvider = new EventProvider() {
            @Override
            public String getMessage(GitHubEvent event) {
                return "Mock event message";
            }

            @Override
            public boolean checkType(String eventType) {
                return true;
            }

            @Override
            public String makeHyperlink(String url, String title) {
                return EventProvider.super.makeHyperlink(url, title);
            }

        };

        List<EventProvider> eventProviders = Collections.singletonList(mockEventProvider);
        GithubClient githubClient = new GithubClient(eventProviders);

        LinkInformationReceiverProvider provider = new LinkInformationReceiverProvider();
        provider.registerReceiver(LinkType.GITHUB, githubClient);

        LinkInfoReceiver actual = provider.getReceiver(LinkType.GITHUB);

        assertThat(actual).isNotNull();
    }
}
