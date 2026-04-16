package org.firstinspires.ftc.teamcode.OpModes.Autonomi;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.AngularVelConstraint;
import com.acmerobotics.roadrunner.MinVelConstraint;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.RaceAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.VelConstraint;
import com.acmerobotics.roadrunner.ftc.*;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Aiming.DriverTest;
import org.firstinspires.ftc.teamcode.Enums.Alliance;
import org.firstinspires.ftc.teamcode.Enums.Motif;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Arm;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Roller;
//import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Shield;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Hood;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Turret;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.Prongs;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.QuickSpindexer;
import org.firstinspires.ftc.teamcode.OpModes.TeleOp;
import org.firstinspires.ftc.teamcode.Sensors.Distance;
import org.firstinspires.ftc.teamcode.Sensors.Gyroscope;
import org.firstinspires.ftc.teamcode.Sensors.Limelight;
import org.firstinspires.ftc.teamcode.Sensors.SensOrange;
import org.firstinspires.ftc.teamcode.Systems.ActionManager;
import org.firstinspires.ftc.teamcode.Systems.RunLater;

import java.util.Arrays;

@Config
@Autonomous(name = "GEARS Near zone 9")
public class GearsNearAuto extends LinearOpMode {
    @Override
    public void runOpMode() {
        SensOrange.initSensor(this);
        Gyroscope.initSensor(this);
        Limelight.initDetection(this);
        //Mechs init
        Arm.initIntake(this);
        Roller.initIntake(this);
        RunLater.setup(this);
        DriverTest.initControls(this);
        Hood.initAim(this);
        ActionManager actionManager = new ActionManager( this, 28);
        Distance.initSensor(this);
        Turret.initTurret(this);

        QuickSpindexer.initSpindexer(this);
        Prongs.initGrate(this);
        TeleOp.autoHasBalls = true;

        double rpm = 2200;
        double dumpTime = 1.0;

        double posMultiplier = 1.0;
        boolean firstDump = false;
        boolean secondDump = false;
        boolean thirdDump = false;
//        double waitTime = 0.0;
        while (!isStopRequested() && !opModeIsActive()) {
            telemetry.addLine("Use x and b to select alliance");
            telemetry.addLine("Use DPAD (up left and right) to toggle dumps");
            if (gamepad1.xWasPressed()){
                posMultiplier = 1.0;
            }
            if (gamepad1.bWasPressed()){
                posMultiplier = -1.0;
            }
            if (gamepad1.dpadUpWasPressed()){
                firstDump =!firstDump;
            }
            if (gamepad1.dpadLeftWasPressed()){
                secondDump =!secondDump;
            }
            if (gamepad1.dpadRightWasPressed()){
                thirdDump =!thirdDump;
            }
            if (posMultiplier == 1.0) {
                telemetry.addData("Alliance", "Blue");
                TeleOp.alliance = Alliance.BLUE;
            }
            if (posMultiplier == -1.0) {
                telemetry.addData("Alliance", "Red");
                TeleOp.alliance = Alliance.RED;
            }
            telemetry.addData("First Dump", firstDump);
            telemetry.addData("Second Dump", secondDump);
            telemetry.addData("Third Dump", thirdDump);
            telemetry.update();
        }

        VelConstraint driveSpeed = new MinVelConstraint(Arrays.asList(
                new TranslationalVelConstraint(120.0),
                new AngularVelConstraint(Math.PI)
        ));

        VelConstraint intakeSpeed = new MinVelConstraint(Arrays.asList(
                new TranslationalVelConstraint(15.0),
                new AngularVelConstraint(Math.PI/2)
        ));

        Pose2d startPosNear = new Pose2d(-47.0, posMultiplier*-49.0, posMultiplier*Math.toRadians(0.0));
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPosNear);

        //Poses
        Pose2d shootPosNear1 = new Pose2d(-24.0, posMultiplier*-24.0, posMultiplier*Math.toRadians(0.0));
        Pose2d shootPosNear2 = new Pose2d(-24.0, posMultiplier*-24.0, posMultiplier*Math.toRadians(0.0));
        Pose2d shootPosNear3 = new Pose2d(-24.0, posMultiplier*-24.0, posMultiplier*Math.toRadians(0.0));
        Pose2d shootPosNear4 = new Pose2d(-44.0, posMultiplier*-24.0, posMultiplier*Math.toRadians(0.0));

