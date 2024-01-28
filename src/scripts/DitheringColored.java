package scripts;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class DitheringColored {

    /* VARIABLES */

    private static ImagePackage imagePackage;
    private static BufferedImage finalImage;
    private static int[] palette;

    /* CONSTRUCTORS */

    private DitheringColored() {
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

        for (int i = 0; i < imagePackage.getPaletteSize(); i++) {
            palette[i] = getPixelsAverage(i * 2);
        }
    }

    private static int getPixelsAverage(int luminosity) {
        float[] pixelValues = new float[4];
        int[] pixelBuffer;

        for (int x = 0; x < imagePackage.getPixelArtWidth(); x++) {
            for (int y = 0; y < imagePackage.getPixelArtHeight(); y++) {

                if (imagePackage.getLuminosity()[x][y] == luminosity) {
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
