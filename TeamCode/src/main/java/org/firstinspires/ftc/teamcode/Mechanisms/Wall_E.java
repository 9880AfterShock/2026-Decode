package org.firstinspires.ftc.teamcode.Mechanisms;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Transfer;
import org.firstinspires.ftc.teamcode.Systems.DelayedAction;
import org.firstinspires.ftc.teamcode.Systems.RunLater;

import java.util.Objects;

public class Wall_E {
    private static Servo leftWheel;
    private static Servo rightWheel;
    private static OpMode opmode; // opmode var init
    private static String state = "unknown";
    private static double leftScanPos = 0.5;
    private static double rightScanPos = 0.5;
    private static double leftDecodePos = 0.0;
    private static double rightDecodePos = 0.0;

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
