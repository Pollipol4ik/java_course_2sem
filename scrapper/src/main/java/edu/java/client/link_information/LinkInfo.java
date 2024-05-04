package edu.java.client.link_information;

import java.net.URI;
import java.time.OffsetDateTime;

public record LinkInfo(URI url, String title, OffsetDateTime lastActivityDate) {

}
