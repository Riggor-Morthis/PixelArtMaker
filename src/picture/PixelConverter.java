package picture;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public final class PixelConverter {

	/* VARIABLES */
	private static BufferedImage _importedPicture;
	private static float _pictureWidth, _pictureHeight;

	private static int _targetWidth, _targetHeight;

	/* CONSTRUCTORS */

	private PixelConverter() {
		// Just to be a static class
	}

	/* PUBLIC METHODS */

	public static BufferedImage ConvertPicture(String filepath, int targetWidth) {
		importPicture(filepath);
		registerVariables(targetWidth);
		convertPictureToGrayscale();

		return _importedPicture;
	}

	/* PRIVATE METHODS */

	private static void importPicture(String filepath) {
		try {
			File imageFile = new File(filepath);
			_importedPicture = ImageIO.read(imageFile);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static void registerVariables(int targetWidth) {
		_pictureWidth = _importedPicture.getWidth();
		_pictureHeight = _importedPicture.getHeight();

		_targetWidth = targetWidth;
		_targetHeight = (int) Math.rint(_pictureHeight / (_pictureWidth / _targetWidth));
	}

	private static void convertPictureToGrayscale() {
		int pixel;

		for (int x = 0; x < _pictureWidth; x++) {
			for (int y = 0; y < _pictureHeight; y++) {
				pixel = _importedPicture.getRGB(x, y);
				pixel = convertPixelToGrayscale(pixel);
				_importedPicture.setRGB(x, y, pixel);
			}
		}
	}

	private static int convertPixelToGrayscale(int pixel) {
		int r = (pixel >> 16) & 0xff;
		int g = (pixel >> 8) & 0xff;
		int b = pixel & 0xff;

		int luminosity = (int) (.3f * r + .59f * g + .11f * b);

		return (255 << 24) | (luminosity << 16) | (luminosity << 8) | luminosity;
	}

	private static void changePictureResolution() {

	}

}
