package org.firstinspires.ftc.teamcode.Mechanisms.Scoring;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Systems.PID;
import org.firstinspires.ftc.teamcode.Systems.PIDAbstract;

import java.util.ArrayList;
import java.util.List;

public class FlywheelMotor {
    private double speed;
    private final PIDAbstract pid;
    private final List<DcMotorEx> motors;
    private final double ticks;
    public void setSpeed(double speed) {
        this.speed = speed;
    }
    public double getSpeed() {
        return (motors.get(0).getVelocity()/ticks)*60;
    }

    public FlywheelMotor(DcMotorEx motor, double ticks) {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        this.motors = List.of(motor);
        this.speed = 0.0;
        this.pid = new PID(0.1,0,0.01);
        this.ticks = ticks;
    }
    public FlywheelMotor(List<DcMotorEx> motors, double ticks) {
        this.motors = motors;
        for (DcMotorEx motor : motors) {
            motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        this.speed = 0.0;
        this.pid = new PID(0.1,0,0.01);
        this.ticks = ticks;
    }

    public void update() {
        if (!motors.isEmpty()) {
            double val = Math.min(1.0,Math.max(-1.0,pid.step((motors.get(0).getVelocity()/ticks)*60)));
            for (DcMotorEx motor : motors) {
                motor.setPower(val);
            }
        }
    }
}
