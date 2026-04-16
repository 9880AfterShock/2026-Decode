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
        Pose2d startPosFar = new Pose2d(62.6, posMultiplier*-16.0, posMultiplier*Math.toRadians(0.0));
        Pose2d startPosNear = new Pose2d(-47.0, posMultiplier*-49.0, posMultiplier*Math.toRadians(0.0));

        Pose2d shootPosFar1 = new Pose2d(54.5, posMultiplier*-13.0, posMultiplier*Math.toRadians(22.5));
        Pose2d shootPosFar2 = new Pose2d(54.5, posMultiplier*-15.0, posMultiplier*Math.toRadians(25.0));
        Pose2d shootPosFar3 = new Pose2d(56.0, posMultiplier*-17.0, posMultiplier*Math.toRadians(22.5));
        Pose2d shootPosFar4 = new Pose2d(56.0, posMultiplier*-17.0, posMultiplier*Math.toRadians(22.5));

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

        Pose2d parkPosFar = new Pose2d(15.0, posMultiplier*-34.0, posMultiplier*Math.toRadians(90)); //tbd

        VelConstraint drive = new MinVelConstraint(Arrays.asList(
                new TranslationalVelConstraint(120.0),
                new AngularVelConstraint(Math.PI)
        ));

        VelConstraint intake = new MinVelConstraint(Arrays.asList(
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
        myBot.runAction(myBot.getDrive().actionBuilder(startPosNear)
                //toShoot1
                .setTangent(posMultiplier*Math.toRadians(50))
                .splineToLinearHeading(shootPosNear1, posMultiplier*Math.toRadians(50), drive)

                //toPickup1
                .setTangent(posMultiplier*Math.toRadians(0.0))
                .splineToLinearHeading(prePickupNear, posMultiplier*Math.toRadians(-90.0), drive)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(startPickupNear, posMultiplier*Math.toRadians(-90.0), drive)

                //pickup1
                .setTangent(posMultiplier*Math.toRadians(-90))
                .splineToLinearHeading(endPickupNear, posMultiplier*Math.toRadians(-90.0), intake)

                //toShoot1
                //noGate
//                .setTangent(posMultiplier*Math.toRadians(110.0))
//                .splineToLinearHeading(shootPosNear2, posMultiplier*Math.toRadians(110.0), drive)
                //yesGate
                .setTangent(posMultiplier*Math.toRadians(90.0))
                .splineToLinearHeading(gatePosNear1, posMultiplier*Math.toRadians(-90.0), drive)
                .waitSeconds(1.0)
                .setTangent(posMultiplier*Math.toRadians(125.0))
                .splineToLinearHeading(shootPosNear2, posMultiplier*Math.toRadians(125.0), drive)

                //toPickup2
                .setTangent(posMultiplier*Math.toRadians(0.0))
                .splineToLinearHeading(prePickupMiddle, posMultiplier*Math.toRadians(-30.0), drive)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(startPickupMiddle, posMultiplier*Math.toRadians(-90.0), drive)

                //pickup2
                .setTangent(posMultiplier*Math.toRadians(-90))
                .splineToLinearHeading(endPickupMiddle, posMultiplier*Math.toRadians(-90.0), intake)

                //toShoot3
                //noGate
//                .setTangent(posMultiplier*Math.toRadians(140.0))
//                .splineToLinearHeading(shootPosNear3, posMultiplier*Math.toRadians(140.0), drive)
                //yesGate
                .setTangent(posMultiplier*Math.toRadians(-180.0))
                .splineToLinearHeading(gatePosNear2, posMultiplier*Math.toRadians(-90.0), drive)
                .waitSeconds(1.0)
                .setTangent(posMultiplier*Math.toRadians(135.0))
                .splineToLinearHeading(shootPosNear3, posMultiplier*Math.toRadians(135.0), drive)

                //toPickup3
                .setTangent(posMultiplier*Math.toRadians(0.0))
                .splineToLinearHeading(prePickupFar, posMultiplier*Math.toRadians(-15.0), drive)
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(startPickupFar, posMultiplier*Math.toRadians(-90.0), drive)

                //pickup3
                .setTangent(posMultiplier*Math.toRadians(-90))
                .splineToLinearHeading(endPickupFar, posMultiplier*Math.toRadians(-90.0), intake)

                //toShoot4
                //noGate
//                .setTangent(posMultiplier*Math.toRadians(160.0))
//                .splineToLinearHeading(shootPosNear4, posMultiplier*Math.toRadians(160.0), drive)
                //yesGate
                .setTangent(posMultiplier*Math.toRadians(180.0))
                .splineToLinearHeading(gatePosNear3, posMultiplier*Math.toRadians(-90.0), drive)
                .waitSeconds(1.0)
                .setTangent(posMultiplier*Math.toRadians(145.0))
                .splineToLinearHeading(shootPosNear4, posMultiplier*Math.toRadians(145.0), drive)


                .build()
        );

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}