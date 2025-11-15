
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

import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Arm;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.QuickSpindexer;
import org.firstinspires.ftc.teamcode.Sensors.Obelisk;
import org.firstinspires.ftc.teamcode.Systems.ActionManager;
import org.firstinspires.ftc.teamcode.Systems.RunLater;

@Config
@Autonomous(name = "Score Preloads (Blue)")
@Disabled
public class FirstAuto extends LinearOpMode {
    @Override
    public void runOpMode() {
        //Mechs init'
        Arm.initIntake(this);
        RunLater.setup(this);
        Obelisk.initDetection(this);
        ActionManager actionManager = new ActionManager( this, 24);

        QuickSpindexer.initSpindexer(this); //ugly but works

        Pose2d startPos = new Pose2d(-55.5, -47.0, Math.toRadians(55.0));
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPos);

        //Poses
        Pose2d scanPos = new Pose2d(-27.0, -27.0, Math.toRadians(-5.0));
        Pose2d shootPos = new Pose2d(-33.0, -33.0, Math.toRadians(55.0));
        Pose2d parkPos = new Pose2d(-60.0, -35.0, Math.toRadians(0.0));


        TrajectoryActionBuilder waitFive = drive.actionBuilder(startPos)
                .waitSeconds(5.0);
        TrajectoryActionBuilder waitOne = drive.actionBuilder(startPos)
                .waitSeconds(1.0);
        TrajectoryActionBuilder waitTwo = drive.actionBuilder(startPos)
                .waitSeconds(1.0);
        TrajectoryActionBuilder waitQuarter = drive.actionBuilder(startPos)
                .waitSeconds(0.25);
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
                        actionManager.shotCue(0),
                        Arm.AutoArmIn(),
                        toScan.build(),
                        Obelisk.AutoScan(),
                        new ParallelAction(
                                new SequentialAction(
                                        actionManager.shotCue(1),
                                        actionManager.spindexer.goToMotif()
                                ),
                                toShoot.build()
                        ),
                        actionManager.rev(4300),
                        actionManager.waitForSpeed(4300),
                        actionManager.launch(), //up to here works fine, shot not always in but it does work
                        actionManager.shotCue(2),
                        actionManager.rev(4400),
                        QuickSpindexer.turnRight(),
                        //actionManager.spindexer.right(),

                        //waitHalf.build(),
                        waitOne.build(), //bc waitfor speed is weird
                        actionManager.waitForSpeed(4400),
                        actionManager.launch(),
                        actionManager.shotCue(3),
                        QuickSpindexer.turnRight(),
                        //actionManager.spindexer.right(),

                        //waitHalf.build(),
                        waitOne.build(), //bc waitfor speed is weird
                        waitTwo.build(),
                        actionManager.waitForSpeed(4400),
                        actionManager.launch(),

                        actionManager.derev(),
                        QuickSpindexer.resetForTele(), //should change later
                        toPark.build()
                )
        );
    }
}