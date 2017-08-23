package com.scwot.collectables.controllers;

import com.scwot.collectables.entities.Artist;
import com.scwot.collectables.services.ArtistService;
import com.scwot.collectables.ui.components.ArtistItem;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.ImageUtils;

@Component
public class MainController {

    @FXML
    ListView<ArtistItem> artistList;

    @Autowired
    private ArtistService artistService;

    @FXML
    void handleTestButtonAction() {

        artistList.getItems().add(new ArtistItem(dummyArtist()));

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
