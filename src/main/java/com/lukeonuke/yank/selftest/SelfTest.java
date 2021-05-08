package com.lukeonuke.yank.selftest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SelfTest {
    /**
     * The method witch contains the test
     * @return <b>true</b> if the test is successful and <b>false</b> if it isn't.
     */
    public abstract boolean run();

    public SelfTest() {

    }
}
