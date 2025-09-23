package org.firstinspires.ftc.teamcode.Mechanisms.Sorting;

import static java.util.List.of;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Enums.BallType;
import org.firstinspires.ftc.teamcode.OpModes.TeleOp;
import org.firstinspires.ftc.teamcode.messages.SpindexerMessage;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public class Spindexer {
    private DcMotorEx motor;
    public Queue<SpindexerMessage> messageQueue = new ArrayDeque<>(100);
    private double ticksPerRotation;
    public List<BallType> balls = Arrays.asList(BallType.NONE, BallType.NONE, BallType.NONE);
    public int index = 0;
    private double targetPos = 0;
    public Spindexer(String motorName, TeleOp opMode, double ticksPerRotation) {
        index = 0;
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

    public void update() {
        SpindexerMessage msg = messageQueue.poll();
        if (msg == null) {msg = SpindexerMessage.NONE;}
        switch (msg) {
            case LEFT:
                index +=1;
                targetPos += ticksPerRotation/3;
                motor.setTargetPosition((int) targetPos);
                index %= balls.size();
                break;
            case RIGHT:
                index -=1;
                targetPos -= ticksPerRotation/3;
                motor.setTargetPosition((int) targetPos);
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
