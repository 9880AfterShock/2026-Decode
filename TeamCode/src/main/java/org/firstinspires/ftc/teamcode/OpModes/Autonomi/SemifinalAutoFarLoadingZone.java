package org.firstinspires.ftc.teamcode.OpModes.Autonomi;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.RaceAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.ftc.*;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Aiming.DriverTest;
import org.firstinspires.ftc.teamcode.Enums.Alliance;
import org.firstinspires.ftc.teamcode.Enums.Motif;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Arm;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Roller;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Shield;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Hood;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.QuickSpindexer;
import org.firstinspires.ftc.teamcode.OpModes.TeleOp;
import org.firstinspires.ftc.teamcode.Sensors.Distance;
import org.firstinspires.ftc.teamcode.Sensors.Gyroscope;
import org.firstinspires.ftc.teamcode.Sensors.Limelight;
import org.firstinspires.ftc.teamcode.Systems.ActionManager;
import org.firstinspires.ftc.teamcode.Systems.RunLater;

@Config
@Autonomous(name = "Far zone 9 (Uses human player)")
public class SemifinalAutoFarLoadingZone extends LinearOpMode {
    @Override
    public void runOpMode() {
        Gyroscope.initSensor(this);
        Limelight.initDetection(this);
        //Mechs init
        Arm.initIntake(this);
        Roller.initIntake(this);
        RunLater.setup(this);
        DriverTest.initControls(this); //new
        Hood.initAim(this);
        ActionManager actionManager = new ActionManager( this, 28);
        Distance.initSensor(this);

        QuickSpindexer.initSpindexer(this); //ugly but works
        Shield.initLocking(this);

        double rpm = 3300;
        double shotCooldown = 0.2+0.2; // 0.2 + actual cooldown

        double posMultiplier = 1.0;
//        double waitTime = 0.0;
        while (!isStopRequested() && !opModeIsActive()) {
            telemetry.addLine("Use x and b to select alliance");
            if (gamepad1.xWasPressed()){
                posMultiplier = 1.0;
            }
            if (gamepad1.bWasPressed()){
                posMultiplier = -1.0;
            }
            if (posMultiplier == 1.0) {
                telemetry.addData("Alliance", "Blue");
                TeleOp.alliance = Alliance.BLUE;
            }
            if (posMultiplier == -1.0) {
                telemetry.addData("Alliance", "Red");
                TeleOp.alliance = Alliance.RED;
            }
            telemetry.update();
        }

        Pose2d startPosFar = new Pose2d(62.6, posMultiplier*-16.0, posMultiplier*Math.toRadians(0.0));
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPosFar);

        //Poses
        Pose2d shootPosFar1 = new Pose2d(54.5, posMultiplier*-13.0, posMultiplier*Math.toRadians(22.5));
        Pose2d shootPosFar2 = new Pose2d(54.5, posMultiplier*-15.0, posMultiplier*Math.toRadians(22.5));
        Pose2d shootPosFar3 = new Pose2d(56.0, posMultiplier*-17.0, posMultiplier*Math.toRadians(22.5));

