package com.example.viewer.stages.util;


import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.scene.paint.Color;

public class ColorUtil {
    public static String tohexCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int)(color.getRed() * 255),
                (int)(color.getGreen() * 255),
                (int)(color.getBlue() * 255));
    }

    public static Color grayScaleColor(Color color) {
        final int gray = (int)Math.round(255 * grayScaleValue(color));
        return Color.grayRgb(gray);
    }

    public static double grayScaleValue(Color color) {
        final double r = 0.30 * color.getRed();
        final double g = 0.59 * color.getGreen();
        final double b = 0.11 * color.getBlue();

        return r + g + b;
    }

    // J'ai placé tous mes bitcoins dessus
    public static ArrayList<Color> toGrayNeo2(Collection<Color> collection){
        ArrayList<Color> grayList = new ArrayList<>();
        double size = collection.size()+1;
        for (int i = 0; i < collection.size() ; i++) {
            grayList.add(Color.hsb(0,0, (i+1)/size));
        }
        return grayList;
    }


    public static boolean isAnHexcode(String hex) {
        String res = hex.replaceAll("\\\\n", "");
        Pattern perfectHex = Pattern.compile("^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$");
        Matcher matcher =  perfectHex.matcher(res);
        return matcher.find();
    }
}
