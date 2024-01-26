package main;

import java.io.IOException;

import scripts.DitheringCreator;

public class Main {

    public static void main(String[] args) throws IOException {
        DitheringCreator.create("pictures/joconde.png", 128, 4, "pictures/dithered.png", 5);
        DitheringCreator.create("pictures/joconde.png", "pictures/palette_nes.png", 128, 4,
                "pictures/nes.png", 5);
        DitheringCreator.create("pictures/joconde.png", "pictures/palette_mastersystem.png", 128, 4,
                "pictures/mastersystem.png", 5);
    }
}
