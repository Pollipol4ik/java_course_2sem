package edu.java.scrapper.link_type_resolver;

import edu.java.link_type_resolver.GithubLinkResolver;
import edu.java.link_type_resolver.LinkType;
import edu.java.link_type_resolver.LinkTypeResolver;
import edu.java.link_type_resolver.StackOverflowLinkResolver;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;

public class LinkTypeResolverTest {

    private static Stream<Arguments> inputs() {
        return Stream.of(
            Arguments.of("https://github.com/Pollipol4ik/java_course_2sem", LinkType.GITHUB),
            Arguments.of("https://github.com/Pollipol4ik/java_course_2sem/pull/3", LinkType.GITHUB),
            Arguments.of(
                "https://stackoverflow.com/questions/78055712/different-values-in-an-array-after-assignment-but-identical-values-after-loggin",
                LinkType.STACKOVERFLOW
            ),
            Arguments.of("https://stackoverflow.com/questions/78055712", LinkType.STACKOVERFLOW),
            Arguments.of("https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#io.rest-client.webclient", LinkType.UNKNOWN)
        );
    }

    @ParameterizedTest()
    @MethodSource("inputs")
    @DisplayName("LinkTypeResolver#resolve test")
    public void resolve_shouldReturnCorrectLinkType(String link, LinkType expected) {
        LinkTypeResolver resolver = LinkTypeResolver.link(new GithubLinkResolver(), new StackOverflowLinkResolver());

        LinkType actual = resolver.resolve(link);

        assertThat(actual).isEqualTo(expected);
    }
}
