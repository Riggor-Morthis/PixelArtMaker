package main;

import java.awt.image.BufferedImage;
import java.io.IOException;
import scripts.DitheringCreator;
import scripts.LuminosityConvertor;
import scripts.PaletteCreatorFromFile;
import scripts.PictureImporter;

public class Main {

	public static void main(String[] args) throws IOException {
/* 		for(int i = 2; i < 64; i++){
			DitheringCreator.create("pictures/ScreenLeft.png", 128, i, "pictures/Dithered" +  i + ".png", 5);
		} */

        BufferedImage joconde = PictureImporter.importPicture("pictures/joconde.png");
        int[][] lum = LuminosityConvertor.convert(joconde, 4);
        PaletteCreatorFromFile.create(joconde, "pictures/palette_nes.png", lum, 4);
	}
}
