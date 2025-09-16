package org.firstinspires.ftc.teamcode.Mechanisms.Intake;

import static java.lang.Math.abs;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;


public class Arm { // Prefix for commands

    private static Servo arm; // init motor var
    private static OpMode opmode; // opmode var init
    public static double intakePosition = 0.4;
    public static double transferPosition = 0.6;
    public static String intakeState = "Intaking";
    private static boolean wasIntaking;
    private static double startedIntaking;

    public static void initIntake(OpMode opmode) { // init motor
        arm = opmode.hardwareMap.get(Servo.class, "arm"); // motor config name
        Arm.opmode = opmode;
        wasIntaking = false;
    }

    public static void updateIntake(boolean intakeButtonCurrentlyPressed, boolean outTakeButtonCurrentlyPressed) {
        if (intakeButtonCurrentlyPressed || outTakeButtonCurrentlyPressed){
            intakeState = "Intaking";
            arm.setPosition(intakePosition);
            if (startedIntaking != -1.0){
                startedIntaking = opmode.getRuntime();
            }
        } else {
            intakeState = "Transferring";
            arm.setPosition(transferPosition);
            startedIntaking = -1.0;
        }

        if (intakeState == "Intaking" && (intakeButtonCurrentlyPressed || outTakeButtonCurrentlyPressed) && startedIntaking - opmode.getRuntime() >= 0.5) {
            arm.getController().pwmDisable();
        } else {
            arm.getController().pwmEnable();
        }

        wasIntaking = intakeButtonCurrentlyPressed;

        opmode.telemetry.addData("Intake Arm", intakeState);
    }
}