package org.firstinspires.ftc.teamcode.Mechanisms.Sorting;

import static java.util.List.of;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Enums.BallType;
import org.firstinspires.ftc.teamcode.Enums.Motif;
import org.firstinspires.ftc.teamcode.Sensors.Obelisk;
import org.firstinspires.ftc.teamcode.Systems.ConditionAction;
import org.firstinspires.ftc.teamcode.Systems.DelayedAction;
import org.firstinspires.ftc.teamcode.Systems.RunCondition;
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
    private final Supplier<Boolean> isShooting;
    private double targetPos = 0;
    private boolean isLineup = false;
    private static OpMode opmode;
    public static Boolean reset = false;
    public Spindexer(String motorName, OpMode opMode, double ticksPerRotation, double shootBias, Supplier<Boolean> isShooting) {
        index = 0;
        this.ticksPerRotation = ticksPerRotation;
        this.isShooting = isShooting;
        motor = opMode.hardwareMap.get(DcMotorEx.class, motorName);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setTargetPosition(0);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(0.8);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        opmode = opMode;
    }

    public Spindexer(String motorName, OpMode opMode, double ticksPerRotation, double shootBias, Supplier<Boolean> isShooting, List<BallType> startingBalls) {
        this.balls = startingBalls;
        index = 0;
        this.ticksPerRotation = ticksPerRotation;
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
        this.isShooting = () -> false;
        this.motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.motor.setTargetPosition(0);
        this.motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        this.motor.setPower(0.8);
        this.motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    public Spindexer(DcMotorEx motor,double ticksPerRotation, List<BallType> startingBalls) {
        this.balls = startingBalls;
        this.ticksPerRotation = ticksPerRotation;
        this.motor = motor;

        this.isShooting = () -> false;
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
        if (!reset) {
            while (!messageQueue.isEmpty()) {
            //opmode.telemetry.addData("SPINDEXER CURRENT",motor.getCurrent(CurrentUnit.AMPS));
            SpindexerMessage msg = messageQueue.poll();
            if (msg == null) {
                msg = SpindexerMessage.NONE;
            }
            switch (msg) {
                case RIGHT:
                    index = Math.floorMod(index + 1, balls.size());
                    targetPos += ticksPerRotation / 3;
                    motor.setTargetPosition((int) targetPos);
                    break;
                case LEFT:
                    index = Math.floorMod(index - 1, balls.size());
                    targetPos -= ticksPerRotation / 3;
                    motor.setTargetPosition((int) targetPos);
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
                    balls.set(Math.abs(index % balls.size()), BallType.UNKOWN);
                    break;
                case LINEUP:
                    motor.setTargetPosition((int) (targetPos - (ticksPerRotation / 3) / 2.5));
                    isLineup = true;
                    RunCondition.addAction(new ConditionAction(() -> {
                        RunLater.addAction(new DelayedAction(() -> {
                            isLineup = false;
                            motor.setTargetPosition((int) targetPos);

                        }, 0.85));
                    }, this::isLinedUp));
                    break;
                case LINEUPFixed:
                    int fixedTargetPos = motor.getTargetPosition();
                    motor.setTargetPosition((int) (fixedTargetPos - (ticksPerRotation / 3) / 2.5));
                    isLineup = true;
                    RunLater.addAction(new DelayedAction(() -> {
                        isLineup = false;
                        motor.setTargetPosition((int) (fixedTargetPos));

                    }, 0.85));
                    break;
                case NONE:
                    if (!isLineup) {
                        motor.setTargetPosition((int) targetPos);
                    }
                    break;
            }}
        } else {
            motor.setTargetPosition(motor.getTargetPosition() + 30);
        }
    }


    public void gotoBallType(BallType ballType) {
        if (balls.get(Math.abs(index%balls.size())) != ballType) {
            if (balls.get(Math.floorMod(index + 1, balls.size())) == ballType) {
                queueMessage(SpindexerMessage.RIGHT);
            } else if (balls.get(Math.floorMod(index - 1, balls.size())) == ballType) {
                queueMessage(SpindexerMessage.LEFT);
            }
        }
    }

    public boolean checkMotif(int index, Motif motif) {
        if (motif == Motif.GPP) {
            return balls.get(Math.floorMod(index, balls.size())) == BallType.GREEN && balls.get(Math.floorMod(index + 1, balls.size())) == BallType.PURPLE && balls.get(Math.floorMod(index + 2, balls.size())) == BallType.PURPLE;
        } else if (motif == Motif.PGP) {
            return balls.get(Math.floorMod(index, balls.size())) == BallType.PURPLE && balls.get(Math.floorMod(index + 1, balls.size())) == BallType.GREEN && balls.get(Math.floorMod(index + 2, balls.size())) == BallType.PURPLE;
        } else if (motif == Motif.PPG) {
            return balls.get(Math.floorMod(index, balls.size())) == BallType.PURPLE && balls.get(Math.floorMod(index + 1, balls.size())) == BallType.PURPLE && balls.get(Math.floorMod(index + 2, balls.size())) == BallType.GREEN;
        }
        //How did we get here?
        return false;
    }
    public void gotoMotif(Motif motif) {
        if (!checkMotif(index, motif)) {
            if (checkMotif(index + 1, motif)) {
                queueMessage(SpindexerMessage.RIGHT);
            } else if (checkMotif(index - 1, motif)) {
                queueMessage(SpindexerMessage.LEFT);
            }
        }
        update();
    }

    public void resetEncoder() {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        targetPos = 0.0;
    }

    public void setBalls(List<BallType> balls) {
        this.balls = balls;
    }

    public BallType getCurrentBall() {
        return balls.get(index);
    }

    public class Right implements Action {
        private boolean first;
        public Right() {
            first = true;
        }
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            update();
            if (first) {queueMessage(SpindexerMessage.RIGHT); first=false;}
            return !(Math.abs((double) motor.getCurrentPosition() - (targetPos)) < 0.5);
        }
    }

    public class RunToTargetPos implements Action {
        private boolean first;
        private final Runnable run;
        public RunToTargetPos(Runnable run) {
            this.run = run;
            first = true;
        }
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            update();
            if (first) {run.run(); first=false;}
            return !(Math.abs((double) motor.getCurrentPosition() - (targetPos)) < 0.5);
        }
    }

    public class Left implements Action {
        private boolean first;
        public Left() {
            first = true;
        }
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            update();
            if (first) {queueMessage(SpindexerMessage.LEFT); first=false;}
            return !(Math.abs((double) motor.getCurrentPosition() - (targetPos)) < 0.5);
        }
    }
    public Action right() {
        return new Right();
    }
    public Action left() {
        return new Left();
    }

    public class Update implements Action {
        private final Runnable run;
        public Update(Runnable run) {
            this.run = run;
        }
        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            run.run();
            update();
            return false;
        }
    }
    public Action leftQuick() {
        return new Update(() -> queueMessage(SpindexerMessage.LEFT));
    }
    public Action rightQuick() {
        return new Update(() -> queueMessage(SpindexerMessage.RIGHT));
    }

    public Action goToMotif() {
        return new Update(() -> gotoMotif(Obelisk.motif));
    }

    public boolean isLinedUp() {
        return Math.abs(motor.getCurrentPosition()- motor.getTargetPosition()) < (10d/360d)*ticksPerRotation;
    }
}
