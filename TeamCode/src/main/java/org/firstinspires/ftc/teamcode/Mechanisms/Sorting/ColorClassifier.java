package org.firstinspires.ftc.teamcode.Mechanisms.Sorting;

import org.firstinspires.ftc.teamcode.Color;

import java.util.HashMap;
import java.util.Map;

public class ColorClassifier<T> {

    private final HashMap<Color,T> colormap;
    private final T default_value;
    private final double tolerance;
    public ColorClassifier(T default_value, double tolerance) {
        this.tolerance = tolerance;
        this.default_value=default_value;
        colormap = new HashMap<>();
    }

    public void addColor(Color color, T value) {
        color = color.asHSV();
        colormap.put(color, value);
    }
    public T classify(Color color) {
        color = color.asHSV();
        Color candidate = null;
        for (Map.Entry<Color, T> entry: colormap.entrySet()) {
            if (candidate == null) {
                if (Math.abs(color.getHue()-entry.getKey().getHue()) < tolerance && Math.abs(entry.getKey().getSaturation()-color.getSaturation()) < tolerance ) {
                    candidate = entry.getKey();
                }
            } else if (Math.abs(color.getHue()-entry.getKey().getHue()) < Math.abs(color.getHue()-candidate.getHue()) && Math.abs(color.getHue()-entry.getKey().getHue()) < tolerance && Math.abs(entry.getKey().getSaturation()-0.5) < tolerance){
                candidate = entry.getKey();
            }
        }
        if (candidate == null || !colormap.containsKey(candidate)) {return default_value;}
        return colormap.get(candidate);
    }
}
