package com.scwot.collectables.task;

import com.scwot.collectables.persistence.model.Release;
import com.scwot.collectables.strategy.DefaultImportStrategy;
import javafx.concurrent.Task;

import java.io.File;
import java.util.Collection;
import java.util.List;

public class ImportTask extends Task<Collection<Release>> {

    private List<File> processedDirectoryList;


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
