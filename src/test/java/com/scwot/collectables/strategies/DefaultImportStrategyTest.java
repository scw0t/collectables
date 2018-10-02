package com.scwot.collectables.strategies;

import com.scwot.collectables.AbstractTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DefaultImportStrategyTest extends AbstractTest {

    private static final String TEST_DIR =
            "C:\\Users\\vladislav_evseev\\Documents\\The Brian Jonestown Massacre - Something Else (2018)";

    @Autowired
    private DefaultImportStrategy importStrategy;

    @Test
    public void execute() {
        importStrategy.execute(TEST_DIR);
        System.out.println(importStrategy.getArtist());
    }
}