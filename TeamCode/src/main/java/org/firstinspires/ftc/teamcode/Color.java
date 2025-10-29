package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.Enums.ColorType;

public class Color {
    private double one;
    private double two;
    private double three;
    private ColorType type;

    public static Color rgb2hsv(Color rgb) {
        Color hsv = new Color(0,0,0,ColorType.HSL);

        double max = Math.max(Math.max(rgb.one, rgb.two), rgb.three);
        double min = Math.min(Math.min(rgb.one, rgb.two), rgb.three);
        double delta = max - min;

        if (delta == 0) {
            hsv.one = 360;
            hsv.two = 0;
            hsv.three = max;
            return hsv;
        }

        if (max == rgb.one) {
            hsv.one = (rgb.two - rgb.three) / delta % 6;
        } else if (max == rgb.two) {
            hsv.one = (rgb.three - rgb.one) / delta + 2;
        } else {
            hsv.one = (rgb.one - rgb.two) / delta + 4;
        }
        hsv.one *= 60;

        if (max == 0) {
            hsv.two = 0;
        } else {
            hsv.two = delta / max;
        }
        hsv.one /= 255;
        hsv.two /= 255;
        hsv.three = max;
        hsv.three /= 255;

        return hsv;
    }

    public Color(double one,double two,double three,ColorType type) {
        this.type = type;
        this.one = one;
        this.two = two;
        this.three = three;

    }
    public Color add(Color color) {
        this.type = ColorType.RGB;
        this.one += color.one;
        this.two += color.two;
        this.three += color.three;
        return this;
    }

    public Color divide(double dividen) {
        this.one /= dividen;
        this.two /= dividen;
        this.three /= dividen;
        return this;
    }

    public Color asHSV() {
        if (this.type == ColorType.RGB) {
            return rgb2hsv(this);
        }
        return this;
    }

    public double getRed() {
        return one;
    }

    public double getHue() {
        return one;
    }

    public double getGreen() {
        return two;
    }

    public double getSaturation() {
        return two;
    }

    public double getBlue() {
        return three;
    }

    public double getLightness(){
        return three;
    }
}
