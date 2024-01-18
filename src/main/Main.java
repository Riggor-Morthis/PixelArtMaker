package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import picture.ColoredDitheringConvertor;
import picture.DitheringConvertor;
import picture.GrayscaleConvertor;
import picture.PictureExporter;
import picture.PictureImporter;
import picture.PixelArtConvertor;

public class Main {

	public static void main(String[] args) throws IOException {
		BufferedImage joconde = PictureImporter.importPicture("pictures/joconde.png");
		
		BufferedImage colouredDithering = ColoredDitheringConvertor.convertPicture(joconde, 256, 2);
		PictureExporter.exportPicture(colouredDithering, "pictures/jocondeType2.png", 5);
		
		colouredDithering = ColoredDitheringConvertor.convertPicture(joconde, 256, 4);
		PictureExporter.exportPicture(colouredDithering, "pictures/jocondeType4.png", 5);
		
	}
}
