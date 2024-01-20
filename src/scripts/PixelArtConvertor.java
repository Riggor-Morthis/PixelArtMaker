package scripts;

import java.awt.image.BufferedImage;

public class PixelArtConvertor {

	/* VARIABLES */

	private static BufferedImage _importedPicture;
	private static BufferedImage _exportedPicture;

	private static int _importedWidth, _importedHeight;
	private static int _nbHorizontalTiles, _nbVerticalTiles;
	private static int _tileWidth, _tileHeight;
	private static int _spareXPixels, _spareYPixels;
	private static double _xStepBack, _yStepBack;
	private static int _tileArea;

	/* CONSTRUCTORS */

	private PixelArtConvertor() {
	}

	/* PUBLIC METHODS */

	public static BufferedImage convert(String filepath, int exportedWidth) {
		_importedPicture = PictureImporter.importPicture(filepath);

		createPictureVariables(exportedWidth);
		createLoopVariables();
		mainLoop();

		return _exportedPicture;
	}

	/* PRIVATE METHODS */

	private static void createPictureVariables(int exportedWidth) {
		_importedWidth = _importedPicture.getWidth();
		_importedHeight = _importedPicture.getHeight();

		_nbHorizontalTiles = exportedWidth;
		_nbVerticalTiles = Math
				.round(((float) (_nbHorizontalTiles) / (float) (_importedWidth) * (float) (_importedHeight)));

		_exportedPicture = new BufferedImage(_nbHorizontalTiles, _nbVerticalTiles, BufferedImage.TYPE_INT_ARGB);
	}

	private static void createLoopVariables() {
		_tileWidth = (_importedWidth / _nbHorizontalTiles) + 1;
		_spareXPixels = (_nbHorizontalTiles * _tileWidth) - _importedWidth;

		_tileHeight = (_importedHeight / _nbVerticalTiles) + 1;
		_spareYPixels = (_nbVerticalTiles * _tileHeight) - _importedHeight;

		_xStepBack = (double) _spareXPixels / (double) (_nbHorizontalTiles - 1);
		_yStepBack = (double) _spareYPixels / (double) (_nbVerticalTiles - 1);

		_tileArea = _tileWidth * _tileHeight;
	}

	private static void mainLoop() {
		int xOffset, yOffset;
		int startingXValue, startingYValue;

		for (int xTile = 0; xTile < _nbHorizontalTiles; xTile++) {
			xOffset = (int) Math.round(_xStepBack * xTile);
			startingXValue = xTile * _tileWidth - xOffset;

			for (int yTile = 0; yTile < _nbVerticalTiles; yTile++) {
				yOffset = (int) Math.round(_yStepBack * yTile);
				startingYValue = yTile * _tileHeight - yOffset;

				setTileValue(startingXValue, startingYValue, xTile, yTile);
			}
		}
	}

	private static void setTileValue(int startingXValue, int startingYValue, int xTile, int yTile) {
		int pixel = extractTileValue(startingXValue, startingYValue);
		_exportedPicture.setRGB(xTile, yTile, pixel);
	}

	private static int extractTileValue(int startingXValue, int startingYValue) {
		double totalR = 0, totalG = 0, totalB = 0;
		int pixel;

		for (int x = startingXValue; x < startingXValue + _tileWidth; x++) {
			for (int y = startingYValue; y < startingYValue + _tileHeight; y++) {
				pixel = _importedPicture.getRGB(x, y);
				totalR += (pixel >> 16) & 0xff;
				totalG += (pixel >> 8) & 0xff;
				totalB += pixel & 0xff;
			}
		}

		int r = (int) Math.round(totalR / _tileArea);
		int g = (int) Math.round(totalG / _tileArea);
		int b = (int) Math.round(totalB / _tileArea);

		return (255 << 24) | (r << 16) | (g << 8) | b;
	}
}
