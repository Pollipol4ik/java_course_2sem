package edu.java.repository.link;


import edu.java.client.link_information.LastUpdateTime;
import edu.java.dto.AddLinkRequest;
import edu.java.dto.LinkData;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.RemoveLinkRequest;
import edu.java.dto.ResponseLink;
import edu.java.repository.link.mapper.LinkDataMapper;
import edu.java.repository.link.mapper.LinkResponseMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Log4j2
public class JdbcLinkRepository implements LinkRepository {
    private final LinkResponseMapper mapper = new LinkResponseMapper();
    private final LinkDataMapper dataMapper = new LinkDataMapper();

    private final JdbcTemplate jdbcTemplate;

    @Override
    public ListLinksResponse findAll(Long chatId) {
        List<ResponseLink> linksList =
            jdbcTemplate.query(
                "SELECT link.* FROM link "
                    + "JOIN chat_link ON link.id = chat_link.link_id WHERE chat_link.chat_id = ?",
                mapper,
                chatId
            );
        return new ListLinksResponse(linksList);
    }

    @Override
    public ResponseLink add(Long chatId, AddLinkRequest addLinkRequest) {
        Long linkId = jdbcTemplate.queryForObject(
            "INSERT INTO link (url) VALUES (?) RETURNING id",
            Long.class,
            addLinkRequest.link().toString()
        );
        return new ResponseLink(linkId, addLinkRequest.link());
    }

    @Override
    public ResponseLink remove(Long chatId, RemoveLinkRequest removeLinkRequest) {
        long id = removeLinkRequest.linkId();
        ResponseLink linkResponse = jdbcTemplate.queryForObject("SELECT * FROM link WHERE link_id = (?)", mapper, id);
        jdbcTemplate.update("DELETE FROM link WHERE id = (?)", id);
        return linkResponse;
    }


    @Override
    public LinkData getData(Long linkId) {
        return jdbcTemplate.queryForObject("SELECT last_update_time, url FROM link"
            + " WHERE link_id = (?)", dataMapper, linkId);
    }

    @Override
    public void updateLink(LastUpdateTime info) {

    }

    @Override
    public Long getLinkId(String url) {
        Boolean isInTable =
            jdbcTemplate.queryForObject("SELECT COUNT(id) FROM link WHERE url = (?)", Boolean.class, url);
        if (Boolean.TRUE.equals(isInTable)) {
            return jdbcTemplate.queryForObject("SELECT id FROM link WHERE url = (?)", Long.class, url);
        }
        return 0L;
    }
}
