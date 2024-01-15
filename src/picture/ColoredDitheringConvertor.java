package picture;

import java.awt.image.BufferedImage;
import java.util.function.BiPredicate;

public final class ColoredDitheringConvertor {

	/* VARIABLES */

	private static BufferedImage _exportedPicture;
	private static BufferedImage _coloredPicture;
	private static BufferedImage _grayscaledPicture;

	private static int _pictureWidth, _pictureHeight;

	private static int _whitePixel, _lightWhitePixel, _lightBlackPixel, _mediumWhitePixel, _mediumBlackPixel,
			_darkWhitePixel, _darkBlackPixel, _blackPixel;

	/* CONSTRUCTORS */

	private ColoredDitheringConvertor() {
		// Just to be a private class
	}

	/* PUBLIC METHODS */

	public static BufferedImage convertPicture(String filepath, int exportedWidth) {
		_coloredPicture = PictureImporter.importPicture(filepath);
		_grayscaledPicture = PictureImporter.importPicture(filepath);
		return executeConversion(exportedWidth);
	}

	public static BufferedImage convertPicture(BufferedImage importedPicture, int exportedWidth) {
		_coloredPicture = importedPicture;
		_grayscaledPicture = importedPicture;
		return executeConversion(exportedWidth);
	}

	/* PRIVATE METHODS */

	private static BufferedImage executeConversion(int exportedWidth) {
		preparePictures(exportedWidth);
		prepareVariables();
		getPixelShades();

		return _exportedPicture;
	}

	private static void preparePictures(int exportedWidth) {
		_coloredPicture = PixelArtConvertor.convertPicture(_coloredPicture, exportedWidth);

		_grayscaledPicture = GrayscaleConvertor.convertPicture(_coloredPicture);
		_grayscaledPicture = PixelArtConvertor.convertPicture(_grayscaledPicture, exportedWidth);
	}

	private static void prepareVariables() {
		_pictureWidth = _grayscaledPicture.getWidth();
		_pictureHeight = _grayscaledPicture.getHeight();

		_exportedPicture = new BufferedImage(_pictureWidth, _pictureHeight, BufferedImage.TYPE_INT_ARGB);
	}

	private static void getPixelShades() {
		int[] twoValues;
		BiPredicate<Integer, Integer> ditheringPattern;

		_blackPixel = extractLocationBasedColours(0);
		System.out.println(_blackPixel);

		ditheringPattern = (x, y) -> (x % 2 == 0 || y % 2 == 0);
		twoValues = extractLocationBasedColours(1, ditheringPattern);
		_lightBlackPixel = twoValues[0];
		_lightWhitePixel = twoValues[1];
		System.out.println(_lightWhitePixel + " " + _lightBlackPixel);

		ditheringPattern = (x, y) -> ((x + y) % 2 == 0);
		twoValues = extractLocationBasedColours(1, ditheringPattern);
		_mediumBlackPixel = twoValues[0];
		_mediumWhitePixel = twoValues[1];
		System.out.println(_mediumWhitePixel + " " + _mediumBlackPixel);

		ditheringPattern = (x, y) -> (x % 2 == 0 && y % 2 == 0);
		twoValues = extractLocationBasedColours(1, ditheringPattern);
		_darkBlackPixel = twoValues[0];
		_darkWhitePixel = twoValues[1];
		System.out.println(_darkWhitePixel + " " + _darkBlackPixel);

		_whitePixel = extractLocationBasedColours(4);
		System.out.println(_whitePixel);
	}

	private static int[] extractLocationBasedColours(int targetRange, BiPredicate<Integer, Integer> ditheringPattern) {
		int[] lightColour = new int[5];
		int[] darkColour = new int[5];
		int pixel;

		for (int x = 0; x < _pictureWidth; x++) {
			for (int y = 0; y < _pictureHeight; y++) {
				pixel = _grayscaledPicture.getRGB(x, y);

				if (pixelToLuminosity(pixel) / 51 == targetRange) {
					if (ditheringPattern.test(x, y)) {
						darkColour[0] += (pixel >> 24) & 0xff;
						darkColour[1] += (pixel >> 16) & 0xff;
						darkColour[2] += (pixel >> 8) & 0xff;
						darkColour[3] += pixel & 0xff;
						darkColour[4]++;
					} else {
						lightColour[0] += (pixel >> 24) & 0xff;
						lightColour[1] += (pixel >> 16) & 0xff;
						lightColour[2] += (pixel >> 8) & 0xff;
						lightColour[3] += pixel & 0xff;
						lightColour[4]++;
					}
				}
			}
		}
		
		for (int i = 0; i < 4; i++) {
			lightColour[i] /= lightColour[4];
			darkColour[i] /= darkColour[4];
		}

		int lightPixel = (255 << 24) | (lightColour[1] << 16) | (lightColour[2] << 8) | lightColour[3];
		int darkPixel = (255 << 24) | (darkColour[1] << 16) | (darkColour[2] << 8) | darkColour[3];

		return new int[] { darkPixel, lightPixel };
	}

	private static int extractLocationBasedColours(int targetRange) {
		int[] lightColour = new int[5];
		int pixel;

		for (int x = 0; x < _pictureWidth; x++) {
			for (int y = 0; y < _pictureHeight; y++) {
				pixel = _coloredPicture.getRGB(x, y);

				if (pixelToLuminosity(pixel) / 51 == targetRange) {
					lightColour[0] += (pixel >> 24) & 0xff;
					lightColour[1] += (pixel >> 16) & 0xff;
					lightColour[2] += (pixel >> 8) & 0xff;
					lightColour[3] += pixel & 0xff;
					lightColour[4]++;
				}
			}
		}

		for (int i = 0; i < 4; i++) {
			lightColour[i] /= lightColour[4];
		}

		int lightPixel = (255 << 24) | (lightColour[1] << 16) | (lightColour[2] << 8) | lightColour[3];

		return lightPixel;
	}

	private static int pixelToLuminosity(int pixel) {
		int a = (pixel >> 24) & 0xff;
		int r = (pixel >> 16) & 0xff;
		int g = (pixel >> 8) & 0xff;
		int b = pixel & 0xff;

		return (int) (.3f * r + .59f * g + .11f * b);
	}

	private static void setPixelValue(int x, int y, int avg) {
		switch (avg / 51) {
		case 0:
			_exportedPicture.setRGB(x, y, _blackPixel);
			break;
		case 1:
			if (x % 2 == 0 || y % 2 == 0) {
				_exportedPicture.setRGB(x, y, _darkBlackPixel);
			} else {
				_exportedPicture.setRGB(x, y, _darkWhitePixel);
			}
			break;
		case 2:
			if ((x + y) % 2 == 0) {
				_exportedPicture.setRGB(x, y, _mediumBlackPixel);
			} else {
				_exportedPicture.setRGB(x, y, _mediumWhitePixel);
			}
			break;
		case 3:
			if (x % 2 == 0 && y % 2 == 0) {
				_exportedPicture.setRGB(x, y, _lightBlackPixel);
			} else {
				_exportedPicture.setRGB(x, y, _lightWhitePixel);
			}
			break;
		default:
			_exportedPicture.setRGB(x, y, _whitePixel);
			break;
		}
	}
}
