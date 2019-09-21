package com.scwot.collectables.file.metadata;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.scwot.collectables.file.wrapper.DirectoryScope;
import com.scwot.collectables.file.wrapper.Mp3FileWrapper;
import com.scwot.collectables.file.wrapper.RootDirectoryScope;
import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Data
public class ReleaseScopeMetadata {

    private File root;

    private List<MediumScopeMetadata> mediumScopeMetadataList;
    private boolean isMultiple;

    private SortedSet<String> artists = Sets.newTreeSet();
    private SortedSet<String> albums = Sets.newTreeSet();
    private SortedSet<String> genres = Sets.newTreeSet();
    private SortedSet<String> years = Sets.newTreeSet();
    private Map<String, String> yearAlbum = Maps.newHashMap();

    private String yearRecorded;
    private String yearReleased;

    private String albumArtist;
    private String albumArtistSort;
    private String albumTitle;
    private String releaseMBID;
    private String releaseGroupMBID;
    private String releaseCountry;
    private String releaseStatus;
    private String releaseType;

    private String barcode;

    private byte[] image;
    private byte[] thumbImage;

    private Map<String, String> catalogues = new LinkedHashMap<>();

    public ReleaseScopeMetadata(List<MediumScopeMetadata> mediumScopeMetadataList) {
        this.mediumScopeMetadataList = mediumScopeMetadataList;

        fetch(mediumScopeMetadataList);
    }

    private void fetch(List<MediumScopeMetadata> mediumScopeMetadataList) {
        if (mediumScopeMetadataList.size() > 1) {
            isMultiple = true;
            final List<DirectoryScope> collect = mediumScopeMetadataList.stream().map(s -> s.getProperties()).collect(Collectors.toList());

            final RootDirectoryScope rootDirectoryScope = new RootDirectoryScope(collect);
            System.out.println();
        }

        final Set<String> labels = Sets.newLinkedHashSet();
        final Set<String> catNums = Sets.newLinkedHashSet();

        final Stream<MediumScopeMetadata> dirScopeStream = mediumScopeMetadataList.stream();

        final List<Mp3FileWrapper> mergedAudioList =
                dirScopeStream.flatMap(directoryScopeMetadata -> directoryScopeMetadata.getAudioList().stream()).collect(Collectors.toList());

        mergedAudioList.stream()
                .peek((audio) -> artists.addAll(Optional.ofNullable(audio.getArtists()).orElse(Lists.newArrayList())))
                .peek((audio) -> albums.add(Optional.ofNullable(audio.getAlbumTitle()).orElse(EMPTY)))
                .peek((audio) -> years.addAll(Lists.newArrayList(audio.getOrigYear(), audio.getYear())))
                .peek((audio) -> genres.addAll(Optional.ofNullable(audio.getGenres()).orElse(Lists.newArrayList())))
                .peek((audio) -> labels.addAll(Optional.ofNullable(audio.getLabels()).orElse(Lists.newArrayList())))
                .peek((audio) -> catNums.addAll(Optional.ofNullable(audio.getCatNums()).orElse(Lists.newArrayList())))
                .forEach((audio) -> yearAlbum.put(audio.getAlbumTitle(), audio.getYear()));

        catalogues = createCatalogues(labels, catNums);

        albumArtist = mergedAudioList.stream()
                .flatMap(a -> Stream.of(a.getAlbumArtistTitle())).findFirst().orElse(String.join(" / ", artists));
        albumArtistSort = mergedAudioList.stream()
                .flatMap(a -> Stream.of(a.getAlbumArtistSortTitle())).findFirst().orElse(resolveAlbumArtistSort());
        albumTitle = mergedAudioList.stream()
                .flatMap(a -> Stream.of(a.getAlbumTitle())).findFirst().orElse(Mp3FileWrapper.UNKNOWN_VALUE);
        releaseCountry = mergedAudioList.stream()
                .flatMap(a -> Stream.of(a.getReleaseCountry())).findFirst().orElse(null);
        releaseStatus = mergedAudioList.stream()
                .flatMap(a -> Stream.of(a.getReleaseStatus())).findFirst().orElse(null);
        releaseType = mergedAudioList.stream()
                .flatMap(a -> Stream.of(a.getReleaseType())).findFirst().orElse(null);
        releaseMBID = mergedAudioList.stream()
                .flatMap(a -> Stream.of(a.getReleaseMBID())).findFirst().orElse(null);
        releaseGroupMBID = mergedAudioList.stream()
                .flatMap(a -> Stream.of(a.getReleaseGroupMBID())).findFirst().orElse(null);
        barcode = mergedAudioList.stream()
                .flatMap(a -> Stream.of(a.getBarcode())).findFirst().orElse(null);
        image = mergedAudioList.stream()
                .flatMap(a -> Stream.of(a.getImage())).findFirst().orElse(null);

        yearReleased = Collections.max(years);
        yearRecorded = Collections.min(years);

        System.out.println();
    }

    private String resolveAlbumArtistSort() {
        if (albumArtist.toLowerCase().startsWith("the")) {
            return albumArtist.toLowerCase().replaceFirst("^([Tt])he", EMPTY) + ", The";
        }

        return albumArtist;
    }

    private Map<String, String> createCatalogues(final Set<String> labels,
                                                 final Set<String> catNums) {
        addCatalogueNumberIfNotPresent(labels, catNums);

        final ArrayList<String> labelsList = Lists.newArrayList(labels);
        final ArrayList<String> catNumsList = Lists.newArrayList(catNums);

        if (catNumsList.size() > labelsList.size()) {
            final String joinedCatNums = String.join(" / ", catNumsList);

            return labelsList.stream()
                    .collect(Collectors.toMap(Function.identity(), label -> joinedCatNums, (a, b) -> b, LinkedHashMap::new));
        }

        return IntStream.range(0, labelsList.size())
                .boxed()
                .collect(Collectors.toMap(labelsList::get, catNumsList::get, (s, a) -> s + " / " + a, LinkedHashMap::new));
    }

    private void addCatalogueNumberIfNotPresent(final Set<String> labels,
                                                final Set<String> catNums) {
        final int labelsSize = labels.size();
        final int catNumsSize = catNums.size();
        final int diff = labelsSize - catNumsSize;
        if (diff > 0) {
            IntStream.range(0, diff).forEachOrdered(i -> catNums.add("none"));
        }
    }
}
