package edu.java.scrapper.service;

import edu.java.client.github.GithubClient;
import edu.java.client.link_information.LinkInfo;
import edu.java.client.link_information.LinkInfoReceiver;
import edu.java.dto.AddLinkRequest;
import edu.java.dto.LinkData;
import edu.java.dto.RemoveLinkRequest;
import edu.java.dto.ResponseLink;
import edu.java.exception.ChatNotAuthorizedException;
import edu.java.exception.LinkAlreadyTrackedException;
import edu.java.exception.LinkNotFoundException;
import edu.java.exception.UnsupportedLinkTypeException;
import edu.java.link_type_resolver.LinkType;
import edu.java.repository.chat.ChatRepository;
import edu.java.repository.chat.JdbcChatRepository;
import edu.java.repository.chat_link.ChatLinkRepository;
import edu.java.repository.chat_link.JdbcChatLinkRepository;
import edu.java.repository.link.JdbcLinkRepository;
import edu.java.repository.link.LinkRepository;
import edu.java.service.link.DefaultLinkService;
import edu.java.service.link.LinkService;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DefaultLinkServiceTest {
    @Test
    public void addLink_shouldReturnLinkResponse_whenLinkIsValidAndLinkWasNotInBd() {
        //Arrange
        long chatId = 1L;
        long linkId = 1L;
        String url = "https://github.com/Pollipol4ik/java_course_2sem";
        AddLinkRequest addLinkRequest =
            new AddLinkRequest(url);
        LinkRepository linkRepository = Mockito.mock(JdbcLinkRepository.class);
        Mockito.when(linkRepository.findByUrl(url)).thenReturn(Optional.of(new LinkData(linkId,
            url,
            LinkType.UNKNOWN,
            OffsetDateTime.now(),
            OffsetDateTime.now()
        )));
        Mockito.when(linkRepository.add(chatId, addLinkRequest, LinkType.UNKNOWN)).thenReturn(linkId);
        ChatLinkRepository chatLinkRepository = Mockito.mock(JdbcChatLinkRepository.class);
        Mockito.doNothing().when(chatLinkRepository).add(chatId, linkId);
        ChatRepository chatRepository = Mockito.mock(JdbcChatRepository.class);
        Mockito.when(chatRepository.doesExist(chatId)).thenReturn(true);
        LinkInfoReceiver client = Mockito.mock(GithubClient.class);
        Mockito.when(client.isValidate(URI.create(url))).thenReturn(true);
        Mockito.when(client.receiveLastUpdateTime(URI.create(url)))
            .thenReturn(List.of(new LinkInfo(URI.create(url), "title", OffsetDateTime.now())));
        LinkService service =
            new DefaultLinkService(linkRepository, chatLinkRepository, chatRepository, List.of(client));
        ResponseLink expected = new ResponseLink(linkId, url);
        //Act
        ResponseLink response = service.addLink(chatId, addLinkRequest);
        //Assert
        assertThat(response).isEqualTo(expected);
        Mockito.verify(chatLinkRepository, Mockito.times(1)).add(chatId, linkId);
    }

    @Test
    public void addLink_shouldThrowLinkNotSupportedException_whenLinkNotSupported() {
        //Arrange
        long chatId = 1L;
        String url = "https://github.com/Pollipol4ik/java_course_2sem";
        AddLinkRequest addLinkRequest =
            new AddLinkRequest(url);
        LinkRepository linkRepository = Mockito.mock(JdbcLinkRepository.class);
        ChatLinkRepository chatLinkRepository = Mockito.mock(JdbcChatLinkRepository.class);
        ChatRepository chatRepository = Mockito.mock(JdbcChatRepository.class);
        Mockito.when(chatRepository.doesExist(chatId)).thenReturn(true);
        LinkInfoReceiver client = Mockito.mock(GithubClient.class);
        Mockito.when(client.isValidate(URI.create(url))).thenReturn(false);
        LinkService service =
            new DefaultLinkService(linkRepository, chatLinkRepository, chatRepository, List.of(client));
        //Expect
        assertThatThrownBy(() -> service.addLink(
            chatId,
            addLinkRequest
        )).isInstanceOf(UnsupportedLinkTypeException.class);
    }

    @Test
    public void addLink_shouldThrowLinkAlreadyTrackedException_whenLinkExistsInBd() {
        //Arrange
        long chatId = 1L;
        long linkId = 1L;
        String url = "https://github.com/Pollipol4ik/java_course_2sem";
        AddLinkRequest addLinkRequest =
            new AddLinkRequest(url);
        LinkRepository linkRepository = Mockito.mock(JdbcLinkRepository.class);
        Mockito.when(linkRepository.findByUrl(url)).thenReturn(Optional.of(new LinkData(linkId,
            url,
            LinkType.UNKNOWN,
            OffsetDateTime.now(),
            OffsetDateTime.now()
        )));
        Mockito.when(linkRepository.add(chatId, addLinkRequest, LinkType.UNKNOWN)).thenReturn(linkId);
        ChatLinkRepository chatLinkRepository = Mockito.mock(JdbcChatLinkRepository.class);
        Mockito.doNothing().when(chatLinkRepository).add(chatId, linkId);
        Mockito.when(chatLinkRepository.isTracked(chatId, linkId)).thenReturn(true);
        ChatRepository chatRepository = Mockito.mock(JdbcChatRepository.class);
        Mockito.when(chatRepository.doesExist(chatId)).thenReturn(true);
        LinkInfoReceiver client = Mockito.mock(GithubClient.class);
        Mockito.when(client.isValidate(URI.create(url))).thenReturn(true);
        Mockito.when(client.receiveLastUpdateTime(URI.create(url)))
            .thenReturn(List.of(new LinkInfo(URI.create(url), "title", OffsetDateTime.now())));
        LinkService service =
            new DefaultLinkService(linkRepository, chatLinkRepository, chatRepository, List.of(client));
        //Expect
        assertThatThrownBy(() -> service.addLink(
            chatId,
            addLinkRequest
        )).isInstanceOf(LinkAlreadyTrackedException.class);
    }

    @Test
    public void addLink_shouldReturnLinkResponse_whenLinkIsValidAndLinkWasInBd() {
        long chatId = 1L;
        long linkId = 1L;
        String url = "https://github.com/Pollipol4ik/java_course_2sem";
        AddLinkRequest addLinkRequest = new AddLinkRequest(url);
        LinkRepository linkRepository = Mockito.mock(LinkRepository.class);
        Mockito.when(linkRepository.findByUrl(url)).thenReturn(Optional.of(new LinkData(linkId,
            url,
            LinkType.UNKNOWN,
            OffsetDateTime.now(),
            OffsetDateTime.now()
        )));
        Mockito.when(linkRepository.add(chatId, addLinkRequest, LinkType.UNKNOWN)).thenReturn(linkId);
        Mockito.when(linkRepository.findById(linkId)).thenReturn(Optional.of(new LinkData(linkId,
            url,
            LinkType.UNKNOWN,
            OffsetDateTime.now(),
            OffsetDateTime.now()
        )));
        ChatLinkRepository chatLinkRepository = Mockito.mock(ChatLinkRepository.class);
        Mockito.doNothing().when(chatLinkRepository).add(chatId, linkId);
        Mockito.when(chatLinkRepository.isTracked(chatId, linkId)).thenReturn(false);
        ChatRepository chatRepository = Mockito.mock(ChatRepository.class);
        Mockito.when(chatRepository.doesExist(chatId)).thenReturn(true);
        LinkInfoReceiver client = Mockito.mock(GithubClient.class);
        Mockito.when(client.isValidate(URI.create(url))).thenReturn(true);
        Mockito.when(client.receiveLastUpdateTime(URI.create(url)))
            .thenReturn(List.of(new LinkInfo(URI.create(url), "title", OffsetDateTime.now())));
        LinkService service =
            new DefaultLinkService(linkRepository, chatLinkRepository, chatRepository, List.of(client));
        ResponseLink expected = new ResponseLink(linkId, url);
        ResponseLink response = service.addLink(chatId, addLinkRequest);
        assertThat(response).isEqualTo(expected);
        Mockito.verify(chatLinkRepository, Mockito.times(1)).add(chatId, linkId);
    }

    @Test
    public void addLink_shouldThrowChatNotAuthorizedException_whenChatWasNotInBd() {
        //Arrange
        long chatId = 1L;
        String url = "https://github.com/Pollipol4ik/java_course_2sem";
        AddLinkRequest addLinkRequest =
            new AddLinkRequest(url);
        LinkRepository linkRepository = Mockito.mock(JdbcLinkRepository.class);
        ChatLinkRepository chatLinkRepository = Mockito.mock(JdbcChatLinkRepository.class);
        ChatRepository chatRepository = Mockito.mock(JdbcChatRepository.class);
        Mockito.when(chatRepository.doesExist(chatId)).thenReturn(false);
        LinkInfoReceiver client = Mockito.mock(GithubClient.class);
        LinkService service =
            new DefaultLinkService(linkRepository, chatLinkRepository, chatRepository, List.of(client));
        //Expect
        assertThatThrownBy(() -> service.addLink(
            chatId,
            addLinkRequest
        )).isInstanceOf(ChatNotAuthorizedException.class);
    }

    @Test
    public void deleteLink_shouldReturnLinkResponseAndRemoveLinkFromLinkTable_whenOnlyOneUserTracksIt() {
        long chatId = 1L;
        long linkId = 1L;
        String url = "google.com";
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(linkId);

        LinkRepository linkRepository = Mockito.mock(LinkRepository.class);

        Mockito.when(linkRepository.findById(linkId)).thenReturn(Optional.of(new LinkData(linkId,
            url,
            LinkType.UNKNOWN,
            OffsetDateTime.now(),
            OffsetDateTime.now()
        )));

        ChatLinkRepository chatLinkRepository = Mockito.mock(ChatLinkRepository.class);
        Mockito.doNothing().when(chatLinkRepository).remove(linkId, chatId);
        Mockito.when(chatLinkRepository.isTracked(chatId, linkId)).thenReturn(true);
        Mockito.when(chatLinkRepository.findAllByLinkId(linkId)).thenReturn(List.of());

        ChatRepository chatRepository = Mockito.mock(ChatRepository.class);
        LinkInfoReceiver client = Mockito.mock(GithubClient.class);

        LinkService service =
            new DefaultLinkService(linkRepository, chatLinkRepository, chatRepository, List.of(client));

        ResponseLink expected = new ResponseLink(linkId, url);

        ResponseLink response = service.removeLink(chatId, removeLinkRequest);

        assertThat(response).isEqualTo(expected);
        Mockito.verify(linkRepository, Mockito.times(1)).remove(linkId);
    }

    @Test
    public void deleteLink_shouldThrowLinkNotFoundException_whenNobodyTracksIt() {
        // Arrange
        long chatId = 1L;
        long linkId = 1L;
        RemoveLinkRequest removeLinkRequest = new RemoveLinkRequest(linkId);
        LinkRepository linkRepository = Mockito.mock(LinkRepository.class);
        Mockito.when(linkRepository.findById(linkId)).thenReturn(Optional.empty());

        ChatLinkRepository chatLinkRepository = Mockito.mock(ChatLinkRepository.class);
        Mockito.when(chatLinkRepository.isTracked(chatId, linkId)).thenReturn(false);
        Mockito.when(chatLinkRepository.findAllByLinkId(linkId)).thenReturn(Collections.emptyList());
        ChatRepository chatRepository = Mockito.mock(ChatRepository.class);
        LinkInfoReceiver client = Mockito.mock(GithubClient.class);
        LinkService service =
            new DefaultLinkService(linkRepository, chatLinkRepository, chatRepository, List.of(client));

        // Act & Assert
        assertThatThrownBy(() -> service.removeLink(chatId, removeLinkRequest))
            .isInstanceOf(LinkNotFoundException.class)
            .hasMessage("Link id = %s not found", removeLinkRequest.linkId());
    }

}
