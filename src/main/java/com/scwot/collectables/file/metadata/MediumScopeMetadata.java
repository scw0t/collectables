package com.scwot.collectables.file.metadata;

import com.scwot.collectables.file.wrapper.DirectoryScope;
import com.scwot.collectables.file.wrapper.Mp3FileWrapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Data
public class MediumScopeMetadata {

    private static final String VA_VALUE = "Various Artists";
    private List<Mp3FileWrapper> audioList;
    private Integer discNumber;
    private String format;
    private DirectoryScope properties;

    public void convert(final DirectoryScope properties) {
        this.properties = properties;

        audioList = properties.getListOfAudios();
        audioList.sort(Comparator.comparingInt(Mp3FileWrapper::getTrack));

        discNumber = audioList.stream()
                .flatMap(a -> Stream.of(a.getDiscNumber())).findFirst().orElse(1);

        format = audioList.stream()
                .flatMap(a -> Stream.of(a.getFormat())).findFirst().orElse(null);
    }

    /*TODO: fix for collaboration albums / splits / etc.*/
    /*private static String pickArtistNameTitle(SortedSet<String> artists) {
        if (artists.size() == 1) {
            return artists.first();
        }

        return VA_VALUE;
    }*/

}
