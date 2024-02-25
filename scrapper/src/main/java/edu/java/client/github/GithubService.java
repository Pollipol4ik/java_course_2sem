package edu.java.client.github;

import edu.java.client.dto.github.RepositoryResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface GithubService {

    @GetExchange("/repos/{owner}/{repo}")
    RepositoryResponse getRepository(@PathVariable String owner, @PathVariable String repo);
}
