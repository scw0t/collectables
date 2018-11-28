package com.scwot.collectables.filesystem;

import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.images.Artwork;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.mockito.Mockito.*;

public class Mp3FileWrapperTest {

    private static final String[] GENRE_DELIMITERS = {",", ";", "\\\\", "/"};

    private static final String UNKNOWN_VALUE = "[unknown]";
    private static final String ARTIST_TITLE = "artist name";
    private static final String ALBUM_TITLE = "album name";
    private static final String MUSICBRAINZ_RELEASEID = "someID1234";
    private static final String RECORD_LABEL = "record label";
    private static final String TITLE = "song title";
    private static final String TRACK = "01";
    private static final String YEAR = "2018";
    private static final String ORIGINAL_YEAR = "2000";
    private static final String DISC_NO = "1";
    private static final String GENRE = "some weird genre";
    private static final String CAT_NUM = "num01";

    private Artwork artwork = mock(Artwork.class);
    private List<Artwork> artworkList = new ArrayList<>();

    private List<TagField> tagFieldList = new ArrayList<>();

    private MP3File mp3File = mock(MP3File.class);
    private Tag tag = mock(Tag.class);
    private AbstractID3v2Tag id3v2Tag = mock(AbstractID3v2Tag.class);
    private TagField originalYearTagField = mock(TagField.class);
    private TagField catalogNumTagField = mock(TagField.class);

    private Mp3FileWrapper testWrapper = spy(new Mp3FileWrapper());

