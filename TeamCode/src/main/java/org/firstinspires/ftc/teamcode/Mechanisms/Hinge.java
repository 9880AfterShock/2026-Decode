package org.firstinspires.ftc.teamcode.Mechanisms;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class Hinge {
    private static Servo hingeLeft; // init servo var
    private static Servo hingeRight; // init servo var
    private static OpMode opmode; // opmode var init
    public static double leftInPosition = 0.95;
    public static double leftOutPosition = 0.65;
    public static double rightInPosition = 1.0;
    public static double rightOutPosition = 0.7;
    public static Boolean lifting = false;

    public static void initBase(OpMode opmode) { // init motor
        hingeLeft = opmode.hardwareMap.get(Servo.class, "hingeLeft"); //Port 3 on control hub
        hingeRight = opmode.hardwareMap.get(Servo.class, "hingeRight"); //Port 4 on control hub
        Hinge.opmode = opmode;
        lifting = false;
    }

    public static void updateBase(boolean toggleBase) {
        if (toggleBase){
            lifting = !lifting;
        }

        if (lifting) {
            hingeLeft.setPosition(leftOutPosition);
            hingeRight.setPosition(rightOutPosition);
        } else {
            hingeLeft.setPosition(leftInPosition);
            hingeRight.setPosition(rightInPosition);
        }

        opmode.telemetry.addData("Returning to Base?", lifting);
    }
}
