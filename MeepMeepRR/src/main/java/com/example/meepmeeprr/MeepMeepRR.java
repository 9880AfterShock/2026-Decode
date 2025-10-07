package com.example.meepmeeprr;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepRR {
    public static void main(String[] args) {
        Pose2d forwardPos = new Pose2d(-30.0, 0.0, 0.0);
        Pose2d backwardPos = new Pose2d(30.0, 0.0, 0.0);
        Pose2d turnPos = new Pose2d(1.0, 1.0, -180.0);

        MeepMeep meepMeep = new MeepMeep(600);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(0, 0, 0))
                .setTangent(Math.toRadians(0.0))
                .splineToLinearHeading(forwardPos, Math.toRadians(0.0))
                .build()
        );

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}