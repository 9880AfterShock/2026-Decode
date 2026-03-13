package com.example.meepmeeprr;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepRR {
    public final static double posMultiplier = 1.0;
    public static void main(String[] args) {
        Pose2d startPosFar = new Pose2d(62.6, posMultiplier*-16.0, posMultiplier*Math.toRadians(0.0)); //tbd
        Pose2d startPosNear = new Pose2d(-55.5, posMultiplier*-47.0, posMultiplier*Math.toRadians(55.0)); //tbd

        Pose2d shootPosFar1 = new Pose2d(54.5, posMultiplier*-13.0, posMultiplier*Math.toRadians(22.5));
        Pose2d shootPosFar2 = new Pose2d(54.5, posMultiplier*-15.0, posMultiplier*Math.toRadians(25.0));
        Pose2d shootPosFar3 = new Pose2d(56.0, posMultiplier*-17.0, posMultiplier*Math.toRadians(22.5));
        Pose2d shootPosFar4 = new Pose2d(56.0, posMultiplier*-17.0, posMultiplier*Math.toRadians(22.5));

        Pose2d shootPosNear1 = new Pose2d(54.5, posMultiplier*-13.0, posMultiplier*Math.toRadians(22.5));

        Pose2d prePickupNear = new Pose2d(-12.0, posMultiplier*-26.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d startPickupNear = new Pose2d(-12.0, posMultiplier*-35.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d endPickupNear = new Pose2d(-12.0, posMultiplier*-50.0, posMultiplier*Math.toRadians(-90.0));

        Pose2d prePickupMiddle = new Pose2d(12.0, posMultiplier*-26.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d startPickupMiddle = new Pose2d(12.0, posMultiplier*-35.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d endPickupMiddle = new Pose2d(12.0, posMultiplier*-50.0, posMultiplier*Math.toRadians(-90.0));

        Pose2d prePickupFar = new Pose2d(36.0, posMultiplier*-26.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d startPickupFar = new Pose2d(36.0, posMultiplier*-35.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d endPickupFar = new Pose2d(36.0, posMultiplier*-50.0, posMultiplier*Math.toRadians(-90.0));

        Pose2d gatePosNear1 = new Pose2d(0.0, posMultiplier*-51.0, posMultiplier*Math.toRadians(-90.0)); //tbd

        Pose2d parkPosFar = new Pose2d(15.0, posMultiplier*-34.0, posMultiplier*Math.toRadians(90)); //tbd


        MeepMeep meepMeep = new MeepMeep(700); //600 is default

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
//                .setColorScheme(new ColorSchemeBlueDark())
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(14.0,17.0) //17.9 when intake out, but to be safe
                .build();


        //Premier Auto Near
        myBot.runAction(myBot.getDrive().actionBuilder(startPosNear)
                .setTangent(Math.toRadians(202.5))
                .splineToLinearHeading(shootPosNear1, Math.toRadians(202.5), new TranslationalVelConstraint(100.0))

                .build()
        );

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}