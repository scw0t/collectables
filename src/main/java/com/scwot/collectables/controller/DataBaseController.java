package com.scwot.collectables.controller;

import com.scwot.collectables.persistence.service.ArtistService;
import com.scwot.collectables.persistence.service.GenreService;
import com.scwot.collectables.persistence.service.LabelService;
import com.scwot.collectables.persistence.service.LinkService;
import com.scwot.collectables.persistence.service.MediumService;
import com.scwot.collectables.persistence.service.ReleaseGroupService;
import com.scwot.collectables.persistence.service.ReleaseService;
import com.scwot.collectables.persistence.service.TrackService;
import com.scwot.collectables.strategy.DefaultImportStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DataBaseController {

    @Autowired
    private ArtistService artistService;
    @Autowired
    private ReleaseGroupService releaseGroupService;
    @Autowired
    private ReleaseService releaseService;
    @Autowired
    private MediumService mediumService;
    @Autowired
    private TrackService trackService;
    @Autowired
    private LabelService labelService;
    @Autowired
    private GenreService genreService;
    @Autowired
    private LinkService linkService;


    public void save(final DefaultImportStrategy entry) {

    }
}
