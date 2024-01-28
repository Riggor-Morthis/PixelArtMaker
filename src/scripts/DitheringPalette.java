package scripts;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class DitheringPalette {

    /* VARIABLES */

    private static ImagePackage imagePackage;
    private static BufferedImage finalImage;
    private static int[] palette;
    private static ArrayList<Integer> filePalette;

    /* CONSTRUCTORS */

    private DitheringPalette() {
    }

    /* PUBLIC METHODS */

    public static void create(ImagePackage iP, String path, int scaleFactor, String palettePath) {
        imagePackage = iP;
        
        try {
            createFilePalette(palettePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        createImagePalette();
        createFinalPalette();
        createImage();
        exportImage(path, scaleFactor);
    }

    /* PRIVATE METHODS */

    private static void createFilePalette(String filePath) throws Exception {
        BufferedImage paletteImage = importPaletteFile(filePath);
        filePalette = new ArrayList<Integer>();
        int pixel;

        for (int x = 0; x < paletteImage.getWidth(); x++) {
            for (int y = 0; y < paletteImage.getHeight(); y++) {
                pixel = paletteImage.getRGB(x, y);
                if (((pixel >> 24) & 0xff) > (255 / 2) && !filePalette.contains(pixel)) {
                    filePalette.add(getNonTransparent(pixel));
                }
            }
        }

        if (filePalette.size() < imagePackage.getPaletteSize()) {
            throw new Exception("Palette file does not have enough colours");
        }
    }

    private static BufferedImage importPaletteFile(String filePath) {
        try {
            File imageFile = new File(filePath);
            return ImageIO.read(imageFile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private static int getNonTransparent(int pixel) {
        int r = (pixel >> 16) & 0xff;
        int g = (pixel >> 8) & 0xff;
        int b = (pixel) & 0xff;
        return 255 << 24 | r << 16 | g << 8 | b;
    }

    private static void createImagePalette() {
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

    private static void createFinalPalette() {
        int[] closestPalette = new int[palette.length];

        do {
            for (int i = 0; i < palette.length; i++) {
                if (closestPalette[i] == 0) {
                    closestPalette[i] = findClosestPaletteColour(palette[i]);
                }
            }
            cleanPaletteList(closestPalette);
        } while (isClosestPaletteValide(closestPalette));

        palette = closestPalette;
    }

    private static int findClosestPaletteColour(int ImageColour) {
        int paletteColour = filePalette.get(0);
        int distance = getDistance(paletteColour, ImageColour);

        for (int i = 1; i < filePalette.size(); i++) {
            if (getDistance(filePalette.get(i), ImageColour) < distance) {
                paletteColour = filePalette.get(i);
                distance = getDistance(filePalette.get(i), ImageColour);
            }
        }

        return paletteColour;
    }

    private static int getDistance(int pixel1, int pixel2) {
        int r = ((pixel1 >> 16) & 0xff) - ((pixel2 >> 16) & 0xff);
        int g = ((pixel1 >> 8) & 0xff) - ((pixel2 >> 8) & 0xff);
        int b = ((pixel1) & 0xff) - ((pixel2) & 0xff);

        return (int) (Math.pow(r, 2) + Math.pow(g, 2) + Math.pow(b, 2));
    }

    private static void cleanPaletteList(int[] closestPalette) {
        for (Integer colour : closestPalette) {
            if (filePalette.contains(colour)) {
                filePalette.remove(colour);
            }
        }
    }

    private static boolean isClosestPaletteValide(int[] closestPalette) {
        int currentPixel;
        boolean foundDuplicate = false;

        for (int i = 0; i < closestPalette.length - 1; i++) {
            currentPixel = closestPalette[i];

            for (int j = i + 1; j < closestPalette.length; j++) {
                if (currentPixel == closestPalette[j]) {
                    foundDuplicate = true;
                    if (getDistance(currentPixel, palette[i]) > getDistance(closestPalette[j], palette[j])) {
                        closestPalette[i] = 0;
                    } else {
                        closestPalette[j] = 0;
                    }
                }
            }
        }

        return foundDuplicate;
    }

    private static void createImage() {
        finalImage = new BufferedImage(imagePackage.getPixelArtWidth(), imagePackage.getPixelArtHeight(),
                BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < finalImage.getWidth(); x++) {
            for (int y = 0; y < finalImage.getHeight(); y++) {
                setColor(x, y, imagePackage.getLuminosity()[x][y]);
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
