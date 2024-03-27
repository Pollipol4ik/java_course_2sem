package edu.java.service.link.jdbc;

import edu.java.dto.AddLinkRequest;
import edu.java.dto.Chat;
import edu.java.dto.LinkData;
import edu.java.dto.ListLinksResponse;
import edu.java.dto.RemoveLinkRequest;
import edu.java.dto.ResponseLink;
import edu.java.exception.LinkNotFoundException;
import edu.java.exception.UnsupportedLinkTypeException;
import edu.java.link_type_resolver.LinkType;
import edu.java.link_type_resolver.LinkTypeResolver;
import edu.java.repository.chat_link.ChatLinkRepository;
import edu.java.repository.link.LinkRepository;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class JdbcLinkServiceTest {

    @Mock
    private LinkRepository linkRepository;

    @Mock
    private ChatLinkRepository chatLinkRepository;

    @Mock
    private LinkTypeResolver linkTypeResolver;

    @InjectMocks
    private JdbcLinkService linkService;

    @Test
    public void getAllLinks_shouldReturnAllLinksForChat() {
        // Arrange
        long chatId = 1L;
        ListLinksResponse expectedResponse = new ListLinksResponse(Collections.emptyList());
        when(linkRepository.findAllByChatId(anyLong())).thenReturn(expectedResponse);

        // Act
        ListLinksResponse actualResponse = linkService.getAllLinks(chatId);

        // Assert
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    public void addLink_shouldAddLinkSuccessfully() {
        // Arrange
        long chatId = 1L;
        AddLinkRequest addLinkRequest = new AddLinkRequest("https://github.com/Pollipol4ik/java_course_2sem");
        LinkType linkType = LinkType.GITHUB;
        when(linkTypeResolver.resolve(anyString())).thenReturn(linkType);
        when(linkRepository.add(anyLong(), any(AddLinkRequest.class), any(LinkType.class))).thenReturn(1L);

        // Act
        ResponseLink responseLink = linkService.addLink(chatId, addLinkRequest);

        // Assert
        assertThat(responseLink).isNotNull();
        assertThat(responseLink.linkId()).isNotNull();

    }

    @Test
    public void addLink_shouldThrowUnsupportedLinkTypeException() {
        // Arrange
        long chatId = 1L;
        AddLinkRequest addLinkRequest = new AddLinkRequest("unsupported");
        LinkType linkType = LinkType.UNKNOWN;
        when(linkTypeResolver.resolve(anyString())).thenReturn(linkType);

        // Act & Assert
        assertThrows(UnsupportedLinkTypeException.class, () -> linkService.addLink(chatId, addLinkRequest));
    }

    @Test
    public void removeLink_shouldRemoveLinkSuccessfully() {
        // Arrange
        long chatId = 1L;
        long linkId = 1L;
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(linkId);
        LinkData linkData = new LinkData(linkId, "https://github.com/Pollipol4ik/java_course_2sem", LinkType.GITHUB,
            OffsetDateTime.parse("2024-02-18T16:38:37Z"), OffsetDateTime.parse("2024-03-23T15:27:24.429813Z")
        );
        when(linkRepository.findById(anyLong())).thenReturn(Optional.of(linkData));
        when(chatLinkRepository.findAllByLinkId(anyLong())).thenReturn(Collections.singletonList(new Chat(chatId)));

        // Act
        ResponseLink responseLink = linkService.removeLink(chatId, removeLinkRequest);

        // Assert
        assertThat(responseLink).isNotNull();
        assertThat(responseLink.linkId()).isEqualTo(linkId);
        verify(chatLinkRepository, times(1)).remove(anyLong(), anyLong());
        verify(linkRepository, never()).remove(anyLong());
    }

    @Test
    public void removeLink_shouldThrowLinkNotFoundException() {
        // Arrange
        long chatId = 1L;
        long linkId = 1L;
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(linkId);
        when(linkRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(LinkNotFoundException.class, () -> linkService.removeLink(chatId, removeLinkRequest));
    }
}
