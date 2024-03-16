package edu.java.repository.link.mapper;


import java.net.URI;
import java.sql.ResultSet;
import java.sql.SQLException;
import edu.java.dto.ResponseLink;
import org.springframework.jdbc.core.RowMapper;

public class LinkResponseMapper implements RowMapper<ResponseLink> {
    @Override
    public ResponseLink mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong("id");
        URI url = URI.create(rs.getString("url"));
        return new ResponseLink(id, url.toString());
    }
}
