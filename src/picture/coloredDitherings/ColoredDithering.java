package picture.coloredDitherings;

import java.awt.image.BufferedImage;

import picture.PixelArtConvertor;

public abstract class ColoredDithering {

    /* VARIABLES */

    protected BufferedImage _exportedPicture;
    protected BufferedImage _importedPicture;

    protected int _pictureWidth, _pictureHeight;

    /* CONSTRUCTORS */

    public ColoredDithering(BufferedImage importedPicture, int exportedWidth) {
        _importedPicture = importedPicture;

        prepareVariables(exportedWidth);
        getPixelShades();
        createPalette();
        mainLoop();
    }

    /* PUBLIC METHODS */

    public BufferedImage getExportedPicture(){
        return _exportedPicture;
    }

    /* PRIVATE METHODS */

    protected void prepareVariables(int exportedWidth) {
        _importedPicture = PixelArtConvertor.convertPicture(_importedPicture, exportedWidth);

        _pictureHeight = _importedPicture.getHeight();
        _pictureWidth = _importedPicture.getWidth();

        _exportedPicture = new BufferedImage(_pictureWidth, _pictureHeight, BufferedImage.TYPE_INT_ARGB);
    }

    protected abstract void getPixelShades();

    protected int extractLocationBasedColours(int targetRange) {
        int[] color = new int[4];
        int counter = 0;
        int pixel;

        for (int x = 0; x < _pictureWidth; x++) {
            for (int y = 0; y < _pictureHeight; y++) {
                pixel = _importedPicture.getRGB(x, y);

                if (pixelToLuminosity(pixel) / 51 == targetRange) {
                    color[0] += (pixel >> 24) & 0xff;
                    color[1] += (pixel >> 16) & 0xff;
                    color[2] += (pixel >> 8) & 0xff;
                    color[3] += pixel & 0xff;
                    counter++;
                }
            }
        }

        if(counter > 0) {
        	return ((255 << 24) | (color[1] / counter << 16) | (color[2] / counter << 8) | color[3] / counter);
        }
        else {
        	return ((255 << 24) | (0 << 16) | (0 << 8) | 0);
        }
    }

    protected int pixelToLuminosity(int pixel) {
        int[] rgb = getPixelValues(pixel);

        return (int) (.3f * rgb[0] + .59f * rgb[1] + .11f * rgb[2]);
    }

    protected int[] getPixelValues(int pixel) {
        int a = (pixel >> 24) & 0xff;
        int r = (pixel >> 16) & 0xff;
        int g = (pixel >> 8) & 0xff;
        int b = pixel & 0xff;

        return new int[] { r, g, b };
    }

    private static int getPixelValue(int[] rgb) {
        return (255 << 24) | (rgb[0] << 16) | (rgb[1] << 8) | rgb[2];
    }

    protected abstract void createPalette();

    protected int[] rgbToHsv(int[] rgb) {
        float r = ((float) rgb[0]) / 255f;
        float g = ((float) rgb[1]) / 255f;
        float b = ((float) rgb[2]) / 255f;

        float M = Math.max(r, Math.max(g, b));
        float m = Math.min(r, Math.min(g, b));
        float d = M - m;

        int h = 0;
        if (M == r) {
            h = Math.round(60f * (((g - b) / d) % 6));
        } else if (M == g) {
            h = Math.round(60f * ((b - r) / d + 2f));
        } else if (M == b) {
            h = Math.round(60f * ((r - g) / d + 4f));
        }

        int s = 0;
        if (M != 0) {
            s = Math.round((d / M) * 100f);
        }

        int v = Math.round(M * 100f);

        return new int[] { h, s, v };
    }

    protected int[] rgbToHsv(int value) {
        return rgbToHsv(getPixelValues(value));
    }

    protected int[] hsvToRgb(int[] hsv) {
        float h = (float) hsv[0];
        float s = ((float) hsv[1]) / 100f;
        float v = ((float) hsv[2]) / 100f;

        if(h >= 1) {
        	h -= 1;
        }
        
        float C = s * v;
        float H = h / 60f;
        float X = C * (1 - Math.abs(H % 2f - 1));

        float[] RGB = new float[3];

        if (H < 1) {
            RGB = new float[] { C, X, 0 };
        } else if (H < 2) {
            RGB = new float[] { X, C, 0 };
        } else if (H < 3) {
            RGB = new float[] { 0, C, X };
        } else if (H < 4) {
            RGB = new float[] { 0, X, C };
        } else if (H < 5) {
            RGB = new float[] { X, 0, C };
        } else if (H < 6) {
            RGB = new float[] { C, 0, X };
        }

        float m = v - C;
        int r = Math.round((RGB[0] + m) * 255f);
        int g = Math.round((RGB[1] + m) * 255f);
        int b = Math.round((RGB[2] + m) * 255f);

        return new int[] { r, g, b };
    }

    protected int hsvToRgb(float h, float s, float v) {
        return getPixelValue(hsvToRgb(new int[] { Math.round(h), Math.round(s), Math.round(v) }));
    }

    protected void mainLoop() {
        int pixel;

        for (int x = 0; x < _pictureWidth; x++) {
            for (int y = 0; y < _pictureHeight; y++) {
                pixel = _importedPicture.getRGB(x, y);
                pixel = pixelToLuminosity(pixel);
                setPixelValue(x, y, pixel);
            }
        }
    }

    protected abstract void setPixelValue(int x, int y, int avg);
}
