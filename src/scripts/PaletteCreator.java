package scripts;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PaletteCreator {

    /* VARIABLES */

    private static BufferedImage _importedPicture;
    private static int[][] _importedLuminosity;
    private static int _paletteSize;

    private static ArrayList<Integer> _exportedPalette;

    /* CONSTRUCTORS */

    private PaletteCreator() {
    }

    /* PUBLIC METHODS */

    public static ArrayList<Integer> create(BufferedImage importedPicture, int[][] importedLuminosity,
            int paletteSize) {
        createVariables(importedPicture, importedLuminosity, paletteSize);
        mainLoop();

        return _exportedPalette;
    }

    /* PRIVATE METHODS */

    private static void createVariables(BufferedImage importedPicture, int[][] importedLuminosity, int paletteSize) {
        _importedPicture = importedPicture;
        _importedLuminosity = importedLuminosity;
        _paletteSize = paletteSize;
        _exportedPalette = new ArrayList<Integer>();
    }

    private static void mainLoop() {
        float[] pixelValues;

        for (int p = 0; p < _paletteSize; p++) {
            pixelValues = checkEveryPixel(2 * p);

            _exportedPalette.add(convertToRgb(pixelValues));
        }
    }

    private static float[] checkEveryPixel(int luminosity) {
        float[] pixelValues = new float[4];
        float[] pixelBuffer;

        for (int x = 0; x < _importedPicture.getWidth(); x++) {
            for (int y = 0; y < _importedPicture.getHeight(); y++) {
                if (_importedLuminosity[x][y] == luminosity) {
                    pixelBuffer = extractPixelRgb(x, y);
                    for (int i = 0; i < 3; i++) {
                        pixelValues[i] += pixelBuffer[i];
                    }
                    pixelValues[3]++;
                }
            }
        }

        return pixelValues;
    }

    private static int convertToRgb(float[] pixelValues) {
        int[] intValues = getAverage(pixelValues);
        return 255 << 24 | intValues[0] << 16 | intValues[1] << 8 | intValues[2];
    }

    private static int[] getAverage(float[] pixelValues) {
        int[] intValues = new int[3];

        for (int i = 0; i < 3; i++) {
            pixelValues[i] /= pixelValues[3];
        }
        for (int i = 0; i < 3; i++) {
            intValues[i] = Math.round(pixelValues[i]);
        }

        return intValues;
    }

    private static float[] extractPixelRgb(int x, int y) {
        int pixel = _importedPicture.getRGB(x, y);
        return new float[] { (pixel >> 16) & 0xff, (pixel >> 8) & 0xff, (pixel) & 0xff };
    }
}
