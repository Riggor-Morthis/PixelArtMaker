package scripts;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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
        exportImage(path, scaleFactor);
    }

    /* PRIVATE METHODS */

    private static void createImage() {
        finalImage = new BufferedImage(imagePackage.getPixelArtWidth(), imagePackage.getPixelArtHeight(),
                BufferedImage.TYPE_INT_ARGB);
        createPalette();

        for (int x = 0; x < finalImage.getWidth(); x++) {
            for (int y = 0; y < finalImage.getHeight(); y++) {
                setColor(x, y, imagePackage.getLuminosity()[x][y]);
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

    private static void setColor(int x, int y, int luminosity) {
        if (luminosity % 2 == 0) {
            setEvenColor(x, y, luminosity / 2);
        } else {
            setOddColor(x, y, (luminosity - 1) / 2, (luminosity + 1) / 2);
        }
    }

    private static void setEvenColor(int x, int y, int luminosity) {
        finalImage.setRGB(x, y, palette[luminosity]);
    }

    private static void setOddColor(int x, int y, int luminosity1, int luminosity2) {
        if ((x + y) % 2 == 0) {
            finalImage.setRGB(x, y, palette[luminosity1]);
        } else {
            finalImage.setRGB(x, y, palette[luminosity2]);
        }
    }

    private static synchronized void exportImage(String path, int scaleFactor) {
        scaleImage(scaleFactor);

        try {
            File outputFile = new File(path);
            ImageIO.write(finalImage, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void scaleImage(int scaleFactor) {
        int pixel;
        BufferedImage bufferImage = new BufferedImage(imagePackage.getPixelArtWidth() * scaleFactor,
                imagePackage.getPixelArtHeight() * scaleFactor,
                BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < imagePackage.getPixelArtWidth(); x++) {
            for (int y = 0; y < imagePackage.getPixelArtHeight(); y++) {
                pixel = finalImage.getRGB(x, y);

                for (int i = 0; i < scaleFactor; i++) {
                    for (int j = 0; j < scaleFactor; j++) {
                        bufferImage.setRGB(x * scaleFactor + i, y * scaleFactor + j, pixel);
                    }
                }
            }
        }

        finalImage = bufferImage;
    }
}
