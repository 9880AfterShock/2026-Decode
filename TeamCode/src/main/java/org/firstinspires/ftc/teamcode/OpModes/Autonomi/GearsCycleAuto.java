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
@Autonomous(name = "GEARS Far zone 12 no spikes")
public class GearsCycleAuto extends LinearOpMode {
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
        Turret.initTurret(this, false);
        TeleOp.afterAuto = true;

        QuickSpindexer.initSpindexer(this);
        Prongs.initGrate(this);
        TeleOp.autoHasBalls = true;

        double rpm = 3250;
        double rpmWaitOffset = -200;

        double posMultiplier = 1.0;
//        boolean firstDump = false;
//        boolean secondDump = false;
//        boolean thirdDump = false;
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
//            if (gamepad1.dpadUpWasPressed()){
//                firstDump =!firstDump;
//            }
//            if (gamepad1.dpadLeftWasPressed()){
//                secondDump =!secondDump;
//            }
//            if (gamepad1.dpadRightWasPressed()){
//                thirdDump =!thirdDump;
//            }
            if (posMultiplier == 1.0) {
                telemetry.addData("Alliance", "Blue");
                TeleOp.alliance = Alliance.BLUE;
            }
            if (posMultiplier == -1.0) {
                telemetry.addData("Alliance", "Red");
                TeleOp.alliance = Alliance.RED;
            }
//            telemetry.addData("First Dump", firstDump);
//            telemetry.addData("Second Dump", secondDump);
//            telemetry.addData("Third Dump", thirdDump);
            telemetry.update();
        }

        VelConstraint driveSpeed = new MinVelConstraint(Arrays.asList(
                new TranslationalVelConstraint(40.0),
                new AngularVelConstraint(Math.PI)
        ));
        VelConstraint rushSpeed = new MinVelConstraint(Arrays.asList(
                new TranslationalVelConstraint(50.0),
                new AngularVelConstraint(Math.PI)
        ));

