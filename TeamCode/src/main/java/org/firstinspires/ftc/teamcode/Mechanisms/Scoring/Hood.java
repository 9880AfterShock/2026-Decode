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
    public static double farPosition = 0.96; //could be as "high" (low) as 0.97
    public static double nearPosition = 0.98;

    public static void initAim(OpMode opmode) { // init motor
        hood = opmode.hardwareMap.get(Servo.class, "hood"); // motor config name
        Hood.opmode = opmode;
    }

    public static void goNear() {
        hood.setPosition(nearPosition);
        hood.setPosition(nearPosition);
        DriverTest.desSpeed = 3300;
    }

    public static void updateAim(double angle) {
        hood.setPosition(((0.92-0.98)*((angle-30)/20))+1.0);
        opmode.telemetry.addData("Hood State (deg)", angle);
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
}
