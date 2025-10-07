package org.firstinspires.ftc.teamcode.Mechanisms.Sorting;

import static java.util.List.of;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Enums.BallType;
import org.firstinspires.ftc.teamcode.Systems.DelayedAction;
import org.firstinspires.ftc.teamcode.Systems.RunLater;
import org.firstinspires.ftc.teamcode.messages.SpindexerMessage;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.function.Supplier;

public class Spindexer {
    private final DcMotorEx motor;
    public Queue<SpindexerMessage> messageQueue = new ArrayDeque<>(100);
    private final double ticksPerRotation;
    public List<BallType> balls = Arrays.asList(BallType.NONE, BallType.NONE, BallType.NONE);
    public int index = 0;
    private double shootBias;
    private Supplier<Boolean> isShooting;
    private double targetPos = 0;
    public Spindexer(String motorName, OpMode opMode, double ticksPerRotation, double shootBias, Supplier<Boolean> isShooting) {
        index = 0;
        this.ticksPerRotation = ticksPerRotation;
        this.shootBias = shootBias;
        this.isShooting = isShooting;
        motor = opMode.hardwareMap.get(DcMotorEx.class, motorName);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setTargetPosition(0);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(0.8);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    public Spindexer(DcMotorEx motor,double ticksPerRotation) {
        this.ticksPerRotation = ticksPerRotation;
        this.motor = motor;
        this.motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motor.setTargetPosition(0);
        this.motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.motor.setPower(0.8);
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
                index = Math.floorMod(index+1,balls.size());
                targetPos += ticksPerRotation/3;
                if (isShooting.get()) {
                    motor.setTargetPosition((int) (targetPos+shootBias));
                } else {
                    motor.setTargetPosition((int) targetPos);
                }
                break;
            case RIGHT:
                index = Math.floorMod(index-1,balls.size());
                targetPos -= ticksPerRotation/3;
                if (isShooting.get()) {
                    motor.setTargetPosition((int) (targetPos+shootBias));
                } else {
                    motor.setTargetPosition((int) targetPos);
                }
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
                balls.set(Math.abs(index%balls.size()), BallType.UNKOWN);
                break;
            case LINEUP:
                motor.setTargetPosition((int) (targetPos-(ticksPerRotation/3)/2.5));
                RunLater.addAction(new DelayedAction(() -> {if (isShooting.get()) {
                    motor.setTargetPosition((int) (targetPos+shootBias));
                } else {
                    motor.setTargetPosition((int) targetPos);
                }}, 0.85));
                break;
            case NONE:
                if (isShooting.get()) {
                    motor.setTargetPosition((int) (targetPos+shootBias));
                } else {
                    motor.setTargetPosition((int) targetPos);
                }
                break;
        }
    }

    public void gotoBallType(BallType ballType) {
        if (balls.get(Math.abs(index%balls.size())) != ballType) {
            if (balls.get(Math.floorMod(index + 1, balls.size())) == ballType) {
                queueMessage(SpindexerMessage.LEFT);
            } else if (balls.get(Math.floorMod(index - 1, balls.size())) == ballType) {
                queueMessage(SpindexerMessage.RIGHT);
            }
        }
    }
}
