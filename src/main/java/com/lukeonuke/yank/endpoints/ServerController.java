package com.lukeonuke.yank.endpoints;

import com.lukeonuke.yank.data.log.LogEntry;
import com.lukeonuke.yank.data.log.LogEntryRepository;
import com.lukeonuke.yank.server.ServerRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Autowired
    LogEntryRepository logEntryRepository;

    @GetMapping("host")
    public HashMap getIp(){
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("ip", ip);
        data.put("port", port);
        return data;
    }

    @PostMapping(path = "send", consumes = "text/plain;charset=UTF-8", produces = "application/json")
    public boolean sendToServer(@RequestBody String command){
        PrintStream consoleInput = serverRunner.getConsoleInput();
        consoleInput.println(command);
        consoleInput.flush();
        return true;
    }
}
