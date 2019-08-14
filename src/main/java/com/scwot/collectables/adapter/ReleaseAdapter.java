package com.scwot.collectables.adapter;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.scwot.collectables.filesystem.Mp3FileWrapper;
import com.scwot.collectables.filesystem.ReleaseMetadata;
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
import java.util.Set;

@Data
public class ReleaseAdapter {

    private ReleaseGroup releaseGroup;
    private Release release;
    private Medium medium;
    private Set<Artist> artists = Sets.newHashSet();
    private List<Track> tracks = Lists.newArrayList();
    private List<Genre> genres = Lists.newArrayList();
    private List<Label> labels = Lists.newArrayList();
    private List<Link> links = Lists.newArrayList();

    public void convert(final ReleaseMetadata metadata) {
        for (Mp3FileWrapper audio : metadata.getAudioList()) {
            artists.add(Artist.builder()
                    .name(audio.getArtistTitle())
                    .mbid(audio.getArtistMBID())
                    .build());

            tracks.add(Track.builder()
                    .name(audio.getTrackTitle())
                    .position(audio.getTrack())
                    .length(audio.getLength())
                    .build());
        }

        /*ReleaseGroup.builder()
                .name(metadata.getAlbumTitle())
                .artistList(artists)
                .mbid(metadata.getReleaseGroupMBID())
                .type(metadata.getReleaseType())*/
    }


}
