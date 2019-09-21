package com.scwot.collectables.adapter;

import com.google.common.collect.Lists;
import com.scwot.collectables.enums.ReleaseSecondaryType;
import com.scwot.collectables.file.metadata.MediumScopeMetadata;
import com.scwot.collectables.file.metadata.ReleaseScopeMetadata;
import com.scwot.collectables.file.wrapper.Mp3FileWrapper;
import com.scwot.collectables.persistence.model.Artist;
import com.scwot.collectables.persistence.model.Genre;
import com.scwot.collectables.persistence.model.Label;
import com.scwot.collectables.persistence.model.Link;
import com.scwot.collectables.persistence.model.Medium;
import com.scwot.collectables.persistence.model.Release;
import com.scwot.collectables.persistence.model.ReleaseGroup;
import com.scwot.collectables.persistence.model.Track;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReleaseAdapter {

    private Release release;
    private ReleaseGroup releaseGroup;
    private List<Artist> artists = Lists.newArrayList();
    private List<Medium> mediums = Lists.newArrayList();
    private List<Genre> genres = Lists.newArrayList();
    private List<Label> labels = Lists.newArrayList();
    private List<Track> trackList = Lists.newArrayList();
    private List<Link> links = Lists.newArrayList();

    public void convert(final ReleaseScopeMetadata rsm) {

        final List<MediumScopeMetadata> mediumScopeMetadataList = rsm.getMediumScopeMetadataList();

        for (MediumScopeMetadata metadata : mediumScopeMetadataList) {
            for (Mp3FileWrapper audio : metadata.getAudioList()) {
                trackList.add(
                        Track.builder()
                                .name(audio.getTrackTitle())
                                .position(audio.getFileNum())
                                .length(audio.getLength())
                                .path(audio.getAudioFile().getFile().getAbsolutePath())
                                .createdAt(LocalDateTime.now())
                                .modifiedAt(LocalDateTime.now())
                                .build()
                );
            }
        }

        for (MediumScopeMetadata metadata : mediumScopeMetadataList) {
            mediums.add(Medium.builder()
                    .format(metadata.getFormat())
                    .createdAt(LocalDateTime.now())
                    .modifiedAt(LocalDateTime.now())
                    .build());
        }

        Release.builder()
                .name(rsm.getAlbumTitle())
                .sortName(rsm.getAlbumArtistSort())
                .mbid(rsm.getReleaseMBID())
                .barcode(rsm.getBarcode())
                //catalogue number
                .yearRecorded(rsm.getYearRecorded())
                .yearReleased(rsm.getYearReleased())
                .country(rsm.getReleaseCountry())
                .status(ReleaseSecondaryType.fromString(rsm.getReleaseStatus()))
                //quality
                //language
                //path
                .build();


    }


}
