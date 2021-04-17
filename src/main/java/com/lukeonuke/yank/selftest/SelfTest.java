package com.lukeonuke.yank.selftest;

public abstract class SelfTest {
    /**
     * The method witch contains the test
     * @return <b>true</b> if the test is successful and <b>false</b> if it isn't.
     */
    public abstract boolean run();

    public SelfTest() {

    }
}
