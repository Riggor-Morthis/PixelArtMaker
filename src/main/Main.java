package main;

import java.io.IOException;
import scripts.DitheringCreator;

public class Main {

	public static void main(String[] args) throws IOException {
		for(int i = 2; i < 64; i++){
			DitheringCreator.create("pictures/ScreenLeft.png", 128, i, "pictures/Dithered" +  i + ".png", 5);
		}
	}
}
