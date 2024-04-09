package edu.java.scrapper.repository.jooq;

import edu.java.dto.AddLinkRequest;
import edu.java.dto.LinkData;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.ResponseLink;
import edu.java.link_type_resolver.LinkType;
import edu.java.repository.link.JooqLinkRepository;
import edu.java.scrapper.IntegrationEnvironment;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DirtiesContext
public class JooqLinkRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private JdbcClient jdbcClient;
    @Autowired
    private JooqLinkRepository linkRepository;

    @Test
    @Transactional
    @Rollback
    public void findAll_shouldReturnListLinksResponseWithEmptyList_whenChatDoesNotTrackAnyLink() {
        //Arrange
        Long chatId = 1L;
        jdbcClient.sql("INSERT INTO chat(id) VALUES (:id)")
            .param("id", chatId)
            .update();
        //Act
        ListLinksResponse list = linkRepository.findAll(chatId);
        //Assert
        assertThat(list.links().size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    public void findAll_shouldReturnListLinksResponseWithLinks_whenChatTracksLinks() {
        //Arrange
        Long chatId = 1L;
        jdbcClient.sql("INSERT INTO chat(id) VALUES (:id)")
            .param("id", chatId)
            .update();
        String url = "google.com";
        Long linkId = 1L;
        jdbcClient.sql("""
                INSERT INTO link(id, url, type, updated_at, last_checked_at)
                VALUES (?, ?, ?, ?, ?)""")
            .param(linkId)
            .param(url)
            .param(LinkType.UNKNOWN.name())
            .param(null)
            .param(OffsetDateTime.now())
            .update();
        jdbcClient.sql("INSERT INTO chat_link(link_id, chat_id) VALUES (:linkId, :chatId)")
            .param("linkId", linkId)
            .param("chatId", chatId)
            .update();
        ListLinksResponse expectedList =
            new ListLinksResponse(List.of(new ResponseLink(linkId, url)));
        //Act
        ListLinksResponse actualList = linkRepository.findAll(chatId);
        //Assert
        assertThat(actualList).isEqualTo(expectedList);
    }

    @Test
    @Transactional
    @Rollback
    public void add_shouldCorrectlyAddLinkInLinkTable_thenReturnLinkResponse() {
        // Arrange
        String url = "google.com";
        AddLinkRequest addLinkRequest = new AddLinkRequest(url);

        // Act
        long linkId = linkRepository.add(1L, addLinkRequest, LinkType.UNKNOWN);
        ResponseLink response = new ResponseLink(linkId, url);

        // Assert
        Long count = jdbcClient.sql("SELECT COUNT(id) FROM link WHERE url = ?")
            .param(url)
            .query(Long.class)
            .single();
        assertThat(count).isEqualTo(1);
    }

    @Test
    @Transactional
    @Rollback
    public void remove_shouldCorrectlyRemoveLinkFromLinkTable_thenReturnLinkResponse() {
        // Arrange
        String url = "google.com";
        Long linkId = 1L;
        jdbcClient.sql("""
                INSERT INTO link(id, url, type, updated_at, last_checked_at)
                VALUES (?, ?, ?, ?, ?)""")
            .param(linkId)
            .param(url)
            .param(LinkType.UNKNOWN.name())
            .param(null)
            .param(OffsetDateTime.now())
            .update();

        // Act
        linkRepository.remove(linkId);

        // Assert
        Long count = jdbcClient.sql("SELECT COUNT(id) FROM link WHERE id = ?")
            .param(linkId)
            .query(Long.class)
            .single();
        assertThat(count).isEqualTo(0L);
    }

    @Test
    @Transactional
    @Rollback
    public void findAllByChatId_shouldReturnEmptyList_whenChatDoesNotTrackAnyLink() {
        // Arrange
        Long chatId = 1L;
        jdbcClient.sql("INSERT INTO chat(id) VALUES (?)")
            .param(chatId)
            .update();

        // Act
        ListLinksResponse list = linkRepository.findAllByChatId(chatId);

        // Assert
        assertThat(list.links().size()).isEqualTo(0);
    }

    @Test
    @Transactional
    @Rollback
    public void findAllByChatId_shouldReturnListWithLinks_whenChatTracksLinks() {
        // Arrange
        Long chatId = 1L;
        jdbcClient.sql("INSERT INTO chat(id) VALUES (?)")
            .param(chatId)
            .update();

        String url = "google.com";
        Long linkId = 1L;
        jdbcClient.sql("""
                INSERT INTO link(id, url, type, updated_at, last_checked_at)
                VALUES (?, ?, ?, ?, ?)""")
            .param(linkId)
            .param(url)
            .param(LinkType.UNKNOWN.name())
            .param(null)
            .param(OffsetDateTime.now())
            .update();

        jdbcClient.sql("INSERT INTO chat_link(link_id, chat_id) VALUES (?, ?)")
            .param(linkId)
            .param(chatId)
            .update();

        List<ResponseLink> expectedLinks = List.of(new ResponseLink(linkId, url));

        // Act
        ListLinksResponse actualList = linkRepository.findAllByChatId(chatId);

        // Assert
        assertThat(actualList.links()).isEqualTo(expectedLinks);
    }

    @Test
    @Transactional
    @Rollback
    public void findByUrl_shouldReturnEmptyOptional_whenLinkWithUrlDoesNotExist() {
        // Arrange
        String url = "google.com";

        // Act
        Optional<LinkData> linkData = linkRepository.findByUrl(url);

        // Assert
        assertThat(linkData).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    public void findByUrl_shouldReturnLinkData_whenLinkWithUrlExists() {
        // Arrange
        String url = "google.com";
        jdbcClient.sql("""
                INSERT INTO link(id, url, type, updated_at, last_checked_at)
                VALUES (?, ?, ?, ?, ?)""")
            .param(1L)
            .param(url)
            .param(LinkType.UNKNOWN.name())
            .param(OffsetDateTime.now())
            .param(OffsetDateTime.now())
            .update();

        // Act
        Optional<LinkData> linkData = linkRepository.findByUrl(url);

        // Assert
        assertThat(linkData).isPresent();
        assertThat(linkData.get().url()).isEqualTo(url);
    }

    @Test
    @Transactional
    @Rollback
    public void findById_shouldReturnEmptyOptional_whenLinkWithIdDoesNotExist() {
        // Act & Assert
        assertThat(linkRepository.findById(1L)).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    public void findById_shouldReturnLinkData_whenLinkWithIdExists() {
        // Arrange
        String url = "google.com";
        jdbcClient.sql("""
                INSERT INTO link(id, url, type, updated_at, last_checked_at)
                VALUES (?, ?, ?, ?, ?)""")
            .param(1L)
            .param(url)
            .param(LinkType.UNKNOWN.name())
            .param(OffsetDateTime.now())
            .param(OffsetDateTime.now())
            .update();

        // Act
        Optional<LinkData> linkData = linkRepository.findById(1L);

        // Assert
        assertThat(linkData).isPresent();
        assertThat(linkData.get().url()).isEqualTo(url);
    }

    @Test
    @Transactional
    @Rollback
    public void findUncheckedLinks_shouldReturnEmptyList_whenNoUncheckedLinksExist() {
        // Act
        List<LinkData> uncheckedLinks = linkRepository.findUncheckedLinks(Duration.ofDays(1));

        // Assert
        assertThat(uncheckedLinks).isEmpty();
    }

    @Test
    @Transactional
    @Rollback
    public void updateInfo_shouldUpdateLastCheckedAtAndUpdatedAt() {
        // Arrange
        String url = "google.com";
        OffsetDateTime updatedAt = OffsetDateTime.now();

        long linkId = linkRepository.add(1L, new AddLinkRequest(url), LinkType.UNKNOWN);

        // Act
        linkRepository.updateInfo(linkId, updatedAt);

        // Assert
        LinkData linkData = linkRepository.findById(linkId).orElseThrow();
        assertThat(linkData.lastCheckedAt()).isEqualToIgnoringNanos(OffsetDateTime.now());
        assertThat(linkData.updatedAt()).isEqualToIgnoringNanos(updatedAt);
    }
}
