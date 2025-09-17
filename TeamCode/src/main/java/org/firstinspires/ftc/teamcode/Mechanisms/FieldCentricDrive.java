package org.firstinspires.ftc.teamcode.Mechanisms;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class FieldCentricDrive { // Prefix for commands
    private static DcMotor leftRear; // init motor vars
    private static DcMotor leftFront;
    private static DcMotor rightRear;
    private static DcMotor rightFront;
    private static IMU imu;
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

        imu = opmode.hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.LEFT));
        imu.initialize(parameters);

        leftRear.setDirection(DcMotorSimple.Direction.REVERSE); // motor directions
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightRear.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);

        leftRear.setZeroPowerBehavior(BRAKE);
        leftFront.setZeroPowerBehavior(BRAKE);
        rightRear.setZeroPowerBehavior(BRAKE);
        rightFront.setZeroPowerBehavior(BRAKE);

        slowMode = false;

        FieldCentricDrive.opmode = opmode;
    }

    public static void updateDrive(float x, float y, float rotation, boolean slowModeButton, boolean resetIMUButton) {
        double leftBackPower;
        double leftFrontPower;
        double rightBackPower;
        double rightFrontPower;

        if (resetIMUButton) {
            imu.resetYaw();
        }

        double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        rotX = rotX * 1.1;  // Counteract imperfect strafing

        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rotation), 1);
        leftFrontPower = (rotY + rotX + rotation) / denominator;
        leftBackPower = (rotY - rotX + rotation) / denominator;
        rightFrontPower = (rotY - rotX - rotation) / denominator;
        rightBackPower = (rotY + rotX - rotation) / denominator;

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