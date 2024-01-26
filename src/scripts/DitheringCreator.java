package scripts;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class DitheringCreator {

    /* VARIABLES */

    private static BufferedImage _exportedPicture;

    private static BufferedImage _importedPicture;
    private static int[][] _importedLuminosity;
    private static ArrayList<Integer> _importedPalette;

    private static int _importedWidth, _importedHeight;
    private static int _paletteSize;

    /* CONSTRUCTORS */

    private DitheringCreator() {
    }

    /* PUBLIC METHODS */

    public static void create(String importedPicturePath, boolean greyscale, int exportedWidth, int paletteSize, String exportFilePath,
            int scaleFactor) throws IOException {

        createVariables(importedPicturePath, exportedWidth, paletteSize);
        _importedPalette = PaletteCreatorFromScratch.create(_importedPicture, greyscale, _importedLuminosity, _paletteSize);

        createPicture();
        PictureExporter.exportPicture(_exportedPicture, exportFilePath, scaleFactor);
    }

    public static void create(String importedPicturePath, String importedFilePath, int exportedWidth, int paletteSize,
            String exportFilePath,
            int scaleFactor) throws IOException {

        createVariables(importedPicturePath, exportedWidth, paletteSize);
        _importedPalette = PaletteCreatorFromFile.create(_importedPicture, importedFilePath, _importedLuminosity,
                paletteSize);

        createPicture();
        PictureExporter.exportPicture(_exportedPicture, exportFilePath, scaleFactor);
    }

    /* PRIVATE METHODS */

    private static void createVariables(String filepath, int exportedWidth, int paletteSize) {
        _paletteSize = Math.clamp(paletteSize, 2, 64);

        _importedPicture = PixelArtConvertor.convert(filepath, exportedWidth);
        _importedLuminosity = LuminosityConvertor.convert(_importedPicture, _paletteSize);

        _importedWidth = _importedPicture.getWidth();
        _importedHeight = _importedPicture.getHeight();

        _exportedPicture = new BufferedImage(_importedWidth, _importedHeight, BufferedImage.TYPE_INT_ARGB);
    }

    private static void createPicture() {
        for (int x = 0; x < _importedWidth; x++) {
            for (int y = 0; y < _importedHeight; y++) {
                setColor(x, y, _importedLuminosity[x][y]);
            }
        }
    }

    private static void setColor(int x, int y, int luminosity) {
        if (luminosity % 2 == 0) {
            setEvenColor(x, y, luminosity / 2);
        } else {
            setOddColor(x, y, (luminosity - 1) / 2, (luminosity + 1) / 2);
        }
    }

    private static void setEvenColor(int x, int y, int luminosity) {
        _exportedPicture.setRGB(x, y, _importedPalette.get(luminosity));
    }

    private static void setOddColor(int x, int y, int luminosity1, int luminosity2) {
        if ((x + y) % 2 == 0) {
            _exportedPicture.setRGB(x, y, _importedPalette.get(luminosity1));
        } else {
            _exportedPicture.setRGB(x, y, _importedPalette.get(luminosity2));
        }
    }
}
