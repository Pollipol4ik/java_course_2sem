package edu.java.scrapper.service;

import edu.java.client.github.GithubClient;
import edu.java.client.link_information.LinkInfo;
import edu.java.dto.AddLinkRequest;
import edu.java.dto.LinkData;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.RemoveLinkRequest;
import edu.java.dto.ResponseLink;
import edu.java.exception.ChatNotAuthorizedException;
import edu.java.exception.LinkAlreadyTrackedException;
import edu.java.exception.LinkNotFoundException;
import edu.java.exception.UnsupportedLinkTypeException;
import edu.java.link_type_resolver.LinkType;
import edu.java.scrapper.IntegrationEnvironment;
import edu.java.service.link.LinkService;
import jakarta.persistence.EntityManager;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class JpaLinkServiceTest extends IntegrationEnvironment {
    @Autowired
    private LinkService linkService;

    @Autowired
    private EntityManager manager;

    @Autowired
    private JdbcClient jdbcClient;

    @MockBean
    private GithubClient client;

    @DynamicPropertySource
    static void jpaProperties(DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "jpa");
    }

    @Test
    @Transactional
    @Rollback
    public void getAllLinks_shouldReturnListLinksResponseWithEmptyList_whenChatDoesNotTrackAnyLink() {
        //Arrange
        Long chatId = 1L;
        jdbcClient.sql("INSERT INTO chat(id) VALUES (:chatId)")
            .param("chatId", chatId)
            .update();
        //Act
        ListLinksResponse list = linkService.getAllLinks(chatId);
        //Assert
        assertThat(list.links().size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    public void getAllLinks_shouldThrowChatNotAuthorizedException_whenChatIsNotInDb() {
        //Arrange
        Long chatId = 1L;
        //Expected
        assertThatThrownBy(() -> linkService.getAllLinks(chatId)).isInstanceOf(ChatNotAuthorizedException.class);
    }

    @Test
    @Transactional
    @Rollback
    public void getAllLinks_shouldReturnListLinksResponseWithLinks_whenChatTracksLinks() {
        //Arrange
        Long chatId = 1L;
        jdbcClient.sql("INSERT INTO chat(id) VALUES (:chatId)")
            .param("chatId", chatId)
            .update();
        String url = "google.com";
        Long linkId = jdbcClient.sql("""
                INSERT INTO link(url, type)
                VALUES (:url, :link_type)
                RETURNING id""")
            .param("url", url)
            .param("link_type", LinkType.UNKNOWN.name())
            .query(Long.class)
            .single();

        jdbcClient.sql("INSERT INTO chat_link(link_id, chat_id) VALUES (:linkId, :chatId)")
            .param("linkId", linkId)
            .param("chatId", chatId)
            .update();

        ListLinksResponse expectedList =
            new ListLinksResponse(List.of(new ResponseLink(linkId, url)));
        //Act
        ListLinksResponse actualList = linkService.getAllLinks(chatId);
        //Assert
        assertThat(actualList).isEqualTo(expectedList);
    }

    @Test
    @Transactional
    @Rollback
    public void deleteLink_shouldRemoveLinkFromLinkTable_whenNobodyTracksIt() {
        //Arrange
        String url = "google.com";
        Long chatId = 1L;
        jdbcClient.sql("INSERT INTO chat(id) VALUES (:chatId)")
            .param("chatId", chatId)
            .update();
        Long linkId = jdbcClient.sql("""
                INSERT INTO link(url, type)
                VALUES (:url, :link_type)
                RETURNING id""")
            .param("url", url)
            .param("link_type", LinkType.UNKNOWN.name())
            .query(Long.class)
            .single();
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(linkId);
        jdbcClient.sql("INSERT INTO chat_link(link_id, chat_id) VALUES (:linkId, :chatId)")
            .param("linkId", linkId)
            .param("chatId", chatId)
            .update();

        ResponseLink expectedResponse = new ResponseLink(linkId, url);
        //Act
        ResponseLink response = linkService.removeLink(chatId, removeLinkRequest);
        manager.flush();
        //Assert
        Long count = jdbcClient.sql("SELECT COUNT(*) FROM link WHERE id = :id")
            .param("id", linkId)
            .query(Long.class)
            .single();
        assertThat(response).isEqualTo(expectedResponse);
        assertThat(count).isEqualTo(0L);
    }

    @Test
    @Transactional
    @Rollback
    public void deleteLink_shouldRemoveLinkOnlyFromChatLinkTable_whenLinkTrackedByMoreThanOneChat() {
        //Arrange
        String url = "google.com";
        Long chatId = 1L;
        Long secChatId = 2L;
        jdbcClient.sql("INSERT INTO chat(id) VALUES (:chatId)")
            .param("chatId", chatId)
            .update();
        jdbcClient.sql("INSERT INTO chat(id) VALUES (:chatId)")
            .param("chatId", secChatId)
            .update();
        Long linkId = jdbcClient.sql("""
                INSERT INTO link(url, type)
                VALUES (:url, :link_type)
                RETURNING id""")
            .param("url", url)
            .param("link_type", LinkType.UNKNOWN.name())
            .query(Long.class)
            .single();
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(linkId);
        jdbcClient.sql("INSERT INTO chat_link(link_id, chat_id) VALUES (:linkId, :chatId)")
            .param("linkId", linkId)
            .param("chatId", chatId)
            .update();

        jdbcClient.sql("INSERT INTO chat_link(link_id, chat_id) VALUES (:linkId, :chatId)")
            .param("linkId", linkId)
            .param("chatId", secChatId)
            .update();

        //Act
        linkService.removeLink(chatId, removeLinkRequest);
        manager.flush();
        //Assert
        Long count = jdbcClient.sql("SELECT COUNT(*) FROM link WHERE id = :id")
            .param("id", linkId)
            .query(Long.class)
            .single();
        Long chatCount = jdbcClient.sql("SELECT COUNT(*) FROM chat_link WHERE chat_id = :chatId")
            .param("chatId", chatId)
            .query(Long.class)
            .single();
        assertThat(count).isEqualTo(1L);
        assertThat(chatCount).isEqualTo(0L);
    }

    @Test
    @Transactional
    @Rollback
    public void deleteLink_shouldThrowChatNotAuthorizedException_whenChatIdDoesNotExist() {
        //Arrange
        Long chatId = 1L;
        Long linkId = 1L;
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(linkId);
        //Expected
        assertThatThrownBy(() -> linkService.removeLink(
            chatId,
            removeLinkRequest
        )).isInstanceOf(ChatNotAuthorizedException.class);
    }

    @Test
    @Transactional
    @Rollback
    public void deleteLink_shouldThrowLinkNotFoundException_whenLinkIdIsNotInDb() {
        //Arrange
        Long chatId = 1L;
        jdbcClient.sql("INSERT INTO chat(id) VALUES (:chatId)")
            .param("chatId", chatId)
            .update();
        Long linkId = 1L;
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(linkId);
        //Expected
        assertThatThrownBy(() -> linkService.removeLink(
            chatId,
            removeLinkRequest
        )).isInstanceOf(LinkNotFoundException.class);
    }

    @Test
    @Transactional
    @Rollback
    public void deleteLink_shouldThrowLinkNotFoundException_whenLinkIsNotTrackedByChat() {
        //Arrange
        String url = "google.com";
        Long chatId = 1L;
        jdbcClient.sql("INSERT INTO chat(id) VALUES (:chatId)")
            .param("chatId", chatId)
            .update();
        Long linkId = jdbcClient.sql("""
                INSERT INTO link(url, type)
                VALUES (:url, :link_type)
                RETURNING id""")
            .param("url", url)
            .param("link_type", LinkType.UNKNOWN.name())
            .query(Long.class)
            .single();
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(linkId);
        //Expected
        assertThatThrownBy(() -> linkService.removeLink(
            chatId,
            removeLinkRequest
        )).isInstanceOf(LinkNotFoundException.class);
    }

    @Test
    @Transactional
    @Rollback
    public void findById_ShouldReturnDataFromDb_WhenLinkExists() {
        // Arrange
        String url = "google.com";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        OffsetDateTime time = OffsetDateTime.now();
        String expectedTime = time.atZoneSameInstant(ZoneOffset.UTC).format(formatter);
        Long linkId = jdbcClient.sql("""
                INSERT INTO link(url, type)
                VALUES (:url, :link_type)
                RETURNING id""")
            .param("url", url)
            .param("link_type", LinkType.UNKNOWN.name())
            .query(Long.class)
            .single();
        jdbcClient.sql("""
                UPDATE link SET  updated_at = :updated_at
                WHERE id = :link_id""")
            .param("updated_at", time)
            .param("link_id", linkId)
            .update();
        // Act
        Optional<LinkData> response = linkService.findById(linkId);
        manager.flush();

        // Assert
        assertTrue(response.isPresent());
        assertEquals(
            expectedTime,
            response.get().updatedAt().atZoneSameInstant(ZoneOffset.UTC).format(formatter)
        );

    }

    @Test
    @Transactional
    @Rollback
    @SneakyThrows
    public void updateInfo_shouldCorrectlyUpdateDataInDb_whenLinkInTable() {
        //Arrange
        String url = "google.com";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        OffsetDateTime time = OffsetDateTime.now();
        String expectedTime = time.format(formatter);

        Long linkId = jdbcClient.sql("""
                INSERT INTO link(url, type)
                VALUES (:url, :link_type)
                RETURNING id""")
            .param("url", url)
            .param("link_type", LinkType.UNKNOWN.name())
            .query(Long.class)
            .single();

        //Act
        linkService.updateInfo(linkId, time);
        manager.flush();
        //Assert
        OffsetDateTime update = jdbcClient.sql("SELECT updated_at FROM link WHERE url = :url")
            .param("url", url)
            .query(OffsetDateTime.class).single();

        assertThat(update.atZoneSameInstant(ZoneId.systemDefault()).format(formatter)).isEqualTo(expectedTime);
    }

    @Test
    @Transactional
    @Rollback
    public void addLink_shouldThrowLinkAlreadyTrackedException_whenLinkIsTracked() {
        //Arrange
        String url = "google.com";
        Mockito.when(client.isValidate(URI.create(url))).thenReturn(true);
        Mockito.when(client.receiveLastUpdateTime(URI.create(url)))
            .thenReturn(List.of(new LinkInfo(URI.create(url), "title", OffsetDateTime.now())));
        Long chatId = 1L;
        Long linkId = jdbcClient.sql("""
                INSERT INTO link(url, type)
                VALUES (:url, :link_type)
                RETURNING id""")
            .param("url", url)
            .param("link_type", LinkType.UNKNOWN.name())
            .query(Long.class)
            .single();
        jdbcClient.sql("INSERT INTO chat(id) VALUES (:chatId)")
            .param("chatId", chatId)
            .update();

        jdbcClient.sql("INSERT INTO chat_link(link_id, chat_id) VALUES (:linkId, :chatId)")
            .param("linkId", linkId)
            .param("chatId", chatId)
            .update();

        AddLinkRequest addLinkRequest = new AddLinkRequest(url);
        //Expected
        assertThatThrownBy(() -> linkService.addLink(
            chatId,
            addLinkRequest
        )).isInstanceOf(LinkAlreadyTrackedException.class);
    }

    @Test
    @Transactional
    @Rollback
    public void addLink_shouldThrowLinkNotSupportedException_whenLinkIsNotValid() {
        //Arrange
        String url = "google.com";
        Mockito.when(client.isValidate(URI.create(url))).thenReturn(false);
        Mockito.when(client.receiveLastUpdateTime(URI.create(url)))
            .thenReturn(List.of(new LinkInfo(URI.create(url), "title", OffsetDateTime.now())));
        Long chatId = 1L;
        jdbcClient.sql("INSERT INTO chat(id) VALUES (:chatId)")
            .param("chatId", chatId)
            .update();
        AddLinkRequest addLinkRequest = new AddLinkRequest(url);
        //Expected
        assertThatThrownBy(() -> linkService.addLink(
            chatId,
            addLinkRequest
        )).isInstanceOf(UnsupportedLinkTypeException.class);
    }

    @Test
    @Transactional
    @Rollback
    public void addLink_shouldThrowChatNotAuthorizedException_whenChatIdIsNotInDb() {
        //Arrange
        String url = "google.com";
        Mockito.when(client.isValidate(URI.create(url))).thenReturn(false);
        Mockito.when(client.receiveLastUpdateTime(URI.create(url)))
            .thenReturn(List.of(new LinkInfo(URI.create(url), "title", OffsetDateTime.now())));
        Long chatId = 1L;
        AddLinkRequest addLinkRequest = new AddLinkRequest(url);
        //Expected
        assertThatThrownBy(() -> linkService.addLink(
            chatId,
            addLinkRequest
        )).isInstanceOf(ChatNotAuthorizedException.class);
    }
}
