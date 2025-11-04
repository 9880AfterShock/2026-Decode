package org.firstinspires.ftc.teamcode.Mechanisms.Scoring;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Aiming.DriverTest;

public class Hood {
    private static Servo hood; // init motor var
    private static OpMode opmode; // opmode var init
    public static double farPosition = 0.98; //could be as "high" (low) as 0.97
    public static double nearPosition = 1.0;
    public static double autoFarPosition = 0.96;
    public static String hoodState = "Near";

    public static void initAim(OpMode opmode) { // init motor
        hood = opmode.hardwareMap.get(Servo.class, "hood"); // motor config name
        Hood.opmode = opmode;
        hoodState = "Near";
    }
/*
    public static void goUp() {
        hood.setPosition(upPosition);
        opmode.telemetry.addData("Hood", "Up"); //not rly used lol
    }
*/
    public static void updateAim(boolean toggleRange) {
        if (toggleRange){
            if (hoodState == "Near") {
                hoodState = "Far";
                hood.setPosition(farPosition);
                DriverTest.desSpeed = 4100;
            } else {
                hoodState = "Near";
                hood.setPosition(nearPosition);
                DriverTest.desSpeed = 3300;
            }
        }
        opmode.telemetry.addData("Hood State", hoodState);
        opmode.telemetry.addData("Hood Target", hood.getPosition());
    }
    public static Action AutoHoodNear() {
        return new Action() {
            public boolean run(@NonNull TelemetryPacket packet) {
                hood.setPosition(nearPosition);
                return false;
            }
        };
    }
    public static Action AutoHoodFar() {
        return new Action() {
            public boolean run(@NonNull TelemetryPacket packet) {
                hood.setPosition(autoFarPosition);
                return false;
            }
        };
    }
}
