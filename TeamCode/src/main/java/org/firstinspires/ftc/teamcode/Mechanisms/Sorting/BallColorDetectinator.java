package org.firstinspires.ftc.teamcode.Mechanisms.Sorting;

import org.firstinspires.ftc.teamcode.Color;
import org.firstinspires.ftc.teamcode.Enums.ColorType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;

public class BallColorDetectinator {
    private static HashSet<Supplier<Color>> sensors = new HashSet<>();
    private static HashMap<Supplier<Color>, ArrayList<Color>> colormap = new HashMap<>();

    public static void addSensor(Supplier<Color> sensor) {
        sensors.add(sensor);
    }
    public static void setup() {
        sensors = new HashSet<>();
        colormap = new HashMap<>();
    }
    public static void update() {
        for (Supplier<Color> sensor : sensors) {
            Color color = sensor.get();
            if (color == null) {continue;}
            if (colormap.containsKey(sensor) && (color.getRed()+ color.getGreen()+ color.getBlue())/3 > 0.15) {
                colormap.get(sensor).add(color);
            } else {
                colormap.put(sensor, new ArrayList<>(List.of(color)));
            }
        }
    }
    public static Color pullData(Supplier<Color> sensor) {
        Color sum  = new Color(0,0,0, ColorType.HSL);
        if (!colormap.containsKey(sensor)) {return sum;}
        ArrayList<Color> colors = colormap.get(sensor);
        for (Color color : colors) {
            sum = sum.add(color.asHSV());
        }
        return sum.divide(colors.size());
    }

}
