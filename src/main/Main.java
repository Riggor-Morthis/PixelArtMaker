package main;

import java.io.IOException;

import scripts.DitheringColored;
import scripts.DitheringColoredAlternative;
import scripts.DitheringGrayscale;
import scripts.DitheringPalette;
import scripts.ImagePackage;

public class Main {

    public static void main(String[] args) throws IOException {
        ImagePackage joconde = new ImagePackage("pictures/joconde.png", 128, 4);
        DitheringGrayscale.create(joconde, "pictures/jocondegreyscale.png", 5);
        DitheringColored.create(joconde, "pictures/jocondecolored.png", 5);
        DitheringColoredAlternative.create(joconde, "pictures/jocondecoloredalternative.png", 5);
        DitheringPalette.create(joconde, "pictures/jocondepalette.png", 5, "pictures/palette_mastersystem.png");
    }
}