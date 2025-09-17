package org.firstinspires.ftc.teamcode.Sensors;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Distance { // Prefix for commands
    private static OpMode opmode; // opmode var init
    private static Rev2mDistanceSensor sensorDistance;

    public static void initSensor(OpMode opmode) {
        sensorDistance = opmode.hardwareMap.get(Rev2mDistanceSensor.class, "distanceSensor");
    }

    public static void updateSensor() {
        opmode.telemetry.addData("Distance Sensor", sensorDistance.getDistance(DistanceUnit.MM));
    }
}
