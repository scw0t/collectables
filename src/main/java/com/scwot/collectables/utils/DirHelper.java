package com.scwot.collectables.utils;

import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class DirHelper {

    public static final String MACOSX_FOLDER_NAME = "__MACOSX";

    private int audioCount;
    private int imagesCount;
    private int othersCount;

    private static final Pattern PATTERN = Pattern.compile("(?i)^(cd |cd|cd_|cd-|disc|disc-|disc_|disc )\\d+.*");

    public void countFileTypes(File dir) {
        final List<File> list = (List<File>) FileUtils.listFiles(dir, null, false);
        try {
            for (File file : list) {
                final String mimeType = Files.probeContentType(file.toPath());

                Arrays.stream(AudioTypes.values())
                        .filter(audioType -> audioType.toString().equals(mimeType))
                        .forEach(audioType -> audioCount = getAudioCount() + 1);

                Arrays.stream(ImageTypes.values())
                        .filter(imageType -> imageType.toString().equals(mimeType))
                        .forEach(imageType -> imagesCount = getImagesCount() + 1);
            }
        } catch (IOException e) {
            throw new RuntimeException("Something wrong while counting file types on dir: " + dir.getAbsolutePath(), e);
        }

        othersCount = list.size() - getAudioCount() - getImagesCount();

        //System.out.println(dir.getAbsolutePath() + ": has ");
        //System.out.println("\t" + getAudioCount() + " audio");
        //System.out.println("\t" + getImagesCount() + " images");
        //System.out.println("\t" + getOthersCount() + " others");
    }

    /*
       Count subfolders which represents separate CDs (CD1, CD2, etc.)
    */
    public static int getCdFoldersCount(File dir) {
        int count = 0;

        final File[] directories = dir.listFiles((current, name) -> new File(current, name).isDirectory());

        if (directories == null) {
            throw new RuntimeException("Error while processing subfolders on dir: " + dir.getAbsolutePath());
        }

        if (directories.length > 1) {
            count = (int) Arrays.stream(directories)
                    .map(directory -> PATTERN.matcher(directory.getName()))
                    .filter(Matcher::matches)
                    .count();
        }

        final int correction = directories.length - count + 1;
        if (correction > count) {
            count = 0;
        }
        return count;
    }

    /*
       deletes unnecessary junk folders
    */
    public static void deleteDirectory(final File dir) {
        if (!dir.isDirectory()) {
            return;
        }

        Optional.ofNullable(dir.listFiles())
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .filter(File::isDirectory)
                .forEach(DirHelper::deleteDirectory);
        //.collect(Collectors.toList());

        /*File[] list = dir.listFiles();
        if (list != null) {

            for (File currentDir : list) {
                if (currentDir.isDirectory()) {
                    deleteDirectory(currentDir);
                }
                currentDir.delete();
            }
        }*/
        if (!dir.delete()) {
            System.out.println("can't delete folder : " + dir);
        }
    }

    public static List<File> listFilesAndDirs(File dir) {
        if (dir == null)
        {
            return Lists.newArrayList();
        }

        return (List<File>) FileUtils.listFilesAndDirs(dir,
                new NotFileFilter(TrueFileFilter.INSTANCE),
                DirectoryFileFilter.DIRECTORY);
    }

    public static boolean hasInnerFolder(File dir) {
        return listFilesAndDirs(dir).size() > 1;
    }

    public boolean doesNotContainRelease(File dir) {
        return !hasAudio() && DirHelper.getCdFoldersCount(dir) == 0 && !hasInnerFolder(dir);
    }

    public boolean containsJustInnerFolders(File dir) {
        return !hasAudio() && DirHelper.getCdFoldersCount(dir) == 0 && hasInnerFolder(dir);
    }

    public static boolean isAudioFile(File file) {
        return isExpectedTypeValue(file, AudioTypes.values());
    }

    public static boolean isImageFile(File file) {
        return isExpectedTypeValue(file, ImageTypes.values());
    }

    private static boolean isExpectedTypeValue(File file, Type[] types) {
        try {
            final String mimeType = Files.probeContentType(file.toPath());
            return Arrays.stream(types).anyMatch(type -> type.toString().equals(mimeType));
        } catch (IOException e) {
            throw new RuntimeException("error while processing type values for: "
                    + Arrays.asList(types) + "\n" + e.getMessage(), e);
        }
    }

    public boolean hasAudio() {
        return audioCount > 0;
    }

    public boolean hasImages() {
        return imagesCount > 0;
    }

    public boolean hasOthers() {
        return othersCount > 0;
    }

    public int getAudioCount() {
        return audioCount;
    }

    public int getImagesCount() {
        return imagesCount;
    }

    public int getOthersCount() {
        return othersCount;
    }

    private enum AudioTypes implements Type {
        MP3 {
            public String toString() {
                return "audio/mpeg";
            }
        }
    }

    private enum ImageTypes implements Type {
        JPG {
            public String toString() {
                return "image/jpeg";
            }
        },

        PNG {
            public String toString() {
                return "image/png";
            }
        },

        GIF {
            public String toString() {
                return "image/gif";
            }
        }
    }

    private enum OtherTypes implements Type {
        TXT {
            public String toString() {
                return "text/plain";
            }
        },

        M3U {
            public String toString() {
                return "audio/x-mpegurl";
            }
        },

        NULL {
            public String toString() {
                return "null";
            }
        }
    }

    private interface Type {
    }

}
