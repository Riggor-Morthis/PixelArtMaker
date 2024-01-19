package main;

import java.awt.image.BufferedImage;
import java.io.IOException;

import picture.AlternativePaletteDithering;
import picture.ColoredDitheringConvertor;
import picture.DitheringConvertor;
import picture.GrayscaleConvertor;
import picture.PaletteDitheringConvertor;
import picture.PictureExporter;
import picture.PictureImporter;
import picture.PixelArtConvertor;

public class Main {

	public static void main(String[] args) throws IOException {
		BufferedImage colouredDithering;
		BufferedImage joconde = PictureImporter.importPicture("pictures/joconde.png");

		colouredDithering = PaletteDitheringConvertor.convert(joconde, 256, 4);
		PictureExporter.exportPicture(colouredDithering, "pictures/jocondePalette.png", 5);
		
		colouredDithering = AlternativePaletteDithering.convert(joconde, 256, 4);
		PictureExporter.exportPicture(colouredDithering, "pictures/jocondeAlternative.png", 5);
	}
}
