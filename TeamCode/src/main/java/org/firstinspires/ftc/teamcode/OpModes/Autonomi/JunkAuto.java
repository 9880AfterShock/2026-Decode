
package org.firstinspires.ftc.teamcode.OpModes.Autonomi;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.*;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.MecanumDrive;

@Autonomous(name = "Test Auto Cross Field JUNK")
public class JunkAuto extends LinearOpMode {

    @Override
    public void runOpMode() {
        //Mechs init

        Pose2d startPos = new Pose2d(-55.0, -55.0, Math.toRadians(55.0)); //start position and rotation
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPos);

        //Poses
        Pose2d forwardPos = new Pose2d(-30.0, 0.0, Math.toRadians(0.0));
        Pose2d backwardPos = new Pose2d(30.0, 0.0, 0.0);
        Pose2d turnPos = new Pose2d(1.0, 1.0, -180.0);


        TrajectoryActionBuilder waitFive = drive.actionBuilder(startPos)
                .waitSeconds(5.0);
        TrajectoryActionBuilder toForward = drive.actionBuilder(turnPos)
                .setTangent(Math.toRadians(0.0))
                .splineToLinearHeading(forwardPos, Math.toRadians(0.0));
        TrajectoryActionBuilder testPath = drive.actionBuilder(startPos)
                .setTangent(Math.toRadians(0.0))
                .splineToLinearHeading(forwardPos, Math.toRadians(0.0))
                .setTangent(0)
                .splineToSplineHeading(new Pose2d(48, 48, 0), Math.PI / 2);
        //.lineToX(30.0)
        //.waitSeconds(5.0);


        while (!isStopRequested() && !opModeIsActive()) {
            // Do nothing
        }

        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        toForward.build()
                )
        );
    }
}