package com.scwot.collectables.task;

import com.scwot.collectables.utils.DirHelper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;

import java.io.File;
import java.util.List;
import java.util.stream.IntStream;

import static com.scwot.collectables.utils.DirHelper.MACOSX_FOLDER_NAME;

public class ScanDirTask extends Task<List<File>> {

    private List<File> inputDirectoryList;
    private List<File> processedDirectoryList;

    public ScanDirTask(List<File> inputDirectoryList) {
        this.inputDirectoryList = inputDirectoryList;
        processedDirectoryList = FXCollections.observableArrayList(inputDirectoryList);
    }

    @Override
    protected List<File> call() {
        Platform.runLater(() -> inputDirectoryList.stream()
                .filter(ScanDirTask::exists)
                .forEach(this::removeUnwantedDirs));
        return getProcessedDirectoryList();
    }

    private void removeUnwantedDirs(File dir) {
        if (dir.getName().equals(MACOSX_FOLDER_NAME)) {
            if (dir.exists()) {
                DirHelper.deleteDirectory(dir);
            }

            processedDirectoryList.removeIf(next -> next.getAbsolutePath().contains(dir.getName()));

            return;
        }

        final DirHelper helper = new DirHelper();
        DirHelper.hasInnerFolder(dir);
        helper.countFileTypes(dir);

        if (helper.doesNotContainRelease(dir)) {
            IntStream.range(0, processedDirectoryList.size())
                    .filter(i -> processedDirectoryList.get(i).compareTo(dir) == 0)
                    .forEach(i -> processedDirectoryList.remove(i));
        }

        if (helper.containsJustInnerFolders(dir)) {
            IntStream.range(0, processedDirectoryList.size())
                    .filter(i -> processedDirectoryList.get(i).compareTo(dir) == 0)
                    .findFirst()
                    .ifPresent(i -> processedDirectoryList.remove(i));
        }

        final int cdSubFoldersCount = DirHelper.getCdFoldersCount(dir);
        final int cdParentFolderCount = DirHelper.getCdFoldersCount(dir.getParentFile());

        if (helper.hasAudio()
                && cdSubFoldersCount == 0
                && cdParentFolderCount > 0) {
            IntStream.range(0, processedDirectoryList.size())
                    .filter(i -> processedDirectoryList.get(i).compareTo(dir) == 0)
                    .forEach(i -> processedDirectoryList.remove(i));
        }
    }

    private static boolean exists(File dir) {
        return dir != null && dir.exists();
    }

    public List<File> getProcessedDirectoryList() {
        return processedDirectoryList;
    }
}
