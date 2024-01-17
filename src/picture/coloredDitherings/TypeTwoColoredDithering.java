package picture.coloredDitherings;

import java.awt.image.BufferedImage;

public class TypeTwoColoredDithering extends ColoredDithering {

	/* VARIABLES */

	private int _blackPixel, _darkPixel, _lightPixel, _whitePixel;

	/* CONSTRUCTORS */

	public TypeTwoColoredDithering(BufferedImage importedPicture, int exportedWidth) {
		super(importedPicture, exportedWidth);
	}

	/* PRIVATE METHODS */

	protected void getPixelShades() {
		_blackPixel = extractLocationBasedColours(0, 1);
		_darkPixel = extractLocationBasedColours(1, 2);
		_lightPixel = extractLocationBasedColours(2, 3);
		_whitePixel = extractLocationBasedColours(3, 4);
	}

	private int extractLocationBasedColours(int targetOne, int targetTwo) {
		int[] color = new int[4];
		int counter = 0;
		int pixel;

		for (int x = 0; x < _pictureWidth; x++) {
			for (int y = 0; y < _pictureHeight; y++) {
				pixel = _importedPicture.getRGB(x, y);

				if (pixelToLuminosity(pixel) / 51 == targetOne || pixelToLuminosity(pixel) / 51 == targetTwo) {
					color[0] += (pixel >> 24) & 0xff;
					color[1] += (pixel >> 16) & 0xff;
					color[2] += (pixel >> 8) & 0xff;
					color[3] += pixel & 0xff;
					counter++;
				}
			}
		}

		return ((255 << 24) | (color[1] / counter << 16) | (color[2] / counter << 8) | color[3] / counter);
	}

	protected void createPalette() {
		// Not needed for us
	}

	protected void setPixelValue(int x, int y, int avg) {
		switch (avg / 51) {
		case 0:
			_exportedPicture.setRGB(x, y, _blackPixel);
			break;
		case 1:
			if ((x + y) % 2 == 0) {
				_exportedPicture.setRGB(x, y, _blackPixel);
			} else {
				_exportedPicture.setRGB(x, y, _darkPixel);
			}
			break;
		case 2:
			if ((x + y) % 2 == 0) {
				_exportedPicture.setRGB(x, y, _darkPixel);
			} else {
				_exportedPicture.setRGB(x, y, _lightPixel);
			}
			break;
		case 3:
			if ((x + y) % 2 == 0) {
				_exportedPicture.setRGB(x, y, _lightPixel);
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
