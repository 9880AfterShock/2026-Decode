package org.firstinspires.ftc.teamcode.Mechanisms.Intake;

import static java.lang.Math.abs;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;


public class Arm { // Prefix for commands

    private static Servo arm; // init motor var
    private static OpMode opmode; // opmode var init
    public static double intakePosition = 0.36;
    public static double neutralPosition = 0.57;
    public static double transferPosition = 0.65;
    public static String intakeState = "Intaking";

    public static void initIntake(OpMode opmode) { // init motor
        arm = opmode.hardwareMap.get(Servo.class, "arm"); // motor config name
        Arm.opmode = opmode;
    }

    public static void updateIntake(boolean intakeButtonCurrentlyPressed, boolean outTakeButtonCurrentlyPressed, boolean transfering) {
        if (intakeButtonCurrentlyPressed || outTakeButtonCurrentlyPressed){
            intakeState = "Intaking";
            arm.setPosition(intakePosition);
        } else {
            if (transfering) {
                intakeState = "Transferring";
                arm.setPosition(transferPosition);
            } else {
                intakeState = "Neutral";
                arm.setPosition(neutralPosition);
            }
        }

        opmode.telemetry.addData("Intake Arm", intakeState);
        opmode.telemetry.addData("Intake Pos", arm.getPosition());
    }



    public static Action AutoArmIn() {
        return new Action() {
            public boolean run(@NonNull TelemetryPacket packet) {
                arm.setPosition(neutralPosition);
                intakeState = "Neutral";
                return false;
            }
        };
    }

    public static Action AutoArmTransfer() {
        return new Action() {
            public boolean run(@NonNull TelemetryPacket packet) {
                arm.setPosition(transferPosition);
                intakeState = "Transferring";
                return false;
            }
        };
    }

    public static Action AutoArmOut() {
        return new Action() {
            public boolean run(@NonNull TelemetryPacket packet) {
                arm.setPosition(intakePosition);
                intakeState = "Intaking";
                return false;
            }
        };
    }
}