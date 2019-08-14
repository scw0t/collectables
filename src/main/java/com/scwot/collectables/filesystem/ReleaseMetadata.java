package com.scwot.collectables.filesystem;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.scwot.collectables.adapter.ReleaseAdapter;
import com.scwot.collectables.persistence.model.Artist;
import com.scwot.collectables.persistence.model.Medium;
import com.scwot.collectables.persistence.model.Track;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jaudiotagger.audio.mp3.MP3File;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Slf4j
@Data
public class ReleaseMetadata {

    private static final String VA_VALUE = "Various Artists";

    private FileSystemWrapper properties;
    private List<Mp3FileWrapper> audioList;
    private Integer discNumber;
    private String releasedYear;
    private String recordedYear;
    private String albumArtist;
    private String albumTitle;
    private String releaseMBID;
    private String releaseGroupMBID;
    private String releaseCountry;
    private String releaseStatus;
    private String releaseType;

    private Map<String, String> yearAlbum = Maps.newHashMap();
    private SortedSet<String> artists = Sets.newTreeSet();
    private SortedSet<String> albums = Sets.newTreeSet();
    private SortedSet<String> genres = Sets.newTreeSet();
    private SortedSet<String> years = Sets.newTreeSet();
    private SortedSet<String> labels = Sets.newTreeSet();
    private SortedSet<String> catNums = Sets.newTreeSet();

    private Set<Artist> artistSet = Sets.newHashSet();

    private ReleaseAdapter releaseAdapter;

    public ReleaseMetadata(FileSystemWrapper properties) {
        this.properties = properties;
    }

    public void readFromFiles() {
        audioList = properties.getListOfAudios();

        audioList.stream()
                .peek((audio) -> artists.addAll(Optional.ofNullable(audio.getArtists()).orElse(Lists.newArrayList())))
                .peek((audio) -> albums.add(Optional.ofNullable(audio.getAlbumTitle()).orElse(EMPTY)))
                .peek((audio) -> years.addAll(Lists.newArrayList(audio.getOrigYear(), audio.getYear())))
                .peek((audio) -> genres.addAll(Optional.ofNullable(audio.getGenres()).orElse(Lists.newArrayList())))
                .peek((audio) -> labels.addAll(Optional.ofNullable(audio.getLabels()).orElse(Lists.newArrayList())))
                .peek((audio) -> catNums.addAll(Optional.ofNullable(audio.getCatNums()).orElse(Lists.newArrayList())))
                .forEach((audio) -> yearAlbum.put(audio.getAlbumTitle(), audio.getYear()));

        discNumber = audioList.stream()
                .flatMap(a -> Stream.of(a.getDiscNumber())).findFirst().orElse(1);
        albumArtist = audioList.stream()
                .flatMap(a -> Stream.of(a.getAlbumArtistTitle())).findFirst().orElse(String.join(" / ", artists));
        albumTitle = audioList.stream()
                .flatMap(a -> Stream.of(a.getAlbumTitle())).findFirst().orElse(Mp3FileWrapper.UNKNOWN_VALUE);
        releaseCountry = audioList.stream()
                .flatMap(a -> Stream.of(a.getReleaseCountry())).findFirst().orElse(null);
        releaseStatus = audioList.stream()
                .flatMap(a -> Stream.of(a.getReleaseStatus())).findFirst().orElse(null);
        releaseType = audioList.stream()
                .flatMap(a -> Stream.of(a.getReleaseType())).findFirst().orElse(null);
        releaseMBID = audioList.stream()
                .flatMap(a -> Stream.of(a.getReleaseMBID())).findFirst().orElse(null);
        releaseGroupMBID = audioList.stream()
                .flatMap(a -> Stream.of(a.getReleaseGroupMBID())).findFirst().orElse(null);

        releasedYear = Collections.max(years);
        recordedYear = Collections.min(years);

        /*label = firstTrack.getLabel() != null ? firstTrack.getLabel() : EMPTY;
        catNum = firstTrack.getCatNum() != null ? firstTrack.getCatNum() : EMPTY;

        artist = pickArtistNameTitle(artists);*/


        /*if (albums.size() > 1) {
            if (albums.size() == 2) {
                if (getLevenshteinDistance(albums.first(), albums.last()) < 2) {
                    album = albums.first();
                } else {
                    album = String.join(" / ", albums);
                    log.debug("Multiple albums: " + album);
                }
            }
        } else {
            album = albums.first();
        }*/

        audioList.sort(Comparator.comparingInt(Mp3FileWrapper::getTrack));
    }

    private Collection<ReleaseMetadata> getReleaseMetadata() {
        final List<Track> tracks = new ArrayList<>();

        for (Mp3FileWrapper audio : audioList) {

            //final List<String> artists = audio.getArtists();
            final String albumTitle = audio.getAlbumTitle();
            final String albumArtistTitle = audio.getAlbumArtistTitle();
            final MP3File audioFile = audio.getAudioFile();
            final List<String> catNums = audio.getCatNums();
            final Integer discNumber = audio.getDiscNumber();
            final List<String> labels = audio.getLabels();
            final List<String> genres = audio.getGenres();
            final int fileNum = audio.getFileNum();
            final Long length = audio.getLength();
            final String artistMBID = audio.getArtistMBID();
            final String releaseGroupMBID = audio.getReleaseGroupMBID();
            final String releaseMBID = audio.getReleaseMBID();
            final String trackMBID = audio.getTrackMBID();
            final String origYear = audio.getOrigYear();

            final String releaseCountry = audio.getReleaseCountry();
            final String releaseStatus = audio.getReleaseStatus();
            final int trackCount = audio.getTrackCount();
            final String trackNumber = audio.getTrackNumber();
            final String releaseType = audio.getReleaseType();
            final String trackTitle = audio.getTrackTitle();
            final String year = audio.getYear();

            for (String artist : artists) {
                final Artist artistToSave = Artist.builder()
                        .mbid(artistMBID)
                        .name(artist)
                        .createdAt(LocalDateTime.now())
                        .modifiedAt(LocalDateTime.now())
                        .build();
                artistSet.add(artistToSave);
            }

            final Track track = Track.builder()
                    .artists(artistSet)
                    .name(audio.getTrackTitle())
                    .path(audio.getAudioFile().getFile().getAbsolutePath())
                    .length(audio.getLength())
                    .position(Integer.valueOf(trackNumber))
                    .createdAt(LocalDateTime.now())
                    .modifiedAt(LocalDateTime.now())
                    .build();
            tracks.add(track);
        }

        final Medium medium = Medium.builder()
                .trackList(tracks)
                .createdAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .build();




        /*properties.getListOfAudios().forEach(audio -> audio.getArtists().forEach(artistString -> artistString));


        Artist.builder()
                .mbid(properties.getListOfAudios().stream().filter(audio -> audio.getArtists()))*/

        return null;
    }



    /*TODO: fix for collaboration albums / splits / etc.*/
    private static String pickArtistNameTitle(SortedSet<String> artists) {
        if (artists.size() == 1) {
            return artists.first();
        }

        return VA_VALUE;
    }

    private static <E> E get(Collection<E> collection, int index) {
        Iterator<E> i = collection.iterator();
        E element = null;
        while (i.hasNext() && index-- >= 0) {
            element = i.next();
        }
        return element;
    }



}
