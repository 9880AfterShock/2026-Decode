package org.firstinspires.ftc.teamcode.Mechanisms.Sorting;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Systems.CountdownAction;
import org.firstinspires.ftc.teamcode.WrapperClasses.BulkWriteServo;

public class Prongs {
    private static Servo prongsServo; // init sevo var
    private static BulkWriteServo prongs;
    private static OpMode opmode; // opmode var init
    public static double intakingPosition = 0.97; // Old prongs used 0.6 felicity changed it from .955
    public static double shootingPosition = 0.7; // Old prongs used 0.96
    public static double primingPosition = 0.97; // Old prongs used 1.0 //todo: remove priming pos bc its non-existant in newer code

    public static void initGrate(OpMode opmode) { // init motor
        prongsServo = opmode.hardwareMap.get(Servo.class, "shield"); //Port 2 on control hub, not renaming servo
        prongs = new BulkWriteServo(prongsServo);
        Prongs.opmode = opmode;
    }

    public static void updateGrate(boolean shooting, boolean priming) {
        CountdownAction shootingposAction = new CountdownAction(() -> prongs.setPosition(shootingPosition),"shootPosAction");
        if (!shooting){
            shootingposAction.resetCountdown(0.1);
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
                prongsServo.setPosition(intakingPosition);
                return false;
            }
        };
    }

    public static Action AutoProngsPrime() {
        return new Action() {
            public boolean run(@NonNull TelemetryPacket packet) {
                prongsServo.setPosition(primingPosition);
                return false;
            }
        };
    }

    public static Action AutoProngsShooting() {
        return new Action() {
            public boolean run(@NonNull TelemetryPacket packet) {
                prongsServo.setPosition(shootingPosition);
                return false;
            }
        };
    }
}
