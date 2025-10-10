package org.firstinspires.ftc.teamcode.Systems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.BallRamp;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.Spindexer;
import org.firstinspires.ftc.teamcode.messages.BallRampMessage;
import org.firstinspires.ftc.teamcode.messages.SpindexerMessage;

public class ActionManager {

    private final Spindexer spindexer;
    private final BallRamp ballRamp;
    private final DcMotorEx shooter;
    private final OpMode opmode;
    private final int shooterTicks;
    private double desSpeed;

    public ActionManager(Spindexer spindexer, BallRamp ballRamp, OpMode opmode, int shooterTicks) {
        this.shooterTicks = shooterTicks;
        this.opmode = opmode;
        this.spindexer = spindexer;
        this.ballRamp = ballRamp;
        this.shooter = opmode.hardwareMap.get(DcMotorEx.class, "shooter");
        shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter.setVelocity(0);
    }
    public Action cycleRamp() {
        spindexer.queueMessage(SpindexerMessage.LINEUP);
        RunLater.addAction(new DelayedAction(() -> ballRamp.queueMessage(BallRampMessage.CYCLE), 0.2));
        return new Action() {

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                spindexer.update();
                RunLater.update();
                return RunLater.isEmpty();
            }
        };
    }

    public Action rev(double desSpeed) {
        this.desSpeed = desSpeed;
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                shooter.setVelocity((desSpeed*shooterTicks)/60);
                return true;
            }
        };
    }
    public Action launch() {

        return new Action() {
            private boolean shot = false;
            private boolean doesreturn = false;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (shooter.getVelocity() >= desSpeed/1.1 && !shot) {
                    ballRamp.queueMessage(BallRampMessage.UP);
                    shot = true;
                    RunLater.addAction(new DelayedAction(() -> {ballRamp.queueMessage(BallRampMessage.DOWN); doesreturn = true;}, 0.2));
                    spindexer.queueMessage(SpindexerMessage.EJECT);
                }
                spindexer.update();
                RunLater.update();
                return doesreturn;
            }
        };
    }

    public Action derev() {

        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                shooter.setVelocity(0);
                return true;
            }
        };
    }
}
