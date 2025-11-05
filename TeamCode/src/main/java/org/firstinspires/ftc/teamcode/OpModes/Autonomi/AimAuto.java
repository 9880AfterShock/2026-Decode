
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
import org.firstinspires.ftc.teamcode.Sensors.Obelisk;

@Config
@Autonomous(name = "Aim RR Test")
@Disabled
public class AimAuto extends LinearOpMode {
    @Override
    public void runOpMode() {
//        //Mechs init'
//        Arm.initIntake(this);
//        RunLater.setup(this);
        Obelisk.initDetection(this);
//        GoalVision.initAprilTag(this);
//        ActionManager actionManager = new ActionManager( this, 24);
        DriveTrain.initDrive(this);

//        QuickSpindexer.initSpindexer(this);

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
                        //actionManager.shotCue(0),
//                        Arm.AutoArmIn(),
                        toScan.build(),
                        Obelisk.AutoScan(),
                        new ParallelAction(
                                new SequentialAction(
//                                        actionManager.shotCue(1),
//                                        actionManager.spindexer.goToMotif(),
//                                        actionManager.cycleRamp()
                                ),
                                toShoot.build()
                        )
                )
        );
        Obelisk.stopVision();
        GoalVision.initAprilTag(this);
        Actions.runBlocking(
//                        actionManager.rev(4300),
                        new ParallelAction(
                                DriveTrain.aim(false),
                                waitTwenty.build()
                        )
//                        actionManager.waitForSpeed(4300),
//                        actionManager.launch()
        );
        Obelisk.stopVision();
        GoalVision.stopVision();
    }
}