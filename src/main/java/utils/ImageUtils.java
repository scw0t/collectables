package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

public class ImageUtils {

    public static byte[] readImage(final String path) {
        final URL url = ImageUtils.class.getClassLoader().getClass().getResource(path);
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
