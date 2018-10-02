package com.scwot.collectables.filesystem;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.getLevenshteinDistance;

@Slf4j
@Data
public class ReleaseMetadata {

    private static final String VA_VALUE = "Various Artists";

    private FileSystemWrapper properties;
    private List<Mp3FileWrapper> audioList;
    private Integer discNumber;
    private String album;
    private String artist;
    private String releasedYear;
    private String recordedYear;
    private String label;
    private String catNum;
    private String genres;
    private Map<String, String> yearAlbum = Maps.newHashMap();
    private String mbReleaseId;

    public ReleaseMetadata(FileSystemWrapper properties) {
        this.properties = properties;
    }

    public void readFromFiles() {
        audioList = properties.getListOfAudios();

        SortedSet<String> artists = Sets.newTreeSet();
        SortedSet<String> albums = Sets.newTreeSet();
        SortedSet<String> genres = Sets.newTreeSet();
        SortedSet<String> years = Sets.newTreeSet();

        audioList.stream()
                .peek((audio) -> artists.add(Optional.ofNullable(audio.getArtistTitle()).orElse(EMPTY)))
                .peek((audio) -> albums.add(Optional.ofNullable(audio.getAlbumTitle()).orElse(EMPTY)))
                .peek((audio) -> years.add(Optional.ofNullable(audio.getOrigYear()).orElse(EMPTY)))
                .peek((audio) -> years.add(Optional.ofNullable(audio.getYear()).orElse(EMPTY)))
                .peek((audio) -> genres.addAll(Optional.ofNullable(audio.getGenres()).orElse(Lists.newArrayList())))
                .peek((audio) -> discNumber = audio.getDiscNumber())
                .forEach((audio) -> yearAlbum.put(audio.getAlbumTitle(), audio.getYear()));

        final Mp3FileWrapper firstTrack = audioList.get(0);
        mbReleaseId = firstTrack.getMbReleaseId() != null ? firstTrack.getMbReleaseId() : EMPTY;
        label = firstTrack.getLabel() != null ? firstTrack.getLabel() : EMPTY;
        catNum = firstTrack.getCatNum() != null ? firstTrack.getCatNum() : EMPTY;

        artist = pickArtistNameTitle(artists);

        releasedYear = Collections.max(years);
        recordedYear = Collections.min(years);

        if (albums.size() > 1) {
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
        }

        audioList.sort(Comparator.comparingInt(Mp3FileWrapper::getTrack));
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
