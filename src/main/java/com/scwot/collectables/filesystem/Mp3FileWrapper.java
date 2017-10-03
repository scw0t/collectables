package com.scwot.collectables.filesystem;

import lombok.Data;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagField;
import org.jaudiotagger.tag.id3.ID3v24Tag;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class Mp3FileWrapper {

    public static final String EMPTY_STRING = "";
    public static final String CUSTOM_FIELD = "TXXX";
    public static final String CATALOGNUMBER_TAG_NAME = "CATALOGNUMBER";
    public static final String ORIGINALYEAR_TAG_NAME = "originalyear";

    private static final String UNKNOWN_VALUE = "[unknown]";
    private static final String[] GENRE_DELIMITERS = {", ", ";", "\\", "/"};
    private static final Pattern TRACK_PATTERN = Pattern.compile("^\\d{1,2}");

    private MP3File audioFile;
    private int fileNum;
    private String fileName;
    private String artistTitle;
    private String albumTitle;
    private String trackTitle;
    private String year;
    private String origYear;
    private String label;
    private String catNum;
    private List<String> genres;
    private Boolean hasArtwork;
    private String trackNumber;
    private Integer discNumber;
    private String MBReleaseID;
    private int trackCount;

    public void read(File file) {
        try {
            audioFile = (MP3File) AudioFileIO.read(file);
            processNullTag(audioFile);

            artistTitle = fromTag(audioFile, FieldKey.ARTIST, UNKNOWN_VALUE);
            albumTitle = fromTag(audioFile, FieldKey.ALBUM, UNKNOWN_VALUE);
            MBReleaseID = fromTag(audioFile, FieldKey.MUSICBRAINZ_RELEASEID, EMPTY_STRING);
            label = fromTag(audioFile, FieldKey.RECORD_LABEL, EMPTY_STRING);
            trackTitle = fromTag(audioFile, FieldKey.TITLE, UNKNOWN_VALUE);
            trackNumber = fromTag(audioFile, FieldKey.TRACK, String.valueOf(trackCount));

            year = boundedFromTag(audioFile, FieldKey.YEAR, EMPTY_STRING);
            origYear = origYearValue(audioFile, FieldKey.ORIGINAL_YEAR, EMPTY_STRING);
            discNumber = discNumberValue(audioFile);
            genres = genresValue(audioFile);
            hasArtwork = artworkValue(audioFile);
            catNum = fromCustomTag(audioFile, CATALOGNUMBER_TAG_NAME, EMPTY_STRING);
        } catch (Exception ex) {
            System.out.println(ex.getMessage() + " " + file.getAbsolutePath());
        }
    }

    private static String boundedFromTag(MP3File audioFile, FieldKey fieldKey, String defaultValue) {
        try {
            return fromTag(audioFile, fieldKey, defaultValue);
        } catch (StringIndexOutOfBoundsException ex) {
            throw new RuntimeException("Error while parsing bounded value from tag: " +
                    audioFile.getFile().getAbsolutePath() + "\n" + ex.getMessage(), ex);
        }
    }

    private static String origYearValue(MP3File audioFile, FieldKey fieldKey, String defaultValue) {
        final String value = boundedFromTag(audioFile, fieldKey, defaultValue);
        final String origYear = fromCustomTag(audioFile, ORIGINALYEAR_TAG_NAME, defaultValue);

        if (!value.isEmpty() && (Integer.valueOf(value) > Integer.valueOf(origYear))) {
            return origYear;
        }

        return value;
    }

    private static String fromCustomTag(MP3File audioFile, String customTagName, String defaultValue) {
        final List<TagField> tags = audioFile.getID3v2Tag().getFields(CUSTOM_FIELD);
        return tags.stream()
                .map(tag -> tagValue(tag.toString(), customTagName))
                .filter(value -> !value.isEmpty())
                .findFirst()
                .orElse(defaultValue);
    }

    private static int discNumberValue(MP3File audioFile) {
        int value = 0;
        String discNumberTag = audioFile.getTag().getFirst(FieldKey.DISC_NO);
        if (!EMPTY_STRING.equals(discNumberTag)) {
            discNumberTag = discNumberTag
                    .replaceFirst("^0", EMPTY_STRING)
                    .replaceAll("/.+", EMPTY_STRING)
                    .replaceAll("\\D", EMPTY_STRING);
            if (!EMPTY_STRING.equals(discNumberTag)) {
                value = Integer.valueOf(discNumberTag);
            }
        }
        return value;
    }

    private static List<String> genresValue(MP3File audioFile) {
        List<String> genresList = Collections.emptyList();
        String[] genres = splittedGenres(audioFile.getTag().getFirst(FieldKey.GENRE));
        if (genres.length > 0) {
            Collections.addAll(genresList, genres);
        } else {
            genresList.add(audioFile.getTag().getFirst(FieldKey.GENRE));
        }
        return genresList;
    }

    private static String[] splittedGenres(String genreString) {
        String[] splittedGenres = {};
        for (String delimiter : GENRE_DELIMITERS) {
            if (genreString.contains(delimiter)) {
                splittedGenres = genreString.split(delimiter);
                break;
            }
        }
        return splittedGenres;
    }

    private static boolean artworkValue(MP3File audioFile) {
        boolean value = false;
        if (!audioFile.getTag().getArtworkList().isEmpty()) {
            value = true;
        }
        return value;
    }

    public int getTrack() {
        int value = 0;
        String trackTag = audioFile.getTag().getFirst(FieldKey.TRACK);

        if (!trackTag.isEmpty()) {
            Matcher matcher = TRACK_PATTERN.matcher(trackTag);
            if (matcher.find())
                value = Integer.valueOf(matcher.group(0));
        }

        return value;
    }

    private static String tagValue(String tag, String key) {
        String value = EMPTY_STRING;
        if (tag.contains(key)) {
            value = tag.split("; ")[1];
            value = value
                    .replaceAll("Text=", EMPTY_STRING)
                    .replaceAll("\"", EMPTY_STRING)
                    .replaceAll(";", EMPTY_STRING)
                    .replaceAll("\u0000", EMPTY_STRING);
        }
        return value;
    }

    private static void processNullTag(MP3File audioFile) {
        if (audioFile.getTag() == null) {
            audioFile.setTag(new ID3v24Tag());
            try {
                audioFile.commit();
            } catch (CannotWriteException ex) {
                throw new RuntimeException("Error while processing null-tag audio file: " +
                        audioFile.getFile().getAbsolutePath() + "\n" + ex.getMessage(), ex);
            }
        }
    }

    private static String fromTag(MP3File audioFile, FieldKey fieldKey, String defaultValue) {
        final String tagValue = fromAudio(audioFile, fieldKey);
        if (!StringUtils.isEmpty(tagValue)) {
            return tagValue;
        }

        return defaultValue;
    }

    private static String fromAudio(MP3File audioFile, FieldKey fieldKey) {
        return audioFile.getTag().getFirst(fieldKey);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("File: ");
        builder.append(audioFile.getFile().getAbsolutePath());
        builder.append(System.lineSeparator());
        builder.append("Artist: ");
        builder.append(artistTitle);
        builder.append(System.lineSeparator());
        builder.append("Album: ");
        builder.append(albumTitle);
        builder.append(System.lineSeparator());
        builder.append("Track: ");
        builder.append(trackNumber);
        builder.append(System.lineSeparator());
        builder.append("Title: ");
        builder.append(trackTitle);
        builder.append(System.lineSeparator());
        builder.append("Year: ");
        builder.append(year);
        builder.append(System.lineSeparator());
        builder.append("Artwork: ");
        builder.append(hasArtwork);
        builder.append(System.lineSeparator());
        builder.append("Disc Number: ");
        builder.append(discNumber);
        builder.append(System.lineSeparator());
        builder.append("Genres: ");
        for (String genre : genres) {
            builder.append(genre);
            builder.append(" / ");
        }
        builder.append(System.lineSeparator());
        builder.append("--------------------------");
        return builder.toString();
    }

}
