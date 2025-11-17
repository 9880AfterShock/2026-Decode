package org.firstinspires.ftc.teamcode.Mechanisms.Intake;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class Shield {
    private static Servo shield; // init sevo var
    private static OpMode opmode; // opmode var init
    public static double lockingPosition = 0.97;
    public static double shootingPosition = 0.902;
    public static String shieldState = "blocking";

    public static void initLocking(OpMode opmode) { // init motor
        shield = opmode.hardwareMap.get(Servo.class, "shield"); // motor config name
        Shield.opmode = opmode;
        shieldState = "locking";
    }

    public static void updateLocking(boolean shooting) {
        if (shooting){
            shield.setPosition(shootingPosition);
            shieldState = "shooting";
        } else {
            shield.setPosition(lockingPosition);
            shieldState = "locking";
        }
        opmode.telemetry.addData("Shield State", shieldState);
    }

    public static Action AutoShieldLock() {
        return new Action() {
            public boolean run(@NonNull TelemetryPacket packet) {
                shield.setPosition(lockingPosition);
                return false;
            }
        };
    }

    public static Action AutoShieldShoot() {
        return new Action() {
            public boolean run(@NonNull TelemetryPacket packet) {
                shield.setPosition(shootingPosition);
                return false;
            }
        };
    }
}
