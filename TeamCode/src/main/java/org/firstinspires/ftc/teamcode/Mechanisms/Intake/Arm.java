package org.firstinspires.ftc.teamcode.Mechanisms.Intake;

import static java.lang.Math.abs;

import androidx.annotation.NonNull;
import androidx.annotation.TransitionRes;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Enums.Motif;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Transfer;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.List;


public class Arm { // Prefix for commands

    private static Servo arm; // init motor var
    private static OpMode opmode; // opmode var init
    public static double intakePosition = 0.55; //was 0.4 for old intake
    public static double transferPosition = 0.4; //was 0.66 for old intake
    public static String intakeState = "Intaking";
//    private static double startedIntaking;

    public static void initIntake(OpMode opmode) { // init motor
        arm = opmode.hardwareMap.get(Servo.class, "arm"); // motor config name
//        startedIntaking = -1.0;
        Arm.opmode = opmode;
    }

    public static void updateIntake(boolean intakeButtonCurrentlyPressed, boolean outTakeButtonCurrentlyPressed) {
        if (intakeButtonCurrentlyPressed || outTakeButtonCurrentlyPressed){
            intakeState = "Intaking";
            arm.setPosition(intakePosition);
//            if (startedIntaking == -1.0){
//                startedIntaking = opmode.getRuntime();
//            }
        } else {
            intakeState = "Transferring";
            arm.setPosition(transferPosition);
//            startedIntaking = -1.0;
        }

//        if (intakeState == "Intaking" && (intakeButtonCurrentlyPressed || outTakeButtonCurrentlyPressed) && opmode.getRuntime() - startedIntaking >= 0.3) {
//            arm.getController().pwmDisable();
//        } else {
//            arm.getController().pwmEnable();
//        }

        opmode.telemetry.addData("Intake Arm", intakeState);
        opmode.telemetry.addData("Intake Pos", arm.getPosition());
//        opmode.telemetry.addData("Intake Timer", startedIntaking);
    }



    public static Action AutoArmIn() {
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