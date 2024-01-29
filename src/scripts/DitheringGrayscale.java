package scripts;

import java.awt.image.BufferedImage;

public class DitheringGrayscale {

    /* VARIABLES */

    private static ImagePackage imagePackage;
    private static BufferedImage finalImage;
    private static int[] palette;

    /* CONSTRUCTORS */

    private DitheringGrayscale() {
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
        int greyStep = 255 / (imagePackage.getPaletteSize() - 1);

        for (int i = 0; i < imagePackage.getPaletteSize(); i++) {
            palette[i] = convertToRgb(greyStep * i);
        }
    }

    private static int convertToRgb(int grey) {
        return 255 << 24 | grey << 16 | grey << 8 | grey;
    }
}
