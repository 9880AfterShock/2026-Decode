package org.firstinspires.ftc.teamcode.Mechanisms.Sorting;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


public class QuickSpindexer { // Prefix for commands
    private static DcMotor spindexer; // init motor var
    private static OpMode opmode; // opmode var init
    private static double targetPosition;
    private static boolean wasClockwise;
    private static boolean wasCounterclockwise;

    public static void initSpindexer(OpMode opmode) { // init motor
        spindexer = opmode.hardwareMap.get(DcMotor.class, "spindexer"); // motor config name
        spindexer.setZeroPowerBehavior(BRAKE);
        targetPosition = 0;

        QuickSpindexer.opmode = opmode;
    }

    public static void updateSpindexer(boolean clockwise, boolean counterclockwise) {

        if (clockwise && !wasClockwise){
            targetPosition += 537.7/3;
        }
        if (counterclockwise && !wasCounterclockwise) {
            targetPosition -= 537.7/3;
        }

        spindexer.setTargetPosition((int) targetPosition);
        wasClockwise = clockwise;
        wasCounterclockwise = counterclockwise;

        spindexer.setPower(1.0);
    }
}