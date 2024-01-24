package scripts;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PictureExporter {

    /* CONSTRUCTORS */

    private PictureExporter() {
        // Just to be a static class
    }

    /* PUBLIC METHODS */

    public static synchronized void exportPicture(BufferedImage importedPicture, String path, int scaleFactor)
            throws IOException {
        BufferedImage exportedPicture = PictureScaler.scale(importedPicture, scaleFactor);

        File outputFile = new File(path);
        ImageIO.write(exportedPicture, "png", outputFile);
    }
}
