package org.firstinspires.ftc.teamcode.Mechanisms;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class Hinge {
    private static Servo hingeLeft; // init servo var
    private static Servo hingeRight; // init servo var
    private static OpMode opmode; // opmode var init
    public static double inPosition = 1.0;
    public static double outPosition = 0.0;
    public static boolean lifting = false;

    public static void initBase(OpMode opmode) { // init motor
        hingeLeft = opmode.hardwareMap.get(Servo.class, "hingeLeft"); //Port 3 on control hub
        hingeRight = opmode.hardwareMap.get(Servo.class, "hingeRight"); //Port 4 on control hub
        Hinge.opmode = opmode;
        lifting = false;
    }

    public static void updateBase(boolean toggleBase) {
        if (toggleBase){
            if (lifting) {
                lifting = false;
            } else {
                lifting = true;
            }
        }

        if (lifting) {
            hingeLeft.setPosition(outPosition);
            hingeRight.setPosition(outPosition);
        } else {
            hingeLeft.setPosition(inPosition);
            hingeRight.setPosition(inPosition);
        }

        opmode.telemetry.addData("Returning to Base?", lifting);
    }
}
