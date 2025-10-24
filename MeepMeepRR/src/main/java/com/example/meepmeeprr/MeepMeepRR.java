package com.example.meepmeeprr;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepRR {
    public static void main(String[] args) {
        Pose2d startPos = new Pose2d(-55.5, -47.0, Math.toRadians(55.0));
        Pose2d scanPos = new Pose2d(-27.0, -27.0, Math.toRadians(-15.0));
        Pose2d shootPos = new Pose2d(-33.0, -33.0, Math.toRadians(55));
        Pose2d parkPos = new Pose2d(-60.0, -35.0, Math.toRadians(0.0));
        Pose2d startPickup1 = new Pose2d(-12.0, -30.0, -Math.toRadians(90.0));
        Pose2d endPickup1 = new Pose2d(-12.0, -55.0, -Math.toRadians(90.0));
        Pose2d startPickup2 = new Pose2d(12.0, -30.0, -Math.toRadians(90.0));
        Pose2d endPickup2 = new Pose2d(12.0, -55.0, -Math.toRadians(90.0));
        Pose2d startPickup3 = new Pose2d(35.0, -30.0, -Math.toRadians(90.0));
        Pose2d endPickup3 = new Pose2d(35.0, -55.0, -Math.toRadians(90.0));

        MeepMeep meepMeep = new MeepMeep(600);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(14.0,15.6) //17.9 when intake out, but to be safe
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(startPos)
                //to scan
                .setTangent(Math.toRadians(55.0))
                .splineToLinearHeading(scanPos, Math.toRadians(45.0))

                .waitSeconds(1)

                //to shoot
                .setTangent(Math.toRadians(-125.0))
                .splineToLinearHeading(shootPos, Math.toRadians(-125.0))

                .waitSeconds(3)

                //topickup1
                .setTangent(Math.toRadians(45.0))
                .splineToLinearHeading(startPickup1, Math.toRadians(-45.0))
                //pickup1
                .setTangent(Math.toRadians(-90.0))
                .splineToLinearHeading(endPickup1, Math.toRadians(-90.0))
                //back to shoot
                .setTangent(Math.toRadians(125.0))
                .splineToLinearHeading(shootPos, Math.toRadians(125.0))

                .waitSeconds(3)

                //topickup2
                .setTangent(Math.toRadians(45.0))
                .splineToLinearHeading(startPickup2, Math.toRadians(-45.0))
                //pickup2
                .setTangent(Math.toRadians(-90.0))
                .splineToLinearHeading(endPickup2, Math.toRadians(-90.0))
                //back to shoot
                .setTangent(Math.toRadians(125.0))
                .splineToLinearHeading(shootPos, Math.toRadians(125.0))

                .waitSeconds(3)

                //topickup3
                .setTangent(Math.toRadians(45.0))
                .splineToLinearHeading(startPickup3, Math.toRadians(-45.0))
                //pickup3
                .setTangent(Math.toRadians(-90.0))
                .splineToLinearHeading(endPickup3, Math.toRadians(-90.0))
                //back to shoot
                .setTangent(Math.toRadians(125.0))
                .splineToLinearHeading(shootPos, Math.toRadians(125.0))

                .waitSeconds(3)

                //to park
                .setTangent(Math.toRadians(180.0))
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