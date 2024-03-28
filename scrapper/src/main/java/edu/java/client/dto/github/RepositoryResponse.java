package edu.java.client.dto.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

public record RepositoryResponse(
    @JsonProperty("pushed_at") OffsetDateTime lastUpdate
) {
}
