package org.firstinspires.ftc.teamcode.Mechanisms.Scoring;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Aiming.DriverTest;
import org.firstinspires.ftc.teamcode.WrapperClasses.BulkWriteServo;
@Config
public class Hood {
    private static Servo hoodServo; // init motor var
    private static BulkWriteServo hood; // init motor var
    private static OpMode opmode; // opmode var init
    public static double farPosition = 0.92;
    public static double nearPosition = 0.94;
    public static double flatPosition = 0.96; //lowest the hood can be
    public static String hoodState = "Near";

    public static void initAim(OpMode opmode) { // init motor
        hoodServo = opmode.hardwareMap.get(Servo.class, "hood"); //Port 5 on expansion hub
        hood = new BulkWriteServo(hoodServo);
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
                hoodServo.setPosition(nearPosition);
                return false;
            }
        };
    }
    public static Action AutoHoodFar() {
        return new Action() {
            public boolean run(@NonNull TelemetryPacket packet) {
                hoodServo.setPosition(farPosition);
                return false;
            }
        };
    }
    public static Action AutoHoodFlat() {
        return new Action() {
            public boolean run(@NonNull TelemetryPacket packet) {
                hoodServo.setPosition(flatPosition);
                return false;
            }
        };
    }
}
