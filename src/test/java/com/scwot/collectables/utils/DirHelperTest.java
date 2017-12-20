package com.scwot.collectables.utils;

import java.io.File;

import org.junit.Test;

public class DirHelperTest {

    private String testPath1 = "D:\\MUSIC\\albums\\Primal Scream - Evil Heat";
    private String testPath2 = "D:\\MUSIC\\albums\\2001 - Bootleg Series, Volume 1 - "
            + "[2001, Polydor, 514 589 067-2] (3CD)";

    @Test
    public void countFileTypes1() throws Exception {
        DirHelper dirHelper = new DirHelper();
        File dir = new File(testPath1);
        dirHelper.countFileTypes(dir);
        printInfo(dirHelper, dir);
    }

    @Test
    public void countFileTypes2() throws Exception {
        DirHelper dirHelper = new DirHelper();
        File dir = new File(testPath2);
        dirHelper.countFileTypes(dir);
        printInfo(dirHelper, dir);
    }

    @Test
    public void getCdFoldersCount() throws Exception {
        File dir = new File(testPath2);
        int cdFoldersCount = DirHelper.getCdFoldersCount(dir);

        System.out.println(cdFoldersCount);
    }

    private void printInfo(DirHelper dirHelper, File dir) {
        System.out.print("has audio: " + dirHelper.hasAudio());
        System.out.println("\taudio: " + dirHelper.getAudioCount());
        System.out.print("has images: " + dirHelper.hasImages());
        System.out.println("\timages: " + dirHelper.getImagesCount());
        System.out.print("has others: " + dirHelper.hasOthers());
        System.out.println("\tothers: " + dirHelper.getOthersCount());

        System.out.println("has inner folder: " + dirHelper.hasInnerFolder(dir));
    }

}