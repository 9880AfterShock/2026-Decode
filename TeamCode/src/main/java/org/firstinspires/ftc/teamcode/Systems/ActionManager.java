package org.firstinspires.ftc.teamcode.Systems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Aiming.DriverTest;
import org.firstinspires.ftc.teamcode.Enums.BallType;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.BallRamp;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.Spindexer;
import org.firstinspires.ftc.teamcode.messages.BallRampMessage;
import org.firstinspires.ftc.teamcode.messages.SpindexerMessage;

import java.sql.Driver;
import java.util.Arrays;
import java.util.Random;

import kotlin.random.RandomKt;

public class ActionManager {

    public final Spindexer spindexer;
    public final BallRamp ballRamp;
    private final DcMotorEx shooterUp;
    private final DcMotorEx shooterDown;
    private final OpMode opmode;
    private final int shooterTicks;
    public boolean spindexerBias = false;

    public ActionManager(OpMode opmode, int shooterTicks) {
        this.shooterTicks = shooterTicks;
        spindexerBias = false;
        this.opmode = opmode;
        spindexer = new Spindexer("spindexer", opmode, 1425.1, 11, () -> spindexerBias, Arrays.asList(BallType.GREEN, BallType.PURPLE, BallType.PURPLE));
        ballRamp = new BallRamp(opmode, "ramp", 0.04, 0.22);
        this.shooterUp = opmode.hardwareMap.get(DcMotorEx.class, "shooterUp");
        this.shooterDown = opmode.hardwareMap.get(DcMotorEx.class, "shooterUp");
        shooterUp.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterUp.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterUp.setVelocity(0);
        shooterDown.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterDown.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterDown.setVelocity(0);
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

            DriverTest.desSpeed = rpm;
            DriverTest.update(false, false, false, true, false);

            spindexerBias = true;
//            shooterUp.setVelocity((rpm*shooterTicks)/60);
//            shooterDown.setVelocity((rpm*shooterTicks)/60);
//            shooterUp.setPower(0.82);
//            shooterDown.setPower(0.82);
            //0.8079 was from OBJ
            spindexer.update();
            telemetryPacket.put("Shooter Speed (reving)",(shooterUp.getVelocity()/shooterTicks)*60);
            return false;
        };
    }

    public Action waitForSpeed(double rpm) {

        return new Action() {
            private double lastSpeed = 0.0;

            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                double speed = (shooterUp.getVelocity() / shooterTicks) * 60;
                telemetryPacket.put("Shooter Speed (waitfor)",(shooterUp.getVelocity()/shooterTicks)*60);
                boolean returnv = !( speed >= rpm && speed <= rpm + 100 && Math.abs(speed-lastSpeed) <= 50); //100 is margin of error on upper bound
                lastSpeed = speed;
                return returnv;
            }
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
//                    spindexer.queueMessage(SpindexerMessage.EJECT);
//                    spindexer.update();
                    first = false;
                }
                //spindexer.update();
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
            DriverTest.update(false, false, false, false, false);
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

    public Action rampUp() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                ballRamp.queueMessage(BallRampMessage.UP);
                ballRamp.update();
                return false;
            }
        };
    }

    public Action rampDown() {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                ballRamp.queueMessage(BallRampMessage.DOWN);
                ballRamp.update();
                return false;
            }
        };
    }

    public Action waitForSpeedSafe(double rpm) {
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                double rotationsPerMinute = Math.abs((shooterUp.getVelocity()/shooterTicks)*60);
                telemetryPacket.put("===SAFE RPM===", rotationsPerMinute);
                return Math.abs(rotationsPerMinute-rpm) > 200;
            }
        };
    }
}