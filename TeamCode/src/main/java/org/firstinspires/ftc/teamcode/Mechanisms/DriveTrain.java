package org.firstinspires.ftc.teamcode.Mechanisms;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import static org.firstinspires.ftc.teamcode.MecanumDrive.PARAMS;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Aiming.GoalVision;
import org.firstinspires.ftc.teamcode.TwoDeadWheelLocalizer;
import org.firstinspires.ftc.teamcode.messages.BallRampMessage;

public class DriveTrain { // Prefix for commands
    private static DcMotorEx leftRear; // init motor vars
    private static DcMotorEx leftFront;
    private static DcMotorEx rightRear;
    private static DcMotorEx rightFront;
    private static OpMode opmode; // opmode var init
    private static final double speedDivider = 3.0; // divider for slow mode
    public static boolean slowMode = false;
    private static boolean slowModeButtonCurrentlyPressed = false;
    private static boolean slowModeButtonPreviouslyPressed = false;
    private static final double kP = 0.02;  //0.02 to 0.05
    private static double rotation;
    private static IMU imu;
    private static Pose2d pos;
    private static TwoDeadWheelLocalizer localizer;
    private static final Pose2d goalTarget = new Pose2d(-57.0, -55.0, Math.toRadians(0.0));

    public static void initDrive(OpMode opmode) { // init motors
        leftRear = opmode.hardwareMap.get(DcMotorEx.class, "leftRear"); // motor config names
        leftFront = opmode.hardwareMap.get(DcMotorEx.class, "leftFront");
        rightRear = opmode.hardwareMap.get(DcMotorEx.class, "rightRear");
        rightFront = opmode.hardwareMap.get(DcMotorEx.class, "rightFront");

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

        rotation = 0;

        imu = opmode.hardwareMap.get(IMU.class, "imu");
        localizer = new TwoDeadWheelLocalizer(opmode.hardwareMap, imu, PARAMS.inPerTick, pos);
        pos = new Pose2d(0.0, 0.0, Math.toRadians(0.0));
    }

    public static void updateDrive(float strafe, float drive, float turn, boolean slowModeButton, boolean align, boolean flipSide) { //flips from blue side (false) to red side (true)
        if (align) {
            rotation = GoalVision.getRotation();
            if (rotation == -9880.0) {
//                opmode.telemetry.addData("Estimated Pos X", localizer.getPose().position.x);
//                opmode.telemetry.addData("Estimated Pos Y", localizer.getPose().position.y);
//                opmode.telemetry.addData("Estimated Pos Heading", localizer.getPose().heading.toDouble());
                if (flipSide){
                    Pose2d targetFlipped = new Pose2d(goalTarget.position.x,- goalTarget.position.y, -goalTarget.heading.toDouble());
                    rotation = Math.atan2(targetFlipped.position.x - localizer.getPose().position.x, targetFlipped.position.y - localizer.getPose().position.y) - localizer.getPose().heading.toDouble();
                    rotation = Math.atan2(Math.sin(rotation), Math.cos(rotation));
                } else {
                    rotation = Math.atan2(goalTarget.position.x - localizer.getPose().position.x, goalTarget.position.y - localizer.getPose().position.y) - localizer.getPose().heading.toDouble();
                    rotation = Math.atan2(Math.sin(rotation), Math.cos(rotation));
                }
            }
            turn = (float) Range.clip(rotation * kP, -0.4, 0.4);
            if (Math.abs(rotation) < 0.5) {
                turn = 0;
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
        opmode.telemetry.addData("HEY TIMO", "HERE IS THE MOTOR AMPS STUFFFF FOR DRIVE MOTORS");
//        opmode.telemetry.addData("Front left", leftFront.getCurrent(CurrentUnit.AMPS));
//        opmode.telemetry.addData("Front right", rightFront.getCurrent(CurrentUnit.AMPS));
//        opmode.telemetry.addData("Rear left", leftRear.getCurrent(CurrentUnit.AMPS));
//        opmode.telemetry.addData("Rear right", rightRear.getCurrent(CurrentUnit.AMPS));
    }

    private static void updateSpeed(boolean slowModeButton) {
        slowModeButtonCurrentlyPressed = slowModeButton;
        if (slowModeButtonCurrentlyPressed && !slowModeButtonPreviouslyPressed) {
            slowMode = !slowMode;
        }
        slowModeButtonPreviouslyPressed = slowModeButtonCurrentlyPressed;
    }

    public static Action aim(boolean flipSide) {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                updateDrive(0,0,0, false, true, flipSide);
                return Math.abs(rotation) < 1.0;
            }
        };
    }
}