package scripts.imagemanipulation;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImportImage {

    /// STATIC CLASS ///
    
    private ImportImage(){

    }

    /// PUBLIC METHODS ///

    public static synchronized BufferedImage importImage(String path) {
        BufferedImage bufferedImage;

        try {
            File file = new File(path);
            bufferedImage = ImageIO.read(file);
            return bufferedImage;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

}
