package org.firstinspires.ftc.teamcode.Mechanisms.Generic;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import java.util.List;
import java.util.Set;

public class SpeedMotor {
    private final DcMotorEx motor;
    private final double ticksPerRotation;
    private static List<Runnable> motorUpdate;
    public SpeedMotor (OpMode opMode, String motorName,double ticksPerRotation) {
        this.ticksPerRotation = ticksPerRotation;
        motor = opMode.hardwareMap.get(DcMotorEx.class,motorName);
        motor.setZeroPowerBehavior(FLOAT);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public SpeedMotor (OpMode opMode, String motorName,double ticksPerRotation, DcMotorSimple.Direction dir) {
        this.ticksPerRotation = ticksPerRotation;
        motor = opMode.hardwareMap.get(DcMotorEx.class,motorName);
        motor.setZeroPowerBehavior(FLOAT);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setDirection(dir);
    }

    public void update() {

    }

    public static void update_all() {

    }
}
