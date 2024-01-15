package picture;

import java.awt.image.BufferedImage;

public final class DitheringConvertor {

	/* VARIABLES */

	private static BufferedImage _importedPicture;
	private static BufferedImage _exportedPicture;

	private static int _pictureWidth, _pictureHeight;
	private static int _whitePixel, _blackPixel;

	/* CONSTRUCTORS */

	private DitheringConvertor() {
		// static class
	}

	/* PUBLIC METHODS */

	public static BufferedImage convertPicture(String filepath) {
		_importedPicture = PictureImporter.importPicture(filepath);
		return executeConversion();
	}

	public static BufferedImage convertPicture(BufferedImage importedPicture) {
		_importedPicture = importedPicture;
		return executeConversion();
	}

	/* PRIVATE METHODS */

	private static BufferedImage executeConversion() {
		prepareVariables();
		mainLoop();

		return _exportedPicture;
	}

	private static void prepareVariables() {
		_pictureWidth = _importedPicture.getWidth();
		_pictureHeight = _importedPicture.getHeight();

		_exportedPicture = new BufferedImage(_pictureWidth, _pictureHeight, BufferedImage.TYPE_INT_ARGB);

		_whitePixel = (255 << 24) | (255 << 16) | (255 << 8) | 255;
		_blackPixel = (255 << 24) | (0 << 16) | (0 << 8) | 0;
	}

	private static void mainLoop() {
		int pixel;
		int avg;
		int useless;

		for (int x = 0; x < _pictureWidth; x++) {
			for (int y = 0; y < _pictureHeight; y++) {
				pixel = _importedPicture.getRGB(x, y);
				useless = ((pixel >> 24) & 0xff);
				avg = ((pixel >> 16) & 0xff) + ((pixel >> 8) & 0xff) + (pixel & 0xff);
				avg /= 3;

				setPixelValue(x, y, avg);
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
				_exportedPicture.setRGB(x, y, _blackPixel);
			} else {
				_exportedPicture.setRGB(x, y, _whitePixel);
			}
			break;
		case 2:
			if ((x + y) % 2 == 0) {
				_exportedPicture.setRGB(x, y, _blackPixel);
			} else {
				_exportedPicture.setRGB(x, y, _whitePixel);
			}
			break;
		case 3:
			if (x % 2 == 0 && y % 2 == 0) {
				_exportedPicture.setRGB(x, y, _blackPixel);
			} else {
				_exportedPicture.setRGB(x, y, _whitePixel);
			}
			break;
		default:
			_exportedPicture.setRGB(x, y, _whitePixel);
			break;
		}
	}
}
