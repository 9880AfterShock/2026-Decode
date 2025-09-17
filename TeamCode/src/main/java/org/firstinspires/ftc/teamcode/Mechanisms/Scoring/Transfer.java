package org.firstinspires.ftc.teamcode.Mechanisms.Scoring;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class Transfer {
    private static Servo transfer; // init motor var
    private static OpMode opmode; // opmode var init
    public static double upPosition = 0.0;
    public static double downPosition = 1.0;
    public static String transferState = "Down";

    public static void initTransfer(OpMode opmode) { // init motor
        transfer = opmode.hardwareMap.get(Servo.class, "transfer"); // motor config name
        transferState = "Down";
        Transfer.opmode = opmode;
    }

    public static void updateTransfer(boolean transferButtonPressed) {
        if (transferButtonPressed){
            transfer.setPosition(upPosition);
            transferState = "Up";
        } else {
            transfer.setPosition(downPosition);
            transferState = "Down";
        }

        opmode.telemetry.addData("Transfer State", transferState);
    }
}
