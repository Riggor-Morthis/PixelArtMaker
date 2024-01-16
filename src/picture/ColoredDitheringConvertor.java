package picture;

import java.awt.image.BufferedImage;
import java.util.function.BiPredicate;

public final class ColoredDitheringConvertor {

	/* VARIABLES */

	private static BufferedImage _exportedPicture;
	private static BufferedImage _importedPicture;

	private static int _pictureWidth, _pictureHeight;

	private static int[] _lightPixel, _mediumPixel, _darkPixel;
	private static int _whitePixel, _blackPixel;

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
		createPalette();
		mainLoop();

		return _exportedPicture;
	}

	private static void prepareVariables(int exportedWidth) {
		_importedPicture = PixelArtConvertor.convertPicture(_importedPicture, exportedWidth);

		_pictureWidth = _importedPicture.getWidth();
		_pictureHeight = _importedPicture.getHeight();

		_exportedPicture = new BufferedImage(_pictureWidth, _pictureHeight, BufferedImage.TYPE_INT_ARGB);

		_lightPixel = new int[2];
		_mediumPixel = new int[2];
		_darkPixel = new int[2];
	}

	private static void getPixelShades() {
		_blackPixel = extractLocationBasedColours(0);
		_darkPixel[0] = extractLocationBasedColours(1);
		_mediumPixel[0] = extractLocationBasedColours(2);
		_lightPixel[0] = extractLocationBasedColours(3);
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
		int[] rgb = getPixelValues(pixel);

		return (int) (.3f * rgb[0] + .59f * rgb[1] + .11f * rgb[2]);
	}

	private static int[] getPixelValues(int pixel) {
		int a = (pixel >> 24) & 0xff;
		int r = (pixel >> 16) & 0xff;
		int g = (pixel >> 8) & 0xff;
		int b = pixel & 0xff;

		return new int[] { r, g, b };
	}

	private static int getPixelValue(int[] rgb) {
		return (255 << 24) | (rgb[0] << 16) | (rgb[1] << 8) | rgb[2];
	}

	private static void createPalette() {
		int[] hsv = rgbToHsv(_darkPixel[0]);
		_darkPixel[0] = hsvToRgb(hsv[0], Math.clamp(hsv[1] * 0.8f, 0f, 255f), Math.clamp(hsv[2] * 1.1f, 0f, 255f));
		_darkPixel[1] = hsvToRgb(hsv[0], Math.clamp(hsv[1] * 1.1f, 0f, 255f), Math.clamp(hsv[2] * 0.8f, 0f, 255f));

		hsv = rgbToHsv(_mediumPixel[0]);
		_mediumPixel[0] = hsvToRgb(hsv[0], Math.clamp(hsv[1] * 0.8f, 0f, 255f), Math.clamp(hsv[2] * 1.1f, 0f, 255f));
		_mediumPixel[1] = hsvToRgb(hsv[0], Math.clamp(hsv[1] * 1.1f, 0f, 255f), Math.clamp(hsv[2] * 0.8f, 0f, 255f));

		hsv = rgbToHsv(_lightPixel[0]);
		_lightPixel[0] = hsvToRgb(hsv[0], Math.clamp(hsv[1] * 0.8f, 0f, 255f), Math.clamp(hsv[2] * 1.1f, 0f, 255f));
		_lightPixel[1] = hsvToRgb(hsv[0], Math.clamp(hsv[1] * 1.1f, 0f, 255f), Math.clamp(hsv[2] * 0.8f, 0f, 255f));
	}

	private static int[] rgbToHsv(int[] rgb) {
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

	private static int[] rgbToHsv(int value) {
		return rgbToHsv(getPixelValues(value));
	}

	private static int[] hsvToRgb(int[] hsv) {
		float h = (float) hsv[0];
		float s = ((float) hsv[1]) / 100f;
		float v = ((float) hsv[2]) / 100f;

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

	private static int hsvToRgb(float h, float s, float v) {
		return getPixelValue(hsvToRgb(new int[] { Math.round(h), Math.round(s), Math.round(v) }));
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
			if (x % 2 == 0 || y % 2 == 0) {
				_exportedPicture.setRGB(x, y, _darkPixel[1]);
			} else {
				_exportedPicture.setRGB(x, y, _darkPixel[0]);
			}
			break;
		case 2:
			if ((x + y) % 2 == 0) {
				_exportedPicture.setRGB(x, y, _mediumPixel[1]);
			} else {
				_exportedPicture.setRGB(x, y, _mediumPixel[0]);
			}
			break;
		case 3:
			if (x % 2 == 0 && y % 2 == 0) {
				_exportedPicture.setRGB(x, y, _lightPixel[1]);
			} else {
				_exportedPicture.setRGB(x, y, _lightPixel[0]);
			}
			break;
		default:
			_exportedPicture.setRGB(x, y, _whitePixel);
			break;
		}
	}
}
