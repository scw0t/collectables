package com.scwot.collectables.tasks;

import com.google.common.collect.Lists;
import com.scwot.collectables.entities.Release;
import com.scwot.collectables.strategies.DefaultImportStrategy;
import javafx.concurrent.Task;

import java.io.File;
import java.util.Collection;
import java.util.List;

public class ImportTask extends Task<Collection<Release>> {

    private List<File> processedDirectoryList;
    private List<Release> releaseList = Lists.newArrayList();


    public ImportTask(List<File> processedDirectoryList) {
        this.processedDirectoryList = processedDirectoryList;
    }

    @Override
    protected Collection<Release> call() {

        for (File dir : processedDirectoryList) {
            addRelease(dir);
        }

        return null;
    }

    private void addRelease(File dir) {

        final DefaultImportStrategy strategy = new DefaultImportStrategy();
        strategy.execute(dir);
        System.out.println();


    }
}
