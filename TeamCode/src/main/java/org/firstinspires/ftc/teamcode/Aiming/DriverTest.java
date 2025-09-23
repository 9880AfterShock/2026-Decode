package org.firstinspires.ftc.teamcode.Aiming;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class DriverTest {
    private static OpMode opmode;
    public static int distanceFromGoal;

    public static void initControls(OpMode opmode) {
        DriverTest.opmode = opmode;
        distanceFromGoal = 0;
    }

    public static void update(boolean increase, boolean decrease, boolean fire){
        if (increase) {
            distanceFromGoal += 1;
        }
        if (decrease){
            distanceFromGoal -= 1;
        }
        if (fire) {
            CADEN DO YOUR THING HERE
        }

        opmode.telemetry.addData("Distance From Goal", distanceFromGoal);
    }
}
