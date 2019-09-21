import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class GeneralTest {

    private String[] ARTWORK_FOLDER_NAMES = {"cover", "covers", "artwork", "artworks", "images"};

    @Test
    public void first(){
        final boolean containsAny = StringUtils.containsAny("Artwork 600 dpi".toLowerCase(), ARTWORK_FOLDER_NAMES);

        System.out.println(containsAny);
    }

}
