package scripts;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImagePackage {

    /* VARIABLES */

    private BufferedImage baseImage;
    private int[][] luminosity;
    private int paletteSize;

    private int imageWidth, imageHeight;
    private int pixelArtWidth, pixelArtHeight;

    private int tileWidth, tileHeight;
    private float xStepBack, yStepBack;

    /* CONSTRUCTORS */

    public ImagePackage(String imagePath, int imageWidth, int paletteSize) {
        importImage(imagePath);
        convertToPixelArt(imageWidth);
        convertToLuminosity(paletteSize);
    }

    /* PRIVATE METHODS */

    private void importImage(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            baseImage = ImageIO.read(imageFile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        imageHeight = baseImage.getHeight();
        imageWidth = baseImage.getWidth();

        luminosity = new int[imageWidth][imageHeight];
    }

    private void convertToPixelArt(int paWidth) {
        createPixelArtVariables(paWidth);
        pixelArtLoop();
    }

    private void createPixelArtVariables(int paWidth) {
        pixelArtWidth = paWidth;
        pixelArtHeight = Math
                .round(((float) (pixelArtWidth) / (float) (imageWidth) * (float) (imageHeight)));

        tileWidth = (imageWidth / pixelArtWidth) + 1;
        tileHeight = (imageHeight / pixelArtHeight) + 1;

        int sparePixel = (pixelArtWidth * tileWidth) - imageWidth;
        xStepBack = (float) (sparePixel) / (float) (pixelArtWidth - 1);

        sparePixel = (pixelArtHeight * tileHeight) - imageHeight;
        yStepBack = (float) (sparePixel) / (float) (pixelArtHeight - 1);
    }

    private void pixelArtLoop() {
        int xOffset, yOffset;
        int currentXCoord, currentYCoord;
        BufferedImage bufferedImage = new BufferedImage(pixelArtWidth, pixelArtHeight, BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < pixelArtWidth; x++) {
            xOffset = Math.round(xStepBack * x);
            currentXCoord = x * tileWidth - xOffset;

            for (int y = 0; y < pixelArtHeight; y++) {
                yOffset = Math.round(yStepBack * y);
                currentYCoord = y * tileHeight - yOffset;

                bufferedImage.setRGB(x, y, extractTileValue(currentXCoord, currentYCoord));
            }
        }

        baseImage = bufferedImage;
    }

    private int extractTileValue(int currentXCoord, int currentYCoord) {
        double totalR = 0, totalG = 0, totalB = 0;
        int pixel;
        int counter = 0;

        for (int x = currentXCoord; x < currentXCoord + tileWidth; x++) {
            for (int y = currentYCoord; y < currentYCoord + tileHeight; y++) {
                pixel = baseImage.getRGB(x, y);

                if (((pixel >> 24) & 0xff) > (255 / 2)) {
                    totalR += (pixel >> 16) & 0xff;
                    totalG += (pixel >> 8) & 0xff;
                    totalB += pixel & 0xff;

                    counter++;
                }
            }
        }

        int r = (int) Math.round(totalR / counter);
        int g = (int) Math.round(totalG / counter);
        int b = (int) Math.round(totalB / counter);

        return (255 << 24) | (r << 16) | (g << 8) | b;
    }

    private void convertToLuminosity(int pS) {
        paletteSize = pS;
        luminosity = new int[pixelArtWidth][pixelArtHeight];
        int[] lumValues = firstLuminosityLoop();
        secondLuminosityLoop(lumValues);
    }

    private int[] firstLuminosityLoop() {
        int lumValue;
        int lMin = getLuminosityValue(baseImage.getRGB(0, 0));
        int lMax = getLuminosityValue(baseImage.getRGB(0, 0));

        for (int x = 0; x < pixelArtWidth; x++) {
            for (int y = 0; y < pixelArtHeight; y++) {
                lumValue = getLuminosityValue(baseImage.getRGB(x, y));
                luminosity[x][y] = lumValue;

                if (lMin > lumValue) {
                    lMin = lumValue;
                } else if (lMax < lumValue) {
                    lMax = lumValue;
                }
            }
        }

        return new int[] { lMin, lMax };
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

    private void secondLuminosityLoop(int[] lumValues) {
        int lMin = lumValues[0];
        int lMax = lumValues[1];
        int nbClusters = (2 * paletteSize - 2);
        int shadeSteps = (int) Math.floor(((float) lMax - (float) lMin) / (float) nbClusters);

        for (int x = 0; x < pixelArtWidth; x++) {
            for (int y = 0; y < pixelArtHeight; y++) {
                luminosity[x][y] = Math.clamp((luminosity[x][y] - lMin) / shadeSteps, 0, nbClusters);
            }
        }
    }

    /* GETTERS */

    public BufferedImage getBaseImage() {
        return baseImage;
    }

    public int[][] getLuminosity() {
        return luminosity;
    }

    public int getPaletteSize() {
        return paletteSize;
    }

    public int getPixelArtWidth() {
        return pixelArtWidth;
    }

    public int getPixelArtHeight() {
        return pixelArtHeight;
    }
}
