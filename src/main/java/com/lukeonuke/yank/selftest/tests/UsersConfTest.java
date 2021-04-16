package com.lukeonuke.yank.selftest.tests;

import com.lukeonuke.yank.selftest.SelfTest;

import java.io.File;
import java.io.IOException;

public class UsersConfTest extends SelfTest {
    @Override
    public boolean run() {
        File usersConf = new File("users.conf");
        if(!usersConf.exists()){
            try {
                return usersConf.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
}
