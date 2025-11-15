package org.firstinspires.ftc.teamcode.OpModes.Autonomi;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.ftc.*;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Aiming.DriverTest;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Arm;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Roller;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Hood;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.QuickSpindexer;
import org.firstinspires.ftc.teamcode.Sensors.Obelisk;
import org.firstinspires.ftc.teamcode.Systems.ActionManager;
import org.firstinspires.ftc.teamcode.Systems.RunLater;

@Config
@Autonomous(name = "Far zone 3 not 6")
public class LM1AutoFar extends LinearOpMode {
    ElapsedTime runtime = new ElapsedTime();
    @Override
    public void runOpMode() {
        //Mechs init'
        Arm.initIntake(this);
        Roller.initIntake(this);
        RunLater.setup(this);
        Obelisk.initDetection(this);
        DriverTest.initControls(this); //new
        Hood.initAim(this);
        ActionManager actionManager = new ActionManager( this, 28);

        QuickSpindexer.initSpindexer(this); //ugly but works

        double waitTime = 0.0;
        double posMultiplier = 1.0;

        while (!isStopRequested() && !opModeIsActive()) {
            telemetry.addLine("Use dpad to change delay, and x and b to select alliance");
            if (gamepad1.xWasPressed()){
                posMultiplier = 1.0;
            }
            if (gamepad1.bWasPressed()){
                posMultiplier = -1.0;
            }
            if (gamepad1.dpadUpWasPressed()){
                waitTime += 1.0;
            }
            if (gamepad1.dpadDownWasPressed()){
                waitTime -= 1.0;
            }
            if (waitTime < 0){
                waitTime = 0.0;
            }
            telemetry.addData("Wait time", waitTime);
            if (posMultiplier == 1.0) {
                telemetry.addData("Alliance", "Blue");
            }
            if (posMultiplier == -1.0) {
                telemetry.addData("Alliance", "Red");
            }
            Obelisk.update();
            telemetry.addData("Current Motif", Obelisk.motif);
            telemetry.update();
//            Obelisk.update();
        }

        Pose2d startPosFar = new Pose2d(62.6, posMultiplier*-16.0, posMultiplier*Math.toRadians(0.0));
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPosFar);

