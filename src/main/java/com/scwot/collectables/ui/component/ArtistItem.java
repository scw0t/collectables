package com.scwot.collectables.ui.component;

import static com.scwot.collectables.utils.ImageUtils.DEFAULT_PHOTO_PATH;

import com.scwot.collectables.persistence.model.Artist;
import com.scwot.collectables.utils.ImageUtils;

import java.io.ByteArrayInputStream;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ArtistItem extends HBox {

    private static final int DEFAULT_WIDTH = 80;

    public ArtistItem(final Artist artist) {
        super();
        this.setSpacing(10);

        this.getChildren().addAll(
                initImageView(artist.getImage()),
                initArtistInfoBox(artist));
    }

    private VBox initArtistInfoBox(Artist artist) {
        final VBox innerVBox = new VBox();
        innerVBox.setSpacing(10);
        innerVBox.setAlignment(Pos.CENTER);

        final Label sortNameLabel = new Label(artist.getSortName());
        final Label activeYearsLabel = new Label(artist.getBeginDate() + " - " + artist.getEndDate());

        innerVBox.getChildren().addAll(sortNameLabel, activeYearsLabel);
        return innerVBox;
    }

    private ImageView initImageView(byte[] imageBytes) {
        if (imageBytes == null) {
            imageBytes = ImageUtils.readImage(DEFAULT_PHOTO_PATH);
        }

        final Image image = new Image(new ByteArrayInputStream(imageBytes),
                DEFAULT_WIDTH, 0, true, true);
        return new ImageView(image);
    }
}
