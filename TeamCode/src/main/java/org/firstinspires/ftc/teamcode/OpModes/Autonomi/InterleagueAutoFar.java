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
@Autonomous(name = "Far zone 9")
public class InterleagueAutoFar extends LinearOpMode {
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
        ActionManager actionManager = new ActionManager(this, 28);
        Distance.initSensor(this);

        QuickSpindexer.initSpindexer(this); //ugly but works
        Shield.initLocking(this);

        double rpm = 4100;
        double shotCooldown = 0.2+0.2;

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
            if (waitTime < 0.0){
                waitTime = 0.0;
            }
            if (waitTime > 4.0){
                waitTime = 4.0;
            }
            telemetry.addData("Wait time", waitTime);
            if (posMultiplier == 1.0) {
                telemetry.addData("Alliance", "Blue");
                TeleOp.alliance = Alliance.BLUE;
            }
            if (posMultiplier == -1.0) {
                telemetry.addData("Alliance", "Red");
                TeleOp.alliance = Alliance.RED;
            }
            Limelight.updateMotif();
            telemetry.addData("Current Motif", Limelight.motif);
            telemetry.update();
        }

        Pose2d startPosFar = new Pose2d(62.6, posMultiplier*-16.0, posMultiplier*Math.toRadians(0.0));
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPosFar);

        //Poses
        Pose2d shootPosFar1 = new Pose2d(54.5, posMultiplier*-13.0, posMultiplier*Math.toRadians(23.5));
        Pose2d shootPosFar2 = new Pose2d(54.5, posMultiplier*-13.0, posMultiplier*Math.toRadians(25));
        Pose2d shootPosFar3 = new Pose2d(54.5, posMultiplier*-14.0, posMultiplier*Math.toRadians(28.5));


        Pose2d startPickup1 = new Pose2d(37.0, posMultiplier*-34.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d firstPickup1 = new Pose2d(37.0, posMultiplier*-36.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d secondPickup1 = new Pose2d(37.0, posMultiplier*-40.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d endPickup1 = new Pose2d(37.0, posMultiplier*-48.0, posMultiplier*-Math.toRadians(90.0));

        Pose2d startPickup2 = new Pose2d(15.0, posMultiplier*-30.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d firstPickup2 = new Pose2d(12.0, posMultiplier*-32.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d secondPickup2 = new Pose2d(12.0, posMultiplier*-36.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d endPickup2 = new Pose2d(15.0, posMultiplier*-45.0, posMultiplier*-Math.toRadians(90.0));

        Pose2d startPickup3 = new Pose2d(-12.0, posMultiplier*-30.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d firstPickup3 = new Pose2d(-12.0, posMultiplier*-32.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d secondPickup3 = new Pose2d(-12.0, posMultiplier*-36.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d endPickup3 = new Pose2d(-12.0, posMultiplier*-45.0, posMultiplier*-Math.toRadians(90.0));

        Pose2d gatePose = new Pose2d(0.0, posMultiplier*-55.0, posMultiplier*Math.toRadians(0.0));
        Pose2d parkPosFar = new Pose2d(-40.0, posMultiplier*-50.0, posMultiplier*Math.toRadians(90.0));

        TrajectoryActionBuilder toShoot1 = drive.actionBuilder(startPosFar)
                .setTangent(posMultiplier*Math.toRadians(-110.0))
                .splineToLinearHeading(shootPosFar1, posMultiplier*Math.toRadians(110.0));

        TrajectoryActionBuilder toPickup1 = drive.actionBuilder(shootPosFar1)
                .setTangent(posMultiplier*Math.toRadians(180))
                .splineToLinearHeading(startPickup1, posMultiplier*Math.toRadians(-90));
        TrajectoryActionBuilder pickupFirst1 = drive.actionBuilder(startPickup1)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(firstPickup1, posMultiplier*Math.toRadians(-90.0));
        TrajectoryActionBuilder pickupSecond1 = drive.actionBuilder(firstPickup1)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(secondPickup1, posMultiplier*Math.toRadians(-90.0));
        TrajectoryActionBuilder pickupThird1 = drive.actionBuilder(secondPickup1)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(endPickup1, posMultiplier*Math.toRadians(-90.0));
        TrajectoryActionBuilder slowPickup1 = drive.actionBuilder(startPickup1)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(endPickup1, posMultiplier*Math.toRadians(-90.0), new TranslationalVelConstraint(5.0));

        TrajectoryActionBuilder toPickup2 = drive.actionBuilder(shootPosFar2)
                .setTangent(posMultiplier*Math.toRadians(-150.0))
                .splineToLinearHeading(startPickup2, posMultiplier*Math.toRadians(-150.0));
        TrajectoryActionBuilder slowPickup2 = drive.actionBuilder(startPickup2)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(endPickup2, posMultiplier*Math.toRadians(-90.0), new TranslationalVelConstraint(5.0));

        TrajectoryActionBuilder toShoot2 = drive.actionBuilder(endPickup1)
                .setTangent(posMultiplier*Math.toRadians(125.0))
                .splineToLinearHeading(shootPosFar2, posMultiplier*Math.toRadians(125.0));

        TrajectoryActionBuilder toShoot3 = drive.actionBuilder(endPickup1)
                .setTangent(posMultiplier*Math.toRadians(35.0))
                .splineToLinearHeading(shootPosFar3, posMultiplier*Math.toRadians(35.0));

        TrajectoryActionBuilder toPark = drive.actionBuilder(shootPosFar3)
                .setTangent(posMultiplier*Math.toRadians(-125))
                .splineToLinearHeading(parkPosFar, posMultiplier*Math.toRadians(-125));

        TrajectoryActionBuilder waitVariable = drive.actionBuilder(startPosFar)
                .waitSeconds(waitTime);

        TrajectoryActionBuilder waitBallIn1 = drive.actionBuilder(startPosFar)
                .waitSeconds(0.3);
        TrajectoryActionBuilder waitBallIn2 = drive.actionBuilder(startPosFar)
                .waitSeconds(0.3);
        TrajectoryActionBuilder waitBallIn3 = drive.actionBuilder(startPosFar)
                .waitSeconds(0.3);
        TrajectoryActionBuilder waitBallInSpindexer1 = drive.actionBuilder(startPosFar)
                .waitSeconds(0.3);
        TrajectoryActionBuilder waitBallInSpindexer2 = drive.actionBuilder(startPosFar)
                .waitSeconds(0.3);
        TrajectoryActionBuilder waitBallInSpindexer3 = drive.actionBuilder(startPosFar)
                .waitSeconds(0.3);

        Gyroscope.setRotation(Math.toDegrees(startPosFar.heading.toDouble()));
        TeleOp.autoEndRotation = Math.toDegrees(parkPosFar.heading.toDouble());


        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        actionManager.shotCue(0),
                        Shield.AutoShieldShoot(),
                        Arm.AutoArmIn(),
                        Hood.AutoHoodFar(),
                        actionManager.rev(rpm), //moved here bc PID
                        Limelight.AutoScanWithInit(),
                        new ParallelAction(
                                QuickSpindexer.toMotifFrom(Motif.GPP),
                                toShoot1.build()
                        ),
//                        Limelight.AutoAim1(shootPosFar, drive, posMultiplier, -110.0, -110.0)
//                        )); Actions.runBlocking(new SequentialAction(
//                        Limelight.alignShoot1.build(),

                        waitVariable.build(),

                        //First volley start
                        actionManager.rev(rpm),

                        actionManager.shotCue(1),
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

                        Arm.AutoArmOut(),
                        Shield.AutoShieldLock(),
                        Roller.AutoIntakeOn(),

                        toPickup1.build(),

                        //First pickup start
                        new ParallelAction(
                                slowPickup1.build(),
                                new SequentialAction(
                                        Distance.waitForBallIn(),
                                        Roller.AutoIntakeOff(),
                                        Arm.AutoArmInWait(),
                                        Distance.waitForBallPassed(),
                                        QuickSpindexer.turnLeft(),
                                        Roller.AutoIntakeOn(),
                                        Arm.AutoArmOut(),
                                        Distance.waitForBallIn(),
                                        Roller.AutoIntakeOff(),
                                        Arm.AutoArmInWait(),
                                        Distance.waitForBallPassed(),
                                        QuickSpindexer.turnLeft(),
                                        Roller.AutoIntakeOn(),
                                        Arm.AutoArmOut(),
                                        Distance.waitForBallInDelay(),
                                        Roller.AutoIntakeOff(),
                                        Distance.waitForBallPassed()
                                )
                        ),
                        //First Pickup End

                        //Sort
                        new ParallelAction(
                                new SequentialAction(
                                        Arm.AutoArmInWait(),
                                        QuickSpindexer.toMotifFrom(Motif.PPG)
                                ),
                                Shield.AutoShieldShoot(),
                                toShoot2.build()
                        ),

                        //Second volley start
                        actionManager.rev(rpm),

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

                        Arm.AutoArmOut(),
                        Shield.AutoShieldLock(),
                        Roller.AutoIntakeOn(),

                        toPickup2.build(),

                        //Second pickup start
                        new ParallelAction(
                                slowPickup2.build(),
                                new SequentialAction(
                                        Distance.waitForBallIn(),
                                        Roller.AutoIntakeOff(),
                                        Arm.AutoArmInWait(),
                                        Distance.waitForBallPassed(),
                                        QuickSpindexer.turnLeft(),
                                        Roller.AutoIntakeOn(),
                                        Arm.AutoArmOut(),
                                        Distance.waitForBallIn(),
                                        Roller.AutoIntakeOff(),
                                        Arm.AutoArmInWait(),
                                        Distance.waitForBallPassed(),
                                        QuickSpindexer.turnLeft(),
                                        Roller.AutoIntakeOn(),
                                        Arm.AutoArmOut(),
                                        Distance.waitForBallInDelay(),
                                        Roller.AutoIntakeOff(),
                                        Distance.waitForBallPassed()
                                )
                        ),
                        //Second Pickup End

                        //Sort
                        new ParallelAction(
                                new SequentialAction(
                                        Arm.AutoArmInWait(),
                                        QuickSpindexer.toMotifFrom(Motif.PGP)
                                ),
                                Shield.AutoShieldShoot(),
                                toShoot3.build()
                        ),

                        //Third volley start
                        actionManager.rev(rpm),

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
                        //Third volley end

//                        QuickSpindexer.resetForTele(), //should change later
                        toPark.build()
                )
        );
    }
}