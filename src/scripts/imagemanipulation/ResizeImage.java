package scripts.imagemanipulation;

import java.awt.image.BufferedImage;

public class ResizeImage {

    /// NESTED CLASSES ///

    public static class ImageReducer {

        /// VARIABLES ///

        private int l_targetWidth, l_targetHeight;
        private int l_imageWidth, l_imageHeight;
        private int l_tileWidth, l_tileHeight;
        private float l_xStepBack, l_yStepBack;
        private BufferedImage l_bufferedImage;

        /// CONSTRUCTORS ///

        public ImageReducer(BufferedImage image, int targetWidth) {
            setVariables(image, targetWidth);
            reductionLoop(image);
        }

        /// PRIVATE METHODS

        private void setVariables(BufferedImage image, int targetWidth) {
            int sparePixel;

            l_imageWidth = image.getWidth();
            l_imageHeight = image.getHeight();

            l_targetWidth = targetWidth;
            l_targetHeight = Math.round(((float) l_targetWidth / (float) l_imageWidth) * (float) l_imageHeight);

            l_tileWidth = (l_imageWidth / l_targetWidth) + 1;
            l_tileHeight = (l_imageHeight / l_targetHeight) + 1;

            sparePixel = (l_targetWidth * l_tileWidth) - l_imageWidth;
            l_xStepBack = (float) (sparePixel) / (float) (l_targetWidth - 1);

            sparePixel = (l_targetHeight * l_tileHeight) - l_imageHeight;
            l_yStepBack = (float) (sparePixel) / (float) (l_targetHeight - 1);

            l_bufferedImage = new BufferedImage(l_targetWidth, l_targetHeight, BufferedImage.TYPE_INT_ARGB);
        }

        private void reductionLoop(BufferedImage image) {
            int xOffset, yOffset;
            int currentXCoord, currentYCoord;

            for (int x = 0; x < l_targetWidth; x++) {
                xOffset = Math.round(l_xStepBack * x);
                currentXCoord = x * l_tileWidth - xOffset;

                for (int y = 0; y < l_targetHeight; y++) {
                    yOffset = Math.round(l_yStepBack * y);
                    currentYCoord = y * l_tileHeight - yOffset;

                    l_bufferedImage.setRGB(x, y, extractTileValue(image, currentXCoord, currentYCoord));
                }
            }
        }

        private int extractTileValue(BufferedImage image, int currentXCoord, int currentYCoord) {
            double totalA = 0, totalR = 0, totalG = 0, totalB = 0;
            int pixel;
            int counter = l_tileWidth * l_tileHeight;

            for (int x = currentXCoord; x < currentXCoord + l_tileWidth; x++) {
                for (int y = currentYCoord; y < currentYCoord + l_tileHeight; y++) {
                    pixel = image.getRGB(x, y);
                    totalA += (pixel >> 24) & 0xff;
                    totalR += (pixel >> 16) & 0xff;
                    totalG += (pixel >> 8) & 0xff;
                    totalB += pixel & 0xff;
                }
            }
            int a = (int) Math.round(totalA / counter);
            int r = (int) Math.round(totalR / counter);
            int g = (int) Math.round(totalG / counter);
            int b = (int) Math.round(totalB / counter);

            return (a << 24) | (r << 16) | (g << 8) | b;
        }

        /// GETTERS SETTERS ///

        public BufferedImage getBufferedImage() {
            return l_bufferedImage;
        }
    }

    public static class ImageExtender {

        /// VARIABLES ///

        private int l_imageWidth, l_imageHeight;
        private BufferedImage l_bufferedImage;

        /// CONSTRUCTORS ///

        public ImageExtender(BufferedImage image, int scaleFactor) {
            setVariables(image, scaleFactor);
            extendLoop(image, scaleFactor);
        }

        /// PRIVATE METHODS ///

        private void setVariables(BufferedImage image, int scaleFactor) {
            l_imageWidth = image.getWidth();
            l_imageHeight = image.getHeight();

            l_bufferedImage = new BufferedImage(l_imageWidth * scaleFactor,
                    l_imageHeight * scaleFactor,
                    BufferedImage.TYPE_INT_ARGB);
        }

        private void extendLoop(BufferedImage image, int scaleFactor) {
            int pixel;

            for (int x = 0; x < l_imageWidth; x++) {
                for (int y = 0; y < l_imageHeight; y++) {
                    pixel = image.getRGB(x, y);

                    for (int i = 0; i < scaleFactor; i++) {
                        for (int j = 0; j < scaleFactor; j++) {
                            l_bufferedImage.setRGB(x * scaleFactor + i, y * scaleFactor + j, pixel);
                        }
                    }
                }
            }
        }

        /// GETTERS SETTERS ///

        public BufferedImage getBufferedImage() {
            return l_bufferedImage;
        }
    }

    /// STATIC CLASS ///

    private ResizeImage() {

    }

    /// PUBLIC METHODS ///

    public static BufferedImage reduceImage(BufferedImage image, int targetWidth) {
        ImageReducer imageReducer = new ImageReducer(image, targetWidth);
        return imageReducer.getBufferedImage();
    }

    public static BufferedImage extendImage(BufferedImage image, int scaleFactor){
        ImageExtender imageExtender = new ImageExtender(image, scaleFactor);
        return imageExtender.getBufferedImage();
    }

}
