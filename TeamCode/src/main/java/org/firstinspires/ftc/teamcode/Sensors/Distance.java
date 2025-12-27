package org.firstinspires.ftc.teamcode.Sensors;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.sun.tools.javac.code.Types;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.Spindexer;

public class Distance { // Prefix for commands
    private static OpMode opmode; // opmode var init
    private static DistanceSensor sensorDistanceIntake;
    private static DistanceSensor sensorDistanceSpindexer;

    public static void initSensor(OpMode opmode) {
        sensorDistanceIntake = opmode.hardwareMap.get(DistanceSensor.class, "distanceSensorIntake"); //Plugged into I2C Bus 0 on expansion hub
        sensorDistanceSpindexer = opmode.hardwareMap.get(DistanceSensor.class, "distanceSensorSpindexer"); //Plugged into I2C Bus 0 on control hub
        Rev2mDistanceSensor sensorTimeOfFlightIntake = (Rev2mDistanceSensor) sensorDistanceIntake;
        Rev2mDistanceSensor sensorTimeOfFlightSpindexer = (Rev2mDistanceSensor) sensorDistanceSpindexer;
        Distance.opmode = opmode;
    }

    public static void updateSensor() {
        opmode.telemetry.addData("Distance Sensor Intake", sensorDistanceIntake.getDistance(DistanceUnit.MM));
        opmode.telemetry.addData("Distance Sensor Spindexer", sensorDistanceSpindexer.getDistance(DistanceUnit.MM));
        opmode.telemetry.addData("Ball In Intake", ballInIntake());
        opmode.telemetry.addData("Ball In Spindexer", ballInSpindexer());
    }

    public static boolean ballInSpindexer(){
        return sensorDistanceSpindexer.getDistance(DistanceUnit.MM) <= 150;
    }

    public static boolean ballInIntake(){
        return sensorDistanceIntake.getDistance(DistanceUnit.MM) <= 100;
    }

    public static Action waitForBallIn() {
        return new Action() {
            private boolean first = true;
            double scanTime;
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (first){
                    scanTime = opmode.getRuntime();
                    first = false;
                }
                return !(ballInIntake() || opmode.getRuntime() - scanTime >= 1.0);
            }
        };
    }

    public static Action waitForBallPassed() {
        return new Action() {
            private boolean first = true;
            double scanTime;
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (first){
                    scanTime = opmode.getRuntime();
                    first = false;
                }
                return !(ballInIntake() || opmode.getRuntime() - scanTime >= 1.0);
            }
        };
    }

    public static Action waitForBallInShortDelay() {
        return new Action() {
            private boolean first = true;
            double scanTime;
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (first){
                    scanTime = opmode.getRuntime();
                    first = false;
                }
                return !(ballInIntake() || opmode.getRuntime() - scanTime >= 1.9);
            }
        };
    }

    public static Action waitForBallInSpindexer() {
        return new Action() {
            private boolean first = true;
            double scanTime;
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (first){
                    scanTime = opmode.getRuntime();
                    first = false;
                }
                return !(ballInSpindexer() || opmode.getRuntime() - scanTime >= 1.0);
            }
        };
    }

    public static Action waitForBallInLonger() {
        return new Action() {
            private boolean first = true;
            double scanTime;
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (first){
                    scanTime = opmode.getRuntime();
                    first = false;
                }
                return !(ballInIntake() || opmode.getRuntime() - scanTime >= 2.0);
            }
        };
    }
}
