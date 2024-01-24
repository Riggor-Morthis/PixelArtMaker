package scripts;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PaletteExtractorFromFile {

    /* VARIABLES */
    private static ArrayList<Integer> _exportedPalette;

    /* CONSTRUCTORS */

    private PaletteExtractorFromFile() {
    }

    /* PUBLIC METHODS */

    public static ArrayList<Integer> extract(String paletteFilePath) {
        createFilePalette(paletteFilePath);

        return _exportedPalette;
    }

    /* PRIVATE METHODS */

    private static void createFilePalette(String paletteFilePath) {
        BufferedImage importedFile = PictureImporter.importPicture(paletteFilePath);
        _exportedPalette = new ArrayList<Integer>();

        for (int x = 0; x < importedFile.getWidth(); x++) {
            for (int y = 0; y < importedFile.getHeight(); y++) {
                checkPixel(importedFile, x, y);
            }
        }
    }

    private static void checkPixel(BufferedImage paletteFile, int x, int y) {
        if (extractPixelAlpha(paletteFile, x, y) != 0) {
            int pixel = paletteFile.getRGB(x, y);
            if (!_exportedPalette.contains(pixel)) {
                _exportedPalette.add(pixel);
            }
        }
    }

    private static int extractPixelAlpha(BufferedImage importedPicture, int x, int y) {
        int pixel = importedPicture.getRGB(x, y);
        return (pixel >> 24) & 0xff;
    }
}
