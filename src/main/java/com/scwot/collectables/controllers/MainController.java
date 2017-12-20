package com.scwot.collectables.controllers;

import static com.scwot.collectables.utils.ImageUtils.DEFAULT_PHOTO_PATH;

import com.google.common.collect.Lists;
import com.scwot.collectables.entities.Artist;
import com.scwot.collectables.services.ArtistService;
import com.scwot.collectables.tasks.ScanDirTask;
import com.scwot.collectables.ui.components.ArtistItem;
import com.scwot.collectables.utils.DirHelper;
import com.scwot.collectables.utils.ImageUtils;

import java.io.File;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainController {

    @FXML
    private VBox mainBox;
    @FXML
    private ListView<ArtistItem> artistListView;

    @Autowired
    private ArtistService artistService;

    @FXML
    private void initialize() {
        final List<Artist> artistList = artistService.getAll();
        artistList.forEach(artist -> artistListView.getItems().add(new ArtistItem(artist)));
        initDragAndDrop();
    }

    private void initDragAndDrop() {
        mainBox.setOnDragOver((DragEvent event) -> {
            final Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                event.acceptTransferModes(TransferMode.LINK);
            } else {
                event.consume();
            }
        });

        mainBox.setOnDragDropped((DragEvent event) -> {
            final Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                final List<File> dirs = filterDirectories(db.getFiles());
                event.setDropCompleted(true);

                if (event.isDropCompleted()) {
                    searchForReleases(dirs);
                }
                event.consume();
            }
        });
    }

    private void searchForReleases(List<File> dirs) {
        final ScanDirTask scanTask = new ScanDirTask(dirs);

        final Thread scanProcessThread = new Thread(scanTask);
        scanProcessThread.setDaemon(true);
        scanProcessThread.start();

        scanTask.setOnSucceeded(event -> {
            final List<File> processedDirs = scanTask.getProcessedDirectoryList();
            crawlForReleaseData(processedDirs);
        });
    }

    private void crawlForReleaseData(List<File> processedDirs) {
        for (File dir : processedDirs) {
            System.out.println(dir.getAbsolutePath());
        }

        /*importTask.initialize(processedDirs);
        Thread thread = new Thread(importTask);
        thread.start();*/
    }

    private List<File> filterDirectories(List<File> files) {
        final List<File> dirs = Lists.newArrayList();
        files.stream()
                .filter(File::isDirectory)
                .map(DirHelper::listFilesAndDirs)
                .forEach(dirs::addAll);
        return dirs;
    }

    @FXML
    void handleTestButtonAction() {
        Artist artist = artistService.save(dummyArtist());
        artistListView.getItems().add(new ArtistItem(artist));
    }

    private Artist dummyArtist() {
        return Artist.builder()
                .name("The Artist")
                .sortName("Artist, The")
                .image(ImageUtils.readImage(DEFAULT_PHOTO_PATH))
                .beginDate("1980")
                .endDate("1985")
                .isGroup(true)
                .build();
    }

}
