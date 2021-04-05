package com.lukeonuke.yank;

import com.lukeonuke.yank.server.ServerRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.PrintStream;

@RestController
@RequestMapping("/api/v1/server/")
public class ServerController {
    @Value("${server.port:25565}")
    long port;
    @Value("${server.ip:localhost}")
    String ip;
    @Autowired
    ServerRunner serverRunner;

    @GetMapping("ip")
    public String getIp(){
        return ip;
    }

    @GetMapping("port")
    public String getPort(){
        return ip;
    }

    @PostMapping(path = "send", consumes = "text/plain;charset=UTF-8", produces = "application/json")
    public boolean sendToServer(@RequestBody String string){
        PrintStream consoleInput = serverRunner.getConsoleInput();
        consoleInput.println(string);
        consoleInput.flush();
        return true;
    }
}
