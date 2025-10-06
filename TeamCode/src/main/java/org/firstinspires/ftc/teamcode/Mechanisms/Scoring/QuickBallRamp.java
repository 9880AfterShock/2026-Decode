package org.firstinspires.ftc.teamcode.Mechanisms.Scoring;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class QuickBallRamp {
    private static Servo transfer; // init motor var
    private static OpMode opmode; // opmode var init
    public static double upPosition = 0.2;
    public static double downPosition = 0.03;
    public static String transferState = "Down";


    public static void initTransfer(OpMode opmode) { // init motor
        transfer = opmode.hardwareMap.get(Servo.class, "ramp"); // motor config name
        transferState = "Down";
        QuickBallRamp.opmode = opmode;
    }

    public static void updateTransfer(boolean transferButtonPressed) {
        if (transferButtonPressed){
            transfer.setPosition(downPosition);
            transferState = "Down";
        } else {
            transfer.setPosition(upPosition);
            transferState = "Up";
        }

        opmode.telemetry.addData("Ramp State", transferState);
    }
}
