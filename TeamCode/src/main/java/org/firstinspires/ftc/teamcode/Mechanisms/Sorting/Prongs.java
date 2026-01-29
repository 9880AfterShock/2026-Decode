package org.firstinspires.ftc.teamcode.Mechanisms.Sorting;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class Prongs {
    private static Servo prongs; // init sevo var
    private static OpMode opmode; // opmode var init
    public static double intakingPosition = 0.7;
    public static double shootingPosition = 0.96;
    public static double primingPosition = 1.0;

    public static void initGrate(OpMode opmode) { // init motor
        prongs = opmode.hardwareMap.get(Servo.class, "shield"); //Port 2 on control hub, not renaming servo
        Prongs.opmode = opmode;
    }

    public static void updateGrate(boolean shooting, boolean priming) {
        if (shooting){
            prongs.setPosition(shootingPosition);
        } else {
            if (priming){
                prongs.setPosition(primingPosition);
            } else {
                prongs.setPosition(intakingPosition);
            }
        }
//        opmode.telemetry.addData("Prong Target", prongs.getPosition());
    }

    public static Action AutoProngsIntake() {
        return new Action() {
            public boolean run(@NonNull TelemetryPacket packet) {
                prongs.setPosition(intakingPosition);
                return false;
            }
        };
    }

    public static Action AutoProngsPrime() {
        return new Action() {
            public boolean run(@NonNull TelemetryPacket packet) {
                prongs.setPosition(primingPosition);
                return false;
            }
        };
    }

    public static Action AutoProngsShooting() {
        return new Action() {
            public boolean run(@NonNull TelemetryPacket packet) {
                prongs.setPosition(shootingPosition);
                return false;
            }
        };
    }
}
