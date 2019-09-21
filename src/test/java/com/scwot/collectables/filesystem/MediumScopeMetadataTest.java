package com.scwot.collectables.filesystem;

import com.google.common.collect.Lists;
import com.scwot.collectables.file.metadata.MediumScopeMetadata;
import com.scwot.collectables.file.wrapper.DirectoryScope;
import com.scwot.collectables.file.wrapper.Mp3FileWrapper;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class MediumScopeMetadataTest {

    private static final String ARTIST_TITLE = "Artist title";
    private static final String ALBUM_TITLE = "Album title";
    private static final String ORIG_YEAR = "1990";
    private static final String YEAR = "2000";
    private static final String GENRE_1 = "genre1";
    private static final String GENRE_2 = "genre2";
    private static final int DISC_NUM = 1;
    private static final String MBID = "someMBID";
    private static final String CAT_NUM = "catN1";
    private static final String LABEL = "Label1";
    private DirectoryScope properties = mock(DirectoryScope.class);
    private MediumScopeMetadata mediumScopeMetadata = spy(new MediumScopeMetadata());
    private Mp3FileWrapper audio = mock(Mp3FileWrapper.class);

    private List<Mp3FileWrapper> audioList = Lists.newArrayList();

    @Before
    public void setUp() {
        audioList.add(audio);

        when(properties.getListOfAudios()).thenReturn(audioList);
        when(audio.getArtistTitle()).thenReturn(ARTIST_TITLE);
        when(audio.getAlbumTitle()).thenReturn(ALBUM_TITLE);
        when(audio.getOrigYear()).thenReturn(ORIG_YEAR);
        when(audio.getYear()).thenReturn(YEAR);
        when(audio.getGenres()).thenReturn(Lists.newArrayList(GENRE_1, GENRE_2));
        when(audio.getDiscNumber()).thenReturn(DISC_NUM);
        when(audio.getReleaseMBID()).thenReturn(MBID);
        /*when(audio.getCatNum()).thenReturn(CAT_NUM);
        when(audio.getLabel()).thenReturn(LABEL);*/
    }

    @Test
    public void readFromFiles() {
        /*releaseMetadata.readFromFiles();
        assertEquals(ARTIST_TITLE, releaseMetadata.getArtist());
        assertEquals(ALBUM_TITLE, releaseMetadata.getAlbum());
        assertEquals(ORIG_YEAR, releaseMetadata.getRecordedYear());
        assertEquals(YEAR, releaseMetadata.getReleasedYear());
        assertEquals(Integer.valueOf(DISC_NUM), releaseMetadata.getDiscNumber());
        assertEquals(MBID, releaseMetadata.getMbReleaseId());
        assertEquals(CAT_NUM, releaseMetadata.getCatNum());
        assertEquals(LABEL, releaseMetadata.getLabel());*/
    }
}