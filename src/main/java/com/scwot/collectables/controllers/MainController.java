package com.scwot.collectables.controllers;

import com.scwot.collectables.entities.Artist;
import com.scwot.collectables.services.ArtistService;
import com.scwot.collectables.ui.components.ArtistItem;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.ImageUtils;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class MainController {

    @FXML
    private ListView<ArtistItem> artistListView;

    @Autowired
    private ArtistService artistService;

    @FXML
    private void initialize() {
        final List<Artist> artistList = artistService.getAll();
        for (final Artist artist : artistList) {
            artistListView.getItems().add(new ArtistItem(artist));
        }
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
                .image(ImageUtils.readImage("/default/default-photo.jpg"))
                .beginDate("1980")
                .endDate("1985")
                .isGroup(true)
                .build();
    }

}
