package com.scwot.collectables.task;

import com.scwot.collectables.utils.DirHelper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static com.scwot.collectables.utils.DirHelper.MACOSX_FOLDER_NAME;

public class ScanDirTask extends Task<List<File>> {

    private List<File> inputDirs;
    private List<File> filteredDirs;

    public ScanDirTask(List<File> inputDirs) {
        this.inputDirs = inputDirs;
        filteredDirs = FXCollections.observableArrayList(inputDirs);
    }

    @Override
    protected List<File> call() {
        Platform.runLater(() -> inputDirs.stream()
                .filter(ScanDirTask::exists)
                .forEach(this::removeUnwantedDirs));
        return getFilteredDirs();
    }

    private void removeUnwantedDirs(File dir) {
        if (dir.getName().equals(MACOSX_FOLDER_NAME)) {

            filteredDirs.removeIf(next -> next.getAbsolutePath().contains(dir.getName()));

            return;
        }

        final DirHelper helper = new DirHelper();
        DirHelper.hasInnerFolder(dir);
        helper.countFileTypes(dir);

        if (helper.doesNotContainRelease(dir)) {
            remove(dir);
        }

        if (helper.containsJustInnerFolders(dir)) {
            remove(dir);
        }

        final int cdSubFoldersCount = DirHelper.getCdFoldersCount(dir);
        final int cdParentFolderCount = DirHelper.getCdFoldersCount(dir.getParentFile());

        if (helper.hasAudio()
                && cdSubFoldersCount == 0
                && cdParentFolderCount > 0) {
            remove(dir);
        }
    }

    private void remove(File dir) {
        final Optional<File> inner = filteredDirs.stream()
                .filter(file -> file.compareTo(dir) == 0)
                .findFirst();

        filteredDirs.remove(inner.get());
    }

    private static boolean exists(File dir) {
        return dir != null && dir.exists();
    }

    public List<File> getFilteredDirs() {
        return filteredDirs;
    }
}
