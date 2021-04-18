package com.lukeonuke.yank.selftest.tests;

import com.lukeonuke.yank.selftest.SelfTest;

import java.io.*;
import java.util.Properties;

public class ProgramPropertiesTest extends SelfTest {
    @Override
    public boolean run() {
        File programProperties = new File("program.properties");
        Properties prop = new Properties();
        if(!programProperties.exists()){
            try {
                programProperties.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            try (OutputStream output = new FileOutputStream(programProperties)) {
                prop.setProperty("spring.security.oauth2.client.registration.google.client-id", "client-id");
                prop.setProperty("spring.security.oauth2.client.registration.google.client-secret", "secret");
                prop.setProperty("spring.jpa.hibernate.ddl-auto", "update");
                prop.setProperty("spring.datasource.url", "jdbc:h2:file:" + new File("db").getAbsolutePath());
                prop.setProperty("minecraft.server.ip", "IP");
                prop.setProperty("minecraft.server.port", "25565");
                prop.setProperty("minecraft.server.start", "java -Xmx5120M -Xms1024M -jar server.jar -nogui");
                prop.store(output, "Yank configuration file, look to yank docs for more info");
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
