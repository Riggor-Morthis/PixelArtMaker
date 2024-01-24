package scripts;

public class ColorsAssociation {

    /* VARIABLES */
    private int[] _fileColor;
    private int[] _scratchColor;
    private float _colorsDistance;

    /* CONSTRUCTORS */

    public ColorsAssociation(int filePixel, int scratchPixel) {
        _fileColor = getRgbValues(filePixel);
        _scratchColor = getRgbValues(scratchPixel);
        _colorsDistance = calculateDistance(_fileColor, _scratchColor);
    }

    /* PUBLIC METHODS */

    public void tryNewColor(int newColor) {
        int[] newValues = getRgbValues(newColor);
        float newDistance = calculateDistance(newValues, _scratchColor);
        if (newDistance < _colorsDistance) {
            _colorsDistance = newDistance;
            _fileColor = newValues;
        }
    }

    public int getFileColor() {
        return 255 << 24 | _fileColor[0] << 16 | _fileColor[1] << 8 | _fileColor[2];
    }

    public boolean equals(ColorsAssociation other) {
        for (int i = 0; i < 3; i++) {
            if (_scratchColor[i] != other.get_scratchColor()[i]) {
                return false;
            }
        }

        return true;
    }

    public int getScratchColor(){
        return 255 << 24 | _scratchColor[0] << 16 | _scratchColor[1] << 8 | _scratchColor[2];
    }

    /* PRIVATE METHODS */

    private float calculateDistance(int[] fileColor, int[] scratchColor) {
        return (float) (Math.pow(fileColor[0] - scratchColor[0], 2)
                + Math.pow(fileColor[1] - scratchColor[1], 2)
                + Math.pow(fileColor[2] - scratchColor[2], 2));
    }

    private int[] getRgbValues(int pixel) {
        return new int[] { (pixel >> 16) & 0xff, (pixel >> 8) & 0xff, (pixel) & 0xff };
    }

    /* GETTERS */

    public int[] get_scratchColor() {
        return _scratchColor;
    }

    public float get_colorsDistance() {
        return _colorsDistance;
    }

}
