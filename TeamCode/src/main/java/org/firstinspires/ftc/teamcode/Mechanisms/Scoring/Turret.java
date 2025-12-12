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
    public static double currentPosition = 0.0;


    public static void initTurret(OpMode opmode) { // init motor
        leftServo = opmode.hardwareMap.get(Servo.class, "leftTurret"); // plugged into ___
//        rightServo = opmode.hardwareMap.get(Servo.class, "rightTurret"); // plugged into ___
        leftEncoder = opmode.hardwareMap.get(AnalogInput.class, "leftEncoder"); // plugged into ___
//        rightEncoder = opmode.hardwareMap.get(AnalogInput.class, "rightEncoder"); // plugged into ___

        targetPosition = 0.0;
        currentPosition = getPosition();
        Turret.opmode = opmode;
    }

    public static void updateTurret(double increment) { // init motor
        targetPosition += increment;

        updatePosition();

        double difference = (targetPosition - currentPosition);
        setPower(Range.clip(difference,-1,1)); //PID goes here

        opmode.telemetry.addData("Turret", "WIP");
        opmode.telemetry.addData("TargetPos", targetPosition);
        opmode.telemetry.addData("CurrentPos", getPosition());
    }

    private static double getPosition(){
        return (((leftEncoder.getVoltage() / 3.3) * 360) + ((rightEncoder.getVoltage() / 3.3) * 360))/2; //average 2 encoder poses, might need to reset
    }

    private static void updatePosition(){
        double diff = getPosition() - (currentPosition%360);
        if (diff > 200) {
            currentPosition += diff-360;
        } else {
            currentPosition += diff;
        }
    }

    private static void setPower(double motorPower){ //set servo power from a hypothetical point of a motor controlling the turret
        leftServo.setPosition((motorPower/2)+0.5);
//        rightServo.setPosition((motorPower/2)+0.5);
    }
}
