package org.firstinspires.ftc.teamcode.Mechanisms;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Aiming.GoalVision;

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

        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftRear.setZeroPowerBehavior(BRAKE);
        leftFront.setZeroPowerBehavior(BRAKE);
        rightRear.setZeroPowerBehavior(BRAKE);
        rightFront.setZeroPowerBehavior(BRAKE);

        slowMode = false;

        DriveTrain.opmode = opmode;
    }

    public static void updateDrive(float strafe, float drive, float turn, boolean slowModeButton, boolean align) {
        if (align) {
            double rotation = GoalVision.getRotation();
            if (rotation != -9880.0) {
                strafe = (float) GoalVision.getRotation();
                double kP = 0.02;  // Between 0.02–0.05
                turn = (float) Range.clip(rotation * kP, -0.4, 0.4);
                if (Math.abs(rotation) < 1.0) {
                    turn = 0;
                }
            }
        }
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