//        double driveSpeed = 40.0;
//        double intakeSpeed = 15.0;

        VelConstraint wallIntakeSpeed = new MinVelConstraint(Arrays.asList(
                new TranslationalVelConstraint(20.0),
                new AngularVelConstraint(Math.PI/2)
        ));
        VelConstraint intakeSpeed = new MinVelConstraint(Arrays.asList(
                new TranslationalVelConstraint(15.0),
                new AngularVelConstraint(Math.PI/2)
        ));

        Pose2d startPosFar = new Pose2d(61.0, posMultiplier*-22.0, posMultiplier*Math.toRadians(-45.0));
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPosFar);

        //Poses
        Pose2d shootPosFar1;
        Pose2d shootPosFar2;
        Pose2d shootPosFar3;
        Pose2d shootPosFar4;
        Pose2d shootPosFar5;
        double turretAngle;
        if (posMultiplier == 1){
            shootPosFar1 = new Pose2d(61.0, posMultiplier*-22.0, posMultiplier*Math.toRadians(-45.0));
            shootPosFar2 = new Pose2d(60.0, posMultiplier*-22.0, posMultiplier*Math.toRadians(-43.0));
            shootPosFar3 = new Pose2d(60.0, posMultiplier*-22.0, posMultiplier*Math.toRadians(-43.0));
            shootPosFar4 = new Pose2d(60.0, posMultiplier*-21.0, posMultiplier*Math.toRadians(-41.0));
            shootPosFar5 = new Pose2d(60.0, posMultiplier*-22.0, posMultiplier*Math.toRadians(-37.0)); //not making here atm lol
            turretAngle = 59;
        } else {
            shootPosFar1 = new Pose2d(61.0, posMultiplier*-22.0, posMultiplier*Math.toRadians(-42.0));
            shootPosFar2 = new Pose2d(60.0, posMultiplier*-22.0, posMultiplier*Math.toRadians(-42.0));
            shootPosFar3 = new Pose2d(60.0, posMultiplier*-21.5, posMultiplier*Math.toRadians(-42.0));
            shootPosFar4 = new Pose2d(60.0, posMultiplier*-19.5, posMultiplier*Math.toRadians(-42.0));
            shootPosFar5 = new Pose2d(60.0, posMultiplier*-20.5, posMultiplier*Math.toRadians(-42.0));
            turretAngle = 62;
        }

        Pose2d prePickupFar = new Pose2d(36.0, posMultiplier*-30.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d startPickupFar = new Pose2d(36.0, posMultiplier*-37.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d endPickupFar = new Pose2d(36.0, posMultiplier*-56.0, posMultiplier*Math.toRadians(-90.0));

        Pose2d prePickupCorner;
        Pose2d startPickupCorner;
        Pose2d midPickupCorner;
        Pose2d endPickupCorner;
        if (posMultiplier == 1){
            prePickupCorner = new Pose2d(55.0, posMultiplier*-55.0, posMultiplier*Math.toRadians(-60.0));
            startPickupCorner = new Pose2d(55.0, posMultiplier*-61.0, posMultiplier*Math.toRadians(-60.0));
            midPickupCorner = new Pose2d(58.25, posMultiplier*-61.0, posMultiplier*Math.toRadians(-60.0));
            endPickupCorner = new Pose2d(62.5, posMultiplier*-62.0, posMultiplier*-Math.toRadians(10.0));
        } else{
            prePickupCorner = new Pose2d(55.0, posMultiplier*-56.0, posMultiplier*Math.toRadians(-60.0));
            startPickupCorner = new Pose2d(55.0, posMultiplier*-62.0, posMultiplier*Math.toRadians(-60.0));
            midPickupCorner = new Pose2d(58.25, posMultiplier*-62.0, posMultiplier*Math.toRadians(-60.0));
            endPickupCorner = new Pose2d(62.5, posMultiplier*-63.0, posMultiplier*-Math.toRadians(10.0));
        }

        Pose2d preSlamPos1 = new Pose2d(55.0, posMultiplier*-61.0, posMultiplier*Math.toRadians(-135.0));
        Pose2d slamPos1 = new Pose2d(35.0, posMultiplier*-61.0, posMultiplier*Math.toRadians(-135.0));
        Pose2d preSlamPos2 = new Pose2d(55.0, posMultiplier*-61.0, posMultiplier*Math.toRadians(-135.0));
        Pose2d slamPos2 = new Pose2d(35.0, posMultiplier*-61.0, posMultiplier*Math.toRadians(-135.0));

        Pose2d parkRotationFar = new Pose2d(38.5, posMultiplier*-26.0, posMultiplier*Math.toRadians(90));
        Pose2d parkPosFar = new Pose2d(13.5, posMultiplier*-34.0, posMultiplier*Math.toRadians(90.0));

//        TrajectoryActionBuilder toShoot1 = drive.actionBuilder(startPosNear)
//                .setTangent(posMultiplier*Math.toRadians(50))
//                .splineToLinearHeading(shootPosNear1, posMultiplier*Math.toRadians(50), driveSpeed);

//        TrajectoryActionBuilder toPickup1 = drive.actionBuilder(shootPosFar1)
//                .setTangent(posMultiplier*Math.toRadians(180.0))
//                .splineToLinearHeading(prePickupFar, posMultiplier*Math.toRadians(-90.0), driveSpeed)
//                .setTangent(posMultiplier*Math.toRadians(-90.0))
//                .splineToLinearHeading(startPickupFar, posMultiplier*Math.toRadians(-90.0), driveSpeed);
//
//        TrajectoryActionBuilder pickup1 = drive.actionBuilder(startPickupFar)
//                .setTangent(posMultiplier*Math.toRadians(-90))
//                .splineToLinearHeading(endPickupFar, posMultiplier*Math.toRadians(-90.0), intakeSpeed);

//        TrajectoryActionBuilder toShoot2 = drive.actionBuilder(endPickupFar)
//                .setTangent(posMultiplier*Math.toRadians(52.5))
//                .splineToLinearHeading(shootPosFar2, posMultiplier*Math.toRadians(52.5), rushSpeed);

        TrajectoryActionBuilder toPickup2 = drive.actionBuilder(shootPosFar2)
                .setTangent(posMultiplier*Math.toRadians(-100.0))
                .splineToLinearHeading(prePickupCorner, posMultiplier*Math.toRadians(-100.0), driveSpeed)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(startPickupCorner, posMultiplier*Math.toRadians(-90.0), driveSpeed);

        TrajectoryActionBuilder pickup2 = drive.actionBuilder(startPickupCorner)
                .setTangent(posMultiplier*Math.toRadians(0.0))
                .splineToLinearHeading(midPickupCorner, posMultiplier*Math.toRadians(0.0), intakeSpeed)
                .setTangent(posMultiplier*Math.toRadians(0.0))
                .splineToLinearHeading(endPickupCorner, posMultiplier*Math.toRadians(0.0), intakeSpeed);

        TrajectoryActionBuilder toShoot3 = drive.actionBuilder(endPickupCorner)
                .setTangent(posMultiplier*Math.toRadians(95.0))
                .splineToLinearHeading(shootPosFar3, posMultiplier*Math.toRadians(95.0), rushSpeed);

        TrajectoryActionBuilder slam1 = drive.actionBuilder(shootPosFar3)
                .setTangent(posMultiplier*Math.toRadians(-100.0))
                .splineToLinearHeading(preSlamPos1, posMultiplier*Math.toRadians(-100.0), driveSpeed)
                .setTangent(posMultiplier*Math.toRadians(180.0))
                .splineToLinearHeading(slamPos1, posMultiplier*Math.toRadians(180.0), intakeSpeed);

        TrajectoryActionBuilder back1 = drive.actionBuilder(slamPos1)
                .setTangent(posMultiplier*Math.toRadians(60.0))
                .splineToLinearHeading(shootPosFar4, posMultiplier*Math.toRadians(60.0), rushSpeed);

        TrajectoryActionBuilder slam2 = drive.actionBuilder(shootPosFar4)
                .setTangent(posMultiplier*Math.toRadians(-120.0))
                .splineToLinearHeading(preSlamPos2, posMultiplier*Math.toRadians(-120.0), driveSpeed)
                .setTangent(posMultiplier*Math.toRadians(-120.0))
                .splineToLinearHeading(slamPos2, posMultiplier*Math.toRadians(-120.0), driveSpeed);

        TrajectoryActionBuilder back2 = drive.actionBuilder(slamPos2)
                .setTangent(posMultiplier*Math.toRadians(60.0))
                .splineToLinearHeading(shootPosFar5, posMultiplier*Math.toRadians(60.0), rushSpeed);

        TrajectoryActionBuilder toPark = drive.actionBuilder(shootPosFar5)
                .setTangent(posMultiplier*Math.toRadians(195.0))
                .splineToLinearHeading(parkRotationFar, posMultiplier*Math.toRadians(195.0), driveSpeed)
                .setTangent(posMultiplier*Math.toRadians(200.0))
                .splineToLinearHeading(parkPosFar, posMultiplier*Math.toRadians(200.0), rushSpeed);



        TrajectoryActionBuilder waitPickup1 = drive.actionBuilder(endPickupFar)
                .waitSeconds(5.0);
        TrajectoryActionBuilder waitPickup2 = drive.actionBuilder(endPickupCorner)
                .waitSeconds(9.0);
        TrajectoryActionBuilder waitSlam1 = drive.actionBuilder(slamPos1)
                .waitSeconds(3.0);
        TrajectoryActionBuilder waitSlam2 = drive.actionBuilder(slamPos2)
                .waitSeconds(3.0);


        Gyroscope.setRotation(Math.toDegrees(startPosFar.heading.toDouble()));
        TeleOp.autoEndPosition = parkPosFar;

        double ballInSpindexerTimer = 0.0;


        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
                new RaceAction(
                        actionManager.updateSpeedOverTime(),
                        Turret.turretLoop(),
                        new SequentialAction(
                                Distance.setMissed(false),
                                Turret.setTurretTarget(posMultiplier*-turretAngle),
                                actionManager.shotCue(0),
                                Hood.AutoHoodFar(),
                                actionManager.rev(rpm),
                                new ParallelAction(
                                        new SequentialAction(
                                                Arm.AutoArmOut(),
                                                Prongs.AutoProngsShooting(),
                                                QuickSpindexer.addRevOffset(),
                                                actionManager.waitFor(0.3),
                                                Arm.AutoArmRev()
                                        ),
//                                        toShoot1.build()
                                        Turret.waitForTurret()
                                ),
                                actionManager.shotCue(1),
                                //First Volley
                                //actionManager.waitForSpeedSafe(rpm + rpmWaitOffset),
                                actionManager.startTripleRPMBoost(true),
                                QuickSpindexer.autoFullCycle(true),
                                actionManager.endTripleRPMBoost(true),
                                actionManager.hasBalls(false),
//                                actionManager.derev(),

//                                //First Pickup
//                                Arm.AutoArmOut(),
//                                Prongs.AutoProngsIntake(),
//                                Roller.AutoIntakeOn(),
//                                new RaceAction(
//                                        new SequentialAction(
//                                                toPickup1.build(),
//                                                pickup1.build(),
//                                                waitPickup1.build(),
//                                                Roller.AutoIntakeOff(),
//                                                Arm.AutoArmIn()
//                                        ),
//                                        new SequentialAction(
//                                                Distance.waitForBallInTimer(5.0),
//                                                Roller.AutoIntakeOff(),
//                                                Arm.AutoArmIn(),
//                                                Distance.waitForBallInSpindexer(),
//                                                actionManager.waitFor(ballInSpindexerTimer),
//                                                QuickSpindexer.turnLeftHalfTime(),
//                                                Roller.AutoIntakeOn(),
//                                                Arm.AutoArmOut(),
//                                                Distance.waitForBallIn(),
//                                                Roller.AutoIntakeOff(),
//                                                Arm.AutoArmIn(),
//                                                Distance.waitForBallInSpindexer(),
//                                                actionManager.waitFor(ballInSpindexerTimer),
//                                                QuickSpindexer.turnLeftHalfTime(),
//                                                Roller.AutoIntakeOn(),
//                                                Arm.AutoArmOut(),
//                                                Distance.waitForBallIn(),
//                                                Roller.AutoIntakeOff(),
//                                                Arm.AutoArmIn()
//                                        )
//                                ),
//
//                                //2nd Sort
//                                new ParallelAction(
//                                        actionManager.rev(rpm),
//                                        new SequentialAction(
//                                                Roller.AutoIntakeEject(),
//                                                Distance.waitForBallInSpindexer(),
////                                                actionManager.waitFor(1.0),
//                                                new SequentialAction(
//                                                        Arm.AutoArmOut(),
//                                                        Prongs.AutoProngsShooting(),
//                                                        QuickSpindexer.addRevOffset(),
//                                                        actionManager.waitFor(0.3),
//                                                        Arm.AutoArmRev(),
//                                                        Roller.AutoIntakeOff()
//                                                )
//                                        ),
//                                        toShoot2.build()
//                                ),
//
//                                //2nd volley
//                                //actionManager.waitForSpeedSafe(rpm + rpmWaitOffset),
//                                actionManager.startTripleRPMBoost(true),
//                                QuickSpindexer.autoFullCycle(true),
//                                actionManager.endTripleRPMBoost(true),
//                                Distance.setMissed(false),
//                                actionManager.hasBalls(false),

                                //2nd Pickup
                                Arm.AutoArmOut(),
                                Prongs.AutoProngsIntake(),
                                Roller.AutoIntakeOn(),
                                new RaceAction(
                                        new SequentialAction(
                                                toPickup2.build(),
                                                pickup2.build(),
                                                waitPickup2.build(),
                                                Roller.AutoIntakeOff(),
                                                Arm.AutoArmIn()
                                        ),
                                        new SequentialAction(
                                                Distance.waitForBallInTimer(7.0),
                                                Roller.AutoIntakeOff(),
                                                Arm.AutoArmIn(),
                                                Distance.waitForBallInSpindexer(),
                                                actionManager.waitFor(ballInSpindexerTimer),
                                                QuickSpindexer.turnLeftHalfTime(),
                                                Roller.AutoIntakeOn(),
                                                Arm.AutoArmOut(),
                                                Distance.waitForBallIn(),
                                                Roller.AutoIntakeOff(),
                                                Arm.AutoArmIn(),
                                                Distance.waitForBallInSpindexer(),
                                                actionManager.waitFor(ballInSpindexerTimer),
                                                QuickSpindexer.turnLeftHalfTime(),
                                                Roller.AutoIntakeOn(),
                                                Arm.AutoArmOut(),
                                                Distance.waitForBallIn(),
                                                Roller.AutoIntakeOff(),
                                                Arm.AutoArmIn()
                                        )
                                ),

                                //3rd Sort
                                new ParallelAction(
                                        actionManager.rev(rpm),
                                        new SequentialAction(
                                                Roller.AutoIntakeEject(),
                                                Distance.waitForBallInSpindexer(),
//                                                actionManager.waitFor(1.0),
                                                new SequentialAction(
                                                        Arm.AutoArmOut(),
                                                        Prongs.AutoProngsShooting(),
                                                        QuickSpindexer.addRevOffset(),
                                                        actionManager.waitFor(0.3),
                                                        Arm.AutoArmRev(),
                                                        Roller.AutoIntakeOff()
                                                )
                                        ),
                                        toShoot3.build()
                                ),

                                //Third Volley
                                //actionManager.waitForSpeedSafe(rpm + rpmWaitOffset),
                                actionManager.startTripleRPMBoost(true),
                                QuickSpindexer.autoFullCycle(true),
                                actionManager.endTripleRPMBoost(true),
                                actionManager.hasBalls(false),

                                //1st Slam
                                Arm.AutoArmOut(),
                                Prongs.AutoProngsIntake(),
                                Roller.AutoIntakeOn(),
                                new RaceAction(
                                        new SequentialAction(
                                                slam1.build(),
                                                waitSlam1.build(),
                                                Roller.AutoIntakeOff(),
                                                Arm.AutoArmIn()
                                        ),
                                        new SequentialAction(
                                                Distance.waitForBallInTimer(3.5),
                                                Roller.AutoIntakeOff(),
                                                Arm.AutoArmIn(),
                                                Distance.waitForBallInSpindexer(),
                                                actionManager.waitFor(ballInSpindexerTimer),
                                                QuickSpindexer.turnLeftHalfTime(),
                                                Roller.AutoIntakeOn(),
                                                Arm.AutoArmOut(),
                                                Distance.waitForBallIn(),
                                                Roller.AutoIntakeOff(),
                                                Arm.AutoArmIn(),
                                                Distance.waitForBallInSpindexer(),
                                                actionManager.waitFor(ballInSpindexerTimer),
                                                QuickSpindexer.turnLeftHalfTime(),
                                                Roller.AutoIntakeOn(),
                                                Arm.AutoArmOut(),
                                                Distance.waitForBallIn(),
                                                Roller.AutoIntakeOff(),
                                                Arm.AutoArmIn()
                                        )
                                ),

                                //1st back
                                new ParallelAction(
                                        actionManager.rev(rpm),
                                        new SequentialAction(
                                                Roller.AutoIntakeEject(),
                                                Distance.waitForBallInSpindexer(),
//                                                actionManager.waitFor(1.0),
                                                new SequentialAction(
                                                        Arm.AutoArmOut(),
                                                        Prongs.AutoProngsShooting(),
                                                        QuickSpindexer.addRevOffset(),
                                                        actionManager.waitFor(0.3),
                                                        Arm.AutoArmRev(),
                                                        Roller.AutoIntakeOff()
                                                )
                                        ),
                                        back1.build()
                                ),

                                //4th volley
                                //actionManager.waitForSpeedSafe(rpm + rpmWaitOffset),
                                actionManager.startTripleRPMBoost(true),
                                QuickSpindexer.autoFullCycle(true),
                                actionManager.endTripleRPMBoost(true),
                                actionManager.hasBalls(false),

                                //2nd Slam
                                Arm.AutoArmOut(),
                                Prongs.AutoProngsIntake(),
                                Roller.AutoIntakeOn(),
                                new RaceAction(
                                        new SequentialAction(
                                                slam2.build(),
                                                waitSlam2.build(),
                                                Roller.AutoIntakeOff(),
                                                Arm.AutoArmIn()
                                        ),
                                        new SequentialAction(
                                                Distance.waitForBallInTimer(3.5),
                                                Roller.AutoIntakeOff(),
                                                Arm.AutoArmIn(),
                                                Distance.waitForBallInSpindexer(),
                                                actionManager.waitFor(ballInSpindexerTimer),
                                                QuickSpindexer.turnLeftHalfTime(),
                                                Roller.AutoIntakeOn(),
                                                Arm.AutoArmOut(),
                                                Distance.waitForBallIn(),
                                                Roller.AutoIntakeOff(),
                                                Arm.AutoArmIn(),
                                                Distance.waitForBallInSpindexer(),
                                                actionManager.waitFor(ballInSpindexerTimer),
                                                QuickSpindexer.turnLeftHalfTime(),
                                                Roller.AutoIntakeOn(),
                                                Arm.AutoArmOut(),
                                                Distance.waitForBallIn(),
                                                Roller.AutoIntakeOff(),
                                                Arm.AutoArmIn()
                                        )
                                ),

                                //2nd back
                                new ParallelAction(
                                        actionManager.rev(rpm),
                                        new SequentialAction(
                                                Roller.AutoIntakeEject(),
                                                Distance.waitForBallInSpindexer(),
//                                                actionManager.waitFor(1.0),
                                                new SequentialAction(
                                                        Arm.AutoArmOut(),
                                                        Prongs.AutoProngsShooting(),
                                                        QuickSpindexer.addRevOffset(),
                                                        actionManager.waitFor(0.3),
                                                        Arm.AutoArmRev(),
                                                        Roller.AutoIntakeOff()
                                                )
                                        ),
                                        back2.build()
                                ),

                                //5th volley
                                //actionManager.waitForSpeedSafe(rpm + rpmWaitOffset),
                                actionManager.startTripleRPMBoost(true),
                                QuickSpindexer.autoFullCycle(true),
                                actionManager.endTripleRPMBoost(true),
                                actionManager.hasBalls(false),

                                //Ending
                                actionManager.derev(),
                                toPark.build(),

                                actionManager.waitFor(30.0)
                        )
                )
        );
    }
}