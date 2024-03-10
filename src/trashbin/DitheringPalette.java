package trashbin;

import java.awt.image.BufferedImage;
import java.io.File;
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
        Utils.exportImage(path, scaleFactor, finalImage, iP);
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
            palette[i] = Utils.getPixelsAverage(i * 2, imagePackage);
        }
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
                finalImage = Utils.setColor(x, y, imagePackage.getLuminosity()[x][y], finalImage, palette);
            }
        }
    }
}
