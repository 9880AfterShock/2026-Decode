
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
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Arm;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Hood;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.QuickSpindexer;
import org.firstinspires.ftc.teamcode.Sensors.Obelisk;
import org.firstinspires.ftc.teamcode.Systems.ActionManager;
import org.firstinspires.ftc.teamcode.Systems.RunLater;

@Config
@Autonomous(name = "Blue far zone 6")
public class LM1AutoBlueFar extends LinearOpMode {
    @Override
    public void runOpMode() {
        //Mechs init'
        Arm.initIntake(this);
        RunLater.setup(this);
        Obelisk.initDetection(this);
        Hood.initAim(this);
        ActionManager actionManager = new ActionManager( this, 28);

        QuickSpindexer.initSpindexer(this); //ugly but works

        Pose2d startPosFar = new Pose2d(62.6, -16.0, Math.toRadians(0.0));
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPosFar);

        //Poses
        Pose2d startPickup1 = new Pose2d(-12.0, -30.0, Math.toRadians(-90.0));
        Pose2d endPickup1 = new Pose2d(-12.0, -55.0, -Math.toRadians(90.0));
        Pose2d startPickup2 = new Pose2d(12.0, -30.0, -Math.toRadians(90.0));
        Pose2d endPickup2 = new Pose2d(12.0, -55.0, -Math.toRadians(90.0));
        Pose2d startPickup3 = new Pose2d(35.5, -30.0, -Math.toRadians(90.0));
        Pose2d endPickup3 = new Pose2d(35.5, -55.0, -Math.toRadians(90.0));
        Pose2d gatePose = new Pose2d(0.0, -55.0, Math.toRadians(0.0));
        Pose2d shootPosFar = new Pose2d(60.0, -12.0, Math.toRadians(22.5));
        Pose2d parkPosFar = new Pose2d(37.75, -32.75, Math.toRadians(90.0));


        TrajectoryActionBuilder waitQuarter = drive.actionBuilder(startPosFar)
                .waitSeconds(0.25);
        TrajectoryActionBuilder waitTwenty = drive.actionBuilder(startPosFar)
                .waitSeconds(20.0);
        TrajectoryActionBuilder toShoot1 = drive.actionBuilder(startPosFar)
                .setTangent(Math.toRadians(110.0))
                .splineToLinearHeading(shootPosFar, Math.toRadians(110.0));
        TrajectoryActionBuilder toPickup1 = drive.actionBuilder(shootPosFar)
                .setTangent(Math.toRadians(-135.0))
                .splineToLinearHeading(startPickup3, Math.toRadians(-135));
        TrajectoryActionBuilder pickup2 = drive.actionBuilder(startPickup3)
                .setTangent(Math.toRadians(-90.0))
                .splineToLinearHeading(endPickup3, Math.toRadians(-90.0));
        TrajectoryActionBuilder toShoot2 = drive.actionBuilder(endPickup3)
                .setTangent(Math.toRadians(45.0))
                .splineToLinearHeading(shootPosFar, Math.toRadians(45.0));
        TrajectoryActionBuilder toPark = drive.actionBuilder(shootPosFar)
                .setTangent(Math.toRadians(-135.0))
                .splineToLinearHeading(parkPosFar, Math.toRadians(-135.0));


        while (!isStopRequested() && !opModeIsActive()) {
            //Obelisk.update();
        }

        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        actionManager.shotCue(0),
                        Arm.AutoArmIn(),
                        Hood.AutoHoodFar(),
                        new ParallelAction(
                                new SequentialAction(
                                        actionManager.shotCue(1),
                                        Obelisk.AutoScan(),
                                        actionManager.spindexer.goToMotif(),
                                        actionManager.cycleRamp()
                                ),
                                toShoot1.build()
                        ),
                        actionManager.rev(4100),
                        actionManager.waitForSpeedSafe(4100),
                        actionManager.launch(),

                        actionManager.shotCue(2),
                        QuickSpindexer.turnRight(),
                        actionManager.waitForSpeedSafe(4100),
                        actionManager.launch(),

                        actionManager.shotCue(3),
                        QuickSpindexer.turnRight(),
                        actionManager.waitForSpeedSafe(4100),
                        actionManager.launch(),

                        //2nd volley

                        actionManager.derev(),
                        QuickSpindexer.resetForTele(), //should change later
                        toPark.build()
                )
        );
    }
}