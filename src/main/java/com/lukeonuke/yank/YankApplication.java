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

@SpringBootApplication
public class YankApplication {
    public static void main(String[] args) {
        SelfTestRunner selfTestRunner = new SelfTestRunner();
        selfTestRunner.register(new ServerFilesNotLockedTest());
        selfTestRunner.register(new UsersConfTest());
        selfTestRunner.register(new ProgramPropertiesTest());
        selfTestRunner.run();


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
}
