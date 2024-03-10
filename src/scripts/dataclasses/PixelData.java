package scripts.dataclasses;

import java.awt.Color;

public class PixelData {
    
    /// NESTED CLASSES ///

    public class ArgbData{

        /// VARIABLES ///

        private int l_a, l_r, l_g, l_b;
        private int l_argb;

        /// CONSTRUCTORS ///

        public ArgbData(int argb){
            l_argb = argb;
            convertToRGB();
        }

        /// PUBLIC METHODS ///

        public float getRgbAverage(){
            return (l_r + l_g + l_b) / 3.0f;
        }

        /// PRIVATE METHODS ///

        private void convertToRGB(){
            l_a = (l_argb >> 24) & 0xff;
            l_r = (l_argb >> 16) & 0xff;
            l_g = (l_argb >> 8) & 0xff;
            l_b = (l_argb) & 0xff;
        }

        /// GETTERS SETTERS ///

        public int getR(){
            return l_r;
        }
        public int getG(){
            return l_g;
        }
        public int getB(){
            return l_b;
        }
    }

    private class HsvData{
        
        /// VARIABLES ///

        int l_h, l_s, l_v;

        /// CONSTRUCTORS ///

        public HsvData(ArgbData argb){
            int r = argb.getR();
            int g = argb.getG();
            int b = argb.getB();
            float[] hsv = new float[3];

            Color.RGBtoHSB(r, g, b, hsv);

            l_h = Math.round(hsv[0]);
            l_s = Math.round(hsv[1] * 100);
            l_v = Math.round(hsv[2] * 100);
        }
    }

    /// VARIABLES ///

    private ArgbData l_argb;
    private HsvData l_hsv;
    private int l_paletteIndex;

    /// CONSTRUCTORS ///

    public PixelData(int argb){
        l_argb = new ArgbData(argb);
    }

}
