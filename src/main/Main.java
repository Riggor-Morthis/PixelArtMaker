package main;

import java.awt.image.BufferedImage;
import java.io.IOException;

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
		
		colouredDithering = PaletteDitheringConvertor.convert(joconde, 256, 2);
		PictureExporter.exportPicture(colouredDithering, "pictures/jocondeTwo.png", 5);

		colouredDithering = PaletteDitheringConvertor.convert(joconde, 256, 4);
		PictureExporter.exportPicture(colouredDithering, "pictures/jocondeFour.png", 5);

		colouredDithering = PaletteDitheringConvertor.convert(joconde, 256, 8);
		PictureExporter.exportPicture(colouredDithering, "pictures/jocondeHeight.png", 5);

		colouredDithering = PaletteDitheringConvertor.convert(joconde, 256, 16);
		PictureExporter.exportPicture(colouredDithering, "pictures/jocondeSixteen.png", 5);
	}
}
