package com.scwot.collectables.filesystem;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

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
    private String MBReleaseID;

    public void readFromFiles(FileSystemWrapper properties) {
        audioList = properties.getListOfAudios();

        SortedSet<String> artists = Sets.newTreeSet();
        SortedSet<String> albums = Sets.newTreeSet();
        SortedSet<String> genres = Sets.newTreeSet();
        SortedSet<String> years = Sets.newTreeSet();

        audioList.stream()
                .peek((audio) -> artists.add(audio.getArtistTitle()))
                .peek((audio) -> albums.add(audio.getAlbumTitle()))
                .peek((audio) -> years.add(audio.getOrigYear()))
                .peek((audio) -> years.add(audio.getYear()))
                .peek((audio) -> genres.addAll(audio.getGenres()))
                .peek((audio) -> {
                    if (audio.getDiscNumber() != 0) {
                        discNumber = audio.getDiscNumber();
                    }
                }).forEach((audio) -> yearAlbum.put(audio.getAlbumTitle(), audio.getYear()));

        MBReleaseID = audioList.get(0).getMBReleaseID();
        label = audioList.get(0).getLabel();
        catNum = audioList.get(0).getCatNum();

        artist = pickArtistNameTitle(artists);

        releasedYear = Collections.max(years);
        recordedYear = Collections.min(years);

        if (albums.size() > 1) {
            StringBuilder albStr = new StringBuilder();
            if (albums.size() == 2) {
                if (StringUtils.getLevenshteinDistance(albums.first(), albums.last()) < 2) {
                    albStr = new StringBuilder(albums.first());
                } else {
                    System.out.println("Album size = 2: \"" + albums.first() + "\", \"" + albums.last() + "\"");
                    for (int i = 0; i < albums.size(); i++) {
                        albStr.append(get(albums, i));
                        if (i < albums.size() - 1) {
                            albStr.append(" / ");
                        }
                    }
                }
            }
            album = albStr.toString();
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
