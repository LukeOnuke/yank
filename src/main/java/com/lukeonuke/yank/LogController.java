package com.lukeonuke.yank;

import com.lukeonuke.yank.data.log.LogEntry;
import com.lukeonuke.yank.data.log.LogEntryRepository;
import com.lukeonuke.yank.exception.MalformedRequestException;
import com.lukeonuke.yank.server.ServerRunner;
import com.lukeonuke.yank.service.SseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
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
    public LogEntry getLogById(@PathVariable Long id){
        Optional<LogEntry> logs = repository.findById(id);
        return logs.orElse(null);
    }

    @GetMapping("timestamp/{timestamp}")
    public List<LogEntry> getPageByStartId(@PathVariable Long timestamp){
        return repository.findByTimestamp(timestamp);
    }

    @GetMapping("latest/{pageSize}")
    public List<LogEntry> getLatestLog(@PathVariable Integer pageSize) throws Exception{
        if(Objects.isNull(pageSize) || pageSize > 20){
            throw new MalformedRequestException("Page size bigger than 20");
        }
        return repository.findAll(PageRequest.of(0, pageSize, Sort.by(Sort.Direction.DESC, "id"))).toList();
    }


    @GetMapping("subscription")
    public SseEmitter subscribe() throws IOException {

        SseEmitter emitter = new SseEmitter(-1L);
        sseService.add(emitter);
        emitter.onError((me) -> {
            emitter.complete();
            sseService.remove(emitter);
        });
        emitter.onCompletion(() ->{
            sseService.remove(emitter);
        });

        return emitter;
    }
}
