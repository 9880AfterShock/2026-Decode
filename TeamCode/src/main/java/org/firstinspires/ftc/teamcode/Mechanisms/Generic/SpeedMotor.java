package org.firstinspires.ftc.teamcode.Mechanisms.Generic;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Constants;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Shooter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SpeedMotor {
    private final DcMotorEx motor;
    private final double ticksPerRotation;
    private final double maxRPM;
    private double desiredSpeed;
    private OpMode opMode;
    private double lastPos;
    private double lastTime;
    private double currentSpeed;
    private final double p;
    private final double i;
    private final double d;
    private double lastError;
    private double accum;
    private static final List<Runnable> motorUpdates = new ArrayList<>();
    public SpeedMotor (OpMode opMode, String motorName,double ticksPerRotation, double maxRPM) {
        this.p = Constants.motorProportional;
        this.i = Constants.motorIntegral;
        this.d = Constants.motorDerivative;
        this.lastError = 0;
        this.accum = 0;
        this.desiredSpeed = 0;

        this.maxRPM = maxRPM;
        this.lastPos = 0;
        this.lastTime = opMode.getRuntime();
        this.opMode = opMode;
        this.ticksPerRotation = ticksPerRotation;
        motor = opMode.hardwareMap.get(DcMotorEx.class,motorName);
        motor.setZeroPowerBehavior(FLOAT);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorUpdates.add(this::update);
        desiredSpeed = 0;
        motor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public SpeedMotor (OpMode opMode, String motorName,double ticksPerRotation, double maxRPM, DcMotorSimple.Direction dir) {
        this.p = Constants.motorProportional;
        this.i = Constants.motorIntegral;
        this.d = Constants.motorDerivative;
        this.lastError = 0;
        this.accum = 0;
        this.desiredSpeed = 0;

        this.maxRPM = maxRPM;
        this.lastPos = 0;
        this.lastTime = opMode.getRuntime();
        this.opMode = opMode;
        this.ticksPerRotation = ticksPerRotation;
        motor = opMode.hardwareMap.get(DcMotorEx.class,motorName);
        motor.setZeroPowerBehavior(FLOAT);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorUpdates.add(this::update);
        desiredSpeed = 0;
        motor.setDirection(dir);
    }

    public void update() {
//        double currentPos = Shooter.shooter.getCurrentPosition();
//        double nowTime = opMode.getRuntime();
//        double rotationsPerMinute = Math.abs(((currentPos-lastPos)/maxRPM)/((nowTime-lastTime)/60));
//        double error = desiredSpeed-rotationsPerMinute;
//        if (desiredSpeed == 0) {
//            currentSpeed = 0;
//            motor.setPower(currentSpeed);
//        }else{
//            currentSpeed = Math.max(0,Math.min(1,(((error * p) + ((error - lastError) * d) + (accum * i)) / maxRPM)+currentSpeed));
//            motor.setPower(currentSpeed);
//        }
//        lastPos = currentPos;
//        lastTime = nowTime;
//        accum+=error*i;
//        lastError = error;
        motor.setVelocity(desiredSpeed*ticksPerRotation*60);
    }

    public void setSpeed(double rpm) {
        desiredSpeed = rpm;
    }

    public double getSpeed() {
        return desiredSpeed;
    }

    public static void update_all() {
        for (Runnable motorUpdate : motorUpdates) {
            motorUpdate.run();
        }
    }
}
