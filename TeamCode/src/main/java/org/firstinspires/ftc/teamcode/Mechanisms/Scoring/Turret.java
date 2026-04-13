package org.firstinspires.ftc.teamcode.Mechanisms.Scoring;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@Config
public class Turret {
    private static OpMode opmode;
    private static Servo leftServo; //Left servo
    private static Servo rightServo; //Right servo
    private static AnalogInput leftEncoder; //Left servo feedback wire
    private static AnalogInput rightEncoder; //Right servo feedback wire
//    private static AnalogInput encoder;
    public static double targetPosition = 0.0;
    public static double leftCurrentPosition = 0.0;
    public static double rightCurrentPosition = 0.0;
    public static double currentPosition = 0.0;
    public static final double minTurret = -75.0;
    public static final  double maxTurret = 75.0;
    private static final double turretCenterOffset = 0.9446299213; //distance the robot is forward from the turret (-23.99360 mm)
    public static double K = 0.005;
    public static double kStatic = 0.07;

    public static final double leftOffset = 0.0;
    public static final double rightOffset = 0.0;

    //pid should be around (0.02, 0.0005, 0.0025); for one servo, what about turret?

    public static void initTurret(OpMode opmode) { // init motor
        leftServo = opmode.hardwareMap.get(Servo.class, "leftTurret"); // plugged into Expansion Hub Port 4
        rightServo = opmode.hardwareMap.get(Servo.class, "rightTurret"); // plugged into Control Hub Port 1
//        encoder = opmode.hardwareMap.get(AnalogInput.class, "axonEncoder"); // plugged into ___
        leftEncoder = opmode.hardwareMap.get(AnalogInput.class, "leftEncoder"); // plugged into CH 0
        rightEncoder = opmode.hardwareMap.get(AnalogInput.class, "rightEncoder"); // plugged into CH 1

        targetPosition = 0.0;
        leftCurrentPosition = getPosition(leftEncoder.getVoltage());
        rightCurrentPosition = getPosition(rightEncoder.getVoltage());
        Turret.opmode = opmode;
    }

    public static void updateTurret(double increment, double degrees) {
        updatePosition();
        currentPosition = (((leftCurrentPosition + leftOffset) + (rightCurrentPosition + rightOffset)) / 2) * (24.0/78);

        double difference = (targetPosition - currentPosition);
        double diffSign;
        if (difference > 0){
            diffSign = 1;
        } else {
            diffSign = -1;
        }
//        leftServo.setPosition(calcPower(kStatic));
//        rightServo.setPosition(calcPower(kStatic));
//        leftServo.setPosition(calcPower(Range.clip(difference*K+(diffSign*kStatic),-1,1))); //PID goes here //ignore for now
//        rightServo.setPosition(calcPower(Range.clip(difference*K+(diffSign*kStatic),-1,1))); //PID goes here //ignore for now

        opmode.telemetry.addData("Turret:___", "WIP");
        opmode.telemetry.addData("-TargetPos", targetPosition);
        opmode.telemetry.addData("-CurrentPos", currentPosition);
        opmode.telemetry.addData("-CurrentPosLeft", leftCurrentPosition + leftOffset);
        opmode.telemetry.addData("-CurrentPosRight", rightCurrentPosition + rightOffset);
        opmode.telemetry.addData("-RawCurrentPosLeft", leftCurrentPosition);
        opmode.telemetry.addData("-RawCurrentPosRight", rightCurrentPosition);
    }

    private static double getPosition(double voltage){
        return ((voltage / 3.245) * 360);
    }

    private static void updatePosition(){
        leftCurrentPosition += normalizeAngle(getPosition(leftEncoder.getVoltage()) - (((leftCurrentPosition % 360) + 360) % 360));
        rightCurrentPosition += normalizeAngle(getPosition(rightEncoder.getVoltage()) - (((rightCurrentPosition % 360) + 360) % 360));
    }

    private static double normalizeAngle(double raw) {
        double fixed = raw;
        if (fixed > 180) {
            fixed -= 360;
        } else if (fixed < -180) {
            fixed += 360;
        }
        return fixed;
    }

    private static double calcPower(double motorPower){ //set servo power from a hypothetical point of a motor controlling the turret
        return (motorPower/2)+0.5;
    }

    public static Pose2d turretTransform(Pose2d beforeTransform, double rotation){ //rotation in degrees
        double x = (turretCenterOffset *Math.cos(rotation)) + beforeTransform.position.x;
        double y = (turretCenterOffset *Math.sin(rotation)) + beforeTransform.position.y;
        return new Pose2d(x, y, beforeTransform.heading.toDouble() + Math.toRadians(currentPosition));
    }

    public static Action lock() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                updateTurret(0.0, 0.0);
                return false;
            }
        };
    }

    public static void updateTuner(TelemetryPacket packet) {
        updatePosition();
        opmode.telemetry.addData("-RawCurrentPosLeft", leftCurrentPosition);
        opmode.telemetry.addData("-RawCurrentPosRight", rightCurrentPosition);
        packet.put("Left Raw", leftCurrentPosition);
        packet.put("Right Raw", rightCurrentPosition);
    }
}