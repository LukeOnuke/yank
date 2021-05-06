package com.lukeonuke.yank.server;

import com.lukeonuke.yank.data.log.LogEntry;
import com.lukeonuke.yank.data.log.LogEntryRepository;
import com.lukeonuke.yank.service.SseServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.PreDestroy;
import java.io.*;
import java.time.Instant;

@Component
@Profile("!test")
public class ServerRunner implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerRunner.class);
    private PrintStream consoleInput;
    private BufferedReader consoleOutput;
    private Process process = null;
    @Autowired
    SseServiceImpl sseService;
    @Autowired
    LogEntryRepository repository;
    @Value("${minecraft.server.start:java -Xmx5120M -Xms1024M -jar server.jar -nogui}")
    String startCommand;
    @Value("${minecraft.server.restartCount:5}")
    Long maxRestartCount;
    private long restartCount=0;
    @Autowired ApplicationContext appContext;

    @Override
    public void run(String... args) throws Exception {
        startServer();
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

        pushMessage(data, MediaType.APPLICATION_JSON);
    }

    private void startServer(){
        String[] command; //= new String[]{"java", "-Xmx5120M", "-Xms1024M", "-jar", "server.jar", "-nogui"};
        command = startCommand.split(" ");
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
                if (save != null) {
                    if(!save.equals("")){
                        try{
                            log(save);
                        }catch (IllegalStateException ex){
                            LOGGER.warn("Database is unreachable when trying to write to it");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        restartCount++;
        if(restartCount < maxRestartCount){
            LOGGER.info("Restart attempt " + (restartCount + 1) + "/" + maxRestartCount);
            startServer();
        }else{
            LOGGER.info("Reached max restart interval of : " + maxRestartCount);
            SpringApplication.exit(appContext, () -> 0);
        }

    }

    public BufferedReader getConsoleOutput() {
        return consoleOutput;
    }

    public PrintStream getConsoleInput() {
        return consoleInput;
    }

    public void pushMessage(Object message, MediaType mediaType){
        //send over using web push
        SseEmitter emitter;
        for(int index = 0; index < sseService.getSsEmitters().size(); index++){
            emitter = sseService.getSsEmitters().get(index);
            try {
                emitter.send(message, mediaType);
            } catch (IOException e) {
                sseService.remove(emitter);
            }
        }
    }

    public void pushAndSaveMessage(LogEntry entry){
        pushMessage(entry, MediaType.APPLICATION_JSON);
        repository.save(entry);
    }
}
