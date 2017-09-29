package com.scwot.collectables.strategies;

import com.google.common.collect.Lists;
import com.scwot.collectables.filesystem.FileSystemWrapper;
import com.scwot.collectables.filesystem.Mp3FileWrapper;
import com.scwot.collectables.filesystem.ReleaseMetadata;
import com.scwot.collectables.utils.DirHelper;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

@Data
public class DefaultImportStrategy/* implements InputStrategy*/ {

    private FileSystemWrapper root;

    private String artist;
    private String album;
    private String year;
    private String origYear;
    private String MBReleaseID;
    private List<ReleaseMetadata> releaseMetadataList = Lists.newArrayList();
    private String label;
    private String catNumber;

    private int cdCount = 0;
    private int entryCount = 0;
    private int cdNotProcessed = 0;

    public void execute(File inpitDir) {
        try {
            root = new FileSystemWrapper(inpitDir);
            walk(inpitDir);

            String rootStr = root.toString();
            System.out.println(rootStr);

            for (FileSystemWrapper prop : root.getChilds()) {
                String propStr = prop.toString();
                System.out.println(propStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!releaseMetadataList.isEmpty() && releaseMetadataList.get(0) != null) {
            artist = releaseMetadataList.get(0).getArtist();
            album = releaseMetadataList.get(0).getAlbum();
            year = releaseMetadataList.get(0).getReleasedYear();
            origYear = releaseMetadataList.get(0).getRecordedYear();
            MBReleaseID = releaseMetadataList.get(0).getMBReleaseID();
            label = releaseMetadataList.get(0).getLabel();
            catNumber = releaseMetadataList.get(0).getCatNum();
        }
    }

    private void walk(File parent) {
        try {
            Path startPath = parent.toPath();
            Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult preVisitDirectory(Path dir,
                                                         BasicFileAttributes attrs) {
                    int trackCount = 0;
                    File[] list = dir.toFile().listFiles();
                    FileSystemWrapper currentEntry = new FileSystemWrapper(dir.toFile());

                    System.out.println("-----> Curr dir: " + dir.toFile().toString());
                    for (File file : list) {
                        if (!file.isDirectory()) {
                            if (DirHelper.isAudioFile(file)) {
                                Mp3FileWrapper audio = new Mp3FileWrapper();
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

                    if (entryCount == 0) {
                        root = currentEntry;
                        cdCount = DirHelper.getCDFoldersCount(dir.toFile());
                    } else {
                        root.addChild(currentEntry);
                        if (cdNotProcessed == 0)
                            cdNotProcessed = cdCount;
                    }

                    if (currentEntry.hasAudio()) {
                        addMedium(currentEntry);
                    }

                    entryCount++;

                    System.out.println("--------------");
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addMedium(FileSystemWrapper properties) {
        ReleaseMetadata releaseMetadata = new ReleaseMetadata();
        releaseMetadata.readFromFiles(properties);
        if (releaseMetadata.getDiscNumber() == 0)
            releaseMetadata.setDiscNumber(discNumber(properties));
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
