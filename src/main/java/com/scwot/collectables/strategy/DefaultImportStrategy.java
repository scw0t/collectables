package com.scwot.collectables.strategy;

import com.google.common.collect.Lists;
import com.scwot.collectables.file.wrapper.DirectoryScope;
import com.scwot.collectables.file.wrapper.Mp3FileWrapper;
import com.scwot.collectables.file.metadata.MediumScopeMetadata;
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
import java.util.Collections;
import java.util.List;

@Data
@Slf4j
@Service
public class DefaultImportStrategy/* implements InputStrategy*/ {

    private DirectoryScope root;

    private List<MediumScopeMetadata> mediumScopeMetadataList = Lists.newArrayList();

    private int cdCount = 0;
    private int entryCount = 0;
    private int cdNotProcessed = 0;

    public List<MediumScopeMetadata> execute(File currentDir) {
        if (!currentDir.exists()) {
            log.warn(currentDir + " not exists!");
            return Collections.emptyList();
        }

        root = new DirectoryScope(currentDir);
        walk(currentDir);

        log.debug(root.toString());

        for (DirectoryScope prop : root.getChildList()) {
            log.debug(prop.toString());
        }

        return mediumScopeMetadataList;
    }

    private void walk(File parent) {
        try {
            Path startPath = parent.toPath();
            Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult preVisitDirectory(Path dir,
                                                         BasicFileAttributes attrs) {
                    final DirectoryScope currentEntry = new DirectoryScope(dir.toFile());

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

    private void read(Path dir, DirectoryScope currentEntry) {
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

    private void addMedium(DirectoryScope properties) {
        properties.setIsMedium(true);
        final MediumScopeMetadata mediumScopeMetadata = new MediumScopeMetadata();
        mediumScopeMetadata.convert(properties);
        if (mediumScopeMetadata.getDiscNumber() == 0) {
            mediumScopeMetadata.setDiscNumber(discNumber(properties));
        }
        mediumScopeMetadataList.add(mediumScopeMetadata);
    }

    private int discNumber(DirectoryScope currentEntry) {
        int cdN = 0;
        if (cdCount > 0 && currentEntry.hasAudio()) {
            cdN = cdCount - --cdNotProcessed;
        }
        return cdN;
    }
}
