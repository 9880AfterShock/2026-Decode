package org.firstinspires.ftc.teamcode.Sensors;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Distance { // Prefix for commands
    private static OpMode opmode; // opmode var init
    private static DistanceSensor sensorDistance;

    public static void initSensor(OpMode opmode) {
        sensorDistance = opmode.hardwareMap.get(DistanceSensor.class, "distanceSensor"); //Plugged into I2C Bus 0 on expansion hub
        Rev2mDistanceSensor sensorTimeOfFlight = (Rev2mDistanceSensor) sensorDistance;
        Distance.opmode = opmode;
    }

    public static void updateSensor() {
        opmode.telemetry.addData("Distance Sensor", sensorDistance.getDistance(DistanceUnit.MM));
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
                return !(sensorDistance.getDistance(DistanceUnit.MM) <= 100 || opmode.getRuntime() - scanTime >= 1.0);
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
                return !(sensorDistance.getDistance(DistanceUnit.MM) >= 100 || opmode.getRuntime() - scanTime >= 1.0);
            }
        };
    }
}