    @Before
    public void setUp(){
        doReturn(mp3File).when(testWrapper).readAudio(any(File.class));
        when(mp3File.getTag()).thenReturn(tag);
        when(mp3File.getID3v2Tag()).thenReturn(id3v2Tag);
        when(id3v2Tag.getFields(Mp3FileWrapper.CUSTOM_FIELD)).thenReturn(tagFieldList);

        artworkList.add(artwork);
        tagFieldList.add(originalYearTagField);
        tagFieldList.add(catalogNumTagField);

        when(tag.getFirst(FieldKey.ARTIST)).thenReturn(ARTIST_TITLE);
        when(tag.getFirst(FieldKey.ALBUM)).thenReturn(ALBUM_TITLE);
        when(tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID)).thenReturn(MUSICBRAINZ_RELEASEID);
        when(tag.getFirst(FieldKey.RECORD_LABEL)).thenReturn(RECORD_LABEL);
        when(tag.getFirst(FieldKey.TITLE)).thenReturn(TITLE);
        when(tag.getFirst(FieldKey.TRACK)).thenReturn(TRACK);
        when(tag.getFirst(FieldKey.YEAR)).thenReturn(YEAR);
        when(tag.getFirst(FieldKey.ORIGINAL_YEAR)).thenReturn(ORIGINAL_YEAR);
        when(tag.getFirst(FieldKey.DISC_NO)).thenReturn(DISC_NO);
        when(tag.getFirst(FieldKey.GENRE)).thenReturn(GENRE);
        when(tag.getArtworkList()).thenReturn(artworkList);
        when(originalYearTagField.toString()).thenReturn(Mp3FileWrapper.ORIGINALYEAR_TAG_NAME + "; " + ORIGINAL_YEAR);
        when(catalogNumTagField.toString()).thenReturn(Mp3FileWrapper.CATALOGNUMBER_TAG_NAME + "; " + CAT_NUM);

    }

    @Test
    public void readAllCorrectly() {
        testWrapper.read(new File(EMPTY));

        assertEquals(ALBUM_TITLE, testWrapper.getAlbumTitle());
        assertEquals(ARTIST_TITLE, testWrapper.getArtistTitle());
        assertEquals(MUSICBRAINZ_RELEASEID, testWrapper.getMbReleaseId());
        //assertEquals(RECORD_LABEL, testWrapper.getLabel());
        assertEquals(TITLE, testWrapper.getTrackTitle());
        assertEquals(TRACK, testWrapper.getTrackNumber());
        assertEquals(YEAR, testWrapper.getYear());
        assertEquals(ORIGINAL_YEAR, testWrapper.getOrigYear());
        //assertEquals(CAT_NUM, testWrapper.getCatNum());
        assertEquals(Integer.valueOf(DISC_NO), testWrapper.getDiscNumber());
        assertTrue(testWrapper.getGenres().contains(GENRE));
    }

    @Test
    public void readWithGenreDelimiters() {
        final String extraGenre = "genre2";

        for (String delimiter : GENRE_DELIMITERS) {
            when(tag.getFirst(FieldKey.GENRE)).thenReturn(GENRE + delimiter + " " + extraGenre);
            testWrapper.read(new File(EMPTY));
            assertNotNull(testWrapper.getGenres());
            assertFalse(testWrapper.getGenres().isEmpty());
            assertTrue(testWrapper.getGenres().contains(GENRE));
            assertTrue(testWrapper.getGenres().contains(extraGenre));
        }
    }

    @Test
    public void readWithNullOrEmptyArtist() {
        when(tag.getFirst(FieldKey.ARTIST)).thenReturn(null);
        testWrapper.read(new File(EMPTY));
        assertEquals(UNKNOWN_VALUE, testWrapper.getArtistTitle());

        when(tag.getFirst(FieldKey.ARTIST)).thenReturn(EMPTY);
        testWrapper.read(new File(EMPTY));
        assertEquals(UNKNOWN_VALUE, testWrapper.getArtistTitle());
    }

    @Test
    public void readWithNullOrEmptyAlbum() {
        when(tag.getFirst(FieldKey.ALBUM)).thenReturn(null);
        testWrapper.read(new File(EMPTY));
        assertEquals(UNKNOWN_VALUE, testWrapper.getAlbumTitle());

        when(tag.getFirst(FieldKey.ALBUM)).thenReturn(EMPTY);
        testWrapper.read(new File(EMPTY));
        assertEquals(UNKNOWN_VALUE, testWrapper.getAlbumTitle());
    }

    @Test
    public void readWithNullOrEmptyMbReleaseId() {
        when(tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID)).thenReturn(null);
        testWrapper.read(new File(EMPTY));
        assertEquals(EMPTY, testWrapper.getMbReleaseId());

        when(tag.getFirst(FieldKey.MUSICBRAINZ_RELEASEID)).thenReturn(EMPTY);
        testWrapper.read(new File(EMPTY));
        assertEquals(EMPTY, testWrapper.getMbReleaseId());
    }

    @Test
    public void readWithNullOrEmptyLabel() {
        when(tag.getFirst(FieldKey.RECORD_LABEL)).thenReturn(null);
        testWrapper.read(new File(EMPTY));
        //assertEquals(EMPTY, testWrapper.getLabel());

        when(tag.getFirst(FieldKey.RECORD_LABEL)).thenReturn(EMPTY);
        testWrapper.read(new File(EMPTY));
        //assertEquals(EMPTY, testWrapper.getLabel());
    }

    @Test
    public void readWithNullOrEmptyTitle() {
        when(tag.getFirst(FieldKey.TITLE)).thenReturn(null);
        testWrapper.read(new File(EMPTY));
        assertEquals(UNKNOWN_VALUE, testWrapper.getTrackTitle());

        when(tag.getFirst(FieldKey.TITLE)).thenReturn(EMPTY);
        testWrapper.read(new File(EMPTY));
        assertEquals(UNKNOWN_VALUE, testWrapper.getTrackTitle());
    }

    @Test
    public void readWithNullOrEmptyTrack() {
        when(tag.getFirst(FieldKey.TRACK)).thenReturn(null);
        testWrapper.read(new File(EMPTY));
        assertEquals(0, testWrapper.getTrack());

        when(tag.getFirst(FieldKey.TRACK)).thenReturn(EMPTY);
        testWrapper.read(new File(EMPTY));
        assertEquals(0, testWrapper.getTrack());
    }

    @Test
    public void readWithNullOrEmptyYear() {
        when(tag.getFirst(FieldKey.YEAR)).thenReturn(null);
        testWrapper.read(new File(EMPTY));
        assertEquals(EMPTY, testWrapper.getYear());

        when(tag.getFirst(FieldKey.YEAR)).thenReturn(EMPTY);
        testWrapper.read(new File(EMPTY));
        assertEquals(EMPTY, testWrapper.getYear());
    }

    @Test
    public void readWithNullOrEmptyOrigYear() {
        when(tag.getFirst(FieldKey.ORIGINAL_YEAR)).thenReturn(null);
        testWrapper.read(new File(EMPTY));
        assertEquals(EMPTY, testWrapper.getOrigYear());

        when(tag.getFirst(FieldKey.ORIGINAL_YEAR)).thenReturn(EMPTY);
        testWrapper.read(new File(EMPTY));
        assertEquals(EMPTY, testWrapper.getOrigYear());
    }

    @Test
    public void readWithNullOrEmptyCustomOrigYear() {
        /*when(originalYearTagField.toString()).thenReturn(null);
        testWrapper.read(new File(EMPTY));
        assertEquals(YEAR, testWrapper.getOrigYear());*/
    }

    @Test
    public void readWithNullOrEmptyDiscNum() {
        when(tag.getFirst(FieldKey.DISC_NO)).thenReturn(null);
        testWrapper.read(new File(EMPTY));
        assertEquals(0, testWrapper.getDiscNumber().intValue());

        when(tag.getFirst(FieldKey.DISC_NO)).thenReturn(EMPTY);
        testWrapper.read(new File(EMPTY));
        assertEquals(0, testWrapper.getDiscNumber().intValue());
    }

    @Test
    public void readWithNullOrEmptyGenres() {
        when(tag.getFirst(FieldKey.GENRE)).thenReturn(null);
        testWrapper.read(new File(EMPTY));
        assertNotNull(testWrapper.getGenres());
        assertTrue(testWrapper.getGenres().isEmpty());

        when(tag.getFirst(FieldKey.GENRE)).thenReturn(EMPTY);
        testWrapper.read(new File(EMPTY));
        assertNotNull(testWrapper.getGenres());
        assertTrue(testWrapper.getGenres().isEmpty());
    }
}