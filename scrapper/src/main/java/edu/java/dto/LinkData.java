package edu.java.dto;

import edu.java.link_type_resolver.LinkType;
import java.time.OffsetDateTime;
import lombok.Builder;

@Builder
public record LinkData(long id, String url, LinkType type, OffsetDateTime updatedAt, OffsetDateTime lastCheckedAt) {

}
