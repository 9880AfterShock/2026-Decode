package org.firstinspires.ftc.teamcode.Aiming;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Maths.Trajectory;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Shooter;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Transfer;
import org.firstinspires.ftc.teamcode.Systems.ControlManager;
import org.firstinspires.ftc.teamcode.Systems.DelayedAction;
import org.firstinspires.ftc.teamcode.Systems.RunLater;
import org.firstinspires.ftc.teamcode.messages.BallRampMessage;
import org.firstinspires.ftc.teamcode.messages.SpindexerMessage;

public class DriverTest {
    private static OpMode opmode;
    public static double distanceFromGoal;

    private static final double numTicks = 28;

    private static double lastPos;

    public static double desSpeed = 3300;

    private static double lastTime;
    private static DcMotorEx shooterup;
    private static DcMotorEx shooterdown;

    public static void initControls(OpMode opmode) {
        DriverTest.opmode = opmode;
        shooterup = opmode.hardwareMap.get(DcMotorEx.class, "shooterUp");
        shooterup.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterup.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterup.setVelocity(0);
        shooterdown = opmode.hardwareMap.get(DcMotorEx.class, "shooterDown");
        shooterdown.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterdown.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterdown.setVelocity(0);
        distanceFromGoal = 0;
        lastPos = shooterup.getCurrentPosition();
        lastTime = opmode.getRuntime();
    }

    public static void update(boolean increase, boolean decrease, boolean fire, boolean rev, boolean intake){
        double rotationsPerMinute = Math.abs((shooterup.getVelocity()/numTicks)*60);
//        if (increase) {
//            distanceFromGoal += 0.3048*0.5;
//            desSpeed = Trajectory.getVelocity(distanceFromGoal,1.1176-0.3937,0.036, Math.toRadians(30)).rpm;
////            desSpeed += 100;
//        }
//        if (decrease){
//            distanceFromGoal -= 0.3048*0.5;
//            desSpeed = Trajectory.getVelocity(distanceFromGoal,1.1176-0.3937,0.036, Math.toRadians(30)).rpm;
////            desSpeed -= 100;
//        }
        if (rev) {
            shooterup.setVelocity((desSpeed*numTicks)/60);
            shooterdown.setVelocity((desSpeed*numTicks)/60);
            if (Math.abs(rotationsPerMinute-desSpeed) < 150 && fire) {
                ControlManager.ballRamp.queueMessage(BallRampMessage.UP);
                ControlManager.shot = true;
                RunLater.addAction(new DelayedAction(() -> {ControlManager.ballRamp.queueMessage(BallRampMessage.DOWN);}, 0.2));
                ControlManager.spindexer.queueMessage(SpindexerMessage.EJECT);
            }
        } else {
            shooterup.setVelocity(0);
            shooterdown.setVelocity(0);
        }
        if (!fire && !rev && intake) {
            shooterup.setVelocity(-25*30);
            shooterdown.setVelocity(-25*30);
        }

        opmode.telemetry.addData("Can fire? ", Math.abs(rotationsPerMinute-desSpeed) < 150);
        opmode.telemetry.addData("Fire?", fire);
        opmode.telemetry.addData("Distance From Goal in feet", distanceFromGoal/0.3048);
        opmode.telemetry.addData("Speed RPM", rotationsPerMinute);
        opmode.telemetry.addData("Desired Speed RPM", desSpeed);
    }
}
