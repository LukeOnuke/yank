package com.lukeonuke.yank;

import com.lukeonuke.yank.data.log.LogEntry;
import com.lukeonuke.yank.data.log.LogEntryRepository;
import com.lukeonuke.yank.server.ServerRunner;
import com.lukeonuke.yank.service.SseServiceImpl;
import org.apache.catalina.servlets.DefaultServlet;
import org.apache.juli.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/log/")
public class LogController {
    @Autowired
    LogEntryRepository repository;

    @Autowired
    private SseServiceImpl sseService;

    private final Logger LOGGER = LoggerFactory.getLogger(ServerRunner.class);

    @GetMapping("id/{id}")
    public LogEntry getLogById(@PathVariable Long id, @AuthenticationPrincipal OAuth2User user, HttpServletResponse response){
        if(!Objects.requireNonNull(user.getAttribute("email")).equals("lukakresoja3@gmail.com")){
            throw new AccessDeniedException("NOT AUTHORISED");
        }
        Optional<LogEntry> logs = repository.findById(id);
        return logs.orElse(null);
    }

    @GetMapping("subscription")
    public SseEmitter subscribe() throws IOException {

        SseEmitter emitter = new SseEmitter(-1L);
        sseService.add(emitter);
        emitter.onError((me) -> {
            emitter.complete();
            sseService.remove(emitter);
            LOGGER.info(me.getMessage());
        });

        return emitter;
    }
}
