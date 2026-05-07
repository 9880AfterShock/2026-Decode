package org.firstinspires.ftc.teamcode.Sensors;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.QuickSpindexer;

public class Distance { // Prefix for commands
    private static OpMode opmode; // opmode var init
    private static DistanceSensor sensorDistanceIntake;
    private static DistanceSensor sensorDistanceSpindexer;
    private static double sensorIntakeCache = 0.0;
    private static double sensorSpindexerCache = 0.0;
    private static int loopTracker = 0;
    private static final int loopInterval = 6;
    public static boolean missedIntake = false;

    public static void initSensor(OpMode opmode) {
        sensorDistanceIntake = opmode.hardwareMap.get(DistanceSensor.class, "distanceSensorIntake"); //Plugged into I2C Bus 0 on expansion hub
        sensorDistanceSpindexer = opmode.hardwareMap.get(DistanceSensor.class, "distanceSensorSpindexer"); //Plugged into I2C Bus 0 on control hub
//        Rev2mDistanceSensor sensorTimeOfFlightIntake = (Rev2mDistanceSensor) sensorDistanceIntake;
//        Rev2mDistanceSensor sensorTimeOfFlightSpindexer = (Rev2mDistanceSensor) sensorDistanceSpindexer;
        Distance.opmode = opmode;
    }

    private static void reRead() {
        sensorIntakeCache = sensorDistanceIntake.getDistance(DistanceUnit.MM);
        sensorSpindexerCache = sensorDistanceSpindexer.getDistance(DistanceUnit.MM);
    }

    public static void updateSensor() {
        loopTracker++;

        opmode.telemetry.addData("Distance Sensor Intake", sensorIntakeCache);
        opmode.telemetry.addData("Distance Sensor Spindexer", sensorSpindexerCache);
        if (loopTracker >= loopInterval) {
            loopTracker = 0;
            reRead();
        }

        if (QuickSpindexer.aligned() && loopTracker == 0){
            QuickSpindexer.hasBall[QuickSpindexer.currentSlot-1] = ballInSpindexer();
        }

        opmode.telemetry.addData("Spindexer Aligned", QuickSpindexer.aligned());
        opmode.telemetry.addData("Ball In Intake", ballInIntake());
        opmode.telemetry.addData("Ball In Spindexer", ballInSpindexer());
    }

    public static boolean ballInSpindexer(){
        return sensorSpindexerCache < 130;
    }

    public static boolean ballInIntake(){
        return sensorIntakeCache < 100;
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

    public static Action waitForBallInTimer(double time) {
        return new Action() {
            private boolean first = true;
            double scanTime;
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (first){
                    scanTime = opmode.getRuntime();
                    first = false;
                }
                return !(ballInIntake() || opmode.getRuntime() - scanTime >= time);
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

    public static Action waitForBallInSpindexer() { //has missed compatibility
        return new Action() {
            private boolean first = true;
            double scanTime;
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (first){
                    scanTime = opmode.getRuntime();
                    first = false;
                }
                missedIntake = (!ballInSpindexer()) && opmode.getRuntime() - scanTime >= 1.0;
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

    public static Action waitForBallInCycles() {
        return new Action() {
            private boolean first = true;
            double scanTime;
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (first){
                    scanTime = opmode.getRuntime();
                    first = false;
                }
                return !(ballInIntake());
            }
        };
    }

    public static Action waitForBallInSpindexerCycles() {
        return new Action() {
            private boolean first = true;
            double scanTime;
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (first){
                    scanTime = opmode.getRuntime();
                    first = false;
                }
                return !(ballInSpindexer() || opmode.getRuntime() - scanTime >= 2.5);
            }
        };
    }

    public static Action waitForBallNotInCycles() {
        return new Action() {
            private boolean first = true;
            double scanTime;
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (first){
                    scanTime = opmode.getRuntime();
                    first = false;
                }
                return ballInIntake();
            }
        };
    }

    public static Action setMissed(boolean state){
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                missedIntake = state;
                return false;
            }
        };
    }
}
