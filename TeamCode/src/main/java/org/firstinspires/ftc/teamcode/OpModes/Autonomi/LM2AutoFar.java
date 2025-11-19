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
@Autonomous(name = "Far zone 6")
public class LM2AutoFar extends LinearOpMode {
    @Override
    public void runOpMode() {
        //Mechs init
        Arm.initIntake(this);
        Roller.initIntake(this);
        RunLater.setup(this);
        Obelisk.initDetection(this);
        DriverTest.initControls(this); //new
        Hood.initAim(this);
        ActionManager actionManager = new ActionManager(this, 28);

        QuickSpindexer.initSpindexer(this); //ugly but works
        Shield.initLocking(this);

        double rpm = 4200;
        double shotCooldown = 0.2+0.6;

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
            if (waitTime > 4.0){
                waitTime = 4.0;
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
        }

        Pose2d startPosFar = new Pose2d(62.6, posMultiplier*-16.0, posMultiplier*Math.toRadians(0.0));
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPosFar);

        //Poses
        Pose2d shootPosFar = new Pose2d(54.5, posMultiplier*-13.0, posMultiplier*Math.toRadians(23.5));

        Pose2d startPickup1 = new Pose2d(37.0, posMultiplier*-34.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d firstPickup1 = new Pose2d(37.0, posMultiplier*-36.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d secondPickup1 = new Pose2d(37.0, posMultiplier*-40.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d endPickup1 = new Pose2d(37.0, posMultiplier*-48.0, posMultiplier*-Math.toRadians(90.0));

        Pose2d startPickup2 = new Pose2d(12.0, posMultiplier*-30.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d firstPickup2 = new Pose2d(12.0, posMultiplier*-32.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d secondPickup2 = new Pose2d(12.0, posMultiplier*-36.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d endPickup2 = new Pose2d(12.0, posMultiplier*-45.0, posMultiplier*-Math.toRadians(90.0));

        Pose2d startPickup3 = new Pose2d(-12.0, posMultiplier*-30.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d firstPickup3 = new Pose2d(-12.0, posMultiplier*-32.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d secondPickup3 = new Pose2d(-12.0, posMultiplier*-36.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d endPickup3 = new Pose2d(-12.0, posMultiplier*-45.0, posMultiplier*-Math.toRadians(90.0));

        Pose2d gatePose = new Pose2d(0.0, posMultiplier*-55.0, posMultiplier*Math.toRadians(0.0));
        Pose2d parkPosFar = new Pose2d(60.0, posMultiplier*-38.0, posMultiplier*Math.toRadians(90.0));

        TrajectoryActionBuilder toShoot1 = drive.actionBuilder(startPosFar)
                .setTangent(posMultiplier*Math.toRadians(-110.0))
                .splineToLinearHeading(shootPosFar, posMultiplier*Math.toRadians(110.0));

        TrajectoryActionBuilder toPickup1 = drive.actionBuilder(shootPosFar)
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

        TrajectoryActionBuilder toShoot2 = drive.actionBuilder(endPickup1)
                .setTangent(posMultiplier*Math.toRadians(125.0))
                .splineToLinearHeading(shootPosFar, posMultiplier*Math.toRadians(125.0));

        TrajectoryActionBuilder toPark = drive.actionBuilder(shootPosFar)
                .setTangent(posMultiplier*Math.toRadians(-90))
                .splineToLinearHeading(parkPosFar, posMultiplier*Math.toRadians(-90));

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

        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        actionManager.shotCue(0),
                        Shield.AutoShieldShoot(),
                        Arm.AutoArmIn(),
                        Hood.AutoHoodFar(),
                        actionManager.rev(rpm), //moved here bc PID
                        Obelisk.AutoScanWithInit(),
                        new ParallelAction(
                                QuickSpindexer.toMotifFrom(Motif.GPP),
                                toShoot1.build()
                        ),

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
                        waitBallInSpindexer1.build(),
                        QuickSpindexer.turnLeft(),

                        Arm.AutoArmOut(),
                        Roller.AutoIntakeOn(),
                        pickupSecond1.build(),
                        waitBallIn2.build(),
                        Arm.AutoArmInWait(),
                        Roller.AutoIntakeOff(),
                        waitBallInSpindexer2.build(),
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
                                QuickSpindexer.toMotifFrom(Motif.PPG),
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

                        QuickSpindexer.resetForTele(), //should change later
                        toPark.build()
                )
        );
    }
}