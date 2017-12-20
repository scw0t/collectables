package com.scwot.collectables.filesystem;

import com.google.common.collect.Lists;
import lombok.Data;

import java.io.File;
import java.util.List;

import static java.lang.Boolean.FALSE;

@Data
public class FileSystemWrapper {

    private List<FileSystemWrapper> childs = Lists.newArrayList();

    private List<Mp3FileWrapper> listOfAudios = Lists.newArrayList();
    private List<File> listOfImages = Lists.newArrayList();
    private List<File> listOfOthers = Lists.newArrayList();
    private List<File> listOfInnerFolders = Lists.newArrayList();

    private File currentDir;
    private String dirName;
    private Integer isMultiCd;
    private Boolean hasMoreThanOneArtists = FALSE;
    private Boolean isMedium = FALSE;

    public FileSystemWrapper(File inputDir) {
        dirName = inputDir.getName();
    }

    public void addChild(FileSystemWrapper child) {
        childs.add(child);
    }

    public void addAudio(Mp3FileWrapper audio) {
        listOfAudios.add(audio);
    }

    public void addImage(File image) {
        listOfImages.add(image);
    }

    public void addOther(File other) {
        listOfOthers.add(other);
    }

    public boolean hasAudio() {
        return !listOfAudios.isEmpty();
    }

    public boolean hasImages() {
        return !listOfImages.isEmpty();
    }

    public boolean hasOthers() {
        return !listOfOthers.isEmpty();
    }

    public boolean hasInnerFolders() {
        return !listOfInnerFolders.isEmpty();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Current dir: ");
        builder.append(currentDir.getAbsolutePath());
        builder.append(System.lineSeparator());

        builder.append("hasAudio: ");
        builder.append(hasAudio());
        builder.append(", amount: ");
        builder.append(listOfAudios.size());
        builder.append(System.lineSeparator());
        builder.append(printList(listOfAudios));
        builder.append(System.lineSeparator());

        builder.append("hasImages: ");
        builder.append(hasImages());
        builder.append(", amount: ");
        builder.append(listOfImages.size());
        builder.append(System.lineSeparator());
        builder.append(printList(listOfImages));
        builder.append(System.lineSeparator());

        builder.append("hasOthers: ");
        builder.append(hasOthers());
        builder.append(", amount: ");
        builder.append(listOfOthers.size());
        builder.append(System.lineSeparator());
        builder.append(printList(listOfOthers));
        builder.append(System.lineSeparator());

        return builder.toString();
    }

    private String printList(List<?> list) {
        StringBuilder builder = new StringBuilder();

        for (Object obj : list) {
            builder.append("\t");
            builder.append(obj.toString());
            builder.append(System.lineSeparator());
        }

        return builder.toString();
    }

}
