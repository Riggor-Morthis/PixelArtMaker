package picture;

import java.awt.image.BufferedImage;
import java.util.function.BiPredicate;

public final class ColoredDitheringConvertor {

	/* VARIABLES */

	private static BufferedImage _exportedPicture;
	private static BufferedImage _importedPicture;

	private static int _pictureWidth, _pictureHeight;

	private static int _whitePixel, _lightPixel, _mediumPixel, _darkPixel, _blackPixel;

	/* CONSTRUCTORS */

	private ColoredDitheringConvertor() {
		// Just to be a private class
	}

	/* PUBLIC METHODS */

	public static BufferedImage convertPicture(String filepath, int exportedWidth) {
		_importedPicture = PictureImporter.importPicture(filepath);
		return executeConversion(exportedWidth);
	}

	public static BufferedImage convertPicture(BufferedImage importedPicture, int exportedWidth) {
		_importedPicture = importedPicture;
		return executeConversion(exportedWidth);
	}

	/* PRIVATE METHODS */

	private static BufferedImage executeConversion(int exportedWidth) {
		prepareVariables(exportedWidth);
		getPixelShades();
		mainLoop();

		return _exportedPicture;
	}

	private static void prepareVariables(int exportedWidth) {
		_importedPicture = PixelArtConvertor.convertPicture(_importedPicture, exportedWidth);

		_pictureWidth = _importedPicture.getWidth();
		_pictureHeight = _importedPicture.getHeight();

		_exportedPicture = new BufferedImage(_pictureWidth, _pictureHeight, BufferedImage.TYPE_INT_ARGB);
	}

	private static void getPixelShades() {
		_blackPixel = extractLocationBasedColours(0);
		_lightPixel = extractLocationBasedColours(1);
		_mediumPixel = extractLocationBasedColours(2);
		_darkPixel = extractLocationBasedColours(3);
		_whitePixel = extractLocationBasedColours(4);
	}

	private static int extractLocationBasedColours(int targetRange) {
		int[] color = new int[4];
		int counter = 0;
		int pixel, grayPixel;

		for (int x = 0; x < _pictureWidth; x++) {
			for (int y = 0; y < _pictureHeight; y++) {
				pixel = _importedPicture.getRGB(x, y);
				grayPixel = pixelToLuminosity(pixel);

				if (grayPixel / 51 == targetRange) {
					color[0] += (pixel >> 24) & 0xff;
					color[1] += (pixel >> 16) & 0xff;
					color[2] += (pixel >> 8) & 0xff;
					color[3] += pixel & 0xff;

					counter++;
				}
			}
		}

		for (int i = 0; i < 4; i++) {
			color[i] /= counter;
		}

		return ((255 << 24) | (color[1] << 16) | (color[2] << 8) | color[3]);
	}

	private static int pixelToLuminosity(int pixel) {
		int a = (pixel >> 24) & 0xff;
		int r = (pixel >> 16) & 0xff;
		int g = (pixel >> 8) & 0xff;
		int b = pixel & 0xff;

		return (int) (.3f * r + .59f * g + .11f * b);
	}

	private static int[] rgbToHsv(int[] rgb) {
		float r = (float) rgb[0] / 255f;
		float g = (float) rgb[1] / 255f;
		float b = (float) rgb[3] / 255f;

		float M = Math.max(r, Math.max(g, b));
		float m = Math.min(r, Math.min(g, b));
		float c = M - m;
		int s = (int) ((c / M) * 100f);

		float R = (M - r) / c;
		float G = (M - g) / c;
		float B = (M - b) / c;

		float H = 0;
		if (M == m) {
			H = 0;
		} else if (M == r) {
			H = 0f + B - G;
		} else if (M == g) {
			H = 2f + R - B;
		} else if (M == b) {
			H = 4f + G - R;
		}
		
		int h = (int) (((H / 6f) % 1 ) * 360f);
		int v = (int) (M * 100f);
		
		return new int[] {h, s, v};
	}

	private static void mainLoop() {
		int pixel;

		for (int x = 0; x < _pictureWidth; x++) {
			for (int y = 0; y < _pictureHeight; y++) {
				pixel = _importedPicture.getRGB(x, y);
				pixel = pixelToLuminosity(pixel);
				setPixelValue(x, y, pixel);
			}
		}
	}

	private static void setPixelValue(int x, int y, int avg) {
		switch (avg / 51) {
		case 0:
			_exportedPicture.setRGB(x, y, _blackPixel);
			break;
		case 1:
			_exportedPicture.setRGB(x, y, _lightPixel);
			break;
		case 2:
			_exportedPicture.setRGB(x, y, _mediumPixel);
			break;
		case 3:
			_exportedPicture.setRGB(x, y, _darkPixel);
			break;
		default:
			_exportedPicture.setRGB(x, y, _whitePixel);
			break;
		}
	}
}
