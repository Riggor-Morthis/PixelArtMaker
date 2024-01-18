package picture;

import java.awt.image.BufferedImage;

public class PaletteDitheringConvertor {

	/* VARIABLES */

	private static BufferedImage _importedPicture;
	private static BufferedImage _exportedPicture;

	private static int _pictureWidth, _pictureHeight;

	private static int[] _colorPalette;
	private static int _paletteSize;
	private static int _shadeSteps;

	/* CONSTRUCTORS */

	private PaletteDitheringConvertor() {
		// Just for a static class
	}

	/* PUBLIC METHODS */

	public static BufferedImage convert(BufferedImage importedPicture, int exportedWidth, int paletteSize) {
		_importedPicture = importedPicture;
		return executeConversion(exportedWidth, paletteSize);
	}

	public static BufferedImage convert(String path, int exportedWidth, int paletteSize) {
		_importedPicture = PictureImporter.importPicture(path);
		return executeConversion(exportedWidth, paletteSize);
	}

	/* PRIVATE METHODS */

	private static BufferedImage executeConversion(int exportedWidth, int paletteSize) {
		prepareVariables(exportedWidth, paletteSize);
		getPixelShades();
		colorsThePicture();

		return _exportedPicture;
	}

	private static void prepareVariables(int exportedWidth, int paletteSize) {
		_paletteSize = Math.clamp(paletteSize, 2, 254);
		_colorPalette = new int[_paletteSize];
		_shadeSteps = Math.round(255f / (float)(_paletteSize + 1));

		_importedPicture = PixelArtConvertor.convertPicture(_importedPicture, exportedWidth);
		_pictureHeight = _importedPicture.getHeight();
		_pictureWidth = _importedPicture.getWidth();

		_exportedPicture = new BufferedImage(_pictureWidth, _pictureHeight, BufferedImage.TYPE_INT_ARGB);
	}

	private static void getPixelShades() {
		for (int i = 0; i < _paletteSize; i++) {
			_colorPalette[i] = extractLocationBasedColours(i, i + 1);
		}
	}

	private static int extractLocationBasedColours(int targetOne, int targetTwo) {
		int[] color = new int[4];
		int counter = 0;
		int pixel;

		for (int x = 0; x < _pictureWidth; x++) {
			for (int y = 0; y < _pictureHeight; y++) {
				pixel = _importedPicture.getRGB(x, y);

				if (pixelToLuminosity(pixel) / _shadeSteps == targetOne
						|| pixelToLuminosity(pixel) / _shadeSteps == targetTwo) {
					color[0] += (pixel >> 24) & 0xff;
					color[1] += (pixel >> 16) & 0xff;
					color[2] += (pixel >> 8) & 0xff;
					color[3] += pixel & 0xff;
					counter++;
				}
			}
		}

		if (counter > 0) {
			return ((255 << 24) | (color[1] / counter << 16) | (color[2] / counter << 8) | color[3] / counter);
		} else {
			return ((255 << 24) | (0 << 16) | (0 << 8) | 0);
		}
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

	private static void colorsThePicture() {
		for (int x = 0; x < _pictureWidth; x++) {
			for (int y = 0; y < _pictureHeight; y++) {
				findAppropriateColor(x, y);
			}
		}
	}

	private static void findAppropriateColor(int x, int y) {
		int pixel = pixelToLuminosity(_importedPicture.getRGB(x, y));
		int value = pixel / _shadeSteps;

		int i = 0;
		while (i != value && i < _paletteSize) {
			i++;
		}
		setPixelValue(x, y, i);
	}

	private static void setPixelValue(int x, int y, int index) {
		if (index == 0) {
			_exportedPicture.setRGB(x, y, _colorPalette[0]);
		} else if (index == _paletteSize) {
			_exportedPicture.setRGB(x, y, _colorPalette[_paletteSize - 1]);
		} else {
			if ((x + y) % 2 == 0) {
				_exportedPicture.setRGB(x, y, _colorPalette[index - 1]);
			} else {
				_exportedPicture.setRGB(x, y, _colorPalette[index]);
			}
		}
	}
}
