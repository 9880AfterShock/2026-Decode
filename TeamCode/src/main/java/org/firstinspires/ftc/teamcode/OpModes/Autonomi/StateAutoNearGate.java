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
//import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Shield;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Hood;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.Prongs;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.QuickSpindexer;
import org.firstinspires.ftc.teamcode.OpModes.TeleOp;
import org.firstinspires.ftc.teamcode.Sensors.Distance;
import org.firstinspires.ftc.teamcode.Sensors.Gyroscope;
import org.firstinspires.ftc.teamcode.Sensors.Limelight;
import org.firstinspires.ftc.teamcode.Systems.ActionManager;
import org.firstinspires.ftc.teamcode.Systems.RunLater;

@Config
@Autonomous(name = "Near zone 9 (Gate instead of motif)")
public class StateAutoNearGate extends LinearOpMode {
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
//        Shield.initLocking(this);
        Prongs.initGrate(this);

        double rpm = 2600;
        double shotCooldown = 0.2+0.2; // 0.2 + actual cooldown

        double posMultiplier = 1.0;
        boolean secondDump = false;
//        double waitTime = 0.0;
        while (!isStopRequested() && !opModeIsActive()) {
            telemetry.addLine("Use x and b to select alliance");
            telemetry.addLine("Use a to toggle 2nd dump");
            if (gamepad1.xWasPressed()){
                posMultiplier = 1.0;
            }
            if (gamepad1.bWasPressed()){
                posMultiplier = -1.0;
            }
            if (gamepad1.aWasPressed()){
                secondDump = !secondDump;
            }
            if (posMultiplier == 1.0) {
                telemetry.addData("Alliance", "Blue");
                TeleOp.alliance = Alliance.BLUE;
            }
            if (posMultiplier == -1.0) {
                telemetry.addData("Alliance", "Red");
                TeleOp.alliance = Alliance.RED;
            }
            if (secondDump){
                telemetry.addData("Second Dump", "Active");
            } else {
                telemetry.addData("Second Dump", "Disabled");
            }
            telemetry.update();
        }

        Pose2d startPosClose = new Pose2d(-51.5, posMultiplier*-50.5, posMultiplier*Math.toRadians(55.0));
        MecanumDrive drive = new MecanumDrive(hardwareMap, startPosClose);

        //Poses
        Pose2d shootPosClose1 = new Pose2d(-20.0, posMultiplier*-25.0, posMultiplier*Math.toRadians(40.0));
        Pose2d shootPosClose2 = new Pose2d(-24.0, posMultiplier*-25.0, posMultiplier*Math.toRadians(48.0));
        Pose2d shootPosClose3 = new Pose2d(-37.0, posMultiplier*-15.0, posMultiplier*Math.toRadians(63.0));

