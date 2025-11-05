
package org.firstinspires.ftc.teamcode.OpModes.Autonomi;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.ftc.*;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

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
@Autonomous(name = "Near zone 3 not 6")
public class LM1AutoNear extends LinearOpMode {
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
        double posMultiplier = 1.0;
        double waitTime = 0.0;
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
            telemetry.update();
        }

        Pose2d startPosClose = new Pose2d(-55.5, posMultiplier*-47.0, posMultiplier*Math.toRadians(55.0));
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPosClose);

        //Poses
        Pose2d scanPos = new Pose2d(-27.0, posMultiplier*-27.0, posMultiplier*Math.toRadians(-25.0));
        Pose2d shootPosClose = new Pose2d(-30.0, posMultiplier*-33.0, posMultiplier*Math.toRadians(45));
        Pose2d parkPosClose = new Pose2d(-60.0, posMultiplier*-35.0, posMultiplier*Math.toRadians(0.0));
        Pose2d startPickup1 = new Pose2d(-12.0, posMultiplier*-30.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d endPickup1 = new Pose2d(-12.0, posMultiplier*-55.0, posMultiplier*-Math.toRadians(90.0));
        Pose2d startPickup3 = new Pose2d(35.5, posMultiplier*-30.0, posMultiplier*-Math.toRadians(90.0));
        Pose2d endPickup3 = new Pose2d(35.5, posMultiplier*-55.0, posMultiplier*-Math.toRadians(90.0));
        Pose2d gatePose = new Pose2d(0.0, posMultiplier*-55.0, posMultiplier*Math.toRadians(0.0));
        Pose2d parkPosFar = new Pose2d(37.75, posMultiplier*-32.75, posMultiplier*Math.toRadians(90.0));

        //to scan
        TrajectoryActionBuilder toScan = drive.actionBuilder(startPosClose)
                .setTangent(posMultiplier*Math.toRadians(55.0))
                .splineToLinearHeading(scanPos, posMultiplier*Math.toRadians(45.0));
        TrajectoryActionBuilder toShoot1 = drive.actionBuilder(scanPos)
                .setTangent(posMultiplier*Math.toRadians(-125.0))
                .splineToLinearHeading(shootPosClose, posMultiplier*Math.toRadians(-125.0));
        TrajectoryActionBuilder toPickup1 = drive.actionBuilder(shootPosClose)
                .setTangent(posMultiplier*Math.toRadians(45.0))
                .splineToLinearHeading(startPickup1, posMultiplier*Math.toRadians(-45.0));
        TrajectoryActionBuilder pickup1 = drive.actionBuilder(startPickup1)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(endPickup1, posMultiplier*Math.toRadians(-90.0));
        TrajectoryActionBuilder toShoot2 = drive.actionBuilder(startPickup1)
                .setTangent(posMultiplier*Math.toRadians(125.0))
                .splineToLinearHeading(shootPosClose, posMultiplier*Math.toRadians(125.0));
        TrajectoryActionBuilder toPark = drive.actionBuilder(shootPosClose)
                .setTangent(posMultiplier*Math.toRadians(180.0))
                .splineToLinearHeading(parkPosClose, posMultiplier*Math.toRadians(-180.0));
        TrajectoryActionBuilder waitVariable = drive.actionBuilder(startPosClose)
                .waitSeconds(waitTime);

        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        actionManager.shotCue(0),
                        Arm.AutoArmIn(),
                        Hood.AutoHoodNear(),
                        toScan.build(),
                        Obelisk.AutoScan(),
                        new ParallelAction(
                                new SequentialAction(
                                        actionManager.shotCue(1),
                                        actionManager.spindexer.goToMotif(),
                                        actionManager.cycleRamp()
                                ),
                                toShoot1.build()
                        ),

                        waitVariable.build(),
                        actionManager.rev(3000),
//                        waitOne.build(),
                        actionManager.waitForSpeedSafe(3000),
                        actionManager.launch(),

                        actionManager.shotCue(2),
                        QuickSpindexer.turnRight(),
                        actionManager.waitForSpeedSafe(3000),
                        actionManager.launch(),

                        actionManager.shotCue(3),
                        QuickSpindexer.turnRight(),
                        actionManager.waitForSpeedSafe(3000),
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