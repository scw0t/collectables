package com.scwot.collectables.task;

import com.scwot.collectables.file.metadata.DirectoryScopeMetadata;
import com.scwot.collectables.file.metadata.ReleaseScopeMetadata;
import com.scwot.collectables.strategy.DefaultImportStrategy;
import javafx.concurrent.Task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImportTask extends Task<List<ReleaseScopeMetadata>> {

    private List<File> filteredDirs;
    private List<ReleaseScopeMetadata> releaseScopeMetadata = new ArrayList<>();

    public ImportTask(List<File> filteredDirs) {
        this.filteredDirs = filteredDirs;
    }

    @Override
    protected List<ReleaseScopeMetadata> call() {

        for (File dir : filteredDirs) {
            addItem(dir);
        }

        return releaseScopeMetadata;
    }

    private void addItem(File dir) {
        final DefaultImportStrategy strategy = new DefaultImportStrategy();
        final List<DirectoryScopeMetadata> directoryScopeMetadata = strategy.execute(dir);


        for (DirectoryScopeMetadata metadata : directoryScopeMetadata) {

            metadata.getMetadata();

            System.out.println();
        }

        final ReleaseScopeMetadata releaseScopeMetadata = new ReleaseScopeMetadata(directoryScopeMetadata);



        this.releaseScopeMetadata.add(releaseScopeMetadata);
        System.out.println();
    }
}
