package org.firstinspires.ftc.teamcode.OpModes.Autonomi;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.ftc.*;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Aiming.DriverTest;
import org.firstinspires.ftc.teamcode.Enums.Motif;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Arm;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Roller;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Shield;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Hood;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.QuickSpindexer;
import org.firstinspires.ftc.teamcode.Sensors.Obelisk;
import org.firstinspires.ftc.teamcode.Systems.ActionManager;
import org.firstinspires.ftc.teamcode.Systems.RunLater;

@Config
@Autonomous(name = "Near zone with new transfer yay! :D")
public class LM2AutoNear extends LinearOpMode {
    @Override
    public void runOpMode() {
        //Mechs init
        Arm.initIntake(this);
        Roller.initIntake(this);
        RunLater.setup(this);
        Obelisk.initDetection(this);
        DriverTest.initControls(this); //new
        Hood.initAim(this);
        ActionManager actionManager = new ActionManager( this, 28);

        QuickSpindexer.initSpindexer(this); //ugly but works
        Shield.initLocking(this);

        double rpm = 3000;

        double posMultiplier = 1.0;
        double waitTime = 1.0;
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
            if (waitTime < 0.0){
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
        Pose2d firstPickup1 = new Pose2d(-12.0, posMultiplier*-32.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d secondPickup1 = new Pose2d(-12.0, posMultiplier*-36.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d endPickup1 = new Pose2d(-12.0, posMultiplier*-45.0, posMultiplier*-Math.toRadians(90.0));

        Pose2d startPickup2 = new Pose2d(12.0, posMultiplier*-30.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d firstPickup2 = new Pose2d(12.0, posMultiplier*-32.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d secondPickup2 = new Pose2d(12.0, posMultiplier*-36.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d endPickup2 = new Pose2d(12.0, posMultiplier*-45.0, posMultiplier*-Math.toRadians(90.0));

        Pose2d startPickup3 = new Pose2d(35.5, posMultiplier*-30.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d firstPickup3 = new Pose2d(35.5, posMultiplier*-32.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d secondPickup3 = new Pose2d(35.5, posMultiplier*-36.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d endPickup3 = new Pose2d(35.5, posMultiplier*-45.0, posMultiplier*-Math.toRadians(90.0));

        Pose2d gatePose = new Pose2d(0.0, posMultiplier*-55.0, posMultiplier*Math.toRadians(0.0));
        Pose2d parkPosFar = new Pose2d(37.75, posMultiplier*-32.75, posMultiplier*Math.toRadians(90.0));

        TrajectoryActionBuilder toScan = drive.actionBuilder(startPosClose)
                .setTangent(posMultiplier*Math.toRadians(55.0))
                .splineToLinearHeading(scanPos, posMultiplier*Math.toRadians(45.0));

        TrajectoryActionBuilder toShoot1 = drive.actionBuilder(scanPos)
                .setTangent(posMultiplier*Math.toRadians(-125.0))
                .splineToLinearHeading(shootPosClose, posMultiplier*Math.toRadians(-125.0));

        TrajectoryActionBuilder toPickup1 = drive.actionBuilder(shootPosClose)
                .setTangent(posMultiplier*Math.toRadians(45.0))
                .splineToLinearHeading(startPickup1, posMultiplier*Math.toRadians(-45.0));
        TrajectoryActionBuilder pickupFirst1 = drive.actionBuilder(startPickup1)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(firstPickup1, posMultiplier*Math.toRadians(-90.0));
        TrajectoryActionBuilder pickupSecond1 = drive.actionBuilder(firstPickup1)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(secondPickup1, posMultiplier*Math.toRadians(-90.0));
        TrajectoryActionBuilder pickupThird1 = drive.actionBuilder(secondPickup1)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(endPickup1, posMultiplier*Math.toRadians(-90.0));
        //need color sensor/proximity sensor/ distance sensor for this version:
//        TrajectoryActionBuilder pickupFirstRow = drive.actionBuilder(startPickup1)
//                .setTangent(posMultiplier*Math.toRadians(-90.0))
//                .splineToLinearHeading(endPickup1, posMultiplier*Math.toRadians(-90.0), new TranslationalVelConstraint(10.0));

        TrajectoryActionBuilder toShoot2 = drive.actionBuilder(endPickup1)
                .setTangent(posMultiplier*Math.toRadians(125.0))
                .splineToLinearHeading(shootPosClose, posMultiplier*Math.toRadians(125.0));
/*
        TrajectoryActionBuilder toPickup2 = drive.actionBuilder(shootPosClose)
                .setTangent(posMultiplier*Math.toRadians(30.0))
                .splineToLinearHeading(startPickup2, posMultiplier*Math.toRadians(-30.0));
        TrajectoryActionBuilder pickupFirst2 = drive.actionBuilder(startPickup2)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(firstPickup2, posMultiplier*Math.toRadians(-90.0));
        TrajectoryActionBuilder pickupSecond2 = drive.actionBuilder(firstPickup2)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(secondPickup2, posMultiplier*Math.toRadians(-90.0));
        TrajectoryActionBuilder pickupThird2 = drive.actionBuilder(secondPickup2)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(endPickup2, posMultiplier*Math.toRadians(-90.0));

        TrajectoryActionBuilder toShoot3 = drive.actionBuilder(endPickup2)
                .setTangent(posMultiplier*Math.toRadians(145.0))
                .splineToLinearHeading(shootPosClose, posMultiplier*Math.toRadians(145.0));

        TrajectoryActionBuilder toPickup3 = drive.actionBuilder(shootPosClose)
                .setTangent(posMultiplier*Math.toRadians(20.0))
                .splineToLinearHeading(startPickup3, posMultiplier*Math.toRadians(-20.0));
        TrajectoryActionBuilder pickupFirst3 = drive.actionBuilder(startPickup3)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(firstPickup3, posMultiplier*Math.toRadians(-90.0));
        TrajectoryActionBuilder pickupSecond3 = drive.actionBuilder(firstPickup3)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(secondPickup3, posMultiplier*Math.toRadians(-90.0));
        TrajectoryActionBuilder pickupThird3 = drive.actionBuilder(secondPickup3)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(endPickup3, posMultiplier*Math.toRadians(-90.0));

        TrajectoryActionBuilder toShoot4 = drive.actionBuilder(endPickup3)
                .setTangent(posMultiplier*Math.toRadians(145.0))
                .splineToLinearHeading(shootPosClose, posMultiplier*Math.toRadians(145.0));
        */

        TrajectoryActionBuilder toPark = drive.actionBuilder(shootPosClose)
                .setTangent(posMultiplier*Math.toRadians(180.0))
                .splineToLinearHeading(parkPosClose, posMultiplier*Math.toRadians(-180.0));

        TrajectoryActionBuilder waitVariable = drive.actionBuilder(startPosClose)
                .waitSeconds(waitTime);

        TrajectoryActionBuilder waitBallIn1 = drive.actionBuilder(startPosClose)
                .waitSeconds(0.2);
        TrajectoryActionBuilder waitBallIn2 = drive.actionBuilder(startPosClose)
                .waitSeconds(0.3);
        TrajectoryActionBuilder waitBallIn3 = drive.actionBuilder(startPosClose)
                .waitSeconds(0.3);

        TrajectoryActionBuilder waitServoDown = drive.actionBuilder(startPosClose)
                .waitSeconds(0.2);

        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        actionManager.shotCue(0),
                        Shield.AutoShieldShoot(),
                        Arm.AutoArmIn(),
                        Hood.AutoHoodNear(),
                        actionManager.rev(rpm), //moved here bc PID
                        toScan.build(),
                        Obelisk.AutoScan(),
                        new ParallelAction(
                                QuickSpindexer.toMotifFrom(Motif.GPP),
                                toShoot1.build()
                        ),

                        waitVariable.build(),

                        //First volley start
                        actionManager.rev(3000),

                        actionManager.shotCue(1),
                        actionManager.waitForSpeedSafe(rpm),
                        Arm.AutoLaunchStart(),
                        actionManager.waitFor(0.4),
                        Arm.AutoLaunchEnd(),

                        actionManager.shotCue(2),
                        QuickSpindexer.turnRight(),
                        actionManager.waitForSpeedSafe(rpm),
                        Arm.AutoLaunchStart(),
                        actionManager.waitFor(0.4),
                        Arm.AutoLaunchEnd(),

                        actionManager.shotCue(3),
                        QuickSpindexer.turnRight(),
                        actionManager.waitForSpeedSafe(rpm),
                        Arm.AutoLaunchStart(),
                        actionManager.waitFor(0.4),
                        Arm.AutoLaunchEnd(),

                        actionManager.derev(),
                        //First volley end


                        //First pickup start
                        Arm.AutoArmOut(),
                        Shield.AutoShieldLock(),
                        Roller.AutoIntakeOn(),

                        toPickup1.build(),
                        //intake is turned on earlier to be safe
                        pickupFirst1.build(),
                        waitBallIn1.build(),
                        Arm.AutoArmInWait(),
                        Roller.AutoIntakeOff(),
                        QuickSpindexer.turnLeft(),

                        Arm.AutoArmOut(),
                        Roller.AutoIntakeOn(),
                        pickupSecond1.build(),
                        waitBallIn2.build(),
                        Arm.AutoArmInWait(),
                        Roller.AutoIntakeOff(),
                        QuickSpindexer.turnLeft(),

                        Arm.AutoArmOut(),
                        Roller.AutoIntakeOn(),
                        pickupThird1.build(),
                        waitBallIn3.build(),
                        Arm.AutoArmInWait(),
                        Roller.AutoIntakeOff(),
                        //First Pickup End

                        //Sort
                        new ParallelAction(
                                Shield.AutoShieldShoot(),
                                QuickSpindexer.toMotifFrom(Motif.GPP),
                                toShoot2.build()
                        ),

                        //Second volley start
                        actionManager.rev(rpm),

                        actionManager.shotCue(4),
                        actionManager.waitForSpeedSafe(rpm),
                        Arm.AutoLaunchStart(),
                        actionManager.waitFor(0.4),
                        Arm.AutoLaunchEnd(),

                        actionManager.shotCue(5),
                        QuickSpindexer.turnRight(),
                        actionManager.waitForSpeedSafe(rpm),
                        Arm.AutoLaunchStart(),
                        actionManager.waitFor(0.4),
                        Arm.AutoLaunchEnd(),

                        actionManager.shotCue(6),
                        QuickSpindexer.turnRight(),
                        actionManager.waitForSpeedSafe(rpm),
                        Arm.AutoLaunchStart(),
                        actionManager.waitFor(0.4),
                        Arm.AutoLaunchEnd(),

                        actionManager.derev(),
                        //Second volley end e

                        QuickSpindexer.resetForTele(), //should change later
                        toPark.build()
                )
        );
    }
}