package edu.java.bot.service;

import edu.java.bot.dto.UpdateLink;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class UpdateServiceImpl implements UpdateService {
    @Override
    public void updateLink(UpdateLink linkUpdate) {
        log.info("Link was updated");
    }
}
