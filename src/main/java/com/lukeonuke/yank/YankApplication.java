package com.lukeonuke.yank;

import com.lukeonuke.yank.selftest.SelfTestRunner;
import com.lukeonuke.yank.selftest.tests.ProgramPropertiesTest;
import com.lukeonuke.yank.selftest.tests.ServerFilesNotLockedTest;
import com.lukeonuke.yank.selftest.tests.UsersConfTest;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
public class YankApplication {
    private static ArrayList<String> argList;
    public static void main(String[] args) {
        argList = new ArrayList<>(Arrays.asList(args));

        SelfTestRunner selfTestRunner = new SelfTestRunner();
        selfTestRunner.register(new ServerFilesNotLockedTest());
        selfTestRunner.register(new UsersConfTest());
        selfTestRunner.register(new ProgramPropertiesTest());
        if(!argList.contains("-skiptests")){
            selfTestRunner.run();
        }

        SpringApplication.run(YankApplication.class, args);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer properties =
                new PropertySourcesPlaceholderConfigurer();
        properties.setLocation(new FileSystemResource("program.properties"));
        properties.setIgnoreResourceNotFound(false);
        return properties;
    }

    /**
     * Get the list of arguments
     * @return Arguments in type <b>ArrayList</b>
     */
    public static ArrayList<String> getArgList() {
        return argList;
    }
}
