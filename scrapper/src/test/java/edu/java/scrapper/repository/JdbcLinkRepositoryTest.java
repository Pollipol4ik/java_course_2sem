//package edu.java.scrapper.repository;
//
//import edu.java.dto.AddLinkRequest;
//import edu.java.dto.LinkData;
//import edu.java.dto.ListLinksResponse;
//import edu.java.link_type_resolver.LinkType;
//import edu.java.repository.link.JdbcLinkRepository;
//import java.time.Duration;
//import java.time.OffsetDateTime;
//import java.util.List;
//import java.util.Optional;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.jdbc.core.simple.JdbcClient;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringBootTest
//public class JdbcLinkRepositoryTest {
//
//    @Autowired
//    private JdbcClient jdbcClient;
//
//    @Autowired
//    private JdbcLinkRepository repository;
//
//    @Transactional
//    @Rollback
//    @Test
//    public void findAllShouldReturnAllLinks() {
//        jdbcClient.sql(
//                "INSERT INTO link(id, url, type, updated_at, last_checked_at) VALUES (1, 'example.com', 'UNKNOWN', now(), now())")
//            .update();
//        jdbcClient.sql(
//                "INSERT INTO link(id, url, type, updated_at, last_checked_at) VALUES (2, 'test.com', 'UNKNOWN', now(), now())")
//            .update();
//
//        ListLinksResponse response = repository.findAll(0L);
//
//        assertThat(response).isNotNull();
//        assertThat(response.links()).hasSize(2);
//    }
//
//    @Transactional
//    @Rollback
//    @Test
//    public void addShouldReturnGeneratedId() {
//        AddLinkRequest addLinkRequest = new AddLinkRequest("newlink.com");
//        LinkType linkType = LinkType.UNKNOWN;
//
//        long generatedId = repository.add(0L, addLinkRequest, linkType);
//
//        assertThat(generatedId).isGreaterThan(0);
//    }
//
//    @Transactional
//    @Rollback
//    @Test
//    public void findAllByChatIdShouldReturnLinksForChatId() {
//        long chatId = 1L;
//        long linkId1 = 1L;
//        long linkId2 = 2L;
//
//        jdbcClient.sql("INSERT INTO chat(id) VALUES (:chatId)").param("chatId", chatId).update();
//        jdbcClient.sql(
//                "INSERT INTO link(id, url, type, updated_at, last_checked_at) VALUES (:linkId, 'example.com', 'UNKNOWN', now(), now())")
//            .param("linkId", linkId1).update();
//        jdbcClient.sql(
//                "INSERT INTO link(id, url, type, updated_at, last_checked_at) VALUES (:linkId, 'test.com', 'UNKNOWN', now(), now())")
//            .param("linkId", linkId2).update();
//        jdbcClient.sql("INSERT INTO chat_link(chat_id, link_id) VALUES (:chatId, :linkId)").param("chatId", chatId)
//            .param("linkId", linkId1).update();
//        jdbcClient.sql("INSERT INTO chat_link(chat_id, link_id) VALUES (:chatId, :linkId)").param("chatId", chatId)
//            .param("linkId", linkId2).update();
//
//        ListLinksResponse response = repository.findAllByChatId(chatId);
//
//        assertThat(response).isNotNull();
//        assertThat(response.links()).hasSize(2);
//    }
//
//    @Rollback
//    @Test
//    public void removeShouldRemoveLink() {
//        long linkId = 1L;
//        jdbcClient.sql(
//                "INSERT INTO link(id, url, type, updated_at, last_checked_at) VALUES (:linkId, 'example.com', 'UNKNOWN', now(), now())")
//            .param("linkId", linkId).update();
//
//        repository.remove(linkId);
//
//        Optional<LinkData> linkDataOptional = repository.findById(linkId);
//        assertThat(linkDataOptional).isEmpty();
//    }
//
//    @Transactional
//    @Rollback
//    @Test
//    public void findByUrlShouldReturnLinkIfExists() {
//        long linkId = 1L;
//        String url = "example.com";
//        jdbcClient.sql(
//                "INSERT INTO link(id, url, type, updated_at, last_checked_at) VALUES (:linkId, :url, 'UNKNOWN', now(), now())")
//            .param("linkId", linkId).param("url", url).update();
//
//        Optional<LinkData> linkDataOptional = repository.findByUrl(url);
//
//        assertThat(linkDataOptional).isPresent();
//        assertThat(linkDataOptional.get().id()).isEqualTo(linkId);
//    }
//
//    @Transactional
//    @Rollback
//    @Test
//    public void findByUrlShouldReturnEmptyOptionalIfNotExists() {
//        String nonExistentUrl = "nonexistent.com";
//
//        Optional<LinkData> linkDataOptional = repository.findByUrl(nonExistentUrl);
//
//        assertThat(linkDataOptional).isEmpty();
//    }
//
//    @Transactional
//    @Rollback
//    @Test
//    public void findUncheckedLinksShouldReturnLinksToUpdate() {
//        long linkId1 = 1L;
//        long linkId2 = 2L;
//        Duration checkPeriod = Duration.ofDays(1);
//
//        OffsetDateTime now = OffsetDateTime.now();
//        OffsetDateTime lastCheckedAt1 = now.minusDays(2);
//        OffsetDateTime lastCheckedAt2 = now.minusDays(1);
//
//        jdbcClient.sql(
//                "INSERT INTO link(id, url, type, updated_at, last_checked_at) VALUES (:linkId, 'link1.com', 'UNKNOWN', :now, :lastCheckedAt)")
//            .param("linkId", linkId1).param("now", now).param("lastCheckedAt", lastCheckedAt1).update();
//        jdbcClient.sql(
//                "INSERT INTO link(id, url, type, updated_at, last_checked_at) VALUES (:linkId, 'link2.com', 'UNKNOWN', :now, :lastCheckedAt)")
//            .param("linkId", linkId2).param("now", now).param("lastCheckedAt", lastCheckedAt2).update();
//
//        List<LinkData> uncheckedLinks = repository.findUncheckedLinks(checkPeriod);
//
//        assertThat(uncheckedLinks).hasSize(1);
//        assertThat(uncheckedLinks.get(0).id()).isEqualTo(linkId1);
//    }
//
//    @Transactional
//    @Rollback
//    @Test
//    public void updateInfoShouldUpdateLastCheckedAtAndUpdatedAt() {
//        long linkId = 1L;
//        OffsetDateTime updatedAt = OffsetDateTime.now().minusDays(1);
//        jdbcClient.sql(
//                "INSERT INTO link(id, url, type, updated_at, last_checked_at) VALUES (:linkId, 'example.com', 'UNKNOWN', :updatedAt, now())")
//            .param("linkId", linkId).param("updatedAt", updatedAt).update();
//
//        repository.updateInfo(linkId, updatedAt);
//
//        Optional<LinkData> updatedLinkOptional = repository.findById(linkId);
//        assertThat(updatedLinkOptional).isPresent();
//        assertThat(updatedLinkOptional.get().lastCheckedAt()).isAfterOrEqualTo(updatedAt);
//    }
//}
