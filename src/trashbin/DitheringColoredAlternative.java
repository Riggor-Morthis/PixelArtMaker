package trashbin;

import java.awt.image.BufferedImage;

public class DitheringColoredAlternative {

    /* VARIABLES */

    private static ImagePackage imagePackage;
    private static BufferedImage finalImage;
    private static int[] palette;

    /* CONSTRUCTORS */
    private DitheringColoredAlternative() {
    }

    /* PUBLIC METHODS */

    public static void create(ImagePackage iP, String path, int scaleFactor) {
        imagePackage = iP;
        createImage();
        Utils.exportImage(path, scaleFactor, finalImage, iP);
    }

    /* PRIVATE METHODS */

    private static void createImage() {
        finalImage = new BufferedImage(imagePackage.getPixelArtWidth(), imagePackage.getPixelArtHeight(),
                BufferedImage.TYPE_INT_ARGB);
        createPalette();

        for (int x = 0; x < finalImage.getWidth(); x++) {
            for (int y = 0; y < finalImage.getHeight(); y++) {
                finalImage = Utils.setColor(x, y, imagePackage.getLuminosity()[x][y], finalImage, palette);
            }
        }
    }

    private static void createPalette() {
        palette = new int[imagePackage.getPaletteSize()];

        for (int i = 0; i < imagePackage.getPaletteSize(); i++) {
            palette[i] = getPixelsAverage(i * 2, imagePackage);
        }
    }

    private static int getPixelsAverage(int luminosity, ImagePackage imagePackage) {
        float[] pixelValues = new float[4];
        int[] pixelBuffer;

        for (int x = 0; x < imagePackage.getPixelArtWidth(); x++) {
            for (int y = 0; y < imagePackage.getPixelArtHeight(); y++) {

                if (imagePackage.getLuminosity()[x][y] == luminosity
                        || imagePackage.getLuminosity()[x][y] == luminosity - 1
                        || imagePackage.getLuminosity()[x][y] == luminosity + 1) {
                    pixelBuffer = extractPixelRgb(imagePackage.getBaseImage().getRGB(x, y));
                    for (int i = 0; i < 3; i++) {
                        pixelValues[i] += pixelBuffer[i];
                    }
                    pixelValues[3]++;
                }
            }
        }

        return getAverage(pixelValues);
    }

    private static int[] extractPixelRgb(int pixel) {
        return new int[] { (pixel >> 16) & 0xff, (pixel >> 8) & 0xff, (pixel) & 0xff };
    }

    private static int getAverage(float[] pixelValues) {
        int r = Math.round(pixelValues[0] / pixelValues[3]);
        int g = Math.round(pixelValues[1] / pixelValues[3]);
        int b = Math.round(pixelValues[2] / pixelValues[3]);

        return 255 << 24 | r << 16 | g << 8 | b;
    }
}
