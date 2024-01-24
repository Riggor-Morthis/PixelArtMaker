package scripts;

public class PaletteCreatorFromFile {
    
    /* NESTED CLASS */

    private class ColorsAssociation{

        /* VARIABLES */
        private int[] _fileColor;
        private int[] _scratchColor;
        private float _colorsDistance;

        /* CONSTRUCTORS */

        public ColorsAssociation(int filePixel, int scratchPixel){
            _fileColor = getRgbValues(filePixel);
            _scratchColor = getRgbValues(scratchPixel);
            _colorsDistance = calculateDistance(_fileColor, _scratchColor);
        }

        /* PUBLIC METHODS */

        public void tryNewColor(int newColor){
            int[] newValues = getRgbValues(newColor);
            float newDistance = calculateDistance(newValues, _scratchColor);
            if(newDistance < _colorsDistance){
                _colorsDistance = newDistance;
            }
        }

        /* PRIVATE METHODS */

        private float calculateDistance(int[] fileColor, int[] scratchColor){
            return (float) ((Math.pow(fileColor[0] - scratchColor[0], 2)
            + Math.pow(fileColor[0] - scratchColor[0], 2)
            + Math.pow(fileColor[0] - scratchColor[0], 2)) /3f);
        }

        private int[] getRgbValues(int pixel){
            return new int[] { (pixel >> 16) & 0xff, (pixel >> 8) & 0xff, (pixel) & 0xff };
        }
    }

}
