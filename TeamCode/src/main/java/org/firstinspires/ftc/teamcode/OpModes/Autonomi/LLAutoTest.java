package org.firstinspires.ftc.teamcode.OpModes.Autonomi;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.ftc.*;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Aiming.GoalVision;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.Mechanisms.DriveTrain;
import org.firstinspires.ftc.teamcode.Sensors.Limelight;

@Config
@Disabled
@Autonomous(name = "LL AUTO Test")
public class LLAutoTest extends LinearOpMode {
    @Override
    public void runOpMode() {
        DriveTrain.initDrive(this);
        Limelight.initDetection(this);

// QuickSpindexer.initSpindexer(this);

        Pose2d startPos = new Pose2d(-55.5, -47.0, Math.toRadians(55.0));
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPos);

//Poses
        Pose2d scanPos = new Pose2d(-27.0, -27.0, Math.toRadians(-25.0));
        Pose2d shootPos = new Pose2d(-33.0, -33.0, Math.toRadians(55.0));
        Pose2d parkPos = new Pose2d(-60.0, -35.0, Math.toRadians(0.0));

        TrajectoryActionBuilder waitTwenty = drive.actionBuilder(startPos)
                .waitSeconds(20.0);
        TrajectoryActionBuilder toScan = drive.actionBuilder(startPos)
                .setTangent(Math.toRadians(0.0))
                .splineToLinearHeading(scanPos, Math.toRadians(0.0));
        TrajectoryActionBuilder toShoot = drive.actionBuilder(scanPos)
                .setTangent(Math.toRadians(-125.0))
                .splineToLinearHeading(shootPos, Math.toRadians(-125.0));
        TrajectoryActionBuilder toPark = drive.actionBuilder(shootPos)
                .setTangent(Math.toRadians(180.0))
                .splineToLinearHeading(parkPos, Math.toRadians(-180.0));
//.lineToX(30.0)
//.waitSeconds(5.0);


        while (!isStopRequested() && !opModeIsActive()) {
// Do nothing
        }

        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        toScan.build(),
                        Limelight.AutoScan(),
                        waitTwenty.build())
        );
    }
}