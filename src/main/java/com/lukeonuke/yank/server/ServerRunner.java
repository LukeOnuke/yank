package com.lukeonuke.yank.server;

import com.lukeonuke.yank.data.log.LogEntry;
import com.lukeonuke.yank.data.log.LogEntryRepository;
import com.lukeonuke.yank.service.SseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.PreDestroy;
import java.io.*;
import java.time.Instant;

@Component
@Profile("!test")
public class ServerRunner implements CommandLineRunner {
    private final Logger LOGGER = LoggerFactory.getLogger(ServerRunner.class);
    private PrintStream consoleInput;
    private BufferedReader consoleOutput;
    private Process process = null;
    @Autowired
    SseServiceImpl sseService;
    @Autowired
    LogEntryRepository repository;

    @Override
    public void run(String... args) throws Exception {
        // save a few customers
        String[] command = new String[]{"java", "-Xmx5120M", "-Xms1024M", "-jar", "server.jar", "-nogui"};
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(new File("server/").getAbsoluteFile());
        processBuilder.redirectErrorStream(true);
        try {
            process = processBuilder.start();//Runtime.getRuntime().exec(command, new String[]{}, new File("server/"));//Runtime.getRuntime().exec(command, new String[]{}, new File("server/"));
            LOGGER.info("[game-server] Started process with PID:" + process.pid());
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert process != null;

        consoleOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
        consoleInput = new PrintStream(process.getOutputStream());

        String save;
        while(process.isAlive()){
            try {
                save = consoleOutput.readLine();
                if(!save.equals("")){
                    log(save);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @PreDestroy
    public void onExit() {
        log("Closing server");
        assert consoleInput != null;
        consoleInput.println("stop");
        consoleInput.flush();
        LOGGER.info("Issued stop command, now waiting for it to close the game server.");
        while(process.isAlive()){
            //Wait for the server to close
        }
        LOGGER.info("Game server closed, resuming shutdown sequence.");
        consoleInput.close();
        try {
            consoleOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void log(String message){
        LOGGER.info("[game-server] " + message);

        if(message.length() >= 2047){
            message = message.substring(0, 2047);
        }
        LogEntry data = new LogEntry(message, Instant.now().toEpochMilli());
        repository.save(data);

        //send over using web push
        SseEmitter emitter;
        for(int index = 0; index < sseService.getSsEmitters().size(); index++){
            emitter = sseService.getSsEmitters().get(index);
            try {
                emitter.send(data, MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                sseService.remove(emitter);
            }
        }
        /*sseService.getSsEmitters().forEach((SseEmitter emitter) -> {
            try {
                emitter.send(data, MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                sseService.remove(emitter);
            }
        });*/
    }

    public BufferedReader getConsoleOutput() {
        return consoleOutput;
    }

    public PrintStream getConsoleInput() {
        return consoleInput;
    }
}
