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
@Autonomous(name = "Near zone 9, Gate instead of sorting")
public class InterleagueAutoNearGate extends LinearOpMode {
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

        double rpm = 3200;
        double shotCooldown = 0.2+0.2; // 0.2 + actual cooldown

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
            if (waitTime > 2.0){
                waitTime = 2.0;
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
            telemetry.update();
        }

        Pose2d startPosClose = new Pose2d(-55.5, posMultiplier*-47.0, posMultiplier*Math.toRadians(55.0));
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPosClose);

        //Poses
        Pose2d scanPos = new Pose2d(-27.0, posMultiplier*-27.0, posMultiplier*Math.toRadians(-25.0));
        Pose2d shootPosClose1 = new Pose2d(-25.0, posMultiplier*-25.0, posMultiplier*Math.toRadians(50.0));
        Pose2d shootPosClose2 = new Pose2d(-25.0, posMultiplier*-25.0, posMultiplier*Math.toRadians(50.0));
        Pose2d shootPosClose3 = new Pose2d(-48.0, posMultiplier*-10.0, posMultiplier*Math.toRadians(80.0));
//        Pose2d parkPosClose = new Pose2d(-60.0, posMultiplier*-25.0, posMultiplier*Math.toRadians(0.0));

        Pose2d startPickup1 = new Pose2d(-12.0, posMultiplier*-30.0, posMultiplier*Math.toRadians(-90.0));
//        Pose2d firstPickup1 = new Pose2d(-12.0, posMultiplier*-32.0, posMultiplier*Math.toRadians(-90.0));
//        Pose2d secondPickup1 = new Pose2d(-12.0, posMultiplier*-36.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d endPickup1 = new Pose2d(-12.0, posMultiplier*-45.0, posMultiplier*-Math.toRadians(90.0));

        Pose2d startPickup2 = new Pose2d(14.0, posMultiplier*-30.0, posMultiplier*Math.toRadians(-90.0));
//        Pose2d firstPickup2 = new Pose2d(12.0, posMultiplier*-32.0, posMultiplier*Math.toRadians(-90.0));
//        Pose2d secondPickup2 = new Pose2d(12.0, posMultiplier*-36.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d endPickup2 = new Pose2d(14.0, posMultiplier*-45.0, posMultiplier*-Math.toRadians(90.0));

        Pose2d gatePose1 = new Pose2d(0.0, -55.0, Math.toRadians(-90.0));


//        Pose2d startPickup3 = new Pose2d(35.5, posMultiplier*-30.0, posMultiplier*Math.toRadians(-90.0));
//        Pose2d firstPickup3 = new Pose2d(35.5, posMultiplier*-32.0, posMultiplier*Math.toRadians(-90.0));
//        Pose2d secondPickup3 = new Pose2d(35.5, posMultiplier*-36.0, posMultiplier*Math.toRadians(-90.0));
//        Pose2d endPickup3 = new Pose2d(35.5, posMultiplier*-45.0, posMultiplier*-Math.toRadians(90.0));
//
//        Pose2d gatePose = new Pose2d(0.0, posMultiplier*-55.0, posMultiplier*Math.toRadians(0.0));
//        Pose2d parkPosFar = new Pose2d(37.75, posMultiplier*-32.75, posMultiplier*Math.toRadians(90.0));

        TrajectoryActionBuilder toScan = drive.actionBuilder(startPosClose)
                .setTangent(posMultiplier*Math.toRadians(55.0))
                .splineToLinearHeading(scanPos, posMultiplier*Math.toRadians(45.0));

        TrajectoryActionBuilder toShoot1 = drive.actionBuilder(startPosClose)
                .setTangent(posMultiplier*Math.toRadians(55.0))
                .splineToLinearHeading(shootPosClose1, posMultiplier*Math.toRadians(55.0));

        TrajectoryActionBuilder toShootNoScan = drive.actionBuilder(startPosClose)
                .setTangent(posMultiplier*Math.toRadians(35.0))
                .splineToLinearHeading(shootPosClose1, posMultiplier*Math.toRadians(35.0));

        TrajectoryActionBuilder toPickup1 = drive.actionBuilder(shootPosClose1)
                .setTangent(posMultiplier*Math.toRadians(70.0))
                .splineToLinearHeading(startPickup1, posMultiplier*Math.toRadians(-70.0));
        TrajectoryActionBuilder slowPickup1 = drive.actionBuilder(startPickup1)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(endPickup1, posMultiplier*Math.toRadians(-90.0), new TranslationalVelConstraint(5.0));

        TrajectoryActionBuilder toGate1 = drive.actionBuilder(endPickup1)
                .setReversed(true)
                .setTangent(posMultiplier*Math.toRadians(90))
                .splineToLinearHeading(gatePose1, posMultiplier*Math.toRadians(-90.0));

        TrajectoryActionBuilder openGate1 = drive.actionBuilder(gatePose1)
                .waitSeconds(1.0);
