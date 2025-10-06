package org.firstinspires.ftc.teamcode.Mechanisms;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class Wall_E {
    private static Servo leftWheel;
    private static Servo rightWheel;
    private static OpMode opmode; // opmode var init
    private static String state = "unknown";
    private final static double leftScanPos = 0.5; //0.45?
    private final static double rightScanPos = 0.5; //0.475?
    private final static double leftDecodePos = 0.525;
    private final static double rightDecodePos = 0.525;

    public static void initWebcam(OpMode opmode) { // init motor
        leftWheel = opmode.hardwareMap.get(Servo.class, "leftWall-E"); // motor config name
        rightWheel = opmode.hardwareMap.get(Servo.class, "rightWall-E"); // motor config name
        state = "unknown";
        Wall_E.opmode = opmode;
    }

    public static void updateTarget(boolean scanButtonPressed, boolean decodeButtonPressed) {
        if (scanButtonPressed){
            state = "scanning";
            leftWheel.setPosition(leftScanPos);
            rightWheel.setPosition(rightScanPos);
        }
        if (decodeButtonPressed){
            state = "decoding";
            leftWheel.setPosition(leftDecodePos);
            rightWheel.setPosition(rightDecodePos);
        }

        opmode.telemetry.addData("Wall-E Task", state);
    }


}
