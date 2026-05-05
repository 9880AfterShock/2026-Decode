package org.firstinspires.ftc.teamcode.Mechanisms.Scoring;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.WrapperClasses.BulkWriteServo;


public class QuickBallRamp { // Prefix for commands

    private static BulkWriteServo ballRamp; // init servo var
    private static Servo ballRampServo;
    private static OpMode opmode; // opmode var init
    public static double downPosition = 0.08;
    public static double upPosition = 0.57;

    public static void initIntake(OpMode opmode) { // init motor
        ballRampServo = opmode.hardwareMap.get(Servo.class, "ramp"); // servo config name
        ballRamp = new BulkWriteServo(ballRampServo);
        QuickBallRamp.opmode = opmode;
    }

    public static Action AutoRampUp() {
        return new Action() {
            private double startTime = opmode.getRuntime();
            private double waitTime = 0.6;
            public boolean run(@NonNull TelemetryPacket packet) {
                ballRampServo.setPosition(upPosition);
                return false;
            }
        };
    }
    public static Action AutoRampDown() {
        return new Action() { //SleepAction
            private double startTime = opmode.getRuntime();
            private double waitTime = 0.6;
            public boolean run(@NonNull TelemetryPacket packet) {
                ballRampServo.setPosition(downPosition);
                return (startTime+waitTime > opmode.getRuntime());
            }
        };
    }
}