package org.firstinspires.ftc.teamcode.Mechanisms.Sorting;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import static java.lang.Math.abs;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Enums.Motif;
import org.firstinspires.ftc.teamcode.Sensors.Obelisk;
import org.firstinspires.ftc.teamcode.messages.BallRampMessage;
import org.firstinspires.ftc.teamcode.messages.SpindexerMessage;


public class QuickSpindexer { // Prefix for commands
    private static DcMotor spindexer; // init motor var
    private static OpMode opmode; // opmode var init
    private static double targetPosition;
    private static boolean wasClockwise;
    private static boolean wasCounterclockwise;

    public static void initSpindexer(OpMode opmode) { // init motor
        spindexer = opmode.hardwareMap.get(DcMotor.class, "spindexer"); // motor config name
        spindexer.setZeroPowerBehavior(BRAKE);
        spindexer.setTargetPosition(0);
        spindexer.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spindexer.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        targetPosition = 0;

        QuickSpindexer.opmode = opmode;
    }

    public static void updateSpindexer(boolean clockwise, boolean counterclockwise) {

        if (clockwise && !wasClockwise){
            targetPosition += 1425.1/3;
        }
        if (counterclockwise && !wasCounterclockwise) {
            targetPosition -= 1425.1/3;
        }

        spindexer.setTargetPosition((int) targetPosition);
        wasClockwise = clockwise;
        wasCounterclockwise = counterclockwise;

        spindexer.setPower(1.0);
    }

    public static Action goToMotif(){
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (Obelisk.motif == Motif.GPP) {
                    spindexer.setTargetPosition((int) (10.0/360*1425.1)); //10.0 is offset degrees
                }
                if (Obelisk.motif == Motif.PGP) {
                    spindexer.setTargetPosition((int) ((10.0/360*1425.1) - (1425.1/3))); //10.0 is offset degrees
                }
                if (Obelisk.motif == Motif.PPG) {
                    spindexer.setTargetPosition((int) ((10.0/360*1425.1) + (1425.1/3))); //10.0 is offset degrees
                }
                return abs(spindexer.getCurrentPosition()- spindexer.getTargetPosition()) > 40; //40 is tick margin of error
            }
        };
    }

    public static Action turnRight(){
        return new Action() {
            private boolean first = true;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (first) {
                    spindexer.setTargetPosition((int) (spindexer.getTargetPosition()+(1425.1/3)));
                    first = false;
                }
                telemetryPacket.put("Spin Pose", spindexer.getCurrentPosition());
                telemetryPacket.put("Spin Target Pose", spindexer.getTargetPosition());
                return abs(spindexer.getCurrentPosition() - spindexer.getTargetPosition()) > 40; //40 is tick margin of error
            }
        };
    }

    public static Action cycleRampStart(){
        return new Action() {
            @Override
            public  boolean run (@NonNull TelemetryPacket telemetryPacket) {
                spindexer.setTargetPosition((int) (spindexer.getCurrentPosition()-(1425.1/3)/2.5));
                return false;
            }
        };
    }
    public static Action cycleRampEnd(){
        return new Action() {
            @Override
            public  boolean run (@NonNull TelemetryPacket telemetryPacket) {
                spindexer.setTargetPosition((int) (spindexer.getCurrentPosition()+(1425.1/3)/2.5));
                return false;
            }
        };
    }
}