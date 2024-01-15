package picture;

import java.awt.image.BufferedImage;

/**
 * This class allows our color pictures to be converted to grayscale
 */
public final class GrayscaleConvertor {

	/* VARIABLES */

	private static BufferedImage _importedPicture;
	private static BufferedImage _exportedPicture;

	private static int _pictureWidth, _pictureHeight;

	/* CONSTRUCTORS */

	private GrayscaleConvertor() {
		// Just to be a static class
	}

	/* PUBLIC METHODS */

	/**
	 * Converts a picture from RGB colours to grayscale colours
	 * 
	 * @param filepath path to the original picture
	 * @return a picture where each pixel has been converted to its grayscale value
	 */
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
		registerVariables();
		createExportedPicture();
		convertPictureToGrayscale();
		
		return _exportedPicture;
	}
	
	/**
	 * Stores the variables extracted from the original picture
	 */
	private static void registerVariables() {
		_pictureWidth = _importedPicture.getWidth();
		_pictureHeight = _importedPicture.getHeight();
	}

	/**
	 * Creates the returned picture with the correct attributes
	 */
	private static void createExportedPicture() {
		_exportedPicture = new BufferedImage(_pictureWidth, _pictureHeight, BufferedImage.TYPE_INT_ARGB);
	}

	/**
	 * Set the value of each exported pixels according to the corresponding imported
	 * pixel
	 */
	private static void convertPictureToGrayscale() {
		int pixel;

		for (int x = 0; x < _pictureWidth; x++) {
			for (int y = 0; y < _pictureHeight; y++) {
				pixel = _importedPicture.getRGB(x, y);
				pixel = convertPixelToGrayscale(pixel);
				_exportedPicture.setRGB(x, y, pixel);
			}
		}
	}

	/**
	 * Mathematical equation to go from RGB to grayscale
	 * 
	 * @param pixel the INT_ARGB value of the coloured pixel
	 * @return the INT_ARGB of the grayscaled pixel
	 */
	private static int convertPixelToGrayscale(int pixel) {
		int a = (pixel >> 24) & 0xff;
		int r = (pixel >> 16) & 0xff;
		int g = (pixel >> 8) & 0xff;
		int b = pixel & 0xff;

		int luminosity = (int) (.3f * r + .59f * g + .11f * b);

		return (255 << 24) | (luminosity << 16) | (luminosity << 8) | luminosity;
	}
}
