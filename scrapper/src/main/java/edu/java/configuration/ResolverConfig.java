package edu.java.configuration;

import edu.java.link_type_resolver.GithubLinkResolver;
import edu.java.link_type_resolver.LinkTypeResolver;
import edu.java.link_type_resolver.StackOverflowLinkResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResolverConfig {

    @Bean
    public LinkTypeResolver linkTypeResolver() {
        return LinkTypeResolver.link(new StackOverflowLinkResolver(), new GithubLinkResolver());
    }
}
