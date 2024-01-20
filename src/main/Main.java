package main;

import java.io.IOException;
import scripts.DitheringCreator;

public class Main {

	public static void main(String[] args) throws IOException {
		DitheringCreator.create("pictures/joconde.png", 128, 16, "pictures/joconde16.png", 5);
	}
}
