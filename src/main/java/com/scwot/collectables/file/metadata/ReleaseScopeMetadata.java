package com.scwot.collectables.file.metadata;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.scwot.collectables.file.wrapper.Mp3FileWrapper;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Data
public class ReleaseScopeMetadata {

    private List<DirectoryScopeMetadata> directoryScopeMetadataList;

    private Map<String, String> yearAlbum = Maps.newHashMap();
    private SortedSet<String> artists = Sets.newTreeSet();
    private SortedSet<String> albums = Sets.newTreeSet();
    private SortedSet<String> genres = Sets.newTreeSet();
    private SortedSet<String> years = Sets.newTreeSet();
    /*private SortedSet<String> labels = Sets.newTreeSet();
    private SortedSet<String> catNums = Sets.newTreeSet();*/

    public ReleaseScopeMetadata(List<DirectoryScopeMetadata> directoryScopeMetadataList) {
        this.directoryScopeMetadataList = directoryScopeMetadataList;

        fetch(directoryScopeMetadataList);
    }

    private void fetch(List<DirectoryScopeMetadata> directoryScopeMetadataList) {
        final Stream<DirectoryScopeMetadata> dirScopeStream = directoryScopeMetadataList.stream();

        final List<Mp3FileWrapper> mergedAudioList =
                dirScopeStream.flatMap(directoryScopeMetadata -> directoryScopeMetadata.getAudioList().stream()).collect(Collectors.toList());

        mergedAudioList.stream()
                .peek((audio) -> artists.addAll(Optional.ofNullable(audio.getArtists()).orElse(Lists.newArrayList())))
                .peek((audio) -> albums.add(Optional.ofNullable(audio.getAlbumTitle()).orElse(EMPTY)))
                .peek((audio) -> years.addAll(Lists.newArrayList(audio.getOrigYear(), audio.getYear())))
                .peek((audio) -> genres.addAll(Optional.ofNullable(audio.getGenres()).orElse(Lists.newArrayList())))
                /*.peek((audio) -> labels.addAll(Optional.ofNullable(audio.getLabels()).orElse(Lists.newArrayList())))
                .peek((audio) -> catNums.addAll(Optional.ofNullable(audio.getCatNums()).orElse(Lists.newArrayList())))*/
                .forEach((audio) -> yearAlbum.put(audio.getAlbumTitle(), audio.getYear()));
    }
}
