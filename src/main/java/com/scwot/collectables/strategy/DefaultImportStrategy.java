package com.scwot.collectables.strategy;

import com.google.common.collect.Lists;
import com.scwot.collectables.filesystem.FileSystemWrapper;
import com.scwot.collectables.filesystem.Mp3FileWrapper;
import com.scwot.collectables.filesystem.ReleaseMetadata;
import com.scwot.collectables.utils.DirHelper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

@Data
@Slf4j
@Service
public class DefaultImportStrategy/* implements InputStrategy*/ {

    private FileSystemWrapper root;


    /*private String artist;
    private String album;
    private String year;
    private String origYear;
    private String mbReleaseId;
    private String label;
    private String catNumber;
    private List<String> artists = Lists.newArrayList();*/
    private List<ReleaseMetadata> releaseMetadataList = Lists.newArrayList();

    private int cdCount = 0;
    private int entryCount = 0;
    private int cdNotProcessed = 0;

    public void execute(File currentDir) {
        if (!currentDir.exists()) {
            log.warn(currentDir + " not exists!");
            return;
        }

        root = new FileSystemWrapper(currentDir);
        walk(currentDir);

        log.debug(root.toString());

        for (FileSystemWrapper prop : root.getChildList()) {
            log.debug(prop.toString());
        }

        /*if (!releaseMetadataList.isEmpty() && releaseMetadataList.get(0) != null) {
            artist = releaseMetadataList.get(0).getArtist();
            album = releaseMetadataList.get(0).getAlbum();
            year = releaseMetadataList.get(0).getReleasedYear();
            origYear = releaseMetadataList.get(0).getRecordedYear();
            mbReleaseId = releaseMetadataList.get(0).getMbReleaseId();
            label = releaseMetadataList.get(0).getLabel();
            catNumber = releaseMetadataList.get(0).getCatNum();
        }*/
    }

    private void walk(File parent) {
        try {
            Path startPath = parent.toPath();
            Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult preVisitDirectory(Path dir,
                                                         BasicFileAttributes attrs) {
                    final FileSystemWrapper currentEntry = new FileSystemWrapper(dir.toFile());

                    log.debug("-----> Curr dir: " + dir.toFile().toString());
                    read(dir, currentEntry);

                    if (entryCount == 0) {
                        root = currentEntry;
                        cdCount = DirHelper.getCdFoldersCount(dir.toFile());
                    } else {
                        root.addChild(currentEntry);
                        if (cdNotProcessed == 0) {
                            cdNotProcessed = cdCount;
                        }
                    }

                    if (currentEntry.hasAudio()) {
                        addMedium(currentEntry);
                    }

                    entryCount++;

                    log.debug("--------------");
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while walking the directory: "
                    + parent.getAbsolutePath() + "\n" + e.getMessage(), e);
        }
    }

    private void read(Path dir, FileSystemWrapper currentEntry) {
        final File[] list = dir.toFile().listFiles();
        int trackCount = 0;
        if (ArrayUtils.isNotEmpty(list)) {
            for (File file : list) {
                if (!file.isDirectory()) {
                    if (DirHelper.isAudioFile(file)) {
                        final Mp3FileWrapper audio = new Mp3FileWrapper();
                        audio.read(file);
                        audio.setTrackCount(++trackCount);
                        currentEntry.addAudio(audio);
                    } else if (DirHelper.isImageFile(file)) {
                        currentEntry.addImage(file);
                    } else {
                        currentEntry.addOther(file);
                    }
                }
            }
        }
    }

    private void addMedium(FileSystemWrapper properties) {
        final ReleaseMetadata releaseMetadata = new ReleaseMetadata(properties);
        releaseMetadata.readFromFiles();
        if (releaseMetadata.getDiscNumber() == 0) {
            releaseMetadata.setDiscNumber(discNumber(properties));
        }
        releaseMetadata.getProperties().setIsMedium(true);
        releaseMetadataList.add(releaseMetadata);
    }

    private int discNumber(FileSystemWrapper currentEntry) {
        int cdN = 0;
        if (cdCount > 0 && currentEntry.hasAudio()) {
            cdN = cdCount - --cdNotProcessed;
        }
        return cdN;
    }
}
