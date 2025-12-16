package com.example.meepmeeprr;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepRR {
    public final static double posMultiplier = 1.0;
    public static void main(String[] args) {
//        Pose2d startPosClose = new Pose2d(-55.5, -47.0, Math.toRadians(55.0));
//        Pose2d scanPos = new Pose2d(-27.0, -27.0, Math.toRadians(-25.0));
//        Pose2d shootPosClose = new Pose2d(-33.0, -33.0, Math.toRadians(55));
//        Pose2d parkPosClose = new Pose2d(-60.0, -35.0, Math.toRadians(0.0));
//        Pose2d startPickup1 = new Pose2d(-12.0, -30.0, Math.toRadians(-90.0));
//        Pose2d endPickup1 = new Pose2d(-12.0, -55.0, -Math.toRadians(90.0));
//        Pose2d startPickup2 = new Pose2d(12.0, -30.0, -Math.toRadians(90.0));
//        Pose2d endPickup2 = new Pose2d(12.0, -55.0, -Math.toRadians(90.0));
//        Pose2d startPickup3 = new Pose2d(35.5, -25.0, -Math.toRadians(90.0));
//        Pose2d endPickup3 = new Pose2d(35.5, -55.0, -Math.toRadians(90.0));
//        Pose2d gatePose = new Pose2d(0.0, -55.0, Math.toRadians(0.0));
//        Pose2d gatePose1 = new Pose2d(0.0, -55.0, Math.toRadians(-90.0));
//        Pose2d startPosFar = new Pose2d(62.6, -16.0, Math.toRadians(0.0)); //need to figure out
//        Pose2d shootPosFar = new Pose2d(57.5, -12.0, Math.toRadians(22.5));
//        Pose2d parkPosFar = new Pose2d(60.0, -38.0, Math.toRadians(90.0));
        Pose2d startPosClose = new Pose2d(-51.5, posMultiplier*-50.5, posMultiplier*Math.toRadians(-35.0));
        Pose2d scanPos = new Pose2d(-35.0, posMultiplier*-35.0, posMultiplier*Math.toRadians(-45.0));

        Pose2d shootPosClose1 = new Pose2d(-20.0, posMultiplier*-25.0, posMultiplier*Math.toRadians(40.0));
        Pose2d shootPosClose2 = new Pose2d(-24.0, posMultiplier*-25.0, posMultiplier*Math.toRadians(40.0));
        Pose2d shootPosClose3 = new Pose2d(-32.0, posMultiplier*-18.0, posMultiplier*Math.toRadians(55.0));

        Pose2d prePickup1 = new Pose2d(-12.0, posMultiplier*-26.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d startPickup1 = new Pose2d(-12.0, posMultiplier*-30.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d endPickup1 = new Pose2d(-12.0, posMultiplier*-45.0, posMultiplier*-Math.toRadians(90.0));

        Pose2d prePickup2 = new Pose2d(12.0, posMultiplier*-24.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d startPickup2 = new Pose2d(12.0, posMultiplier*-30.0, posMultiplier*Math.toRadians(-90.0));
        Pose2d endPickup2 = new Pose2d(12.0, posMultiplier*-45.0, posMultiplier*-Math.toRadians(90.0));

        MeepMeep meepMeep = new MeepMeep(600);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .setDimensions(14.0,15.6) //17.9 when intake out, but to be safe
                .build();

        //New auto
        myBot.runAction(myBot.getDrive().actionBuilder(startPosClose)
                //toScan
                .setTangent(posMultiplier*Math.toRadians(55.0))
                .splineToLinearHeading(scanPos, posMultiplier*Math.toRadians(45.0))
                        .waitSeconds(2.0) //scanning

                //toShoot1
                .setTangent(posMultiplier*Math.toRadians(30.0))
                .splineToLinearHeading(shootPosClose1, posMultiplier*Math.toRadians(30.0))
                        .waitSeconds(4.0) //shooting

                //to shoot no scan
//                .setTangent(posMultiplier*Math.toRadians(35.0))
//                .splineToLinearHeading(shootPosClose1, posMultiplier*Math.toRadians(35.0));

                //toPickup1
                .setTangent(posMultiplier*Math.toRadians(0.0))
                .splineToLinearHeading(prePickup1, posMultiplier*Math.toRadians(0.0))
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(startPickup1, posMultiplier*Math.toRadians(-90.0))

                //pickUp1
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(endPickup1, posMultiplier*Math.toRadians(-90.0), new TranslationalVelConstraint(5.0))

                //toShoot2
                .setTangent(posMultiplier*Math.toRadians(125.0))
                .splineToLinearHeading(shootPosClose2, posMultiplier*Math.toRadians(125.0))
                        .waitSeconds(4.0) //shooting

                //toPickup2
                .setTangent(posMultiplier*Math.toRadians(0.0))
                .splineToLinearHeading(prePickup2, posMultiplier*Math.toRadians(0.0))
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(startPickup2, posMultiplier*Math.toRadians(-90.0))

                //pickup2
                .setTangent(posMultiplier*Math.toRadians(-90.0))
                .splineToLinearHeading(endPickup2, posMultiplier*Math.toRadians(-90.0), new TranslationalVelConstraint(5.0))

                //toShoot3
                .setTangent(posMultiplier*Math.toRadians(145.0))
                .splineToLinearHeading(shootPosClose3, posMultiplier*Math.toRadians(145.0))
                        .waitSeconds(4.0) //shooting

                .build()
        );

//        //Far auto
//        myBot.runAction(myBot.getDrive().actionBuilder(startPosFar)
//                //scan
//                .waitSeconds(1.0)
//
//                //to shoot
//                .setTangent(Math.toRadians(110.0))
//                .splineToLinearHeading(shootPosFar, Math.toRadians(110.0))
//                //shoot
//                .waitSeconds(3.0)
//
//                //topickup3
//                .setTangent(Math.toRadians(180))
//                .splineToLinearHeading(startPickup3, Math.toRadians(-90))
//                //intake on
//                //pickup3
//                .setTangent(Math.toRadians(-90.0))
//                .splineToLinearHeading(endPickup3, Math.toRadians(-90.0))
//                //intake off?
//                //back to shoot
//                .setTangent(Math.toRadians(45.0))
//                .splineToLinearHeading(shootPosFar, Math.toRadians(45.0))
//                //shoot
//                .waitSeconds(3)
//
//                //topickup2
//                .setTangent(Math.toRadians(-150.0))
//                .splineToLinearHeading(startPickup2, Math.toRadians(-150.0))
//                //intake on
//                //pickup2
//                .setTangent(Math.toRadians(-90.0))
//                .splineToLinearHeading(endPickup2, Math.toRadians(-90.0))
//                //intake off?
//
//                ///*
//                //back to shoot no gate
//                .setTangent(Math.toRadians(35.0))
//                .splineToLinearHeading(shootPosFar, Math.toRadians(35.0))
//                //shoot
//                .waitSeconds(3)
//                //*/
//
//                /*
//                //to gate
//                .setTangent(Math.toRadians(125.0))
//                .splineToLinearHeading(gatePose, Math.toRadians(-90.0))
//                //wait for balls to roooooolllll
//                .waitSeconds(8.0)
//                //back to shoot
//                .setTangent(Math.toRadians(40.0))
//                .splineToLinearHeading(shootPosFar, Math.toRadians(40.0))
//                //shoot
//                .waitSeconds(3)
//                */
//
//                //topickup1
//                .setTangent(Math.toRadians(200.0))
//                .splineToLinearHeading(startPickup1, Math.toRadians(200.0))
//                //intake on
//                //pickup1
//                .setTangent(Math.toRadians(-90.0))
//                .splineToLinearHeading(endPickup1, Math.toRadians(-90.0))
//                //intake off?
//                //back to shoot
//                .setTangent(Math.toRadians(30.0))
//                .splineToLinearHeading(shootPosFar, Math.toRadians(30.0))
//                //shoot
//                .waitSeconds(3)
//
//                //to park
//                .setTangent(Math.toRadians(-90))
//                .splineToLinearHeading(parkPosFar, Math.toRadians(-90))
//
//                .build()
//        );
//        //Near Auto
//        myBot.runAction(myBot.getDrive().actionBuilder(startPosClose)
//                //to scan
//                .setTangent(Math.toRadians(55.0))
//                .splineToLinearHeading(scanPos, Math.toRadians(45.0))
//                //scan
//                .waitSeconds(1)
//
//                //to shoot
//                .setTangent(Math.toRadians(-125.0))
//                .splineToLinearHeading(shootPosClose, Math.toRadians(-125.0))
//                //shoot
//                .waitSeconds(3)
//
//                //topickup1
//                .setTangent(Math.toRadians(45.0))
//                .splineToLinearHeading(startPickup1, Math.toRadians(-45.0))
//                //intake on
//                //pickup1
//                .setTangent(Math.toRadians(-90.0))
//                .splineToLinearHeading(endPickup1, Math.toRadians(-90.0))
//                //intake off?
//
//                //toGateintead
//                .setTangent(Math.toRadians(90))
//                .splineToLinearHeading(gatePose1, Math.toRadians(-90))
//
//                //back to shoot from gate
//                .setTangent(Math.toRadians(125.0))
//                .splineToLinearHeading(shootPosClose, Math.toRadians(125.0))
//
//                /*
//                //back to shoot
//                .setTangent(Math.toRadians(125.0))
//                .splineToLinearHeading(shootPosClose, Math.toRadians(125.0))
//                 */
//                //shoot
//                .waitSeconds(3)
//
//                //topickup2
//                .setTangent(Math.toRadians(30.0))
//                .splineToLinearHeading(startPickup2, Math.toRadians(-30.0))
//                //intake on
//                //pickup2
//                .setTangent(Math.toRadians(-90.0))
//                .splineToLinearHeading(endPickup2, Math.toRadians(-90.0))
//                //intake off?
//
//                ///*
//                //back to shoot no gate
//                .setTangent(Math.toRadians(145.0))
//                .splineToLinearHeading(shootPosClose, Math.toRadians(145.0))
//                //shoot
//                .waitSeconds(3)
//                //*/
//
//                /*
//                //to gate
//                .setTangent(Math.toRadians(125.0))
//                .splineToLinearHeading(gatePose, Math.toRadians(-90.0))
//                //wait for balls to roooooolllll
//                .waitSeconds(8.0)
//                //back to shoot
//                .setTangent(Math.toRadians(130.0))
//                .splineToLinearHeading(shootPosClose, Math.toRadians(130.0))
//                //shoot
//                .waitSeconds(3)
//                */
//
//                //topickup3
//                .setTangent(Math.toRadians(20.0))
//                .splineToLinearHeading(startPickup3, Math.toRadians(-20.0))
//                //intake on
//                //pickup3
//                .setTangent(Math.toRadians(-90.0))
//                .splineToLinearHeading(endPickup3, Math.toRadians(-90.0))
//                //intake off?
//                //back to shoot
//                .setTangent(Math.toRadians(150.0))
//                .splineToLinearHeading(shootPosClose, Math.toRadians(150.0))
//                //shoot
//                .waitSeconds(3)
//
//                //to park
//                .setTangent(Math.toRadians(180.0))
//                .splineToLinearHeading(parkPosClose, Math.toRadians(-180.0))
//
//                .build()
//        );

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}