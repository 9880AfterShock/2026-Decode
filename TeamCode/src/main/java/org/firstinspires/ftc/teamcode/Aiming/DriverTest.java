package org.firstinspires.ftc.teamcode.Aiming;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Maths.Trajectory;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Shooter;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Transfer;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.Spindexer;
import org.firstinspires.ftc.teamcode.OpModes.TeleOp;
import org.firstinspires.ftc.teamcode.messages.SpindexerMessage;

public class DriverTest {
    private static TeleOp opmode;
    public static int distanceFromGoal;

    private static double numTicks = 28;

    private static double lastPos;

    private static double desSpeed = 5000*(Math.PI*2);

    private static double lastTime;

    public static void initControls(TeleOp opmode) {
        DriverTest.opmode = opmode;
        distanceFromGoal = 0;
        lastPos = Shooter.shooter.getCurrentPosition();
        lastTime = opmode.getRuntime();
    }

    public static void update(boolean increase, boolean decrease, boolean fire){
        double currentPos = Shooter.shooter.getCurrentPosition();
        double nowTime = opmode.getRuntime();
        double speedRadianMinutes = Math.abs((((currentPos-lastPos)/numTicks)/((nowTime-lastTime)/60))*(Math.PI*2));
        if (increase) {
            distanceFromGoal += 1;
//            desSpeed = Trajectory.getVelocity(distanceFromGoal,0.5,72/2, 60).rpm;
        }E
        if (decrease){
            distanceFromGoal -= 1;
//            desSpeed = Trajectory.getVelocity(distanceFromGoal,0.5,72/2, 60).rpm;
        }
        if (fire) {
            Shooter.updateShooter(desSpeed);
            if (speedRadianMinutes <= desSpeed) {
                Transfer.updateTransfer(true);
                opmode.spindexer.queueMessage(SpindexerMessage.EJECT);
            }
        } else {
            Shooter.updateShooter(0);
            Transfer.updateTransfer(false);
        }

        lastPos = currentPos;
        lastTime = nowTime;

        opmode.telemetry.addData("Distance From Goal", distanceFromGoal);
        opmode.telemetry.addData("Speed RPM", speedRadianMinutes/(Math.PI*2));
        opmode.telemetry.addData("Desired Speed RPM", desSpeed/(Math.PI*2));
    }
}
