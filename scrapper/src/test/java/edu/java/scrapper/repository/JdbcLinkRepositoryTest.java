package edu.java.scrapper.repository;

import edu.java.dto.AddLinkRequest;
import edu.java.dto.LinkData;
import edu.java.dto.ListLinksResponse;
import edu.java.link_type_resolver.LinkType;
import edu.java.repository.chat.ChatRepository;
import edu.java.repository.link.LinkRepository;
import edu.java.scrapper.IntegrationEnvironment;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
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
public class JdbcLinkRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private JdbcClient jdbcClient;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Test
    @Transactional
    @Rollback
    public void add_shouldAddLinkToDatabase_whenLinkDoesNotExist() {
        // Arrange
        long chatId = 1L;
        AddLinkRequest addLinkRequest = new AddLinkRequest("https://example.com");
        LinkType linkType = LinkType.UNKNOWN;

        // Act
        long linkId = linkRepository.add(chatId, addLinkRequest, linkType);

        // Assert
        Long idCount = jdbcClient.sql("SELECT COUNT(*) FROM link WHERE id = :id")
            .param("id", linkId)
            .query(Long.class)
            .single();
        assertThat(idCount).isEqualTo(1L);
    }

    @Test
    @Transactional
    @Rollback
    public void findAll_shouldReturnListOfLinks_whenLinksExist() {
        // Arrange
        long chatId = 1L;
        AddLinkRequest addLinkRequest = new AddLinkRequest("https://example.com");
        LinkType linkType = LinkType.UNKNOWN;
        linkRepository.add(chatId, addLinkRequest, linkType);

        // Act
        ListLinksResponse listLinksResponse = linkRepository.findAll(chatId);

        // Assert
        assertThat(listLinksResponse.links()).isNotEmpty();
    }

    @Test
    @Transactional
    @Rollback
    public void findById_shouldReturnLinkData_whenLinkExists() {
        // Arrange
        long chatId = 1L;
        AddLinkRequest addLinkRequest = new AddLinkRequest("https://example.com");
        LinkType linkType = LinkType.UNKNOWN;
        long linkId = linkRepository.add(chatId, addLinkRequest, linkType);

        // Act
        Optional<LinkData> linkDataOptional = linkRepository.findById(linkId);

        // Assert
        assertThat(linkDataOptional).isPresent();
    }

    @Test
    @Transactional
    @Rollback
    public void updateInfo_shouldUpdateLinkData_whenLinkExists() {
        // Arrange
        long chatId = 1L;
        AddLinkRequest addLinkRequest = new AddLinkRequest("https://example.com");
        LinkType linkType = LinkType.UNKNOWN;
        long linkId = linkRepository.add(chatId, addLinkRequest, linkType);
        OffsetDateTime updatedAt = OffsetDateTime.now();

        // Act
        linkRepository.updateInfo(linkId, updatedAt);
        Optional<LinkData> updatedLinkData = linkRepository.findById(linkId);

        // Assert
        assertThat(updatedLinkData).isPresent();
        assertThat(updatedLinkData.get().updatedAt().truncatedTo(ChronoUnit.SECONDS))
            .isEqualTo(updatedAt.truncatedTo(ChronoUnit.SECONDS));
    }
}
