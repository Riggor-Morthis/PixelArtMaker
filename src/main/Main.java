package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import picture.PixelConverter;

public class Main {

	static Random rand;
	
	public static void main(String[] args) throws IOException {
		BufferedImage test = PixelConverter.ConvertPicture("pictures/test.png", 256);
		
		File outputFile = new File("pictures/testGreyscale.png");
		ImageIO.write(test, "png", outputFile);
	}
}
