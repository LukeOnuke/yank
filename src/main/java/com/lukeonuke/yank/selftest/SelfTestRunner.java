package com.lukeonuke.yank.selftest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class SelfTestRunner {
    private ArrayList<SelfTest> tests = new ArrayList<>();
    private final Logger LOGGER = LoggerFactory.getLogger(SelfTestRunner.class);

    public boolean register(SelfTest test) {
        return tests.add(test);
    }

    public void run() {
        LOGGER.info("Executing self tests");
        tests.forEach((test) -> {
            LOGGER.info("Executing self-test : " + test.getClass().getSimpleName());
            boolean resp = test.run();
            if (resp) {
                LOGGER.info(appendClassName(test, "Self test passed"));
                LOGGER.info("==============================================");
            } else {
                LOGGER.error(appendClassName(test, "Test failed, see https://lukeonuke.com/yank/errors/#" + test.getClass().getSimpleName()));
                System.exit(1);
            }
        });
    }

    private String appendClassName(SelfTest selfTest, String message) {
        return "[ " + selfTest.getClass().getName() + "] : " + message;
    }
}
