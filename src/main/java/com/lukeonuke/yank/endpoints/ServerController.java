package com.lukeonuke.yank.endpoints;

import com.lukeonuke.yank.data.log.LogEntry;
import com.lukeonuke.yank.data.log.LogEntryRepository;
import com.lukeonuke.yank.server.ServerRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.io.PrintStream;
import java.time.Instant;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/server/")
public class ServerController {
    @Value("${minecraft.server.port:25565}")
    String port;
    @Value("${minecraft.server.ip:localhost}")
    String ip;
    @Autowired
    ServerRunner serverRunner;

    private final Logger LOGGER = LoggerFactory.getLogger(ServerController.class);
    @Autowired
    LogEntryRepository logEntryRepository;
    @Autowired
    private ApplicationContext appContext;

    @GetMapping("host")
    public HashMap getIp(){
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("ip", ip);
        data.put("port", port);
        return data;
    }

    @PostMapping(path = "send", consumes = "text/plain;charset=UTF-8", produces = "application/json")
    public boolean sendToServer(@RequestBody String command, @AuthenticationPrincipal OAuth2User user){
        LOGGER.info(user.getAttribute("email") + " has sent command : " + command);
        serverRunner.pushAndSaveMessage(new LogEntry(user.getAttribute("email") + " has sent command : " + command, Instant.now().toEpochMilli()));
        //Halt command, if i implement more ill need to add a command manager just like the self-test.
        if(command.trim().equals("//halt")){
            serverRunner.getConsoleInput().println("stop");
            serverRunner.getConsoleInput().flush();
            SpringApplication.exit(appContext, () -> 0);
        }

        PrintStream consoleInput = serverRunner.getConsoleInput();
        consoleInput.println(command);
        consoleInput.flush();
        return true;
    }
}
