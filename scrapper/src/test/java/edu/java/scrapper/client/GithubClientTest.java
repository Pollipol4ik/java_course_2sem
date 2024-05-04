//package edu.java.scrapper.client;
//
//import com.github.tomakehurst.wiremock.WireMockServer;
//import edu.java.client.github.GithubClient;
//import edu.java.client.link_information.LastUpdateTime;
//import edu.java.client.link_information.LinkInfoReceiver;
//import java.net.URI;
//import java.time.OffsetDateTime;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
//import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
//import static com.github.tomakehurst.wiremock.client.WireMock.get;
//import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
//import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class GithubClientTest {
//
//    private static WireMockServer wireMockServer;
//
//    @BeforeAll
//    public static void setUp() {
//        String url = "/repos/Pollipol4ik/java_course_2sem";
//        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
//        wireMockServer.start();
//        configureFor(wireMockServer.port());
//
//        String currentDateTime = OffsetDateTime.now().toString();
//
//        wireMockServer.stubFor(get(urlEqualTo(url))
//            .willReturn(aResponse()
//                .withStatus(200)
//                .withHeader("Content-Type", "application/json")
//                .withBody("""
//                    {
//                        "full_name": "Pollipol4ik/java_course_2sem",
//                        "updated_at": "%s"
//                    }
//                    """.formatted(currentDateTime))));
//    }
//
//    @AfterAll
//    public static void tearDown() {
//        wireMockServer.stop();
//    }
//
//    @Test
//    @DisplayName("GithubClient#receiveLastUpdateTime test")
//    public void receiveLastUpdateTime_shouldReturnCorrectResponse() {
//        LinkInfoReceiver client = new GithubClient(wireMockServer.baseUrl());
//        LastUpdateTime actual =
//            client.receiveLastUpdateTime(URI.create("https://github.com/Pollipol4ik/java_course_2sem"));
//        assertThat(actual).isNotNull();
//        assertThat(actual.lastUpdateTime().toLocalDate()).isEqualTo(OffsetDateTime.now().toLocalDate());
//    }
//}
