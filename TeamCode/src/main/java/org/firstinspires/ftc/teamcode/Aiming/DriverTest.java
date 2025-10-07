package org.firstinspires.ftc.teamcode.Aiming;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Maths.Trajectory;
import org.firstinspires.ftc.teamcode.Mechanisms.Generic.SpeedMotor;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Shooter;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Transfer;
import org.firstinspires.ftc.teamcode.States.BallRampState;
import org.firstinspires.ftc.teamcode.Systems.ControlManager;
import org.firstinspires.ftc.teamcode.Systems.DelayedAction;
import org.firstinspires.ftc.teamcode.Systems.RunLater;
import org.firstinspires.ftc.teamcode.messages.BallRamMessage;
import org.firstinspires.ftc.teamcode.messages.SpindexerMessage;

public class DriverTest {
    private static OpMode opmode;
    public static double distanceFromGoal;

    private static final double numTicks = 28;

    private static double lastPos;

    private static double desSpeed = 4000;

    private static double lastTime;
    private static DcMotorEx shooter;

    public static void initControls(OpMode opmode) {
        DriverTest.opmode = opmode;
        shooter = opmode.hardwareMap.get(DcMotorEx.class, "shooter");
        shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter.setVelocity(0);
        distanceFromGoal = 0;
        lastPos = Shooter.shooter.getCurrentPosition();
        lastTime = opmode.getRuntime();
    }

    public static void update(boolean increase, boolean decrease, boolean fire, boolean rev){
        double currentPos = Shooter.shooter.getCurrentPosition();
        double nowTime = opmode.getRuntime();
        double rotationsPerMinute = Math.abs(((currentPos-lastPos)/numTicks)/((nowTime-lastTime)/60));
        if (increase) {
            distanceFromGoal += 0.3048;
            desSpeed = Trajectory.getVelocity(distanceFromGoal,1.1176-0.3937,0.036, Math.toRadians(60)).rpm;
        }
        if (decrease){
            distanceFromGoal -= 0.3048;
            desSpeed = Trajectory.getVelocity(distanceFromGoal,1.1176-0.3937,0.036, Math.toRadians(60)).rpm;
        }
        if (rev&&ControlManager.ballRamp.state == BallRampState.DOWN) {
            shooter.setVelocity((desSpeed*numTicks)/60);
            if (rotationsPerMinute >= desSpeed/2 && fire) {
                ControlManager.ballRamp.queueMessage(BallRamMessage.UP);
                ControlManager.shot = true;
                RunLater.addAction(new DelayedAction(() -> {ControlManager.ballRamp.queueMessage(BallRamMessage.DOWN);}, 0.2));
                ControlManager.spindexer.queueMessage(SpindexerMessage.EJECT);
            }
        } else {
            shooter.setVelocity(0);
        }
        if (!fire) {
            Transfer.updateTransfer(false);
        }

        lastPos = currentPos;
        lastTime = nowTime;

        opmode.telemetry.addData("Can fire? ", rotationsPerMinute >= desSpeed/2);
        opmode.telemetry.addData("Fire?", fire);
        opmode.telemetry.addData("Distance From Goal in feet", distanceFromGoal/0.3048);
        opmode.telemetry.addData("Speed RPM", rotationsPerMinute);
        opmode.telemetry.addData("Desired Speed RPM", desSpeed);
    }
}
