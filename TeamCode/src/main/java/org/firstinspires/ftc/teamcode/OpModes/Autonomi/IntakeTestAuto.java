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
@Autonomous(name = "INTAKE test auto")
public class IntakeTestAuto extends LinearOpMode {
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

        double posMultiplier = 1.0;

        Pose2d startPosNear = new Pose2d(-47.0, posMultiplier*-49.0, posMultiplier*Math.toRadians(0.0));
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPosNear);

        //Poses
        Pose2d shootPosNear4 = new Pose2d(-44.0, posMultiplier*-24.0, posMultiplier*Math.toRadians(0.0));
        Pose2d endPickupNear = new Pose2d(-10.0, posMultiplier*-50.0, posMultiplier*Math.toRadians(-90.0));

        TrajectoryActionBuilder waitPickup1 = drive.actionBuilder(endPickupNear)
                .waitSeconds(99999.0);


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
                                Turret.setTurretTarget(posMultiplier*-45.0),
                                actionManager.shotCue(0),
                                Hood.AutoHoodNear(),
                                actionManager.derev(),

                                //First Pickup
                                Arm.AutoArmOut(),
                                Prongs.AutoProngsIntake(),
                                Roller.AutoIntakeOn(),
//                                toPickup1.build(),
                                actionManager.hasBalls(true),
                                new RaceAction(
                                        new SequentialAction(
//                                                pickup1.build(),
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
//                                                actionManager.waitFor(1.0),
                                                new SequentialAction(
                                                        Arm.AutoArmOut(),
                                                        Prongs.AutoProngsShooting(),
                                                        QuickSpindexer.addRevOffset(),
                                                        actionManager.waitFor(0.5),
                                                        Arm.AutoArmRev()
                                                )
//                                        ),
//                                        toShoot2.build()
                                ),
                                actionManager.waitFor(30.0)
                        )
                )
        ));
    }
}