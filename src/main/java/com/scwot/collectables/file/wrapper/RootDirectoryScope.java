package com.scwot.collectables.file.wrapper;

import com.scwot.collectables.utils.DirHelper;
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class RootDirectoryScope {

    private List<DirectoryScope> directories;

    private String[] ARTWORK_FILE_NAMES = {"folder", "cover", "artwork"};
    private String[] ARTWORK_FOLDER_NAMES = {"cover", "covers", "artwork", "artworks", "images"};
    private byte[] frontImage;

    @SneakyThrows
    public RootDirectoryScope(List<DirectoryScope> directories) {
        this.directories = directories;
        frontImage = Files.readAllBytes(frontArtworkLookup().toPath());
    }

    private File frontArtworkLookup() {
        final List<File> rootFilesAndDirs = findRootFileList();

        File file = null;
        final DirHelper dirHelper = new DirHelper();
        for (File dir : rootFilesAndDirs) {
            if (dir.isDirectory())
            {
                if (StringUtils.containsAny(dir.getName().toLowerCase(), ARTWORK_FOLDER_NAMES))
                {
                    file = dir;
                    break;
                }
            }
        }

        System.out.println();


        final File artworkFolder = rootFilesAndDirs.stream()
                .filter(File::isDirectory)
                .filter(f -> StringUtils.containsAny(f.getName().toLowerCase(), ARTWORK_FOLDER_NAMES))
                .findFirst().get();

        final List<File> artworkFilesAndDirs = DirHelper.listFilesAndDirs(artworkFolder);

        final List<File> rootImages = rootFilesAndDirs.stream()
                .filter(DirHelper::isImageFile)
                .collect(Collectors.toList());

        final File frontImage = rootImages.stream()
                .filter(f -> StringUtils.containsAny(f.getName().toLowerCase(), ARTWORK_FILE_NAMES))
                .findFirst().orElseGet(() -> artworkFilesAndDirs.stream()
                        .filter(DirHelper::isImageFile)
                        .filter(f -> StringUtils.containsAny(f.getName().toLowerCase(), ARTWORK_FILE_NAMES))
                        .findFirst().orElse(null));

        return frontImage;
    }

    private List<File> findRootFileList() {
        final File root = directories.stream()
                .map(DirectoryScope::getCurrentDir)
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .getParentFile();

        return DirHelper.listFilesAndDirs(root);
    }
}