        Pose2d prePickup1 = new Pose2d(-12.0, posMultiplier*-26.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d startPickup1 = new Pose2d(-12.0, posMultiplier*-30.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d endPickup1 = new Pose2d(-12.0, posMultiplier*-50.0, posMultiplier*Math.toRadians(-90.0));

        Pose2d prePickup2 = new Pose2d(12.0, posMultiplier*-24.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d startPickup2 = new Pose2d(12.0, posMultiplier*-30.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d endPickup2 = new Pose2d(12.0, posMultiplier*-45.0, posMultiplier*Math.toRadians(-90.0));

        Pose2d gatePos1 = new Pose2d(0.0, posMultiplier*-51.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d gatePos2 = new Pose2d(0.0, posMultiplier*-51.0, posMultiplier*Math.toRadians(-90.0));

        TrajectoryActionBuilder toShoot1 = drive.actionBuilder(startPosClose)
                .setTangent(posMultiplier*Math.toRadians(37.0))
                .splineToLinearHeading(shootPosClose1, posMultiplier*Math.toRadians(37.0), new TranslationalVelConstraint(40.0));

        TrajectoryActionBuilder toPickup1 = drive.actionBuilder(shootPosClose1)
                .setTangent(posMultiplier*Math.toRadians(0.0))
                .splineToLinearHeading(prePickup1, posMultiplier*Math.toRadians(0.0), new TranslationalVelConstraint(40.0))

                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(startPickup1, posMultiplier*Math.toRadians(-90.0));

        TrajectoryActionBuilder pickup1 = drive.actionBuilder(startPickup1)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(endPickup1, posMultiplier*Math.toRadians(-90.0), new TranslationalVelConstraint(15.0));

        TrajectoryActionBuilder toGate1 = drive.actionBuilder(endPickup1)
                .setTangent(posMultiplier*Math.toRadians(90.0))
                .splineToLinearHeading(gatePos1, posMultiplier*Math.toRadians(-90.0));

        TrajectoryActionBuilder toShoot2 = drive.actionBuilder(gatePos1)
                .setTangent(posMultiplier*Math.toRadians(130.0))
                .splineToLinearHeading(shootPosClose2, posMultiplier*Math.toRadians(130.0), new TranslationalVelConstraint(40.0));

        TrajectoryActionBuilder toPickup2 = drive.actionBuilder(shootPosClose2)
                .setTangent(posMultiplier*Math.toRadians(0.0))
                .splineToLinearHeading(prePickup2, posMultiplier*Math.toRadians(0.0), new TranslationalVelConstraint(40.0))
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(startPickup2, posMultiplier*Math.toRadians(-90.0));

        TrajectoryActionBuilder pickup2 = drive.actionBuilder(startPickup2)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(endPickup2, posMultiplier*Math.toRadians(-90.0), new TranslationalVelConstraint(5.0));

        TrajectoryActionBuilder toShoot3;

        if (secondDump){
            toShoot3 = drive.actionBuilder(endPickup2)
                    //doing gate
                    .setTangent(posMultiplier*Math.toRadians(180.0))
                    .splineToLinearHeading(gatePos2, posMultiplier*Math.toRadians(-90.0))
                    .waitSeconds(0.1)

                    .setTangent(posMultiplier*Math.toRadians(130.0))
                    .splineToLinearHeading(shootPosClose3, posMultiplier*Math.toRadians(130.0), new TranslationalVelConstraint(40.0));
        } else {
            toShoot3 = drive.actionBuilder(endPickup2)
                    .setTangent(posMultiplier*Math.toRadians(125.0))
                    .splineToLinearHeading(shootPosClose3, posMultiplier*Math.toRadians(125.0), new TranslationalVelConstraint(40.0));
        }

        TrajectoryActionBuilder waitPickup1 = drive.actionBuilder(endPickup1)
                .waitSeconds(5.0);
        TrajectoryActionBuilder waitPickup2 = drive.actionBuilder(endPickup2)
                .waitSeconds(5.0);


        Gyroscope.setRotation(Math.toDegrees(startPosClose.heading.toDouble()));
        TeleOp.autoEndPosition = shootPosClose3;

        double ballInSpindexerTimer = 0.2;


        waitForStart();

        if (isStopRequested()) return;
        Limelight.motif = Motif.GPP;

        Actions.runBlocking(
                new RaceAction(
                        actionManager.updateSpeedOverTime(),
                        new SequentialAction(
                                actionManager.shotCue(0),
//                                Shield.AutoShieldShoot(),
                                Prongs.AutoProngsPrime(),
                                Arm.AutoArmIn(),
                                Hood.AutoHoodUp(),
                                actionManager.rev(rpm),
                                QuickSpindexer.addRevOffset(),
                                new ParallelAction(
                                        actionManager.rev(rpm),
                                        toShoot1.build()
                                ),

//                        Limelight.Relocalize(drive),
//                        aimShoot1.build(),

                                //First volley start
                                actionManager.shotCue(1),
                                actionManager.waitForSpeedSafe(rpm),
                                QuickSpindexer.turnRight(),

                                actionManager.shotCue(2),
                                actionManager.waitForSpeedSafe(rpm),
                                QuickSpindexer.turnRight(),

                                actionManager.shotCue(3),
                                actionManager.waitForSpeedSafe(rpm),
                                QuickSpindexer.turnRight(),

                                actionManager.derev(),
                                QuickSpindexer.removeRevOffset(),
                                //First volley end


                                //First pickup start
                                Arm.AutoArmOut(),
//                                Shield.AutoShieldLock(),
                                Prongs.AutoProngsPrime(),
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
                                                actionManager.waitFor(0.5)
                                        ),
//                                        Shield.AutoShieldShoot(),
                                        actionManager.rev(rpm),
                                        QuickSpindexer.addRevOffset(),
                                        new SequentialAction(
                                                toGate1.build(),
                                                actionManager.waitFor(0.3), //wait for balls to roll
                                                toShoot2.build()
                                        )
                                ),

                                //Second volley start
//                        Limelight.Relocalize(drive),
//                        aimShoot2.build(),

                                actionManager.shotCue(40),
                                actionManager.waitForSpeedSafe(rpm),
                                QuickSpindexer.turnRight(),

                                actionManager.shotCue(50),
                                actionManager.waitForSpeedSafe(rpm),
                                QuickSpindexer.turnRight(),

                                actionManager.shotCue(60),
                                actionManager.waitForSpeedSafe(rpm),
                                QuickSpindexer.turnRight(),

                                actionManager.derev(),
                                actionManager.shotCue(70),
                                QuickSpindexer.removeRevOffset(),

                                actionManager.shotCue(700),
                                //Second volley end

                                //2nd pickup start
                                Arm.AutoArmOut(),
//                                Shield.AutoShieldLock(),
                                Prongs.AutoProngsPrime(),
                                Roller.AutoIntakeOn(),

                                toPickup2.build(),

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

                                //Sort 2
                                new ParallelAction(
                                        new SequentialAction(
                                                Distance.waitForBallInSpindexer(),
                                                actionManager.waitFor(0.5)
                                        ),
//                                        Shield.AutoShieldShoot(),
                                        actionManager.rev(rpm),
                                        QuickSpindexer.addRevOffset(),
                                        toShoot3.build()
                                ),

                                //Third volley start
//                        Limelight.Relocalize(drive),
//                        aimShoot3.build(),

                                actionManager.shotCue(7),
                                actionManager.waitForSpeedSafe(rpm),
                                QuickSpindexer.turnRight(),

                                actionManager.shotCue(8),
                                actionManager.waitForSpeedSafe(rpm),
                                QuickSpindexer.turnRight(),

                                actionManager.shotCue(9),
                                actionManager.waitForSpeedSafe(rpm),
                                QuickSpindexer.turnRight(),

                                actionManager.derev(),
                                QuickSpindexer.removeRevOffset()
                        )
                )
        );
    }
}