
package org.firstinspires.ftc.teamcode.OpModes.Autonomi;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.ftc.*;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Enums.BallType;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.BallRamp;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.Spindexer;
import org.firstinspires.ftc.teamcode.Sensors.Obelisk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Config
@Autonomous(name = "Score Preloads (Blue)")
public class FirstAuto extends LinearOpMode {
    @Override
    public void runOpMode() {
        //Mechs init
        Obelisk.initDetection(this);
        Spindexer spindexer = new Spindexer("spindexer", this, 1425.1, 10, () -> false, Arrays.asList(BallType.GREEN, BallType.PURPLE, BallType.PURPLE));
        BallRamp ballRamp = new BallRamp(this, "ramp", 0.07, 0.2);


        Pose2d startPos = new Pose2d(-55.5, -47.0, Math.toRadians(55.0));
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPos);

        //Poses
        Pose2d scanPos = new Pose2d(-23.0, -23.0, Math.toRadians(-20));
        Pose2d shootPos = new Pose2d(-33.0, -33.0, Math.toRadians(45));
        Pose2d parkPos = new Pose2d(-60.0, -20.0, Math.toRadians(0.0));


        TrajectoryActionBuilder waitTwenty = drive.actionBuilder(startPos)
                .waitSeconds(20.0);
        TrajectoryActionBuilder toScan = drive.actionBuilder(startPos)
                .setTangent(Math.toRadians(0.0))
                .splineToLinearHeading(scanPos, Math.toRadians(0.0));
        TrajectoryActionBuilder toShoot = drive.actionBuilder(scanPos)
                .setTangent(Math.toRadians(-125.0))
                .splineToLinearHeading(shootPos, Math.toRadians(-125.0));
        TrajectoryActionBuilder toPark = drive.actionBuilder(shootPos)
                .setTangent(Math.toRadians(135.0))
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
                        Obelisk.AutoScan(),
                        new ParallelAction(
                                spindexer.goToMotif(Obelisk.motif),
                                toShoot.build()

                        )
                        //shoot goes here
                )
        );

//        if (Obelisk.motif == Obelisk.Motif.GPP){
//            return;
//        } else if (Obelisk.motif == Obelisk.Motif.PGP){
//            return;
//        } else if (Obelisk.motif == Obelisk.Motif.PPG){
//            return;
//        }

        Actions.runBlocking(
            new SequentialAction(
                toShoot.build(),
                waitTwenty.build()
            )
        );
    }
}