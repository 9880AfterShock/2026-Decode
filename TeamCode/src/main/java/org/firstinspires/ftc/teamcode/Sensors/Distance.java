package org.firstinspires.ftc.teamcode.Sensors;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Distance { // Prefix for commands
    private static OpMode opmode; // opmode var init
    private static DistanceSensor sensorDistance;

    public static void initSensor(OpMode opmode) {
        sensorDistance = opmode.hardwareMap.get(DistanceSensor.class, "distanceSensor");
        Rev2mDistanceSensor sensorTimeOfFlight = (Rev2mDistanceSensor) sensorDistance;
        Distance.opmode = opmode;
    }

    public static void updateSensor() {
        opmode.telemetry.addData("Distance Sensor", sensorDistance.getDistance(DistanceUnit.MM));
    }
}
