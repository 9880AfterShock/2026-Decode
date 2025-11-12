package org.firstinspires.ftc.teamcode.Mechanisms.Intake;

import static java.lang.Math.abs;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Color;
import org.firstinspires.ftc.teamcode.Enums.BallType;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.BallColorDetectinator;
import org.firstinspires.ftc.teamcode.Systems.DelayedAction;
import org.firstinspires.ftc.teamcode.Systems.RunLater;
import org.firstinspires.ftc.teamcode.messages.SpindexerMessage;


public class Arm { // Prefix for commands

    private static Servo arm; // init motor var
    private static OpMode opmode; // opmode var init
    public static double intakePosition = 0.36;
    public static double neutralPosition = 0.57;
    public static double transferPosition = 0.73;
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
                if (intakeState != "Transferring") {
                    intakeState = "Transferring";
                    RunLater.addAction(new DelayedAction(() -> {arm.setPosition(transferPosition);}, 0.2));
                }
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

    public static Action AutoLaunchStart() {
        return new Action() {
            private boolean first = true;
            public boolean run(@NonNull TelemetryPacket packet) {
                if (first) {
                    RunLater.addAction(new DelayedAction(() -> {arm.setPosition(transferPosition);}, 0.2));
                    Roller.updateIntake(false, false, true, 1.0);
                    first = false;
                }
                RunLater.update();
                return !RunLater.isEmpty();
            }
        };
    }

    public static Action AutoLaunchEnd() {
        return new Action() {
            public boolean run(@NonNull TelemetryPacket packet) {
                Roller.updateIntake(false, false, false, 1.0);
                arm.setPosition(neutralPosition);
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