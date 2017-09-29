package com.scwot.collectables.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public class ImageUtils {

    public static final String DEFAULT_PHOTO_PATH = "/default/default-photo.jpg";

    public static byte[] readImage(final String path) {
        final URL url = ImageUtils.class.getClassLoader().getClass().getResource(path);
        if (url == null) {
            throw new IllegalArgumentException("default photo is missing. Expected path: " + path);
        }

        final File file = new File(url.getPath());
        final byte[] bFile = new byte[(int) file.length()];

        try (final FileInputStream fileInputStream = new FileInputStream(file)) {
            fileInputStream.read(bFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bFile;
    }

}
