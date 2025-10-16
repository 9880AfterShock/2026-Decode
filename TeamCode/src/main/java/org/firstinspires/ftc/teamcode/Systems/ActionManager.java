package org.firstinspires.ftc.teamcode.Systems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Enums.BallType;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.BallRamp;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.Spindexer;
import org.firstinspires.ftc.teamcode.messages.BallRampMessage;
import org.firstinspires.ftc.teamcode.messages.SpindexerMessage;

import java.util.Arrays;

public class ActionManager {

    public final Spindexer spindexer;
    public final BallRamp ballRamp;
    private final DcMotorEx shooter;
    private final OpMode opmode;
    private final int shooterTicks;
    public boolean spindexerBias = false;

    public ActionManager(OpMode opmode, int shooterTicks) {
        this.shooterTicks = shooterTicks;
        spindexerBias = false;
        this.opmode = opmode;
        spindexer = new Spindexer("spindexer", opmode, 1425.1, 10, () -> spindexerBias, Arrays.asList(BallType.GREEN, BallType.PURPLE, BallType.PURPLE));
        ballRamp = new BallRamp(opmode, "ramp", 0.04, 0.22);
        this.shooter = opmode.hardwareMap.get(DcMotorEx.class, "shooter");
        shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter.setDirection(DcMotorSimple.Direction.REVERSE);
        shooter.setVelocity(0);
    }
    public Action cycleRamp() {
        return new Action() {
            private boolean first = true;
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                if (first) {spindexer.queueMessage(SpindexerMessage.LINEUPFixed); RunLater.addAction(new DelayedAction(() -> ballRamp.queueMessage(BallRampMessage.CYCLE), 0.2)); first = false;}
                spindexer.update();
                ballRamp.update();
                RunLater.update();
                return !RunLater.isEmpty();
            }
        };
    }

    public Action rev(double rpm) {
        return telemetryPacket -> {
            spindexerBias = true;
            shooter.setVelocity((rpm*shooterTicks)/60);
            spindexer.update();
            telemetryPacket.put("Speed",(shooter.getVelocity()/shooterTicks)*60);
            return false;
        };
    }

    public Action waitForSpeed(double rpm) {
        return telemetryPacket -> {
            telemetryPacket.put("Speed",(shooter.getVelocity()/shooterTicks)*60);
            return !( (shooter.getVelocity() / shooterTicks) * 60 >= rpm);
        };
    }

    public Action launch() {

        return new Action() {
            private boolean first = true;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (first) {
                    ballRamp.queueMessage(BallRampMessage.UP);
                    ballRamp.update();
                    RunLater.addAction(new DelayedAction(() -> {
                        ballRamp.queueMessage(BallRampMessage.DOWN);
                        ballRamp.update();
                    }, 1.2));
                    RunLater.addAction(new DelayedAction(() -> {},1.4));
                    spindexer.queueMessage(SpindexerMessage.EJECT);
                    spindexer.update();
                    first = false;
                }
                spindexer.update();
                ballRamp.update();
                telemetryPacket.put("runLater actions queued: ",RunLater.getCount());
                RunLater.update();
                return !RunLater.isEmpty();
            }
        };
    }

    public Action derev() {
        return telemetryPacket -> {
            spindexerBias = false;
            shooter.setVelocity(0);
            spindexer.update();
            return false;
        };
    }


    public Action shotCue(int number) {
        return telemetryPacket -> {
            telemetryPacket.put("Shot #", number);
            return false;
        };
    }
}
