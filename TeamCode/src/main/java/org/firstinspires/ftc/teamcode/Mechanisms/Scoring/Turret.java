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

import org.firstinspires.ftc.teamcode.Systems.PID;
import org.firstinspires.ftc.teamcode.WrapperClasses.BulkWriteServo;

@Config
public class Turret {
    private static OpMode opmode;
    private static BulkWriteServo leftServo; //Left servo
    private static BulkWriteServo rightServo; //Right servo
    private static Servo leftServoReal; //Left servo
    private static Servo rightServoReal; //Right servo
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
    public static double kStatic = 0.08;

    public static boolean leftWorking = true; //backup checks on analog input wires
    public static boolean rightWorking = true;

    public static final double leftOffset =  77.65793528505394;
    public static final double rightOffset = 54.5824345146379;

    public static double P = 0.005;
    public static double D = 0.00155;


    public static PID mainPID;

    //pid should be around (0.02, 0.0005, 0.0025); for one servo, what about turret?

    public static void initTurret(OpMode opmode) { // init motor
        leftServoReal = opmode.hardwareMap.get(Servo.class, "leftTurret"); // plugged into Expansion Hub Port 4
        rightServoReal = opmode.hardwareMap.get(Servo.class, "rightTurret"); // plugged into Control Hub Port 1
        leftServo = new BulkWriteServo(leftServoReal);
        rightServo = new BulkWriteServo(rightServoReal);
//        encoder = opmode.hardwareMap.get(AnalogInput.class, "axonEncoder"); // plugged into ___
        leftEncoder = opmode.hardwareMap.get(AnalogInput.class, "leftEncoder"); // plugged into CH 0
        rightEncoder = opmode.hardwareMap.get(AnalogInput.class, "rightEncoder"); // plugged into CH 1

        targetPosition = 0.0;
        leftCurrentPosition = getPosition(leftEncoder.getVoltage());
        rightCurrentPosition = getPosition(rightEncoder.getVoltage());
        Turret.opmode = opmode;

        leftWorking = true;
        rightWorking = true;
        mainPID = new PID(P,0.0,D);
    }

    public static void updateTurret(boolean overRide, double overRideDegrees) {
        if (overRide){
            targetPosition = overRideDegrees;
        }

        updatePosition();

        leftWorking = leftEncoder.getVoltage() != 0;
        rightWorking = leftEncoder.getVoltage() != 0;
        if (leftWorking && rightWorking){
            currentPosition = (((leftCurrentPosition - leftOffset) + (rightCurrentPosition - rightOffset)) / 2) * (24.0/78);
        } else {
            if (leftWorking){
                currentPosition = ((leftCurrentPosition - leftOffset) * (24.0/78));
            } else {
                if (rightWorking){
                    currentPosition = ((rightCurrentPosition - rightOffset) * (24.0/78));
                }
            }
        }

        targetPosition = Range.clip(targetPosition, minTurret, maxTurret); //cap range for safety

        double difference = (targetPosition - currentPosition);
        double diffSign;
        if (Math.abs(difference) > 2){
            if (difference > 0){
                diffSign = 1;
            } else {
                diffSign = -1;
            }
        } else {
            diffSign = 0.0; //freeze static boost if we are close enough to target
        }

//        leftServo.setPosition(calcPower(kStatic));
//        rightServo.setPosition(calcPower(kStatic));
        if (leftWorking || rightWorking){
            leftServo.setPosition(calcPower(Range.clip(mainPID.step(difference) + (diffSign*kStatic),-1,1))); //PID goes here
            rightServo.setPosition(calcPower(Range.clip(mainPID.step(difference) + (diffSign*kStatic),-1,1))); //PID goes here
        } else {
            leftServo.setPosition(calcPower(0.00001));
            rightServo.setPosition(calcPower(0.00001)); //lock turret?
        }

        opmode.telemetry.addData("Turret:___", "WIP");
        opmode.telemetry.addData("-TargetPos", targetPosition);
        opmode.telemetry.addData("-CurrentPos", currentPosition);
        opmode.telemetry.addData("-CurrentPosLeft", leftCurrentPosition + leftOffset);
        opmode.telemetry.addData("-CurrentPosRight", rightCurrentPosition + rightOffset);
        opmode.telemetry.addData("-RawCurrentPosLeft", leftCurrentPosition);
        opmode.telemetry.addData("-RawCurrentPosRight", rightCurrentPosition);
        opmode.telemetry.addData("-LeftEncoderWorking", leftWorking);
        opmode.telemetry.addData("-RightEncoderWorking", rightWorking);
        if (!(leftWorking && rightWorking)) {
            opmode.telemetry.addData("ENCODER DC! ENCODER DC! ENCODER DC! ENCODER DC! ENCODER DC! ENCODER DC! ENCODER DC! ENCODER DC! ENCODER DC! ENCODER DC! ENCODER DC! ENCODER DC! ENCODER DC! ENCODER DC! ENCODER DC! ENCODER DC! ENCODER DC! ENCODER DC! ENCODER DC! ENCODER DC! ENCODER DC! ENCODER DC! ENCODER DC! ENCODER DC! ENCODER DC! ENCODER DC! ENCODER DC! ENCODER DC! ENCODER DC! ENCODER DC! ENCODER DC! ENCODER DC! ", "Axon encoder DC");
        }
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
                updateTurret(true, 0.0);
                leftServo.bulkWrite();
                rightServo.bulkWrite();
                return false;
            }
        };
    }

    public static Action turretLoop() { //only for race actions, never ends!!!
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                updateTurret(false, 0.0);
                leftServo.bulkWrite();
                rightServo.bulkWrite();
                return true;
            }
        };
    }

    public static Action setTurretTarget(double degrees) { //only for race actions, never ends!!!
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                targetPosition = degrees;
                return false;
            }
        };
    }

    public static Action waitForTurret() { //waits for turret to be algined
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                return Math.abs(targetPosition-currentPosition) > 2.0;
            }
        };
    }

    public static void updateTuner(TelemetryPacket packet) {
//        updatePosition();
        leftServoReal.setPosition(calcPower(0.0)); //actually read ctrl hub
        rightServoReal.setPosition(calcPower(0.0)); //actually read ctrl hub
        packet.addLine("Set the leftOffset and rightOffset to these");
        packet.put("Left Raw", getPosition(leftEncoder.getVoltage()));
        packet.put("Right Raw", getPosition(rightEncoder.getVoltage()));
    }
}