package picture;

import java.awt.image.BufferedImage;

public class PaletteDitheringConvertor {

	/* VARIABLES */
	
	private static BufferedImage _importedPicture;
	private static BufferedImage _exportedPicture;
	
	private static int _pictureWidth, _pictureHeight;
	
	private static int[] colorPalette;
	
	/* CONSTRUCTORS */
	
	private PaletteDitheringConvertor() {
		//Just for a static class
	}
	
	/* PUBLIC METHODS */
	
	public static BufferedImage convert(BufferedImage importedPicture, int exportedWidth, int paletteSize) {
		_importedPicture = importedPicture;
		return executeConversion(exportedWidth, paletteSize);
	}
	
	public static BufferedImage convert(String path, int exportedWidth, int paletteSize) {
		_importedPicture = PictureImporter.importPicture(path);
		return executeConversion(exportedWidth, paletteSize);
	}
	
	/* PRIVATE METHODS */
	
	private static BufferedImage executeConversion(int exportedWidth, int paletteSize) {
		
		
		return _exportedPicture;
	}
	
	private static void prepareVariables(int exportedWidth, int paletteSize) {
		colorPalette = new int[Math.clamp(paletteSize, 3, 254)];
		
		_importedPicture = PixelArtConvertor.convertPicture(_importedPicture, exportedWidth);
        _pictureHeight = _importedPicture.getHeight();
        _pictureWidth = _importedPicture.getWidth();

        _exportedPicture = new BufferedImage(_pictureWidth, _pictureHeight, BufferedImage.TYPE_INT_ARGB);
	}
}
