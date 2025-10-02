package org.firstinspires.ftc.teamcode.Sensors;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;

public class SpindexerCamera {
    private static OpMode opmode; // opmode var init
    private static ColorDetectionPipeline colorPipeline;
    public static VisionPortal colorVisionPortal;
    public enum Slots{PURPLE, GREEN, NONE, unknown}; // 21,22,23
    public static Slots[] Inventory; // In order of Front, Back Left, Back Right

    public static void initDetection(OpMode opmode){
        Inventory = new Slots[]{Slots.unknown, Slots.unknown, Slots.unknown};

        colorPipeline = new ColorDetectionPipeline();
        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(opmode.hardwareMap.get(WebcamName.class, "Webcam"));
        builder.addProcessor(colorPipeline);
        colorVisionPortal = builder.build();

        SpindexerCamera.opmode = opmode;
    }

    public static void update(){
        ColorDetectionPipeline.DetectedColor[] detectedColors = colorPipeline.getSlotColors();

        for (int i = 0; i < 3; i++) {
            if (detectedColors[i] == ColorDetectionPipeline.DetectedColor.GREEN) {
                Inventory[i] = Slots.GREEN;
            } else if (detectedColors[i] == ColorDetectionPipeline.DetectedColor.PURPLE) {
                Inventory[i] = Slots.PURPLE;
            } else {
                Inventory[i] = Slots.NONE;
            }

        }

        //set slots based on ROI from webcam
        opmode.telemetry.addData("Front", Inventory[0]);
        opmode.telemetry.addData("Back Left", Inventory[1]);
        opmode.telemetry.addData("Back Right", Inventory[2]);
    }

    public static void stopVision() {
        colorVisionPortal.close();
    }
}