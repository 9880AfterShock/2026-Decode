package com.example.meepmeeprr;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepRR {
    public static void main(String[] args) {
        Pose2d startPos = new Pose2d(-55.5, -47.0, Math.toRadians(55.0));
        Pose2d scanPos = new Pose2d(-27.0, -27.0, Math.toRadians(-25.0));
        Pose2d shootPos = new Pose2d(-33.0, -33.0, Math.toRadians(50));
        Pose2d parkPos = new Pose2d(-60.0, -20.0, Math.toRadians(0.0));

        MeepMeep meepMeep = new MeepMeep(600);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(14.0,15.6) //17.9 when intake out, but to be safe
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(startPos)
                .setTangent(Math.toRadians(55.0))
                .splineToLinearHeading(scanPos, Math.toRadians(45.0))
                .setTangent(Math.toRadians(-125.0))
                .splineToLinearHeading(shootPos, Math.toRadians(-125.0))
//                .setTangent(0)
//                .splineToSplineHeading(new Pose2d(48, 48, 0), Math.PI / 2)
                .setTangent(Math.toRadians(135.0))
                .splineToLinearHeading(parkPos, Math.toRadians(-180.0))
                .build()
        );

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}