package com.scwot.collectables.controller;

import com.scwot.collectables.filesystem.ReleaseMetadata;
import com.scwot.collectables.persistence.model.Artist;
import org.springframework.stereotype.Controller;

@Controller
public class DataBaseController {


    public void save(final ReleaseMetadata rm) {





        final String albumArtist = rm.getAlbumArtist();
    }
}