//        TrajectoryActionBuilder pickupFirst1 = drive.actionBuilder(startPickup1)
//                .setTangent(posMultiplier*Math.toRadians(-90.0))
//                .splineToLinearHeading(firstPickup1, posMultiplier*Math.toRadians(-90.0));
//        TrajectoryActionBuilder pickupSecond1 = drive.actionBuilder(firstPickup1)
//                .setTangent(posMultiplier*Math.toRadians(-90.0))
//                .splineToLinearHeading(secondPickup1, posMultiplier*Math.toRadians(-90.0));
//        TrajectoryActionBuilder pickupThird1 = drive.actionBuilder(secondPickup1)
//                .setTangent(posMultiplier*Math.toRadians(-90.0))
//                .splineToLinearHeading(endPickup1, posMultiplier*Math.toRadians(-90.0));
        //need color sensor/proximity sensor/ distance sensor for this version:
//        TrajectoryActionBuilder pickupFirstRow = drive.actionBuilder(startPickup1)
//                .setTangent(posMultiplier*Math.toRadians(-90.0))
//                .splineToLinearHeading(endPickup1, posMultiplier*Math.toRadians(-90.0), new TranslationalVelConstraint(10.0));

        TrajectoryActionBuilder toShoot2 = drive.actionBuilder(gatePose1)
                .setTangent(posMultiplier*Math.toRadians(130.0))
                .splineToLinearHeading(shootPosClose2, posMultiplier*Math.toRadians(130.0));
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
*/
        TrajectoryActionBuilder toPickup2 = drive.actionBuilder(shootPosClose2)
                .setTangent(posMultiplier*Math.toRadians(45.0))
                .splineToLinearHeading(startPickup2, posMultiplier*Math.toRadians(-45.0));

        TrajectoryActionBuilder slowPickup2 = drive.actionBuilder(startPickup2)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(endPickup2, posMultiplier*Math.toRadians(-90.0), new TranslationalVelConstraint(5.0));

        TrajectoryActionBuilder toShoot3 = drive.actionBuilder(endPickup2)
                .setTangent(posMultiplier*Math.toRadians(145.0))
                .splineToLinearHeading(shootPosClose3, posMultiplier*Math.toRadians(145.0));
