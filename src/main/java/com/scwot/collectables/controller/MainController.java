package com.scwot.collectables.controller;

import com.google.common.collect.Lists;
import com.scwot.collectables.enums.ArtistType;
import com.scwot.collectables.persistence.model.Artist;
import com.scwot.collectables.persistence.service.ArtistService;
import com.scwot.collectables.task.ImportTask;
import com.scwot.collectables.task.ScanDirTask;
import com.scwot.collectables.ui.component.ArtistItem;
import com.scwot.collectables.utils.DirHelper;
import com.scwot.collectables.utils.GuiUtils;
import com.scwot.collectables.utils.ImageUtils;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import static com.scwot.collectables.utils.ImageUtils.DEFAULT_PHOTO_PATH;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Component
public class MainController {

    @FXML
    private VBox mainBox;
    @FXML
    private ListView<ArtistItem> artistListView;

    @Autowired
    private ArtistService artistService;

    @FXML
    public void handleOpenButtonAction() {
        final Stage stage = (Stage) mainBox.getScene().getWindow();
        final File selectedDir = GuiUtils.showDirectoryChooser(stage);
        if (selectedDir.exists()) {
            final List<File> dirs = filterDirectories(Lists.newArrayList(selectedDir));

            if (isNotEmpty(dirs)) {
                lookup(dirs);
            }
        }

        System.out.println(selectedDir.getAbsolutePath());
    }

    @FXML
    void handleTestButtonAction() {
        Artist artist = artistService.save(dummyArtist());
        artistListView.getItems().add(new ArtistItem(artist));
    }

    @FXML
    public void handleCleanButtonAction() {
        List<Artist> artistList = artistService.getAll();
        for (Artist artist : artistList) {
            artistService.delete(artist.getArtistId());
        }
        artistListView.getItems().clear();
    }

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
                    lookup(dirs);
                }
                event.consume();
            }
        });
    }

    private void lookup(List<File> dirs) {
        final ScanDirTask scanTask = new ScanDirTask(dirs);
        scanTask.setOnSucceeded(event -> {
            final List<File> processedDirs = scanTask.getProcessedDirectoryList();

            final ImportTask importTask = new ImportTask(processedDirs);
            final Thread thread = new Thread(importTask);
            thread.setDaemon(true);
            thread.start();
        });

        final Thread scanProcessThread = new Thread(scanTask);
        scanProcessThread.setDaemon(true);
        scanProcessThread.start();
    }

    private List<File> filterDirectories(List<File> files) {
        final List<File> dirs = Lists.newArrayList();
        files.stream()
                .filter(File::isDirectory)
                .map(DirHelper::listFilesAndDirs)
                .forEach(dirs::addAll);
        return dirs;
    }

    private Artist dummyArtist() {
        return Artist.builder()
                .name("The Artist")
                .sortName("Artist, The")
                .image(ImageUtils.readImage(DEFAULT_PHOTO_PATH))
                .beginDate(LocalDate.of(1980, 1, 1))
                .endDate(LocalDate.of(1995, 1, 1))
                .type(ArtistType.GROUP)
                .build();
    }

}
