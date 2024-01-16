package picture;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class PictureImporter {

	private PictureImporter() {
		// Just to be a static class
	}

	/**
	 * Imports and store the user's original picture
	 * 
	 * @param filepath path to the original picture
	 */
	public static BufferedImage importPicture(String filepath) {
		try {
			File imageFile = new File(filepath);
			return ImageIO.read(imageFile);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
}
