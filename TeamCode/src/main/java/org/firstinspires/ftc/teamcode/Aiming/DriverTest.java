package org.firstinspires.ftc.teamcode.Aiming;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.FlywheelMotor;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Hood;
import org.firstinspires.ftc.teamcode.Systems.ControlManager;
import org.firstinspires.ftc.teamcode.Systems.DelayedAction;
import org.firstinspires.ftc.teamcode.Systems.RunLater;
import org.firstinspires.ftc.teamcode.messages.SpindexerMessage;

import java.util.List;

public class DriverTest {
    private static OpMode opmode;
    public static double distanceFromGoal;

    private static final double numTicks = 28;

    private static double lastPos;

    public static double desSpeed = 3300;

    private static double lastTime;
    private static DcMotorEx shooterUp;
    private static DcMotorEx shooterDown;
    private static FlywheelMotor shooter;
    public static boolean canFire;
    public static double avgSpeed = 0;
    private static final Pose2d goalTarget = new Pose2d(-57.0, -55.0, Math.toRadians(0.0));

    public static void initControls(OpMode opmode) {
        DriverTest.opmode = opmode;
        shooterUp = opmode.hardwareMap.get(DcMotorEx.class, "shooterUp"); //Port 3 or 4 on the expansion hub
        shooterDown = opmode.hardwareMap.get(DcMotorEx.class, "shooterDown"); //Port 3 or 4 on the expansion hub
        shooter = new FlywheelMotor(List.of(shooterUp,shooterDown),numTicks);
        distanceFromGoal = 60;
        lastPos = shooterUp.getCurrentPosition();
        lastTime = opmode.getRuntime();
        canFire = false;
    }

    public static void update(boolean increase, boolean decrease, boolean fire, boolean rev, boolean intake, boolean auto){
        double rotationsPerMinute = Math.abs(shooter.getSpeed());
        avgSpeed *= 1.5;
        avgSpeed += rotationsPerMinute*0.5;
        avgSpeed /= 2;
        if (!auto) {
            if (distanceFromGoal < 50) { // Timo experimenting, used to be 70 from goal
                desSpeed = (0.12825*distanceFromGoal*distanceFromGoal)+(4.81307*distanceFromGoal)+2504.82611-500;
                Hood.hoodState = "Near";
                Hood.updateAim(false);
            } else {
                desSpeed = (-0.0381041*distanceFromGoal*distanceFromGoal)+(21.14689*distanceFromGoal)+2119.60164-500;
                Hood.hoodState = "Far";
                Hood.updateAim(false);
            }
        }
//        if (increase) {
////            distanceFromGoal += 0.3048*0.5;
////            desSpeed = Trajectory.getVelocity(distanceFromGoal,1.1176-0.3937,0.036, Math.toRadians(30)).rpm;
////            desSpeed += 50;
//            distanceFromGoal += 5;
//        }
//        if (decrease){
////            distanceFromGoal -= 0.3048*0.5;
////            desSpeed = Trajectory.getVelocity(distanceFromGoal,1.1176-0.3937,0.036*numTicks)/60, Math.toRadians(30)).rpm;
////            desSpeed -= 50;
//            distanceFromGoal -= 5;
//        }
        if (rev) {
            shooter.setSpeed(desSpeed);
            if (Math.abs(avgSpeed-desSpeed) < 200 && fire) {
                if (ControlManager.shot) {
                    RunLater.addAction(new DelayedAction(() -> {
                            ControlManager.shot = true;
                            ControlManager.spindexer.queueMessage(SpindexerMessage.EJECT);
                        }, 0.8));
                }
                ControlManager.shot = false;
                canFire = true;
            } else {
                canFire = false;
            }
        } else {
            canFire = false;
            shooter.setSpeed(1000);
        }
//        if (!fire && !rev && intake) {
//            shooterUp.setVelocity(-25*30);
//            shooterDown.setVelocity(-25*30);
//        }
        shooter.update();
        opmode.telemetry.addData("Can fire? ", canFire);
        opmode.telemetry.addData("Fire?", fire);
        opmode.telemetry.addData("Distance From Goal in inches", distanceFromGoal);
        opmode.telemetry.addData("Speed RPM", rotationsPerMinute);
        opmode.telemetry.addData("Averaged RPM", avgSpeed);
        opmode.telemetry.addData("Desired Speed RPM", desSpeed);
    }
}
