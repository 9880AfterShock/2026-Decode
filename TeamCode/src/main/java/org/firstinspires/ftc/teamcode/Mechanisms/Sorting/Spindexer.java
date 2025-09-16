package org.firstinspires.ftc.teamcode.Mechanisms.Sorting;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.sun.tools.javac.util.List;

import org.firstinspires.ftc.teamcode.Enums.BallType;
import org.firstinspires.ftc.teamcode.OpModes.TeleOp;
import org.firstinspires.ftc.teamcode.messages.SpindexerMessage;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class Spindexer {
    private DcMotorEx motor;
    public Queue<SpindexerMessage> messageQueue = new ArrayDeque<>(100);
    private double ticksPerRotation;
    public List<BallType> balls = List.of(BallType.NONE, BallType.NONE, BallType.NONE);
    private int index = 0;
    private int offset = 0;
    private TeleOp opmode;
    public Spindexer(String motorName, TeleOp opMode, double ticksPerRotation) {
        opmode = opMode;
        this.ticksPerRotation = ticksPerRotation;
        motor = opMode.hardwareMap.get(DcMotorEx.class, motorName);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setTargetPosition(0);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(1.0);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    public Spindexer(DcMotorEx motor,double ticksPerRotation) {
        this.ticksPerRotation = ticksPerRotation;
        this.motor = motor;
        this.motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motor.setTargetPosition(0);
        this.motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.motor.setPower(1.0);
        this.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void queueMessage(SpindexerMessage message) {
        messageQueue.add(message);
    }

    private int getPosForIndex(int index) {
        if (index >= balls.size()) {
            offset += 1;
        } else if (index < 0) {
            offset -= 1;
        }
        return (int) (motor.getTargetPosition()+((index%balls.size())*(ticksPerRotation/3))+(ticksPerRotation*offset));
    }

    public void update() {
        SpindexerMessage msg = messageQueue.poll();
        if (msg == null) {msg = SpindexerMessage.NONE;}
        switch (msg) {
            case LEFT:
                index +=1;
                motor.setTargetPosition(getPosForIndex(index));
                index %= balls.size();
                break;
            case RIGHT:
                index -=1;
                motor.setTargetPosition(getPosForIndex(index));
                index %= balls.size();
                break;
            case INGREEN:
                balls.set(index, BallType.GREEN);
                break;
            case INPURPLE:
                balls.set(index, BallType.PURPLE);
                break;
            case EJECT:
                balls.set(index, BallType.NONE);
                break;
            case INUNKOWN:
                balls.set(index, BallType.UNKOWN);
                break;
            case NONE:

                break;
        }
    }
}
