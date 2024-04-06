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
import org.springframework.jdbc.core.JdbcTemplate;
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
    private JdbcTemplate jdbcTemplate;

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
        jdbcTemplate.update("INSERT INTO chat (id) VALUES (1)");
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
        jdbcTemplate.update("INSERT INTO chat (id) VALUES (1)");
        String url = "google.com";
        Long linkId =
            jdbcTemplate.queryForObject("INSERT INTO link (url,type) VALUES (?,?) RETURNING id", Long.class, url,
                LinkType.UNKNOWN.toString()
            );
        jdbcTemplate.update("INSERT INTO chat_link (chat_id, link_id) VALUES (?, ?)", chatId, linkId);
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
        jdbcTemplate.update("INSERT INTO chat (id) VALUES (?)", chatId);
        Long linkId =
            jdbcTemplate.queryForObject("INSERT INTO link (url,type) VALUES (?,?) RETURNING id", Long.class, url,
                LinkType.UNKNOWN.toString()
            );
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(linkId);
        jdbcTemplate.update("INSERT INTO chat_link (chat_id, link_id) VALUES (?, ?)", chatId, linkId);
        ResponseLink expectedResponse = new ResponseLink(linkId, url);
        //Act
        ResponseLink response = linkService.removeLink(chatId, removeLinkRequest);
        manager.flush();
        //Assert
        Long count =
            jdbcTemplate.queryForObject("SELECT COUNT(id) FROM link WHERE id = ?", Long.class, linkId);
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
        jdbcTemplate.update("INSERT INTO chat (id) VALUES (?)", chatId);
        jdbcTemplate.update("INSERT INTO chat (id) VALUES (?)", secChatId);
        Long linkId =
            jdbcTemplate.queryForObject("INSERT INTO link (url,type) VALUES (?,?) RETURNING id", Long.class, url,
                LinkType.UNKNOWN.toString()
            );
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(linkId);
        jdbcTemplate.update("INSERT INTO chat_link (chat_id, link_id) VALUES (?, ?)", chatId, linkId);
        jdbcTemplate.update("INSERT INTO chat_link (chat_id, link_id) VALUES (?, ?)", secChatId, linkId);
        //Act
        linkService.removeLink(chatId, removeLinkRequest);
        manager.flush();
        //Assert
        Long count =
            jdbcTemplate.queryForObject("SELECT COUNT(id) FROM link WHERE id = ?", Long.class, linkId);
        Long chatCount =
            jdbcTemplate.queryForObject(
                "SELECT COUNT(chat_id) FROM chat_link WHERE chat_id = ?",
                Long.class,
                chatId
            );
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
        jdbcTemplate.update("INSERT INTO chat (id) VALUES (?)", chatId);
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
        jdbcTemplate.update("INSERT INTO chat (id) VALUES (?)", chatId);
        Long linkId =
            jdbcTemplate.queryForObject("INSERT INTO link (url,type) VALUES (?,?) RETURNING id", Long.class, url,
                LinkType.UNKNOWN.toString()
            );
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
        Long linkId = jdbcTemplate.queryForObject(
            "INSERT INTO link (url, type) VALUES (?, ?) RETURNING id", Long.class, url, LinkType.UNKNOWN.toString()
        );
        jdbcTemplate.update("UPDATE link SET updated_at = ? WHERE url = ?", time, url);

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

        Long linkId =
            jdbcTemplate.queryForObject("INSERT INTO link (url,type) VALUES (?,?) RETURNING id", Long.class, url,
                LinkType.UNKNOWN.toString()
            );

        //Act
        linkService.updateInfo(linkId, time);
        manager.flush();
        //Assert
        OffsetDateTime update = jdbcTemplate.queryForObject(
            "SELECT updated_at FROM link WHERE url = (?)",
            OffsetDateTime.class,
            url
        );
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
        jdbcTemplate.update("INSERT INTO chat (id) VALUES (?)", chatId);
        Long linkId =
            jdbcTemplate.queryForObject("INSERT INTO link (url,type) VALUES (?,?) RETURNING id", Long.class, url,
                LinkType.UNKNOWN.toString()
            );
        jdbcTemplate.update("INSERT INTO chat_link (chat_id, link_id) VALUES (?, ?)", chatId, linkId);
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
        jdbcTemplate.update("INSERT INTO chat (id) VALUES (?)", chatId);
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
