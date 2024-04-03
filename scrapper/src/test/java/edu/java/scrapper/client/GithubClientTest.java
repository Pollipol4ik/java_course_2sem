package edu.java.scrapper.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.client.github.GithubClient;
import edu.java.client.github.events.EventProvider;
import edu.java.client.github.events.PushEventProvider;
import edu.java.client.link_information.LinkInfo;
import edu.java.client.link_information.LinkInfoReceiver;
import edu.java.exception.UnsupportedLinkTypeException;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class GithubClientTest {
    private static final String API_LINK = "/repos/Pollipol4ik/test-repository";
    private static final String LINK = "https://github.com/repos/Pollipol4ik/test-repository";
    private static final String UNKNOWN_LINK = "https://youtube.com";
    private final List<EventProvider> providers = List.of(new PushEventProvider());
    private WireMockServer server;

    //Arrange
    @BeforeEach
    public void setUp() {
        server = new WireMockServer(wireMockConfig().dynamicPort());
        server.stubFor(get(urlPathMatching(API_LINK))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    [
                      {
                        "type": "PushEvent",
                        "payload": {
                          "ref": "refs/heads/master"
                        },
                        "actor": {
                          "login": "Pollipol4ik"
                        },
                        "created_at": "2024-03-16T19:22:17Z"
                      }
                    ]""")));
        server.stubFor(get(urlPathMatching("/repos/aboba/abobus/events"))
            .willReturn(aResponse()
                .withStatus(404)));
        server.start();
    }

    @Test
    @DisplayName("Not updated GitHub repository link test")
    public void receiveLastUpdateTime_shouldEmptyList_whenRepositoryWasNotUpdated() {
        //Arrange
        String oldApiLinkEvents = "/repos/Pollipol4ik/test-repository/events";
        String oldApiLink = "/repos/Pollipol4ik/test-repository";
        String oldLink = "https://github.com/repos/Pollipol4ik/test-repository";
        server.stubFor(get(urlPathMatching(oldApiLinkEvents))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    [

                    ]""")));
        server.stubFor(get(urlPathMatching(oldApiLink))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("""
                    {
                        "text": "test"
                    }""")));
        LinkInfoReceiver client = new GithubClient(server.baseUrl(), providers);
        URI url = URI.create(oldLink);
        //Act
        List<LinkInfo> info = client.receiveLastUpdateTime(url);
        //Assert
        assertThat(info).isEmpty();
    }

    @Test
    @DisplayName("Nonexistent GitHub repository link test")
    public void receiveLastUpdateTime_shouldThrowUnsupportedLinkTypeException_whenRepositoryDoesNotExist() {
        //Arrange
        LinkInfoReceiver client = new GithubClient(server.baseUrl(), providers);
        URI url = URI.create("https://github.com/repos/Pollipol4ik/test-repositor");
        //Expect
        assertThatThrownBy(() -> client.receiveLastUpdateTime(url))
            .isInstanceOf(UnsupportedLinkTypeException.class);
    }

    @Test
    @DisplayName("Not GitHub link test")
    public void receiveLastUpdateTime_shouldReturnNull_whenLinkDoesNotSupport() {
        //Arrange
        LinkInfoReceiver client = new GithubClient(server.baseUrl(), providers);
        //Act
        List<LinkInfo> info = client.receiveLastUpdateTime(URI.create(UNKNOWN_LINK));
        //Assert
        assertThat(info).isNull();
    }

    @Test
    @DisplayName("GitHub repository link test")
    public void isValidate_shouldReturnTrue_whenLinkIsValidated() {
        //Arrange
        LinkInfoReceiver client = new GithubClient(server.baseUrl(), providers);
        //Act
        boolean response = client.isValidate(URI.create(LINK));
        //Assert
        assertThat(response).isTrue();
    }

    @Test
    @DisplayName("Not GitHub repository link test")
    public void isValidate_shouldReturnFalse_whenLinkIsNotValidated() {
        //Arrange
        LinkInfoReceiver client = new GithubClient(server.baseUrl(), providers);
        //Act
        boolean response = client.isValidate(URI.create(UNKNOWN_LINK));
        //Assert
        assertThat(response).isFalse();
    }

    @AfterEach
    public void shutdown() {
        server.stop();
    }
}
