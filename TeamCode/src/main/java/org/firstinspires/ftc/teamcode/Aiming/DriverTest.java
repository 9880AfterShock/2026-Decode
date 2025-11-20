package org.firstinspires.ftc.teamcode.Aiming;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Maths.LaunchInformation;
import org.firstinspires.ftc.teamcode.Maths.Trajectory;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Hood;
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
    public static double desSpeed = 3300;

    public static double desRot = 30;
    private static DcMotorEx shooterup;
    private static DcMotorEx shooterdown;
    public static boolean canFire;

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
        canFire = false;
    }

    public static void update(boolean increase, boolean decrease, boolean fire, boolean rev, boolean intake){
        double rotationsPerMinute = Math.abs((shooterup.getVelocity()/numTicks)*60);
        if (increase) {
            distanceFromGoal += 1/39.3700787402;
            LaunchInformation li = Trajectory.getOptimalVelocity(distanceFromGoal,1.1176-0.3937,0.036, Math.toRadians(30), Math.toRadians(50));
            desSpeed = li.rpm;
            desRot = Math.toDegrees(li.angle);
        }
        if (decrease){
            distanceFromGoal -= 1/39.3700787402;
            LaunchInformation li = Trajectory.getOptimalVelocity(distanceFromGoal,1.1176-0.3937,0.036, Math.toRadians(30), Math.toRadians(50));
            desSpeed = li.rpm;
            desRot = Math.toDegrees(li.angle);
        }
        if (rev) {
            shooterup.setVelocity((desSpeed*numTicks)/60);
            shooterdown.setVelocity((desSpeed*numTicks)/60);
            if (Math.abs(rotationsPerMinute-desSpeed) < 200 && fire) {
                canFire = true;
//                ControlManager.ballRamp.queueMessage(BallRampMessage.UP);
                ControlManager.shot = true;
//                RunLater.addAction(new DelayedAction(() -> {ControlManager.ballRamp.queueMessage(BallRampMessage.DOWN);}, 0.2));
                ControlManager.spindexer.queueMessage(SpindexerMessage.EJECT);
            } else {
                canFire = false;
            }
        } else {
            canFire = false;
            shooterup.setVelocity(0);
            shooterdown.setVelocity(0);
        }
        if (!fire && !rev && intake) {
            shooterup.setVelocity(-25*30);
            shooterdown.setVelocity(-25*30);
        }
        Hood.updateAim(desRot);

        opmode.telemetry.addData("Can fire? ", canFire);
        opmode.telemetry.addData("Fire?", fire);
        opmode.telemetry.addData("Distance From Goal in feet and inches", ((int) (distanceFromGoal/0.3048)) + ", " + (Math.floorMod((int) (distanceFromGoal*39.3700787402), 12)));
        opmode.telemetry.addData("Distance From Goal in inches", distanceFromGoal*39.3700787402);
        opmode.telemetry.addData("Speed RPM", rotationsPerMinute);
        opmode.telemetry.addData("Desired Speed RPM", desSpeed);
    }
}
