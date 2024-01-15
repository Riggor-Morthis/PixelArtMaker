package main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import picture.DitheringConvertor;
import picture.GrayscaleConvertor;
import picture.PixelArtConvertor;

public class Main {

	public static void main(String[] args) throws IOException {

		BufferedImage GrayscaleCamera = GrayscaleConvertor.convertPicture("pictures/CameraPicture.png");
		File outputFile = new File("pictures/GrayscaleCamera.png");
		ImageIO.write(GrayscaleCamera, "png", outputFile);
		
		BufferedImage PixelArtCamera = PixelArtConvertor.convertPicture(GrayscaleCamera, 512);
		outputFile = new File("pictures/PixelArtCamera.png");
		ImageIO.write(PixelArtCamera, "png", outputFile);
		
		BufferedImage DitheredCamera = DitheringConvertor.convertPicture(PixelArtCamera);
		outputFile = new File("pictures/DitheredCamera.png");
		ImageIO.write(DitheredCamera, "png", outputFile);
	}
}
