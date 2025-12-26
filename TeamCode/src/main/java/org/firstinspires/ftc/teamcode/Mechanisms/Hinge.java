package org.firstinspires.ftc.teamcode.Mechanisms;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class Hinge {
    private static Servo hingeLeft; // init servo var
    private static Servo hingeRight; // init servo var
    private static OpMode opmode; // opmode var init
    public static double inPosition = 1.0;
    public static double readyPosition = 0.9;
    public static double outPosition = 0.7;
    public static String lifting = "false";

    public static void initBase(OpMode opmode) { // init motor
        hingeLeft = opmode.hardwareMap.get(Servo.class, "hingeLeft"); //Port 3 on control hub
        hingeRight = opmode.hardwareMap.get(Servo.class, "hingeRight"); //Port 4 on control hub
        Hinge.opmode = opmode;
        lifting = "false";
    }

    public static void updateBase(boolean toggleBase) {
        if (toggleBase){
            if (lifting == "false") {
                lifting = "ready";
            } else {
                if (lifting == "ready") {
                    lifting = "true";
                } else {
                    lifting = "false";
                }
            }
        }

        if (lifting == "false") {
            hingeLeft.setPosition(inPosition);
            hingeRight.setPosition(inPosition);
        } else {
            if (lifting == "ready") {
                hingeLeft.setPosition(readyPosition);
                hingeRight.setPosition(readyPosition);
            } else {
                hingeLeft.setPosition(outPosition);
                hingeRight.setPosition(outPosition);
            }
        }

        opmode.telemetry.addData("Returning to Base?", lifting);
    }
}
