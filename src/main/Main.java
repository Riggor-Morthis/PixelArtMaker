package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import picture.ColoredDitheringConvertor;
import picture.DitheringConvertor;
import picture.GrayscaleConvertor;
import picture.PictureExporter;
import picture.PixelArtConvertor;

public class Main {

	public static void main(String[] args) throws IOException {

		BufferedImage colouredDithering;
		String[] names = {"Clouds", "Factory", "Lakeside", "Sky"};
		String path;
		
		for(int i = 0; i < 4; i ++) {
			path = "pictures/" + names[i] + ".png";
			
			for(int j = 1; j < 4; j++){
				colouredDithering = ColoredDitheringConvertor.convertPicture(path, 256, j);
				path = "pictures/" + names[i] + j + ".png";
				PictureExporter.exportPicture(colouredDithering, path, 5);
			}
		}
	}
}
