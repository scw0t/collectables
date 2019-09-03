package com.scwot.collectables.adapter;

import com.scwot.collectables.file.metadata.DirectoryScopeMetadata;
import com.scwot.collectables.persistence.model.Artist;
import com.scwot.collectables.persistence.model.Genre;
import com.scwot.collectables.persistence.model.Label;
import com.scwot.collectables.persistence.model.Link;
import com.scwot.collectables.persistence.model.Medium;
import com.scwot.collectables.persistence.model.Release;
import com.scwot.collectables.persistence.model.ReleaseGroup;
import com.scwot.collectables.persistence.model.Track;
import lombok.Data;

import java.util.List;

@Data
public class ReleaseAdapter {

    private Release release;
    private ReleaseGroup releaseGroup;
    private List<Artist> artists;
    private List<Medium> medium;
    private List<Genre> genres;
    private List<Label> labels;
    private List<Track> trackList;
    private List<Link> links;

    public void convert(final DirectoryScopeMetadata metadata) {


        /*ReleaseGroup.builder()
                .name(metadata.getAlbumTitle())
                .artistList(artists)
                .mbid(metadata.getReleaseGroupMBID())
                .type(metadata.getReleaseType())*/
    }


}
