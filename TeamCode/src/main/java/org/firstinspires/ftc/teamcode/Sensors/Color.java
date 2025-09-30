package org.firstinspires.ftc.teamcode.Sensors;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Color {
    private static OpMode opmode; // opmode var init
    private static NormalizedColorSensor sensorColor;
    static final float[] hsvValues = new float[3];

    public static void initSensor(OpMode opmode) {
        sensorColor = opmode.hardwareMap.get(NormalizedColorSensor.class, "colorSensor");
        Color.opmode = opmode;

        if (sensorColor instanceof SwitchableLight) {
            ((SwitchableLight)sensorColor).enableLight(true);
        }
    }

    public static void updateSensor(float gain) {
        //opmode.telemetry.addData("Distance Sensor", sensorColor.getDistance(DistanceUnit.MM));
        sensorColor.setGain(gain);

        NormalizedRGBA colors = sensorColor.getNormalizedColors();
        android.graphics.Color.colorToHSV(colors.toColor(), hsvValues);

        opmode.telemetry.addLine()
                .addData("Red", "%.3f", colors.red)
                .addData("Green", "%.3f", colors.green)
                .addData("Blue", "%.3f", colors.blue);
        opmode.telemetry.addLine()
                .addData("Hue", "%.3f", hsvValues[0])
                .addData("Saturation", "%.3f", hsvValues[1])
                .addData("Value", "%.3f", hsvValues[2]);
        opmode.telemetry.addData("Alpha", "%.3f", colors.alpha);

        if (sensorColor instanceof DistanceSensor) {
            opmode.telemetry.addData("Color Sensor Distance (cm)", "%.3f", ((DistanceSensor) sensorColor).getDistance(DistanceUnit.CM));
        }

        opmode.telemetry.addData("Color Raw", colors);
    }

}