/*
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

//        TrajectoryActionBuilder toPark = drive.actionBuilder(shootPosClose3)
//                .setTangent(posMultiplier*Math.toRadians(180.0))
//                .splineToLinearHeading(parkPosClose, posMultiplier*Math.toRadians(-180.0));

        TrajectoryActionBuilder waitVariable = drive.actionBuilder(startPosClose)
                .waitSeconds(waitTime);

//        TrajectoryActionBuilder waitBallIn1 = drive.actionBuilder(startPosClose)
//                .waitSeconds(0.3);
//        TrajectoryActionBuilder waitBallIn2 = drive.actionBuilder(startPosClose)
//                .waitSeconds(0.3);
//        TrajectoryActionBuilder waitBallIn3 = drive.actionBuilder(startPosClose)
//                .waitSeconds(0.3);
//        TrajectoryActionBuilder waitBallInSpindexer1 = drive.actionBuilder(startPosClose)
//                .waitSeconds(0.3);
//        TrajectoryActionBuilder waitBallInSpindexer2 = drive.actionBuilder(startPosClose)
//                .waitSeconds(0.3);
//        TrajectoryActionBuilder waitBallInSpindexer3 = drive.actionBuilder(startPosClose)
//                .waitSeconds(0.3);

        TrajectoryActionBuilder waitRev1 = drive.actionBuilder(startPosClose)
                .waitSeconds(0.1);
        TrajectoryActionBuilder waitRev2 = drive.actionBuilder(startPosClose)
                .waitSeconds(0.1);
        TrajectoryActionBuilder waitRev3 = drive.actionBuilder(startPosClose)
                .waitSeconds(0.1);

//        TrajectoryActionBuilder waitTwenty = drive.actionBuilder(startPosClose)
//                .waitSeconds(0.5);

        Gyroscope.setRotation(Math.toDegrees(startPosClose.heading.toDouble()));
        TeleOp.autoEndRotation = Math.toDegrees(shootPosClose3.heading.toDouble());


        Limelight.motif = Motif.GPP;
        waitForStart();

        if (isStopRequested()) return;

        Actions.runBlocking(
                new SequentialAction(
                        actionManager.shotCue(0),
                        Shield.AutoShieldShoot(),
                        Arm.AutoArmIn(),
                        Hood.AutoHoodNear(),
                        actionManager.rev(rpm),
                        new ParallelAction(
                                QuickSpindexer.toMotifFrom(Motif.GPP),
                                toShootNoScan.build()
                        ),
//                        Limelight.AutoAim1(shootPosClose1, drive, posMultiplier, 55.0, 55.0),
//                        Limelight.alignShoot1.build(),

                        waitVariable.build(),
                        waitRev1.build(),

                        //First volley start
//                        actionManager.rev(rpm),

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
                                        Distance.waitForBallIn(),
                                        Roller.AutoIntakeOff(),
                                        Distance.waitForBallPassed()
                                )
                        ),
                        //intake is turned on earlier to be safe
//                        pickupFirst1.build(),
//                        waitBallIn1.build(),
//                        Arm.AutoArmInWait(),
//                        Roller.AutoIntakeOff(),
//                        waitBallInSpindexer1.build(),
//                        QuickSpindexer.turnLeft(),
//
//                        Arm.AutoArmOut(),
//                        Roller.AutoIntakeOn(),
//                        pickupSecond1.build(),
//                        waitBallIn2.build(),
//                        Arm.AutoArmInWait(),
//                        Roller.AutoIntakeOff(),
//                        waitBallInSpindexer2.build(),
//                        QuickSpindexer.turnLeft(),
//
//                        Arm.AutoArmOut(),
//                        Roller.AutoIntakeOn(),
//                        pickupThird1.build(),
//                        waitBallIn3.build(),
//                        Arm.AutoArmInWait(),
//                        Roller.AutoIntakeOff(),
//                        waitBallInSpindexer3.build(),
                        //First Pickup End

                        //Sort
                        new ParallelAction(
                                new SequentialAction(
                                        Arm.AutoArmInWait(),
                                        QuickSpindexer.toMotifFrom(Motif.PGP)
                                ),
                                Shield.AutoShieldShoot(),
                                toGate1.build()
                        ),
                        openGate1.build(),

                        toShoot2.build(),
//                        Limelight.AutoAim2(shootPosClose2, drive, posMultiplier, 125.0, 125.0),
//                        Limelight.alignShoot2.build(),

                        //Second volley start
                        actionManager.rev(rpm),
                        waitRev2.build(),

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
                                        Distance.waitForBallIn(),
                                        Roller.AutoIntakeOff(),
                                        Distance.waitForBallPassed()
                                )
                        ),

                        //Sort
                        new ParallelAction(
                                new SequentialAction(
                                        Arm.AutoArmInWait(),
                                        QuickSpindexer.toMotifFrom(Motif.PPG)
                                ),
                                Shield.AutoShieldShoot(),
                                toShoot3.build()
                        ),

                        //Third volley start
                        actionManager.rev(rpm),
                        waitRev3.build(),

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

                        actionManager.derev()
                        //Third volley end

//                        QuickSpindexer.resetForTele()//, //should change later
//                        toPark.build()
                )
        );
    }
}