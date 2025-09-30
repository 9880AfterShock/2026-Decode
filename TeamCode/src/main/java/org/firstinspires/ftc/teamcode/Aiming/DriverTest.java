package org.firstinspires.ftc.teamcode.Aiming;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Maths.Trajectory;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Shooter;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Transfer;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.Spindexer;
import org.firstinspires.ftc.teamcode.OpModes.TeleOp;
import org.firstinspires.ftc.teamcode.Systems.ControlManager;
import org.firstinspires.ftc.teamcode.messages.SpindexerMessage;

public class DriverTest {
    private static OpMode opmode;
    public static int distanceFromGoal;

    private static double numTicks = 28;

    private static double lastPos;

    private static double desSpeed = 4000*(Math.PI*2);

    private static double lastTime;

    public static void initControls(OpMode opmode) {
        DriverTest.opmode = opmode;
        distanceFromGoal = 0;
        lastPos = Shooter.shooter.getCurrentPosition();
        lastTime = opmode.getRuntime();
    }

    public static void update(boolean increase, boolean decrease, boolean fire, boolean rev){
        double currentPos = Shooter.shooter.getCurrentPosition();
        double nowTime = opmode.getRuntime();
        double speedRadianMinutes = Math.abs((((currentPos-lastPos)/numTicks)/((nowTime-lastTime)/60))*(Math.PI*2));
        if (increase) {
            desSpeed += 100*(Math.PI*2);
//            desSpeed = Trajectory.getVelocity(distanceFromGoal,0.5,72/2, 60).rpm;
        }
        if (decrease){
            desSpeed -= 100*(Math.PI*2);
//            desSpeed = Trajectory.getVelocity(distanceFromGoal,0.5,72/2, 60).rpm;
        }
        if (rev) {
            Shooter.updateShooter(desSpeed);
            if (speedRadianMinutes >= desSpeed/2 && fire) {
                Transfer.updateTransfer(true);
                ControlManager.spindexer.queueMessage(SpindexerMessage.EJECT);
            }
        } else {
            Shooter.updateShooter(0);
        }
        if (!fire) {
            Transfer.updateTransfer(false);
        }

        lastPos = currentPos;
        lastTime = nowTime;

        opmode.telemetry.addData("Distance From Goal", distanceFromGoal);
        opmode.telemetry.addData("Speed RPM", speedRadianMinutes/(Math.PI*2));
        opmode.telemetry.addData("Desired Speed RPM", desSpeed/(Math.PI*2));
    }
}
