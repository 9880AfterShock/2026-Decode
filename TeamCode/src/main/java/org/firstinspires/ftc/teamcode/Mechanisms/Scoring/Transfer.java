package org.firstinspires.ftc.teamcode.Mechanisms.Scoring;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

public class Transfer {
    private static Servo transfer; // init motor var
    private static OpMode opmode; // opmode var init
    public static double upPosition = 0.7;
    public static double downPosition = 0.85;
    public static double transferTime = -1.0;
    public static String transferState = "Down";

    public static void initTransfer(OpMode opmode) { // init motor
        transfer = opmode.hardwareMap.get(Servo.class, "transfer"); // motor config name
        transferState = "Down";
        transferTime = -1.0;
        Transfer.opmode = opmode;
    }

    public static void updateTransfer(boolean transferButtonPressed) {
        if (transferButtonPressed){
            if (transferTime == -1.0){
                transferTime = opmode.getRuntime();
            }
            if (opmode.getRuntime() - transferTime >= 1.0) {
                transfer.setPosition(upPosition);
                transferState = "Up";
            }
        } else {
            transfer.setPosition(downPosition);
            transferState = "Down";
            transferTime = -1.0;
        }

        opmode.telemetry.addData("Transfer State", transferState);
        opmode.telemetry.addData("Transfer Timer", transferTime);
    }
}
