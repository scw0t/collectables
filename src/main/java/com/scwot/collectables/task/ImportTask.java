package com.scwot.collectables.task;

import com.scwot.collectables.filesystem.ReleaseMetadata;
import com.scwot.collectables.strategy.DefaultImportStrategy;
import javafx.concurrent.Task;

import java.io.File;
import java.util.Collection;
import java.util.List;

public class ImportTask extends Task<Collection<ReleaseMetadata>> {

    private List<File> processedDirectoryList;
    private Collection<ReleaseMetadata> releaseMetadataCollection;

    public ImportTask(List<File> processedDirectoryList) {
        this.processedDirectoryList = processedDirectoryList;
    }

    @Override
    protected Collection<ReleaseMetadata> call() {

        for (File dir : processedDirectoryList) {
            addItem(dir);
        }

        return releaseMetadataCollection;
    }

    private void addItem(File dir) {
        final DefaultImportStrategy strategy = new DefaultImportStrategy();
        releaseMetadataCollection.addAll(strategy.execute(dir));
    }
}
