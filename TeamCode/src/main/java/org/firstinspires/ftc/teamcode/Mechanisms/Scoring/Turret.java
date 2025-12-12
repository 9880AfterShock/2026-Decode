package org.firstinspires.ftc.teamcode.Mechanisms.Scoring;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class Turret {
    private static OpMode opmode;
    private static Servo leftServo; //Left servo
    private static Servo rightServo; //Right servo
    private static AnalogInput leftEncoder; //Left servo feedback wire
    private static AnalogInput rightEncoder; //Right servo feedback wire
    public static double targetPosition = 0.0;

    public static void initTurret(OpMode opmode) { // init motor
        leftServo = opmode.hardwareMap.get(Servo.class, "leftTurret"); // plugged into ___
//        rightServo = opmode.hardwareMap.get(Servo.class, "rightTurret"); // plugged into ___
        leftEncoder = opmode.hardwareMap.get(AnalogInput.class, "leftTurret"); // plugged into ___
//        rightEncoder = opmode.hardwareMap.get(AnalogInput.class, "rightTurret"); // plugged into ___

        targetPosition = 0.0;
        Turret.opmode = opmode;
    }

    public static void updateTurret(double increment) { // init motor
        targetPosition += increment;

        double difference = targetPosition - getPosition();
        setPower(Range.clip(difference*0.0001,-1,1)); //PID goes here

        opmode.telemetry.addData("Turret", "WIP");
    }

    private static double getPosition(){
        return /*((*/(leftEncoder.getVoltage() / 3.3) * 360/*) + ((rightEncoder.getVoltage() / 3.3) * 360))/2*/; //average 2 encoder poses, might need to reset
    }

    private static void setPower(double motorPower){ //set servo power from a hypothetical point of a motor controlling the turret
        leftServo.setPosition((motorPower/2)+0.5);
//        rightServo.setPosition((motorPower/2)+0.5);
    }
}
