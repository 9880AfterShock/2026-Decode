
package org.firstinspires.ftc.teamcode.OpModes.Autonomi;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.ftc.*;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.Sensors.Obelisk;

@Config
@Autonomous(name = "Score Preloads (Blue)")
public class FirstAuto extends LinearOpMode {
    @Override
    public void runOpMode() {
        //Mechs init
        Obelisk.initDetection(this);

        Pose2d startPos = new Pose2d(-55.5, -47.0, Math.toRadians(55.0));
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPos);

        //Poses
        Pose2d scanPos = new Pose2d(-23.0, -23.0, Math.toRadians(-20));
        Pose2d shootPos = new Pose2d(-15.0, -15.0, Math.toRadians(45));


        TrajectoryActionBuilder waitTwenty = drive.actionBuilder(startPos)
                .waitSeconds(20.0);
        TrajectoryActionBuilder toScan = drive.actionBuilder(startPos)
                .setTangent(Math.toRadians(0.0))
                .splineToLinearHeading(scanPos, Math.toRadians(0.0));
        TrajectoryActionBuilder toShoot = drive.actionBuilder(startPos)
                .setTangent(Math.toRadians(45.0))
                .splineToLinearHeading(scanPos, Math.toRadians(45.0));
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
                        Obelisk.AutoScan(),
                        toShoot.build()
                )
        );
    }
}