        Pose2d prePickup1 = new Pose2d(36.0, posMultiplier*-26.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d startPickup1 = new Pose2d(36.0, posMultiplier*-35.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d endPickup1 = new Pose2d(36.0, posMultiplier*-50.0, posMultiplier*-Math.toRadians(90.0));

        Pose2d prePickup2 = new Pose2d(55.0, posMultiplier*-55.0, posMultiplier*Math.toRadians(-60.0));
        Pose2d startPickup2 = new Pose2d(55.0, posMultiplier*-62.0, posMultiplier*Math.toRadians(-60.0));
        Pose2d midPickup2 = new Pose2d(58.25, posMultiplier*-62.0, posMultiplier*Math.toRadians(-60.0));
        Pose2d endPickup2 = new Pose2d(62.5, posMultiplier*-62.0, posMultiplier*-Math.toRadians(10.0));

        Pose2d parkPosFar = new Pose2d(15.0, posMultiplier*-34.0, posMultiplier*Math.toRadians(90));

        TrajectoryActionBuilder toShoot1 = drive.actionBuilder(startPosFar)
                .setTangent(posMultiplier*Math.toRadians(140.0))
                .splineToLinearHeading(shootPosFar1, posMultiplier*Math.toRadians(140.0));

        TrajectoryActionBuilder toPickup1 = drive.actionBuilder(shootPosFar1)
                .setTangent(posMultiplier*Math.toRadians(215.0))
                .splineToLinearHeading(prePickup1, posMultiplier*Math.toRadians(215.0))
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(startPickup1, posMultiplier*Math.toRadians(-90.0));

        TrajectoryActionBuilder pickup1 = drive.actionBuilder(startPickup1)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(endPickup1, posMultiplier*Math.toRadians(-90.0), new TranslationalVelConstraint(5.0));

        TrajectoryActionBuilder toShoot2 = drive.actionBuilder(endPickup1)
                .setTangent(posMultiplier*Math.toRadians(65.0))
                .splineToLinearHeading(shootPosFar2, posMultiplier*Math.toRadians(65.0));

        TrajectoryActionBuilder toPickup2 = drive.actionBuilder(shootPosFar2)
                .setTangent(posMultiplier*Math.toRadians(-100.0))
                .splineToLinearHeading(prePickup2, posMultiplier*Math.toRadians(-100.0))
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(startPickup2, posMultiplier*Math.toRadians(-90.0));

        TrajectoryActionBuilder pickup2 = drive.actionBuilder(startPickup2)
                .setTangent(posMultiplier*Math.toRadians(0.0))
                .splineToLinearHeading(midPickup2, posMultiplier*Math.toRadians(0.0), new TranslationalVelConstraint(5.0))
                .setTangent(posMultiplier*Math.toRadians(0.0))
                .splineToLinearHeading(endPickup2, posMultiplier*Math.toRadians(0.0), new TranslationalVelConstraint(5.0));

        TrajectoryActionBuilder toShoot3 = drive.actionBuilder(endPickup2)
                .setTangent(posMultiplier*Math.toRadians(100.0))
                .splineToLinearHeading(shootPosFar3, posMultiplier*Math.toRadians(100.0));

        TrajectoryActionBuilder toPark = drive.actionBuilder(shootPosFar3)
                .setTangent(posMultiplier*Math.toRadians(202.5))
                .splineToLinearHeading(parkPosFar, posMultiplier*Math.toRadians(202.5), new TranslationalVelConstraint(100.0));

        TrajectoryActionBuilder waitPickup1 = drive.actionBuilder(endPickup1)
                .waitSeconds(5.0);
        TrajectoryActionBuilder waitPickup2 = drive.actionBuilder(endPickup2)
                .waitSeconds(5.0);


        Gyroscope.setRotation(Math.toDegrees(startPosFar.heading.toDouble()));
        TeleOp.autoEndPosition = parkPosFar;

        double ballInSpindexerTimer = 0.2;


        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
                new RaceAction(
                        actionManager.updateSpeedOverTime(),
                        new SequentialAction(
                                actionManager.shotCue(0),
                                Shield.AutoShieldShoot(),
                                Arm.AutoArmIn(),
                                Hood.AutoHoodUp(),
                                actionManager.rev(rpm),
                                Limelight.AutoScanWithInit(),
                                new ParallelAction(
                                        actionManager.rev(rpm),
                                        QuickSpindexer.toMotifFrom(Motif.GPP),
                                        toShoot1.build()
                                ),

//                        Limelight.Relocalize(drive),
//                        aimShoot1.build(),

                                //First volley start
                                actionManager.shotCue(1),
                                actionManager.waitFor(1.0),
                                actionManager.waitForSpeedSafe(rpm),
                                Arm.AutoLaunchStart(),
                                actionManager.waitFor(shotCooldown),
                                Arm.AutoLaunchEnd(),

                                actionManager.shotCue(2),
                                QuickSpindexer.turnRight(),
                                actionManager.waitForSpeedSafe(rpm),
                                Arm.AutoLaunchStart(),
                                actionManager.waitFor(shotCooldown),
                                Arm.AutoLaunchEnd(),

                                actionManager.shotCue(3),
                                QuickSpindexer.turnRight(),
                                actionManager.waitForSpeedSafe(rpm),
                                Arm.AutoLaunchStart(),
                                actionManager.waitFor(shotCooldown),
                                Arm.AutoLaunchEnd(),

                                actionManager.derev(),
                                //First volley end


                                //First pickup start
                                Arm.AutoArmOut(),
                                Shield.AutoShieldLock(),
                                Roller.AutoIntakeOn(),

                                toPickup1.build(),

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
                                //First Pickup End

                                //Sort 1
                                new ParallelAction(
                                        new SequentialAction(
                                                Distance.waitForBallInSpindexer(),
                                                actionManager.waitFor(0.5),
                                                QuickSpindexer.toMotifFrom(Motif.PPG)
                                        ),
                                        Shield.AutoShieldShoot(),
                                        actionManager.rev(rpm),
                                        toShoot2.build()
                                ),

                                //Second volley start
//                        Limelight.Relocalize(drive),
//                        aimShoot2.build(),

                                actionManager.shotCue(4),
                                actionManager.waitForSpeedSafe(rpm),
                                Arm.AutoLaunchStart(),
                                actionManager.waitFor(shotCooldown),
                                Arm.AutoLaunchEnd(),

                                actionManager.shotCue(5),
                                QuickSpindexer.turnRight(),
                                actionManager.waitForSpeedSafe(rpm),
                                Arm.AutoLaunchStart(),
                                actionManager.waitFor(shotCooldown),
                                Arm.AutoLaunchEnd(),

                                actionManager.shotCue(6),
                                QuickSpindexer.turnRight(),
                                actionManager.waitForSpeedSafe(rpm),
                                Arm.AutoLaunchStart(),
                                actionManager.waitFor(shotCooldown),
                                Arm.AutoLaunchEnd(),

                                actionManager.derev(),
                                //Second volley end

                                //2nd pickup start
                                Arm.AutoArmOut(),
                                Shield.AutoShieldLock(),
                                Roller.AutoIntakeOn(),

                                toPickup2.build(),

                                new RaceAction(
                                        new SequentialAction(
                                                pickup2.build(),
                                                waitPickup2.build()
                                        ),
                                        new SequentialAction(
                                                Distance.waitForBallInLonger(),
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

                                //Sort 2
                                new ParallelAction(
                                        new SequentialAction(
                                                Distance.waitForBallInSpindexer(),
                                                actionManager.waitFor(0.5),
                                                QuickSpindexer.toMotifFrom(Motif.PGP)
                                        ),
                                        Shield.AutoShieldShoot(),
                                        actionManager.rev(rpm),
                                        toShoot3.build()
                                ),

                                //Third volley start
//                        Limelight.Relocalize(drive),
//                        aimShoot3.build(),

                                actionManager.shotCue(7),
                                actionManager.waitForSpeedSafe(rpm),
                                Arm.AutoLaunchStart(),
                                actionManager.waitFor(shotCooldown),
                                Arm.AutoLaunchEnd(),

                                actionManager.shotCue(8),
                                QuickSpindexer.turnRight(),
                                actionManager.waitForSpeedSafe(rpm),
                                Arm.AutoLaunchStart(),
                                actionManager.waitFor(shotCooldown),
                                Arm.AutoLaunchEnd(),

                                actionManager.shotCue(9),
                                QuickSpindexer.turnRight(),
                                actionManager.waitForSpeedSafe(rpm),
                                Arm.AutoLaunchStart(),
                                actionManager.waitFor(shotCooldown),
                                Arm.AutoLaunchEnd(),

                                actionManager.derev(),
                                toPark.build()
                        )
                )
        );
    }
}