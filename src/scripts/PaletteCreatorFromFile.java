package scripts;

import java.util.ArrayList;
import java.awt.image.BufferedImage;

public class PaletteCreatorFromFile {

    /* VARIABLES */

    private static ArrayList<Integer> _importedScratchPalette;
    private static ArrayList<Integer> _importedFilePalette;
    private static ArrayList<ColorsAssociation> _associations;

    private static ArrayList<Integer> _exportedPalette;

    /* CONSTRUCTORS */

    private PaletteCreatorFromFile() {
    }

    /* PUBLIC METHODS */

    public static ArrayList<Integer> create(BufferedImage importedPicture, String paletteFilePath,
            int[][] importedLuminosity, int paletteSize) {
        try {
            getVariables(importedPicture, paletteFilePath, importedLuminosity, paletteSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mainLoop();
        finalizeFilePalette();

        return _exportedPalette;
    }

    /* PRIVATE METHODS */

    private static void getVariables(BufferedImage importedPicture, String paletteFilePath, int[][] importedLuminosity,
            int paletteSize) throws Exception {
        _importedScratchPalette = PaletteCreatorFromScratch.create(importedPicture, importedLuminosity, paletteSize);
        _importedFilePalette = PaletteExtractorFromFile.extract(paletteFilePath);

        if (_importedFilePalette.size() < _importedScratchPalette.size()) {
            throw new Exception("File does not contain enough unique colours.");
        }

        _associations = new ArrayList<ColorsAssociation>();
    }

    private static void mainLoop() {
        do {
            setDistances();
            removedUsedFileColors();
            removedDuplicateAssociations();
        } while (_associations.size() != _importedScratchPalette.size());
    }

    private static void setDistances() {
        ColorsAssociation association;

        for (int i = 0; i < _importedScratchPalette.size(); i++) {
            association = new ColorsAssociation(_importedFilePalette.get(0), _importedScratchPalette.get(i));

            for (int j = 1; j < _importedFilePalette.size(); j++) {
                association.tryNewColor(_importedFilePalette.get(j));
            }

            _associations.add(association);
        }
    }

    private static void removedUsedFileColors() {
        for (ColorsAssociation a : _associations) {
            if (_importedFilePalette.contains(a.getFileColor())) {
                _importedFilePalette.remove(_importedFilePalette.indexOf(a.getFileColor()));
            }
        }
    }

    private static void removedDuplicateAssociations() {
        ArrayList<ColorsAssociation> buffer = new ArrayList<ColorsAssociation>();

        for (int i = 0; i < _associations.size() - 1; i++) {
            for (int j = i + 1; j < _associations.size(); j++) {

                if (_associations.get(i).equals(_associations.get(j))) {

                    if (_associations.get(i).get_colorsDistance() > _associations.get(j).get_colorsDistance()) {
                        if (!buffer.contains(_associations.get(i))) {
                            buffer.add(_associations.get(i));
                        }
                    } else {
                        if (!buffer.contains(_associations.get(j))) {
                            buffer.add(_associations.get(j));
                        }
                    }
                }
            }
        }

        for (ColorsAssociation b : buffer) {
            _associations.remove(b);
        }
    }

    private static void finalizeFilePalette() {
        _exportedPalette = new ArrayList<Integer>();

        for (int sc : _importedScratchPalette) {
            for (ColorsAssociation ca : _associations) {
                if (sc == ca.getScratchColor()) {
                    _exportedPalette.add(ca.getFileColor());
                }
            }
        }
    }
}
