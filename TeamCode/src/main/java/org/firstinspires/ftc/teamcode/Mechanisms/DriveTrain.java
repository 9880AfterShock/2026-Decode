package org.firstinspires.ftc.teamcode.Mechanisms;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

public class DriveTrain { // Prefix for commands
    private static DcMotor leftRear; // init motor vars
    private static DcMotor leftFront;
    private static DcMotor rightRear;
    private static DcMotor rightFront;
    private static OpMode opmode; // opmode var init
    private static final double speedDivider = 3.0; // divider for slow mode
    public static boolean slowMode = false;
    private static boolean slowModeButtonCurrentlyPressed = false;
    private static boolean slowModeButtonPreviouslyPressed = false;

    public static void initDrive(OpMode opmode) { // init motors
        leftRear = opmode.hardwareMap.get(DcMotor.class, "leftRear"); // motor config names
        leftFront = opmode.hardwareMap.get(DcMotor.class, "leftFront");
        rightRear = opmode.hardwareMap.get(DcMotor.class, "rightRear");
        rightFront = opmode.hardwareMap.get(DcMotor.class, "rightFront");

        leftRear.setDirection(DcMotorSimple.Direction.REVERSE); // motor directions
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightRear.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);

        slowMode = false;

        DriveTrain.opmode = opmode;
    }

    public static void updateDrive(float strafe, float drive, float turn, boolean slowModeButton) {
        double leftBackPower;
        double leftFrontPower;
        double rightBackPower;
        double rightFrontPower;

        leftBackPower = Range.clip(drive + turn - strafe, -1.0, 1.0);
        leftFrontPower = Range.clip(drive + turn + strafe, -1.0, 1.0);
        rightBackPower = Range.clip(drive - turn + strafe, -1.0, 1.0);
        rightFrontPower = Range.clip(drive - turn - strafe, -1.0, 1.0);

        updateSpeed(slowModeButton);

        if (slowMode) {
            leftRear.setPower(leftBackPower / speedDivider);
            leftFront.setPower(leftFrontPower / speedDivider);
            rightRear.setPower(rightBackPower / speedDivider);
            rightFront.setPower(rightFrontPower / speedDivider);
        } else {
            leftRear.setPower(leftBackPower);
            leftFront.setPower(leftFrontPower);
            rightRear.setPower(rightBackPower);
            rightFront.setPower(rightFrontPower);
        }

        opmode.telemetry.addData("Front Motors", "left (%.2f), right (%.2f)", leftFrontPower, rightFrontPower);
        opmode.telemetry.addData("Back Motors", "left (%.2f), right (%.2f)", leftBackPower, rightBackPower);
        opmode.telemetry.addData("Slow mode?", slowMode);
    }

    private static void updateSpeed(boolean slowModeButton) {
        slowModeButtonCurrentlyPressed = slowModeButton;
        if (slowModeButtonCurrentlyPressed && !slowModeButtonPreviouslyPressed) {
            slowMode = !slowMode;
        }
        slowModeButtonPreviouslyPressed = slowModeButtonCurrentlyPressed;
    }
}