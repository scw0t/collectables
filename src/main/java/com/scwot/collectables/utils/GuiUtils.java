package com.scwot.collectables.utils;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class GuiUtils {

    private static final String DEFAULT_DIRECTORY = "C:\\Users\\Vladislav_Evseev\\temp";
    private static final String DEFAULT_ROOT = "C:\\";

    public static File showDirectoryChooser(final Stage primaryStage) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Choose your path");
        chooser.setInitialDirectory(setDefaultDirectory());
        return chooser.showDialog(primaryStage);
    }

    private static File setDefaultDirectory() {
        final File dir = new File(DEFAULT_DIRECTORY);
        if (dir.exists()) {
            return dir;
        }

        return new File(DEFAULT_ROOT);
    }

}
