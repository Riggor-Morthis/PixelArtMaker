package picture;

import java.awt.image.BufferedImage;

import picture.coloredDitherings.ColoredDithering;
import picture.coloredDitherings.TypeOneColoredDithering;

public class ColoredDitheringConvertor {

	/* VARIABLEs */

	private static ColoredDithering coloredDithering;

	/* CONSTRUCTORS */

	private ColoredDitheringConvertor() {
		// Just to be a private class
	}

	/* PUBLIC METHODS */

	public static BufferedImage convertPicture(String filepath, int exportedWidth, int type) {
		return executeConversion(PictureImporter.importPicture(filepath), exportedWidth, type);
	}

	public static BufferedImage convertPicture(BufferedImage importedPicture, int exportedWidth, int type) {
		return executeConversion(importedPicture, exportedWidth, type);
	}

	/* PRIVATE METHODS */
	
	private static BufferedImage executeConversion(BufferedImage importedPicture, int exportedWidth, int type) {
		ColoredDithering coloredDithering;

		switch (type) {
			default:
			coloredDithering = new TypeOneColoredDithering(importedPicture, exportedWidth);
				break;
		}

		return coloredDithering.getExportedPicture();
	}
}
