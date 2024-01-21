package scripts;

import java.awt.image.BufferedImage;

public class LuminosityConvertor {

    /* VARIABLES */

    private static BufferedImage _importedPicture;
    private static int[][] _exportedLuminosity;

    private static int _importedWidth, _importedHeight;

    private static int _lMin, _lMax;

    /* CONSTRUCTORS */

    private LuminosityConvertor() {
    }

    /* PUBLIC METHODS */

    public static int[][] convert(BufferedImage importedPicture, int paletteSize) {
        _importedPicture = importedPicture;
        createVariables();
        firstLoop();
        secondLoop(paletteSize);

        return _exportedLuminosity;
    }

    /* PRIVATE METHODS */

    private static void createVariables() {
        _importedHeight = _importedPicture.getHeight();
        _importedWidth = _importedPicture.getWidth();

        _exportedLuminosity = new int[_importedWidth][_importedHeight];
    }

    private static void firstLoop() {
        int luminosity;
        _lMin = getLuminosityValue(_importedPicture.getRGB(0, 0));
        _lMax = getLuminosityValue(_importedPicture.getRGB(0, 0));

        for (int x = 0; x < _importedWidth; x++) {
            for (int y = 0; y < _importedHeight; y++) {
                luminosity = getLuminosityValue(_importedPicture.getRGB(x, y));
                _exportedLuminosity[x][y] = luminosity;

                if (_lMin > luminosity) {
                    _lMin = luminosity;
                } else if (_lMax < luminosity) {
                    _lMax = luminosity;
                }
            }
        }
    }

    private static int getLuminosityValue(int pixel) {
        int[] rgb = getRgbValues(pixel);
        return Math.round(rgb[0] * (1f / 3f))
                + Math.round(rgb[1] * (1f / 3f))
                + Math.round(rgb[2] * (1f / 3f));
    }

    private static int[] getRgbValues(int pixel) {
        return new int[] { (pixel >> 16) & 0xff, (pixel >> 8) & 0xff, (pixel) & 0xff };
    }

    private static void secondLoop(int paletteSize) {
        int nbClusters = (2 * paletteSize - 2);
        int shadeSteps = (int) Math.floor(((float) _lMax - (float) _lMin) / (float) nbClusters);

        for (int x = 0; x < _importedWidth; x++) {
            for (int y = 0; y < _importedHeight; y++) {
                _exportedLuminosity[x][y] = Math.clamp((_exportedLuminosity[x][y] - _lMin) / shadeSteps, 0, nbClusters);
            }
        }
    }
}
