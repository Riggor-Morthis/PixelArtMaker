package scripts;

import java.awt.image.BufferedImage;

public class PictureScaler {

	/* CONSTRUCTORS */

	private PictureScaler() {
	}

	/* PUBLIC METHODS */

	public static BufferedImage scale(BufferedImage importedPicture, int scaleFactor) {
		int importedWidth = importedPicture.getWidth();
		int importedHeight = importedPicture.getHeight();
		int pixel;

		BufferedImage exportedPicture = new BufferedImage(importedWidth * scaleFactor, importedHeight * scaleFactor,
				BufferedImage.TYPE_INT_ARGB);

		for (int x = 0; x < importedWidth; x++) {
			for (int y = 0; y < importedHeight; y++) {
				pixel = importedPicture.getRGB(x, y);

				for (int i = 0; i < scaleFactor; i++) {
					for (int j = 0; j < scaleFactor; j++) {
						exportedPicture.setRGB(x * scaleFactor + i, y * scaleFactor + j, pixel);
					}
				}
			}
		}

		return exportedPicture;
	}

}
