package org.firstinspires.ftc.teamcode.Aiming;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Hood;
import org.firstinspires.ftc.teamcode.Systems.ControlManager;
import org.firstinspires.ftc.teamcode.messages.SpindexerMessage;

public class DriverTest {
    private static OpMode opmode;
    public static double distanceFromGoal;

    private static final double numTicks = 28;

    private static double lastPos;

    public static double desSpeed = 3300;

    private static double lastTime;
    private static DcMotorEx shooterUp;
    private static DcMotorEx shooterDown;
    public static boolean canFire;

    public static void initControls(OpMode opmode) {
        DriverTest.opmode = opmode;
        shooterUp = opmode.hardwareMap.get(DcMotorEx.class, "shooterUp"); //Port 3 or 4 on the expansion hub
        shooterUp.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterUp.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterUp.setVelocity(0);
        shooterDown = opmode.hardwareMap.get(DcMotorEx.class, "shooterDown"); //Port 3 or 4 on the expansion hub
        shooterDown.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterDown.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterDown.setVelocity(0);
        distanceFromGoal = 60;
        lastPos = shooterUp.getCurrentPosition();
        lastTime = opmode.getRuntime();
        canFire = false;
    }

    public static void update(boolean increase, boolean decrease, boolean fire, boolean rev, boolean intake, boolean auto){
        double rotationsPerMinute = Math.abs((shooterUp.getVelocity()/numTicks)*60);
        if (!auto) {
            if (GoalVision.goalDistance <+ 70) {
                desSpeed = (0.12825*GoalVision.goalDistance*GoalVision.goalDistance)+(4.81307*GoalVision.goalDistance)+2504.82611;
                Hood.hoodState = "Near";
                Hood.updateAim(false);
            } else {
                desSpeed = (-0.0381041*GoalVision.goalDistance*GoalVision.goalDistance)+(21.14689*GoalVision.goalDistance)+2119.60164;                Hood.hoodState = "Far";
                Hood.hoodState = "Far";
                Hood.updateAim(false);
            }
        }

        if (increase) {
//            distanceFromGoal += 0.3048*0.5;
//            desSpeed = Trajectory.getVelocity(distanceFromGoal,1.1176-0.3937,0.036, Math.toRadians(30)).rpm;
//            desSpeed += 50;
            distanceFromGoal += 5;
        }
        if (decrease){
//            distanceFromGoal -= 0.3048*0.5;
//            desSpeed = Trajectory.getVelocity(distanceFromGoal,1.1176-0.3937,0.036, Math.toRadians(30)).rpm;
//            desSpeed -= 50;
            distanceFromGoal -= 5;
        }
        if (rev) {
            shooterUp.setVelocity((desSpeed*numTicks)/60);
            shooterDown.setVelocity((desSpeed*numTicks)/60);
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
            shooterUp.setVelocity(0);
            shooterDown.setVelocity(0);
        }
        if (!fire && !rev && intake) {
            shooterUp.setVelocity(-25*30);
            shooterDown.setVelocity(-25*30);
        }

        opmode.telemetry.addData("Can fire? ", canFire);
        opmode.telemetry.addData("Fire?", fire);
        opmode.telemetry.addData("Distance From Goal in feet", distanceFromGoal/0.3048);
        opmode.telemetry.addData("Speed RPM", rotationsPerMinute);
        opmode.telemetry.addData("Desired Speed RPM", desSpeed);
    }
}
