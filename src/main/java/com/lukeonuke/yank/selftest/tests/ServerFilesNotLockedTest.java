package com.lukeonuke.yank.selftest.tests;

import com.lukeonuke.yank.selftest.SelfTest;

import java.io.File;
import java.nio.file.Files;

public class ServerFilesNotLockedTest extends SelfTest {
    @Override
    public boolean run() {
        return Files.isWritable(new File("server/").toPath()) && Files.isReadable(new File("server/").toPath());
    }
}