        Pose2d prePickupNear = new Pose2d(-12.0, posMultiplier*-30.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d startPickupNear = new Pose2d(-12.0, posMultiplier*-36.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d endPickupNear = new Pose2d(-12.0, posMultiplier*-55.0, posMultiplier*Math.toRadians(-90.0));

        Pose2d prePickupMiddle = new Pose2d(12.0, posMultiplier*-30.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d startPickupMiddle = new Pose2d(12.0, posMultiplier*-36.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d endPickupMiddle = new Pose2d(12.0, posMultiplier*-50.0, posMultiplier*Math.toRadians(-90.0));

        Pose2d prePickupFar = new Pose2d(36.0, posMultiplier*-30.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d startPickupFar = new Pose2d(36.0, posMultiplier*-36.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d endPickupFar = new Pose2d(36.0, posMultiplier*-50.0, posMultiplier*Math.toRadians(-90.0));

        Pose2d gatePosNear1 = new Pose2d(-3.0, posMultiplier*-55.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d gatePosNear2 = new Pose2d(7.0, posMultiplier*-55.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d gatePosNear3 = new Pose2d(7.0, posMultiplier*-55.0, posMultiplier*Math.toRadians(-90.0));

        TrajectoryActionBuilder toShoot1 = drive.actionBuilder(startPosNear)
                .setTangent(posMultiplier*Math.toRadians(50))
                .splineToLinearHeading(shootPosNear1, posMultiplier*Math.toRadians(50), driveSpeed);

        TrajectoryActionBuilder toPickup1 = drive.actionBuilder(shootPosNear1)
                .setTangent(posMultiplier*Math.toRadians(0.0))
                .splineToLinearHeading(prePickupNear, posMultiplier*Math.toRadians(-90.0), driveSpeed)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(startPickupNear, posMultiplier*Math.toRadians(-90.0), driveSpeed);

        TrajectoryActionBuilder pickup1 = drive.actionBuilder(startPickupNear)
                .setTangent(posMultiplier*Math.toRadians(-90))
                .splineToLinearHeading(endPickupNear, posMultiplier*Math.toRadians(-90.0), intakeSpeed);

        TrajectoryActionBuilder toShoot2;
        if (firstDump) {
            toShoot2 = drive.actionBuilder(endPickupNear)
                    .setTangent(posMultiplier*Math.toRadians(90.0))
                    .splineToLinearHeading(gatePosNear1, posMultiplier*Math.toRadians(-90.0), driveSpeed)
                    .waitSeconds(dumpTime)
                    .setTangent(posMultiplier*Math.toRadians(125.0))
                    .splineToLinearHeading(shootPosNear2, posMultiplier*Math.toRadians(125.0), driveSpeed);
        } else{
            toShoot2 = drive.actionBuilder(endPickupNear)
                .setTangent(posMultiplier*Math.toRadians(110.0))
                .splineToLinearHeading(shootPosNear2, posMultiplier*Math.toRadians(110.0), driveSpeed);
        }

        TrajectoryActionBuilder toPickup2 = drive.actionBuilder(shootPosNear2)
                .setTangent(posMultiplier*Math.toRadians(0.0))
                .splineToLinearHeading(prePickupMiddle, posMultiplier*Math.toRadians(-30.0), driveSpeed)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(startPickupMiddle, posMultiplier*Math.toRadians(-90.0), driveSpeed);

        TrajectoryActionBuilder pickup2 = drive.actionBuilder(startPickupMiddle)
                .setTangent(posMultiplier*Math.toRadians(-90))
                .splineToLinearHeading(endPickupMiddle, posMultiplier*Math.toRadians(-90.0), intakeSpeed);

        TrajectoryActionBuilder toShoot3;
        if (secondDump) {
            toShoot3 = drive.actionBuilder(endPickupMiddle)
                    .setTangent(posMultiplier*Math.toRadians(-180.0))
                    .splineToLinearHeading(gatePosNear2, posMultiplier*Math.toRadians(-90.0), driveSpeed)
                    .waitSeconds(1.0)
                    .setTangent(posMultiplier*Math.toRadians(135.0))
                    .splineToLinearHeading(shootPosNear3, posMultiplier*Math.toRadians(135.0), driveSpeed);
        } else{
            toShoot3 = drive.actionBuilder(endPickupMiddle)
                    .setTangent(posMultiplier*Math.toRadians(140.0))
                    .splineToLinearHeading(shootPosNear3, posMultiplier*Math.toRadians(140.0), driveSpeed);
        }

        TrajectoryActionBuilder toPickup3 = drive.actionBuilder(shootPosNear3)
                .setTangent(posMultiplier*Math.toRadians(0.0))
                .splineToLinearHeading(prePickupFar, posMultiplier*Math.toRadians(-15.0), driveSpeed)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(startPickupFar, posMultiplier*Math.toRadians(-90.0), driveSpeed);

        TrajectoryActionBuilder pickup3 = drive.actionBuilder(startPickupFar)
                .setTangent(posMultiplier*Math.toRadians(-90))
                .splineToLinearHeading(endPickupFar, posMultiplier*Math.toRadians(-90.0), intakeSpeed);

        TrajectoryActionBuilder toShoot4;
        if (thirdDump) {
            toShoot4 = drive.actionBuilder(endPickupFar)
                    .setTangent(posMultiplier*Math.toRadians(180.0))
                    .splineToLinearHeading(gatePosNear3, posMultiplier*Math.toRadians(-90.0), driveSpeed)
                    .waitSeconds(1.0)
                    .setTangent(posMultiplier*Math.toRadians(145.0))
                    .splineToLinearHeading(shootPosNear4, posMultiplier*Math.toRadians(145.0), driveSpeed);
        } else{
            toShoot4 = drive.actionBuilder(endPickupFar)
                .setTangent(posMultiplier*Math.toRadians(160.0))
                .splineToLinearHeading(shootPosNear4, posMultiplier*Math.toRadians(160.0), driveSpeed);
        }

        TrajectoryActionBuilder waitPickup1 = drive.actionBuilder(endPickupNear)
                .waitSeconds(5.0);
        TrajectoryActionBuilder waitPickup2 = drive.actionBuilder(endPickupMiddle)
                .waitSeconds(5.0);
        TrajectoryActionBuilder waitPickup3 = drive.actionBuilder(endPickupFar)
                .waitSeconds(5.0);


        Gyroscope.setRotation(Math.toDegrees(startPosNear.heading.toDouble()));
        TeleOp.autoEndPosition = shootPosNear4;

        double ballInSpindexerTimer = 0.4;


        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
                new RaceAction(
                        actionManager.updateSpeedOverTime(),
                        Turret.turretLoop(),
                        new SequentialAction(
                                Distance.setMissed(false),
                                Turret.setTurretTarget(posMultiplier*45.0),
                                actionManager.shotCue(0),
                                Hood.AutoHoodNear(),
                                actionManager.rev(rpm),
                                new ParallelAction(
                                        new SequentialAction(
                                                Arm.AutoArmOut(),
                                                Prongs.AutoProngsShooting(),
                                                QuickSpindexer.addRevOffset(),
                                                actionManager.waitFor(0.5),
                                                Arm.AutoArmRev()
                                        ),
                                        toShoot1.build()
                                ),

                                //First Volley
                                actionManager.waitForSpeedSafe(rpm),
                                actionManager.startTripleRPMBoost(false),
                                QuickSpindexer.autoFullCycle(true),
                                actionManager.endTripleRPMBoost(false),
                                actionManager.hasBalls(false),
                                actionManager.derev(),

                                //First Pickup
                                Arm.AutoArmOut(),
                                Prongs.AutoProngsIntake(),
                                Roller.AutoIntakeOn(),
                                toPickup1.build(),
                                actionManager.hasBalls(true),
                                new RaceAction(
                                        new SequentialAction(
                                                pickup1.build(),
                                                waitPickup1.build()
                                        ),
                                        new SequentialAction(
                                                Distance.waitForBallIn(),
                                                Roller.AutoIntakeOff(),
                                                Arm.AutoArmIn(),
                                                Distance.waitForBallInSpindexer(),
                                                actionManager.waitFor(ballInSpindexerTimer),
                                                QuickSpindexer.turnLeft(),
                                                Roller.AutoIntakeOn(),
                                                Arm.AutoArmOut(),
                                                Distance.waitForBallIn(),
                                                Roller.AutoIntakeOff(),
                                                Arm.AutoArmIn(),
                                                Distance.waitForBallInSpindexer(),
                                                actionManager.waitFor(ballInSpindexerTimer),
                                                QuickSpindexer.turnLeft(),
                                                Roller.AutoIntakeOn(),
                                                Arm.AutoArmOut(),
                                                Distance.waitForBallIn(),
                                                Roller.AutoIntakeOff(),
                                                Arm.AutoArmIn()
                                        )
                                ),

                                //2nd Sort
                                new ParallelAction(
                                        actionManager.rev(rpm),
                                        new SequentialAction(
                                                Distance.waitForBallInSpindexer(),
                                                actionManager.waitFor(1.0),
                                                new SequentialAction(
                                                        Arm.AutoArmOut(),
                                                        Prongs.AutoProngsShooting(),
                                                        QuickSpindexer.addRevOffset(),
                                                        actionManager.waitFor(0.5),
                                                        Arm.AutoArmRev()
                                                )
                                        ),
                                        toShoot2.build()
                                ),

                                //2nd volley
                                actionManager.waitForSpeedSafe(rpm),
                                actionManager.startTripleRPMBoost(false),
                                QuickSpindexer.autoFullCycle(true),
                                actionManager.endTripleRPMBoost(false),
                                Distance.setMissed(false),
                                actionManager.hasBalls(false),

                                //2nd Pickup
                                Arm.AutoArmOut(),
                                Prongs.AutoProngsIntake(),
                                Roller.AutoIntakeOn(),
                                toPickup2.build(),
                                actionManager.hasBalls(true),
                                new RaceAction(
                                        new SequentialAction(
                                                pickup2.build(),
                                                waitPickup2.build()
                                        ),
                                        new SequentialAction(
                                                Distance.waitForBallIn(),
                                                Roller.AutoIntakeOff(),
                                                Arm.AutoArmIn(),
                                                Distance.waitForBallInSpindexer(),
                                                actionManager.waitFor(ballInSpindexerTimer),
                                                QuickSpindexer.turnLeft(),
                                                Roller.AutoIntakeOn(),
                                                Arm.AutoArmOut(),
                                                Distance.waitForBallIn(),
                                                Roller.AutoIntakeOff(),
                                                Arm.AutoArmIn(),
                                                Distance.waitForBallInSpindexer(),
                                                actionManager.waitFor(ballInSpindexerTimer),
                                                QuickSpindexer.turnLeft(),
                                                Roller.AutoIntakeOn(),
                                                Arm.AutoArmOut(),
                                                Distance.waitForBallIn(),
                                                Roller.AutoIntakeOff(),
                                                Arm.AutoArmIn()
                                        )
                                ),

                                //2nd Sort
                                new ParallelAction(
                                        actionManager.rev(rpm),
                                        new SequentialAction(
                                                Distance.waitForBallInSpindexer(),
                                                actionManager.waitFor(1.0),
                                                new SequentialAction(
                                                        Arm.AutoArmOut(),
                                                        Prongs.AutoProngsShooting(),
                                                        QuickSpindexer.addRevOffset(),
                                                        actionManager.waitFor(0.5),
                                                        Arm.AutoArmRev()
                                                )
                                        ),
                                        toShoot3.build()
                                ),

                                //Third Volley
                                actionManager.waitForSpeedSafe(rpm),
                                actionManager.startTripleRPMBoost(false),
                                QuickSpindexer.autoFullCycle(true),
                                actionManager.endTripleRPMBoost(false),
                                actionManager.hasBalls(false),

                                //Ending
                                actionManager.derev(),
                                actionManager.waitFor(30.0)
                        )
                )
        );
    }
}