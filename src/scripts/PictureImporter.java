package scripts;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class PictureImporter {

	/* CONSTRUCTORS */

	private PictureImporter() {
	}

	/* PUBLIC METHODS */

	public static synchronized BufferedImage importPicture(String filepath) {
		try {
			File imageFile = new File(filepath);
			return ImageIO.read(imageFile);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
}
