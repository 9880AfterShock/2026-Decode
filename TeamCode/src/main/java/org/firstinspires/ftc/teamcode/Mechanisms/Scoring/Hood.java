package org.firstinspires.ftc.teamcode.Mechanisms.Scoring;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class Hood {
    private static Servo hood; // init motor var
    private static OpMode opmode; // opmode var init
    public static double upPosition = 0.0;
    //public static double downPosition = 1.0;

    public static void initAim(OpMode opmode) { // init motor
        hood = opmode.hardwareMap.get(Servo.class, "hood"); // motor config name
        Hood.opmode = opmode;
    }

    public static void goUp() {
        hood.setPosition(upPosition);
        opmode.telemetry.addData("Hood", "Up"); //not rly used lol
    }
}
