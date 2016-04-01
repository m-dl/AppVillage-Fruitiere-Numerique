package theron_b.com.visitetablette.tool;

import android.support.v7.graphics.Palette;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyColor {

    private Palette.Swatch m_Swatch;

    public MyColor() {

    }


    public int getMostUsedColor(List<Palette.Swatch> vibrant) {
        ArrayList<Integer> population = new ArrayList<>();
        for (int i = 0 ; i < vibrant.size() ; ++i) {
            population.add(vibrant.get(i).getPopulation());
        }
        Integer tmp = Collections.max(population);
        for (int i = 0 ; i < vibrant.size() ; ++i) {
            if (vibrant.get(i).getPopulation() == tmp) {
                m_Swatch = vibrant.get(i);
                return vibrant.get(i).getRgb();
            }
        }
        return 0;
    }

    private String packedIntegerToHexadecimal(int RGB) {
        int signe = 1;
        if (Math.signum((float)RGB) == -1.0)
            signe = -1;
        int blue = ((RGB) % 255) * signe;
        int green = ((RGB / 256) % 255) * signe;
        int red = ((RGB / 256 / 256) % 255) * signe;
        return String.format("#%02x%02x%02x", red, green, blue);
    }

    public String getHexadecimal(int RGB) {
        return packedIntegerToHexadecimal(RGB);
    }

    public String getTitleColor() {
        int RGB = m_Swatch.getTitleTextColor();
        RGB = lightenColor(RGB);
        return packedIntegerToHexadecimal(RGB);
    }

    private int lightenColor(int rgb) {
        int blue = ((rgb) % 255) + (255 - ((rgb) % 255)) / 2;
        int green = ((rgb / 256) % 255) + (255 - ((rgb / 256) % 255)) / 2;
        int red = ((rgb / 256 / 256) % 255) + (255 - ((rgb / 256 / 256) % 255)) / 2;
        rgb = red % 255;
        rgb = (rgb << 8) + green % 255;
        rgb = (rgb << 8) + blue % 255;
        return rgb;
    }
}
