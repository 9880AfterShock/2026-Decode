package com.example.meepmeeprr;

import com.acmerobotics.roadrunner.AngularVelConstraint;
import com.acmerobotics.roadrunner.MinVelConstraint;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.VelConstraint;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.util.Arrays;

public class MeepMeepRR {
    public final static double posMultiplier = 1.0;
    public static void main(String[] args) {
        /*
        Pose2d startPosFar = new Pose2d(60.0, posMultiplier*-22.0, posMultiplier*Math.toRadians(-45.0));
        Pose2d startPosNear = new Pose2d(-47.0, posMultiplier*-49.0, posMultiplier*Math.toRadians(0.0));

        Pose2d shootPosFar1 = new Pose2d(60.0, posMultiplier*-22.0, posMultiplier*Math.toRadians(-45.0));
        Pose2d shootPosFar2 = new Pose2d(60.0, posMultiplier*-22.0, posMultiplier*Math.toRadians(-45.0));
        Pose2d shootPosFar3 = new Pose2d(60.0, posMultiplier*-22.0, posMultiplier*Math.toRadians(-45.0));
        Pose2d shootPosFar4 = new Pose2d(60.0, posMultiplier*-22.0, posMultiplier*Math.toRadians(-45.0));
        Pose2d shootPosFar5 = new Pose2d(60.0, posMultiplier*-22.0, posMultiplier*Math.toRadians(-45.0));

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

        Pose2d prePickupCorner = new Pose2d(55.0, posMultiplier*-55.0, posMultiplier*Math.toRadians(-60.0));
        Pose2d startPickupCorner = new Pose2d(55.0, posMultiplier*-60.0, posMultiplier*Math.toRadians(-60.0));
        Pose2d midPickupCorner = new Pose2d(58.25, posMultiplier*-60.0, posMultiplier*Math.toRadians(-60.0));
        Pose2d endPickupCorner = new Pose2d(62.5, posMultiplier*-60.0, posMultiplier*-Math.toRadians(10.0));

        Pose2d gatePosNear1 = new Pose2d(-3.0, posMultiplier*-55.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d gatePosNear2 = new Pose2d(7.0, posMultiplier*-55.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d gatePosNear3 = new Pose2d(7.0, posMultiplier*-55.0, posMultiplier*Math.toRadians(-90.0));

        Pose2d preSlamPos1 = new Pose2d(55.0, posMultiplier*-40.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d slamPos1 = new Pose2d(50.0, posMultiplier*-60.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d preSlamPos2 = new Pose2d(50.0, posMultiplier*-40.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d slamPos2 = new Pose2d(35.0, posMultiplier*-60.0, posMultiplier*Math.toRadians(-90.0));

        Pose2d parkPosFar = new Pose2d(13.5, posMultiplier*-34.0, posMultiplier*Math.toRadians(90.0));
        Pose2d parkRotationFar = new Pose2d(38.5, posMultiplier*-20.0, posMultiplier*Math.toRadians(90));
//*/
        /*
        Pose2d startPosFar = new Pose2d(61.0, posMultiplier*-22.0, posMultiplier*Math.toRadians(-45.0));

        Pose2d shootPosFar1 = new Pose2d(61.0, posMultiplier*-22.0, posMultiplier*Math.toRadians(-45.0));
        Pose2d shootPosFar2 = new Pose2d(60.0, posMultiplier*-22.0, posMultiplier*Math.toRadians(-45.0));
        Pose2d shootPosFar3 = new Pose2d(60.0, posMultiplier*-22.0, posMultiplier*Math.toRadians(-45.0));
        Pose2d shootPosFar4 = new Pose2d(60.0, posMultiplier*-22.0, posMultiplier*Math.toRadians(-45.0));
        Pose2d shootPosFar5 = new Pose2d(60.0, posMultiplier*-22.0, posMultiplier*Math.toRadians(-45.0));

        Pose2d prePickupFar = new Pose2d(36.0, posMultiplier*-30.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d startPickupFar = new Pose2d(36.0, posMultiplier*-37.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d endPickupFar = new Pose2d(36.0, posMultiplier*-56.0, posMultiplier*Math.toRadians(-90.0));

        Pose2d prePickupCorner = new Pose2d(55.0, posMultiplier*-55.0, posMultiplier*Math.toRadians(-60.0));
        Pose2d startPickupCorner = new Pose2d(55.0, posMultiplier*-60.0, posMultiplier*Math.toRadians(-60.0));
        Pose2d midPickupCorner = new Pose2d(58.25, posMultiplier*-60.0, posMultiplier*Math.toRadians(-60.0));
        Pose2d endPickupCorner = new Pose2d(62.5, posMultiplier*-60.0, posMultiplier*-Math.toRadians(10.0));

        Pose2d preSlamPos1 = new Pose2d(55.0, posMultiplier*-60.0, posMultiplier*Math.toRadians(-135.0));
        Pose2d slamPos1 = new Pose2d(35.0, posMultiplier*-60.0, posMultiplier*Math.toRadians(-135.0));
        Pose2d preSlamPos2 = new Pose2d(50.0, posMultiplier*-40.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d slamPos2 = new Pose2d(35.0, posMultiplier*-60.0, posMultiplier*Math.toRadians(-90.0));

        Pose2d parkRotationFar = new Pose2d(38.5, posMultiplier*-26.0, posMultiplier*Math.toRadians(90));
        Pose2d parkPosFar = new Pose2d(13.5, posMultiplier*-34.0, posMultiplier*Math.toRadians(90.0));
        //*/
        Pose2d startPosNear = new Pose2d(-47.0, posMultiplier*-49.0, posMultiplier*Math.toRadians(0.0));



        Pose2d shootPosNear1 = new Pose2d(-24.0, posMultiplier*-24.0, posMultiplier*Math.toRadians(0.0));
        Pose2d shootPosNear2 = new Pose2d(-24.0, posMultiplier*-24.0, posMultiplier*Math.toRadians(0.0));
        Pose2d shootPosNear3 = new Pose2d(-24.0, posMultiplier*-24.0, posMultiplier*Math.toRadians(0.0));
        Pose2d shootPosNear4 = new Pose2d(-44.0, posMultiplier*-24.0, posMultiplier*Math.toRadians(0.0));

        Pose2d prePickupNear = new Pose2d(-10.0, posMultiplier*-30.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d startPickupNear = new Pose2d(-10.0, posMultiplier*-37.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d endPickupNear = new Pose2d(-10.0, posMultiplier*-50.5, posMultiplier*Math.toRadians(-90.0));

        Pose2d prePickupMiddle = new Pose2d(14.0, posMultiplier*-30.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d startPickupMiddle = new Pose2d(14.0, posMultiplier*-37.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d endPickupMiddle = new Pose2d(14.0, posMultiplier*-55.0, posMultiplier*Math.toRadians(-90.0));

        Pose2d prePickupFar = new Pose2d(38.0, posMultiplier*-29.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d startPickupFar = new Pose2d(38.0, posMultiplier*-37.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d endPickupFar = new Pose2d(38.0, posMultiplier*-55.0, posMultiplier*Math.toRadians(-90.0));

        Pose2d gatePosNear1 = new Pose2d(7.0, posMultiplier*-55.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d gateIntakePos = new Pose2d(10.0, posMultiplier*-60.0, posMultiplier*Math.toRadians(-135.0));
        Pose2d gatePosNear3 = new Pose2d(7.0, posMultiplier*-55.0, posMultiplier*Math.toRadians(-90.0)); //probably not used

        VelConstraint driveSpeed = new MinVelConstraint(Arrays.asList(
                new TranslationalVelConstraint(40.0),
                new AngularVelConstraint(Math.PI)
        ));
        VelConstraint rushSpeed = new MinVelConstraint(Arrays.asList(
                new TranslationalVelConstraint(50.0),
                new AngularVelConstraint(Math.PI)
        ));

        VelConstraint wallIntakeSpeed = new MinVelConstraint(Arrays.asList(
                new TranslationalVelConstraint(20.0),
                new AngularVelConstraint(Math.PI/2)
        ));
        VelConstraint intakeSpeed = new MinVelConstraint(Arrays.asList(
                new TranslationalVelConstraint(15.0),
                new AngularVelConstraint(Math.PI/2)
        ));

        MeepMeep meepMeep = new MeepMeep(700); //600 is default

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setColorScheme(new ColorSchemeBlueDark()) //Toggle side with this and setting PosMultiplier
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(100.0, 100, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(14.0,15.6) //17.0 for full bot, but this is for centered
                .build();


        //Premier Auto Near
        /*
        myBot.runAction(myBot.getDrive().actionBuilder(startPosNear)
                //toShoot1
                .setTangent(posMultiplier*Math.toRadians(50))
                .splineToLinearHeading(shootPosNear1, posMultiplier*Math.toRadians(50), driveSpeed)

                //toPickup1
                .setTangent(posMultiplier*Math.toRadians(0.0))
                .splineToLinearHeading(prePickupNear, posMultiplier*Math.toRadians(-90.0), driveSpeed)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(startPickupNear, posMultiplier*Math.toRadians(-90.0), driveSpeed)

                //pickup1
                .setTangent(posMultiplier*Math.toRadians(-90))
                .splineToLinearHeading(endPickupNear, posMultiplier*Math.toRadians(-90.0), intakeSpeed)

                //toShoot2
                //noGate
//                .setTangent(posMultiplier*Math.toRadians(110.0))
//                .splineToLinearHeading(shootPosNear2, posMultiplier*Math.toRadians(110.0), driveSpeed)
                //yesGate
                .setTangent(posMultiplier*Math.toRadians(90.0))
                .splineToLinearHeading(gatePosNear1, posMultiplier*Math.toRadians(-90.0), driveSpeed)
                .waitSeconds(1.0)
                .setTangent(posMultiplier*Math.toRadians(125.0))
                .splineToLinearHeading(shootPosNear2, posMultiplier*Math.toRadians(125.0), driveSpeed)

                //toPickup2
                .setTangent(posMultiplier*Math.toRadians(0.0))
                .splineToLinearHeading(prePickupMiddle, posMultiplier*Math.toRadians(-30.0), driveSpeed)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(startPickupMiddle, posMultiplier*Math.toRadians(-90.0), driveSpeed)

                //pickup2
                .setTangent(posMultiplier*Math.toRadians(-90))
                .splineToLinearHeading(endPickupMiddle, posMultiplier*Math.toRadians(-90.0), intakeSpeed)

                //toShoot3
                //noGate
//                .setTangent(posMultiplier*Math.toRadians(140.0))
//                .splineToLinearHeading(shootPosNear3, posMultiplier*Math.toRadians(140.0), driveSpeed)
                //yesGate
                .setTangent(posMultiplier*Math.toRadians(-180.0))
                .splineToLinearHeading(gatePosNear2, posMultiplier*Math.toRadians(-90.0), driveSpeed)
                .waitSeconds(1.0)
                .setTangent(posMultiplier*Math.toRadians(135.0))
                .splineToLinearHeading(shootPosNear3, posMultiplier*Math.toRadians(135.0), driveSpeed)

                //toPickup3
                .setTangent(posMultiplier*Math.toRadians(0.0))
                .splineToLinearHeading(prePickupFar, posMultiplier*Math.toRadians(-15.0), driveSpeed)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(startPickupFar, posMultiplier*Math.toRadians(-90.0), driveSpeed)

                //pickup3
                .setTangent(posMultiplier*Math.toRadians(-90))
                .splineToLinearHeading(endPickupFar, posMultiplier*Math.toRadians(-90.0), intakeSpeed)

                //toShoot4
                //noGate
//                .setTangent(posMultiplier*Math.toRadians(160.0))
//                .splineToLinearHeading(shootPosNear4, posMultiplier*Math.toRadians(160.0), driveSpeed)
                //yesGate
                .setTangent(posMultiplier*Math.toRadians(180.0))
                .splineToLinearHeading(gatePosNear3, posMultiplier*Math.toRadians(-90.0), driveSpeed)
                .waitSeconds(1.0)
                .setTangent(posMultiplier*Math.toRadians(145.0))
                .splineToLinearHeading(shootPosNear4, posMultiplier*Math.toRadians(145.0), driveSpeed)


                .build()
        );//*/

        //Premier Auto Far
        /*
        myBot.runAction(myBot.getDrive().actionBuilder(startPosFar)
                        //toShoot1
//                        .waitSeconds(3.0)
//                        .setTangent(posMultiplier*Math.toRadians(0.0))
//                        .splineToLinearHeading(shootPosFar1, posMultiplier*Math.toRadians(0.0), driveSpeed)
                        //toPickup1
                        .setTangent(posMultiplier*Math.toRadians(180.0))
                        .splineToLinearHeading(prePickupFar, posMultiplier*Math.toRadians(-90.0), driveSpeed)
                        .setTangent(posMultiplier*Math.toRadians(-90.0))
                        .splineToLinearHeading(startPickupFar, posMultiplier*Math.toRadians(-90.0), driveSpeed)

                        //pickup1
                        .setTangent(posMultiplier*Math.toRadians(-90))
                        .splineToLinearHeading(endPickupFar, posMultiplier*Math.toRadians(-90.0), intakeSpeed)

                        //toShoot2
                        .setTangent(posMultiplier*Math.toRadians(52.5))
                        .splineToLinearHeading(shootPosFar2, posMultiplier*Math.toRadians(52.5), driveSpeed)

                        //toPickup2
                        .setTangent(posMultiplier*Math.toRadians(-100.0))
                        .splineToLinearHeading(prePickupCorner, posMultiplier*Math.toRadians(-100.0), driveSpeed)
                        .setTangent(posMultiplier*Math.toRadians(-90.0))
                        .splineToLinearHeading(startPickupCorner, posMultiplier*Math.toRadians(-90.0), driveSpeed)
//
                        //pickup2
                        .setTangent(posMultiplier*Math.toRadians(0.0))
                        .splineToLinearHeading(midPickupCorner, posMultiplier*Math.toRadians(0.0), intakeSpeed)
                        .setTangent(posMultiplier*Math.toRadians(0.0))
                        .splineToLinearHeading(endPickupCorner, posMultiplier*Math.toRadians(0.0), intakeSpeed)

                        //toShoot3
                        .setTangent(posMultiplier*Math.toRadians(95.0))
                        .splineToLinearHeading(shootPosFar3, posMultiplier*Math.toRadians(95.0), driveSpeed)

                        //Slam1
                        .setTangent(posMultiplier*Math.toRadians(-100.0))
                        .splineToLinearHeading(preSlamPos1, posMultiplier*Math.toRadians(-100.0), driveSpeed)
                        .setTangent(posMultiplier*Math.toRadians(180.0))
                        .splineToLinearHeading(slamPos1, posMultiplier*Math.toRadians(180.0), driveSpeed)
                        //Back1
                        .setTangent(posMultiplier*Math.toRadians(60.0))
                        .splineToLinearHeading(shootPosFar4, posMultiplier*Math.toRadians(60.0), driveSpeed)
                        //Slam2
                        .setTangent(posMultiplier*Math.toRadians(-120.0))
                        .splineToLinearHeading(preSlamPos2, posMultiplier*Math.toRadians(-120.0), driveSpeed)
                        .setTangent(posMultiplier*Math.toRadians(-120.0))
                        .splineToLinearHeading(slamPos2, posMultiplier*Math.toRadians(-120.0), driveSpeed)
                        //Back2
                        .setTangent(posMultiplier*Math.toRadians(60.0))
                        .splineToLinearHeading(shootPosFar5, posMultiplier*Math.toRadians(60.0), driveSpeed)
                        //ToPark
                        .setTangent(posMultiplier*Math.toRadians(195.0))
                        .splineToLinearHeading(parkRotationFar, posMultiplier*Math.toRadians(195.0))
                        .setTangent(posMultiplier*Math.toRadians(200.0))
                        .splineToLinearHeading(parkPosFar, posMultiplier*Math.toRadians(200.0), new TranslationalVelConstraint(100.0))

                        .build()
        );//*/

        //Premier Auto Near Gate
//        /*
        myBot.runAction(myBot.getDrive().actionBuilder(startPosNear)
                //toShoot1
                .setTangent(posMultiplier*Math.toRadians(50))
                .splineToLinearHeading(shootPosNear1, posMultiplier*Math.toRadians(50), driveSpeed)

                //toPickup1
                .setTangent(posMultiplier*Math.toRadians(0.0))
                .splineToLinearHeading(prePickupMiddle, posMultiplier*Math.toRadians(-30.0), driveSpeed)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(startPickupMiddle, posMultiplier*Math.toRadians(-90.0), driveSpeed)

                //pickup1
                .setTangent(posMultiplier*Math.toRadians(-90))
                .splineToLinearHeading(endPickupMiddle, posMultiplier*Math.toRadians(-90.0), intakeSpeed)


                //toShoot2
                //noGate
//                .setTangent(posMultiplier*Math.toRadians(140.0))
//                .splineToLinearHeading(shootPosNear3, posMultiplier*Math.toRadians(140.0), rushSpeed)
                //yesGate
                .setTangent(posMultiplier*Math.toRadians(-180.0))
                .splineToLinearHeading(gatePosNear1, posMultiplier*Math.toRadians(-90.0), driveSpeed)
//                .waitSeconds(1.0)
                .setTangent(posMultiplier*Math.toRadians(90.0))
                .splineToLinearHeading(shootPosNear2, posMultiplier*Math.toRadians(180.0), rushSpeed)

                //toPickup2 (gate Intake)
                .setTangent(posMultiplier*Math.toRadians(-10.0))
                .splineToLinearHeading(gateIntakePos, posMultiplier*Math.toRadians(-90.0), driveSpeed)

                //pickup2
                .waitSeconds(3.0)

                //toShoot3
                .setTangent(posMultiplier*Math.toRadians(90.0))
                .splineToLinearHeading(shootPosNear3, posMultiplier*Math.toRadians(180.0), rushSpeed)


                //toPickup3
                .setTangent(posMultiplier*Math.toRadians(0.0))
                .splineToLinearHeading(prePickupNear, posMultiplier*Math.toRadians(-90.0), driveSpeed)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(startPickupNear, posMultiplier*Math.toRadians(-90.0), driveSpeed)


                //pickup3
                .setTangent(posMultiplier*Math.toRadians(-90))
                .splineToLinearHeading(endPickupNear, posMultiplier*Math.toRadians(-90.0), wallIntakeSpeed)

                //toShoot4
                //noGate
                .setTangent(posMultiplier*Math.toRadians(135.0))
                .splineToLinearHeading(shootPosNear4, posMultiplier*Math.toRadians(135.0), rushSpeed)
                //yesGate
//                .setTangent(posMultiplier*Math.toRadians(180.0))
//                .splineToLinearHeading(gatePosNear3, posMultiplier*Math.toRadians(-90.0), driveSpeed)
//                .waitSeconds(1.0)
//                .setTangent(posMultiplier*Math.toRadians(145.0))
//                .splineToLinearHeading(shootPosNear4, posMultiplier*Math.toRadians(145.0), rushSpeed)


                .build()
        );//*/



        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}