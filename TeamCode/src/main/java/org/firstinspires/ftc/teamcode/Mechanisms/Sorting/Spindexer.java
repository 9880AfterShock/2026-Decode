package org.firstinspires.ftc.teamcode.Mechanisms.Sorting;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.sun.tools.javac.util.List;

import org.firstinspires.ftc.teamcode.Enums.BallType;
import org.firstinspires.ftc.teamcode.messages.SpindexerMessage;

import java.util.ArrayDeque;
import java.util.Queue;

public class Spindexer {
    private DcMotorEx motor;
    private Queue<SpindexerMessage> messageQueue = new ArrayDeque<>(100);
    private double ticksPerRotation;
    private List<BallType> balls = List.of(BallType.NONE, BallType.NONE, BallType.NONE);
    private int index = 0;
    public Spindexer(String motorName, LinearOpMode opMode,double ticksPerRotation) {
        this.ticksPerRotation = ticksPerRotation;
        motor = opMode.hardwareMap.get(DcMotorEx.class, motorName);

    }

    public Spindexer(DcMotorEx motor,double ticksPerRotation) {
        this.ticksPerRotation = ticksPerRotation;
        this.motor = motor;
    }

    public void queueMessage(SpindexerMessage message) {
        messageQueue.add(message);
    }

    public void update() {
        SpindexerMessage msg = messageQueue.poll();
        if (msg == null) {msg = SpindexerMessage.NONE;}
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        switch (msg) {
            case LEFT:
                index +=1;
                index %= balls.size();
                motor.setTargetPosition((int) (index*motor.getTargetPosition()+(ticksPerRotation/3)));
                break;
            case RIGHT:
                index -=1;
                index %= balls.size();
                motor.setTargetPosition((int) (index*motor.getTargetPosition()+(ticksPerRotation/3)));
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
