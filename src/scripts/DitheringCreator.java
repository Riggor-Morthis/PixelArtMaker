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

    public static void create(String importedFilePath, int exportedWidth, int paletteSize, String exportFilePath,
            int scaleFactor) throws IOException {
        createVariables(importedFilePath, exportedWidth, paletteSize);
        createPicture();
        PictureExporter.exportPicture(_exportedPicture, exportFilePath, scaleFactor);
    }

    /* PRIVATE METHODS */

    private static void createVariables(String filepath, int exportedWidth, int paletteSize) {
        _importedPicture = PixelArtConvertor.convert(filepath, exportedWidth);
        _importedLuminosity = LuminosityConvertor.convert(_importedPicture, paletteSize);
        _importedPalette = PaletteCreator.create(_importedPicture, _importedLuminosity, paletteSize);

        _paletteSize = Math.clamp(paletteSize, 2, 128);
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
        if (luminosity == 0) {
            _exportedPicture.setRGB(x, y, _importedPalette.get(0));
        } else if (luminosity >= _paletteSize) {
            _exportedPicture.setRGB(x, y, _importedPalette.get(_paletteSize - 1));
        } else {
            if ((x + y) % 2 == 0) {
                _exportedPicture.setRGB(x, y, _importedPalette.get(luminosity - 1));
            } else {
                _exportedPicture.setRGB(x, y, _importedPalette.get(luminosity));
            }
        }
    }
}
