package edu.java.scrapper;

import edu.java.client.github.GithubClient;
import edu.java.client.link_information.LinkInformationReceiver;
import edu.java.client.link_information.LinkInformationReceiverProvider;
import edu.java.link_type_resolver.LinkType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class LinkInformationReceiverProviderTest {

    @Test
    @DisplayName("LinkInformationReceiverProvider#getReceiver basic test")
    public void getReceiver_shouldReturnCorrectReceiver() {
        LinkInformationReceiverProvider provider = new LinkInformationReceiverProvider();
        provider.registerReceiver(LinkType.GITHUB, new GithubClient());

        LinkInformationReceiver actual = provider.getReceiver(LinkType.GITHUB);

        assertThat(actual).isInstanceOf(GithubClient.class);
    }
}
