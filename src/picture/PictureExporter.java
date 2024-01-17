package picture;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PictureExporter {

	/* CONSTRUCTORS */
    
    private PictureExporter() {
    	//Just to be a static class
    }
    
    /* PUBLIC METHODS */
    
    public static void exportPicture(BufferedImage importedPicture, String path) throws IOException {
    	writeFile(importedPicture, path);
    }
    
    public static void exportPicture(BufferedImage importedPicture, String path, int scaleFactor) throws IOException {
    	BufferedImage exportedPicture = enlargePicture(importedPicture, scaleFactor);
    	writeFile(exportedPicture, path);
    }
    
    /* PRIVATE METHODS */
	
    private static void writeFile(BufferedImage importedPicture, String path) throws IOException {
    	File outputFile = new File(path);
		ImageIO.write(importedPicture, "png", outputFile);
    }
    
    private static BufferedImage enlargePicture(BufferedImage importedPicture, int scaleFactor) {
    	int importedWidth = importedPicture.getWidth();
    	int importedHeight = importedPicture.getHeight();
    	int pixel;
    	
    	BufferedImage exportedPicture = new BufferedImage(importedWidth * scaleFactor, importedHeight * scaleFactor, BufferedImage.TYPE_INT_ARGB);
    	
    	for(int x = 0; x < importedWidth; x++) {
    		for(int y = 0; y < importedHeight; y++) {
    			pixel = importedPicture.getRGB(x, y);
    			
    			for(int i = 0; i < scaleFactor; i++) {
    				for(int j = 0; j < scaleFactor; j++) {
    					exportedPicture.setRGB(x * scaleFactor + i, y * scaleFactor + j, pixel);
    				}
    			}
    		}
    	}
    	
    	return exportedPicture;
    }
}
