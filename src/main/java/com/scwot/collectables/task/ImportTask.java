package com.scwot.collectables.task;

import com.scwot.collectables.file.metadata.MediumScopeMetadata;
import com.scwot.collectables.file.metadata.ReleaseScopeMetadata;
import com.scwot.collectables.strategy.DefaultImportStrategy;
import javafx.concurrent.Task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImportTask extends Task<List<ReleaseScopeMetadata>> {

    private List<File> filteredDirs;
    private List<ReleaseScopeMetadata> releaseScopeMetadata;

    public ImportTask(List<File> filteredDirs) {
        this.filteredDirs = filteredDirs;
    }

    @Override
    protected List<ReleaseScopeMetadata> call() {

        releaseScopeMetadata = new ArrayList<>();

        for (File dir : filteredDirs) {
            addItem(dir);
        }

        return releaseScopeMetadata;
    }

    private void addItem(File dir) {
        final DefaultImportStrategy strategy = new DefaultImportStrategy();
        final List<MediumScopeMetadata> mediumScopeMetadata = strategy.execute(dir);

        final ReleaseScopeMetadata releaseScopeMetadata = new ReleaseScopeMetadata(mediumScopeMetadata);



        this.releaseScopeMetadata.add(releaseScopeMetadata);
        System.out.println();
    }
}