        //Poses
        Pose2d startPickup1 = new Pose2d(-12.0, posMultiplier*-30.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d endPickup1 = new Pose2d(-12.0, posMultiplier*-55.0, posMultiplier*-Math.toRadians(90.0));
        Pose2d startPickup2 = new Pose2d(12.0, posMultiplier*-30.0, posMultiplier*-Math.toRadians(90.0));
        Pose2d endPickup2 = new Pose2d(12.0, posMultiplier*-55.0, posMultiplier*-Math.toRadians(90.0));
        Pose2d startPickup3 = new Pose2d(35.5, posMultiplier*-30.0, posMultiplier*-Math.toRadians(90.0));
        Pose2d endPickup3 = new Pose2d(35.5, posMultiplier*-55.0, posMultiplier*-Math.toRadians(90.0));
        Pose2d gatePose = new Pose2d(0.0, posMultiplier*-55.0, posMultiplier*Math.toRadians(0.0));
        Pose2d shootPosFar; //dif aiming angles
        double shooterPower;
        if (posMultiplier == 1){ //blue
            shootPosFar = new Pose2d(57.5, posMultiplier*-12.0, posMultiplier*Math.toRadians(21.5));
            shooterPower = 4150;
        } else { //red
            shootPosFar = new Pose2d(57.5, posMultiplier * -12.0, posMultiplier * Math.toRadians(21.5));
            shooterPower = 4100;
        }
        Pose2d parkPosFar = new Pose2d(37.75, posMultiplier*-32.75, posMultiplier*Math.toRadians(90.0));

        TrajectoryActionBuilder waitOne = drive.actionBuilder(startPosFar)
                .waitSeconds(1.0);
        TrajectoryActionBuilder waitTwo = drive.actionBuilder(startPosFar)
                .waitSeconds(2.0);
        TrajectoryActionBuilder waitTwenty = drive.actionBuilder(startPosFar)
                .waitSeconds(20.0);
        TrajectoryActionBuilder toShoot1 = drive.actionBuilder(startPosFar)
                .setTangent(posMultiplier*Math.toRadians(110.0))
                .splineToLinearHeading(shootPosFar, posMultiplier*Math.toRadians(110.0));
        TrajectoryActionBuilder toPickup1 = drive.actionBuilder(shootPosFar)
                .setTangent(posMultiplier*Math.toRadians(-135.0))
                .splineToLinearHeading(startPickup3, posMultiplier*Math.toRadians(-135));
        TrajectoryActionBuilder pickup1 = drive.actionBuilder(startPickup3)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(endPickup3,posMultiplier* Math.toRadians(-90.0));
        TrajectoryActionBuilder toShoot2 = drive.actionBuilder(endPickup3)
                .setTangent(posMultiplier*Math.toRadians(45.0))
                .splineToLinearHeading(shootPosFar, posMultiplier*Math.toRadians(45.0));
        TrajectoryActionBuilder toPark = drive.actionBuilder(shootPosFar)
                .setTangent(posMultiplier*Math.toRadians(-135.0))
                .splineToLinearHeading(parkPosFar, posMultiplier*Math.toRadians(-135.0));
        TrajectoryActionBuilder waitVariable = drive.actionBuilder(startPosFar)
                .waitSeconds(waitTime);

        waitForStart();
        runtime.reset();

        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        actionManager.shotCue(0),
                        Arm.AutoArmIn(),
                        Hood.AutoHoodFar(),
                        Obelisk.AutoScanWithInit(),
                        new ParallelAction(
                                new SequentialAction(
                                        actionManager.shotCue(1),
                                        actionManager.spindexer.goToMotif()
                                ),
                                toShoot1.build()
                        ),

                        waitVariable.build(), //actionmanager one doesnt work

                        actionManager.rev(shooterPower),
//                        waitOne.build(),
                        actionManager.waitForSpeedSafe(shooterPower),
                        actionManager.launch(),

                        actionManager.shotCue(2),
                        QuickSpindexer.turnRight(),
                        actionManager.waitForSpeedSafe(shooterPower),
                        actionManager.launch(),

                        actionManager.shotCue(3),
                        QuickSpindexer.turnRight(),
                        actionManager.waitForSpeedSafe(shooterPower),
                        actionManager.launch(),

                        actionManager.derev(),
//                        actionManager.rampUp(),
//
//                        Arm.AutoArmOut(),
//                        toPickup1.build(),
//
//                        Roller.AutoIntakeOn(),
//                        pickup1.build(),
//                        Roller.AutoIntakeOff(),
//
//                        //filter things into spindexer neatly
//
//                        Arm.AutoArmIn(),
//
//                        new ParallelAction(
//                                new SequentialAction(
//                                        actionManager.spindexer.goToMotif(), //intakes GPP
//                                        actionManager.cycleRamp()
//                                ),
//                                toShoot2.build()
//                        ),
//
//                        actionManager.rev(4100),
//
//                        actionManager.shotCue(4),
//                        actionManager.waitForSpeedSafe(4100),
//                        actionManager.launch(),
//
//                        actionManager.shotCue(5),
//                        QuickSpindexer.turnRight(),
//                        actionManager.waitForSpeedSafe(4100),
//                        actionManager.launch(),
//
//                        actionManager.shotCue(6),
//                        QuickSpindexer.turnRight(),
//                        actionManager.waitForSpeedSafe(4100),
//                        actionManager.launch(),
//
//                        actionManager.derev(),
                        QuickSpindexer.resetForTele(), //should change later
                        toPark.build()
                )
        );
    }
}