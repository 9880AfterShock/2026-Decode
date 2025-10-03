package org.firstinspires.ftc.teamcode.Mechanisms.Scoring;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.States.BallRamState;
import org.firstinspires.ftc.teamcode.messages.BallRamMessage;
import org.firstinspires.ftc.teamcode.messages.SpindexerMessage;

import java.util.ArrayDeque;
import java.util.Queue;

public class BallRam {
    private OpMode opMode;
    private Servo servo;
    private double downPos;
    private double upPos;
    private BallRamState state;
    public Queue<BallRamMessage> messageQueue = new ArrayDeque<>(100);
    public BallRam(OpMode opMode, String servoName, double downPos, double upPos) {
        this.opMode = opMode;
        this.servo = opMode.hardwareMap.get(Servo.class,servoName);
        this.downPos = downPos;
        this.upPos = upPos;
        this.state = BallRamState.UP;
    }

    public void queueMessage(BallRamMessage message) {
        messageQueue.add(message);
    }

    public void update() {
        BallRamMessage msg = messageQueue.poll();
        if (msg == null) {return;}
        switch (msg) {
            case UP:
                servo.setPosition(upPos);
                this.state = BallRamState.UP;
                break;
            case DOWN:
                servo.setPosition(downPos);
                this.state = BallRamState.DOWN;
                break;
            case CYCLE:
                switch (state) {
                    case UP:
                        servo.setPosition(downPos);
                        this.state = BallRamState.DOWN;
                        break;
                    case DOWN:
                        servo.setPosition(upPos);
                        this.state = BallRamState.UP;
                        break;
                }
                break;
        }
    }
}
