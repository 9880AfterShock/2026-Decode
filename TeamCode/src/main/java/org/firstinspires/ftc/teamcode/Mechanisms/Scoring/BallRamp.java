package org.firstinspires.ftc.teamcode.Mechanisms.Scoring;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.States.BallRampState;
import org.firstinspires.ftc.teamcode.Systems.DelayedAction;
import org.firstinspires.ftc.teamcode.Systems.RunLater;
import org.firstinspires.ftc.teamcode.messages.BallRampMessage;

import java.util.ArrayDeque;
import java.util.Queue;

public class BallRamp {
    private final OpMode opMode;
    private final Servo servo;
    private final double downPos;
    private final double upPos;
    public BallRampState state;

    private BallRampMessage latest;
    public Queue<BallRampMessage> messageQueue = new ArrayDeque<>(100);
    public BallRamp(OpMode opMode, String servoName, double downPos, double upPos) {
        this.opMode = opMode;
        this.servo = opMode.hardwareMap.get(Servo.class,servoName);
        this.downPos = downPos;
        this.upPos = upPos;
        servo.setPosition(upPos);
        this.state = BallRampState.UP;
    }

    public void queueMessage(BallRampMessage message) {
        messageQueue.add(message);
    }

    public void update() {
        BallRampMessage msg = messageQueue.poll();
        opMode.telemetry.addData("latest message",latest);
        if (msg == null) {return;}
        switch (msg) {
            case UP:
                servo.setPosition(upPos);
                RunLater.addAction(new DelayedAction(() -> {this.state = BallRampState.UP;}, 0.5));
                break;
            case DOWN:
                servo.setPosition(downPos);
                RunLater.addAction(new DelayedAction(() -> {this.state = BallRampState.DOWN;}, 0.5));
                break;
            case CYCLE:
                switch (state) {
                    case UP:
                        servo.setPosition(downPos);
                        RunLater.addAction(new DelayedAction(() -> {this.state = BallRampState.DOWN;}, 0.5));
                        break;
                    case DOWN:
                        servo.setPosition(upPos);
                        RunLater.addAction(new DelayedAction(() -> {this.state = BallRampState.UP;}, 0.5));
                        break;
                }
                break;
        }
        latest = msg;
    }
}
