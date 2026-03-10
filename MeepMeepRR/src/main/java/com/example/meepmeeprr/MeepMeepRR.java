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
        Pose2d startPosFar = new Pose2d(62.6, posMultiplier*-16.0, posMultiplier*Math.toRadians(0.0));

        Pose2d shootPosFar1 = new Pose2d(54.5, posMultiplier*-13.0, posMultiplier*Math.toRadians(22.5));
        Pose2d shootPosFar2 = new Pose2d(54.5, posMultiplier*-15.0, posMultiplier*Math.toRadians(25.0));
        Pose2d shootPosFar3 = new Pose2d(56.0, posMultiplier*-17.0, posMultiplier*Math.toRadians(22.5));
        Pose2d shootPosFar4 = new Pose2d(56.0, posMultiplier*-17.0, posMultiplier*Math.toRadians(22.5));

        Pose2d shootPosNear1 = new Pose2d(54.5, posMultiplier*-13.0, posMultiplier*Math.toRadians(22.5));
        Pose2d shootPosNear2 = new Pose2d(54.5, posMultiplier*-15.0, posMultiplier*Math.toRadians(25.0));
        Pose2d shootPosNear3 = new Pose2d(56.0, posMultiplier*-17.0, posMultiplier*Math.toRadians(22.5));
        Pose2d shootPosNear4 = new Pose2d(56.0, posMultiplier*-17.0, posMultiplier*Math.toRadians(22.5));

        Pose2d prePickupNear1 = new Pose2d(36.0, posMultiplier*-26.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d startPickupNear1 = new Pose2d(36.0, posMultiplier*-35, posMultiplier*Math.toRadians(-90.0));
        Pose2d endPickupNear1 = new Pose2d(36.0, posMultiplier*-50.0, posMultiplier*-Math.toRadians(90.0));

        Pose2d prePickupNear2 = new Pose2d(15.0, posMultiplier*-26.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d startPickupNear2 = new Pose2d(15.0, posMultiplier*-35, posMultiplier*Math.toRadians(-90.0));
        Pose2d endPickupNear2 = new Pose2d(15.0, posMultiplier*-50.0, posMultiplier*-Math.toRadians(90.0));

        Pose2d gatePosNear1 = new Pose2d(0.0, posMultiplier*-51.0, posMultiplier*Math.toRadians(-90.0));

        Pose2d parkPosFar = new Pose2d(15.0, posMultiplier*-34.0, posMultiplier*Math.toRadians(90));


        MeepMeep meepMeep = new MeepMeep(600);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
//                .setColorScheme(new ColorSchemeBlueDark())
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(14.0,17.0) //17.9 when intake out, but to be safe
                .build();


        //New auto far
        myBot.runAction(myBot.getDrive().actionBuilder(startPosFar)
                .setTangent(Math.toRadians(202.5))
                .splineToLinearHeading(parkPosFar, Math.toRadians(202.5), new TranslationalVelConstraint(100.0))

                .build()
        );

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}