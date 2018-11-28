package com.scwot.collectables.strategy;

import com.scwot.collectables.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

public class DefaultImportStrategyTest extends AbstractTest {

    private static final File TEST_DIR =
            new File("C:\\Users\\vladislav_evseev\\Documents\\test_stuff\\Klaus Schulze & Lisa Gerrard - Big In Europe Vol.2 - Amsterdam (2CD) [FLAC]");

    @Autowired
    private DefaultImportStrategy importStrategy;

    @Test
    public void execute() {
        importStrategy.execute(TEST_DIR);
        System.out.println(importStrategy.getArtist());
    }
}