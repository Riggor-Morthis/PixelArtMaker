package picture.coloredDitherings;

import java.awt.image.BufferedImage;

public class TypeThreeColoredDithering extends ColoredDithering {

	/* VARIABLES */

	private int[] _whitePixel, _lightPixel, _mediumPixel, _darkPixel, _blackPixel;

	/* CONSTRUCTORS */

	public TypeThreeColoredDithering(BufferedImage importedPicture, int exportedWidth) {
		super(importedPicture, exportedWidth);
	}

	/* PRIVATE METHODS */

	protected void getPixelShades() {
        initializeShades();
        getShadeValues();
	}

    private void initializeShades() {
    	_blackPixel = new int[2];
        _lightPixel = new int[2];
        _mediumPixel = new int[2];
        _darkPixel = new int[2];
        _whitePixel = new int[2];
    }

    private void getShadeValues() {
        _blackPixel[0] = extractLocationBasedColours(0);
        _darkPixel[0] = extractLocationBasedColours(1);
        _mediumPixel[0] = extractLocationBasedColours(2);
        _lightPixel[0] = extractLocationBasedColours(3);
        _whitePixel[0] = extractLocationBasedColours(4);
    }
    
	protected void createPalette() {
		int[] hsv = rgbToHsv(_blackPixel[0]);
		_blackPixel[0] = hsvToRgb(hsv[0], Math.clamp(hsv[1] * 0.8f, 0f, 100f), Math.clamp(hsv[2] * 1.1f, 0f, 100f));
		_blackPixel[1] = hsvToRgb(hsv[0], Math.clamp(hsv[1] * 1.1f, 0f, 100f), Math.clamp(hsv[2] * 0.8f, 0f, 100f));

		hsv = rgbToHsv(_darkPixel[0]);
		_darkPixel[0] = hsvToRgb(hsv[0], Math.clamp(hsv[1] * 0.8f, 0f, 100f), Math.clamp(hsv[2] * 1.1f, 0f, 100f));
		_darkPixel[1] = hsvToRgb(hsv[0], Math.clamp(hsv[1] * 1.1f, 0f, 100f), Math.clamp(hsv[2] * 0.8f, 0f, 100f));

		hsv = rgbToHsv(_mediumPixel[0]);
		_mediumPixel[0] = hsvToRgb(hsv[0], Math.clamp(hsv[1] * 0.8f, 0f, 100f), Math.clamp(hsv[2] * 1.1f, 0f, 100f));
		_mediumPixel[1] = hsvToRgb(hsv[0], Math.clamp(hsv[1] * 1.1f, 0f, 100f), Math.clamp(hsv[2] * 0.8f, 0f, 100f));

		hsv = rgbToHsv(_lightPixel[0]);
		_lightPixel[0] = hsvToRgb(hsv[0], Math.clamp(hsv[1] * 0.8f, 0f, 100f), Math.clamp(hsv[2] * 1.1f, 0f, 100f));
		_lightPixel[1] = hsvToRgb(hsv[0], Math.clamp(hsv[1] * 1.1f, 0f, 100f), Math.clamp(hsv[2] * 0.8f, 0f, 100f));

		hsv = rgbToHsv(_whitePixel[0]);
		_whitePixel[0] = hsvToRgb(hsv[0], Math.clamp(hsv[1] * 0.8f, 0f, 100f), Math.clamp(hsv[2] * 1.1f, 0f, 100f));
		_whitePixel[1] = hsvToRgb(hsv[0], Math.clamp(hsv[1] * 1.1f, 0f, 100f), Math.clamp(hsv[2] * 0.8f, 0f, 100f));
	}

	protected void setPixelValue(int x, int y, int avg) {
		switch (avg / 51) {
		case 0:
			if ((x + y) % 2 == 0) {
				_exportedPicture.setRGB(x, y, _blackPixel[0]);
			} else {
				_exportedPicture.setRGB(x, y, _blackPixel[1]);
			}
			break;
		case 1:
			if ((x + y) % 2 == 0) {
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
			if ((x + y) % 2 == 0) {
				_exportedPicture.setRGB(x, y, _lightPixel[1]);
			} else {
				_exportedPicture.setRGB(x, y, _lightPixel[0]);
			}
			break;
		default:
			if ((x + y) % 2 == 0) {
				_exportedPicture.setRGB(x, y, _whitePixel[0]);
			} else {
				_exportedPicture.setRGB(x, y, _whitePixel[1]);
			}
			break;
		}
	}

}
