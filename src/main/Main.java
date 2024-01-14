package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import picture.GrayscaleConvertor;
import picture.PixelArtConvertor;

public class Main {

	static Random rand;

	public static void main(String[] args) throws IOException {
		
		BufferedImage test = GrayscaleConvertor.ConvertPicture("pictures/test.png");
		BufferedImage pixelTest = PixelArtConvertor.ConvertPicture(test, 256);

		File outputFile = new File("pictures/testGreyscale.png");
		ImageIO.write(pixelTest, "png", outputFile);
		

		//rand = new Random();
		//bigMaff();
	}

	static void bigMaff() {
		int importSize = rand.nextInt(2250) + 250;
		int exportSize = rand.nextInt(240) + 10;
		System.out.println(String.format("On va de %d a %d", importSize, exportSize));

		// ------------------------------------ //
		System.out.println("// ------------------------------------ //");
		// ------------------------------------ //

		int tileSize = (importSize / exportSize) + 1;
		
		int sparePixels = (exportSize * tileSize) - importSize;

		System.out.println(String.format("On aura %d tuiles de %d pixels", exportSize, tileSize));
		System.out.println(String.format("On aura %d pixels en trop", sparePixels));

		// ------------------------------------ //
		System.out.println("// ------------------------------------ //");
		// ------------------------------------ //

		double stepBack = (double) sparePixels / (double) (exportSize - 1);
		System.out.println(String.format("A chaque nouvelle tuile on recule de %f", stepBack));

		// ------------------------------------ //
		System.out.println("// ------------------------------------ //");
		// ------------------------------------ //

		int offset = 0;
		int r = 0;

		for (int i = 0; i < exportSize; i++) {
			offset = (int) (Math.round(stepBack * i));
			r = (i * tileSize) - offset;

			System.out.println(String.format("On travaille sur l'intervalle [[%d ; %d]]", r, (r + tileSize - 1)));
		}
	}
}
