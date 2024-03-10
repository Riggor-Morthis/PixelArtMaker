package trashbin;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Utils {

    /* CONSTRUCTORS */

    private Utils() {
    }

    /* PUBLIC METHODS */

    public static synchronized void exportImage(String path, int scaleFactor, BufferedImage finalImage,
            ImagePackage imagePackage) {
        finalImage = scaleImage(scaleFactor, finalImage, imagePackage);

        try {
            File outputFile = new File(path);
            ImageIO.write(finalImage, "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage setColor(int x, int y, int luminosity, BufferedImage finalImage, int[] palette) {
        if (luminosity % 2 == 0) {
            finalImage = setEvenColor(x, y, luminosity / 2, finalImage, palette);
        } else {
            finalImage = setOddColor(x, y, (luminosity - 1) / 2, (luminosity + 1) / 2, finalImage, palette);
        }
        return finalImage;
    }

    public static int getPixelsAverage(int luminosity, ImagePackage imagePackage) {
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

    /* PRIVATE METHODS */

    private static BufferedImage scaleImage(int scaleFactor, BufferedImage finalImage, ImagePackage imagePackage) {
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

        return bufferImage;
    }

    private static BufferedImage setEvenColor(int x, int y, int luminosity, BufferedImage finalImage, int[] palette) {
        finalImage.setRGB(x, y, palette[luminosity]);
        return finalImage;
    }

    private static BufferedImage setOddColor(int x, int y, int luminosity1, int luminosity2, BufferedImage finalImage,
            int[] palette) {
        if ((x + y) % 2 == 0) {
            finalImage.setRGB(x, y, palette[luminosity1]);
        } else {
            finalImage.setRGB(x, y, palette[luminosity2]);
        }
        return finalImage;
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