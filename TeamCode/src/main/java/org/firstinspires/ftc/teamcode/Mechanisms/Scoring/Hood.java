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
    public static double farPosition = 0.96; //could be as "high" (low) as 0.97 //old 0.96
    public static double nearPosition = 0.98; //old 0.98 ---- Note to Timo: 0.96 is far, 0.98 is near
    public static double upPosition = 0.96;
    public static String hoodState = "Near";

    public static void initAim(OpMode opmode) { // init motor
        hood = opmode.hardwareMap.get(Servo.class, "hood"); //Port 5 on expansion hub
        Hood.opmode = opmode;
        hoodState = "Near";
    }

    public static void goNear() {
        hood.setPosition(nearPosition);
        hoodState = "Near";
        DriverTest.desSpeed = 3300;
        opmode.telemetry.addData("Hood State", hoodState);
    }

    public static void updateAim(boolean toggleRange) {
        if (toggleRange){
            if (hoodState == "Near") {
                hoodState = "Far";
            } else {
                hoodState = "Near";
            }
        }

        if (hoodState == "Far") {
            hood.setPosition(farPosition);
        } else {
            hood.setPosition(nearPosition);
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
                hood.setPosition(farPosition);
                return false;
            }
        };
    }

    public static Action AutoHoodUp() {
        return new Action() {
            public boolean run(@NonNull TelemetryPacket packet) {
                hood.setPosition(upPosition);
                return false;
            }
        };
    }
}
