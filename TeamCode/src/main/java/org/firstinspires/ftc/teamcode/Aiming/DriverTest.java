package org.firstinspires.ftc.teamcode.Aiming;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.FlywheelMotor;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Hood;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.QuickSpindexer;
import org.firstinspires.ftc.teamcode.Systems.ControlManager;
import org.firstinspires.ftc.teamcode.Systems.DelayedAction;
import org.firstinspires.ftc.teamcode.Systems.PID;
import org.firstinspires.ftc.teamcode.Systems.RunLater;

import java.util.List;

@Config
public class DriverTest {
    private static OpMode opmode;
    public static double distanceFromGoal;

    private static final double numTicks = 28;

    public static double desSpeed = 3300;

    private static DcMotorEx shooterUp;
    private static DcMotorEx shooterDown;
    private static FlywheelMotor shooter;
    public static boolean canFire;
    public static double avgSpeed = 0;

    private final static double idleSpeed = 1500;
    private static PID shooterPID = new PID(0.0,0.0,0.0); //placeholder, gets set later
    public static double kS = 0.005; //Kstatic, should not change
    public static double kV = 0.000196; //feedforward scalar

    public static double kP = 0.0014; //PID P
    public static double kD = 0.000011; //PID D

    public static double tripleShootMultiplier = 3.7; //scalar for the boost to RPM based off of distance
    public static double getTripleShootNerf = 185; //flat decreasing value

    public static final double rapidFireDifference = 150; //artifact, just for auto now
    public static double rapidFireCooldown = -rapidFireDifference;

    public static boolean isFarAuto = false;

    public static void initControls(OpMode opmode) {
        DriverTest.opmode = opmode;
        shooterUp = opmode.hardwareMap.get(DcMotorEx.class, "shooterUp"); //Port 3 or 4 on the expansion hub //one with encoder
        shooterDown = opmode.hardwareMap.get(DcMotorEx.class, "shooterDown"); //Port 3 or 4 on the expansion hub //one without encoder
        shooterUp.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooterDown.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooterUp.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterDown.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooter = new FlywheelMotor(List.of(shooterUp,shooterDown),numTicks);
        distanceFromGoal = 60;
        canFire = false;


        TelemetryPacket packet = new TelemetryPacket();
        packet.put("Current RPM", 0.0);
        packet.put("Avg RPM", 0.0);
        packet.put("Desired RPM", 0.0);
        FtcDashboard.getInstance().sendTelemetryPacket(packet);

        isFarAuto = false;

        rapidFireCooldown = -rapidFireDifference;

        shooterPID.p = kP;
        shooterPID.d = kD;
    }

    public static void update(boolean increase, boolean decrease, boolean fire, boolean rev, boolean intake, boolean auto){
        shooterPID.p = kP;
        shooterPID.d = kD;
        double rotationsPerMinute = Math.abs(shooter.getSpeed());
        avgSpeed *= 1.5;
        avgSpeed += rotationsPerMinute*0.5;
        avgSpeed /= 2;
        if (!auto) {
            if (distanceFromGoal < 50) {
                desSpeed = (-0.0741749*distanceFromGoal*distanceFromGoal)+(18.52099*distanceFromGoal)+1876.11169;
                Hood.hoodState = "Near";
                Hood.updateAim(false);
            } else {
                desSpeed = (-0.0460518*distanceFromGoal*distanceFromGoal)+(20.69553*distanceFromGoal)+1683.42864;
                Hood.hoodState = "Far";
                Hood.updateAim(false);
            }
            desSpeed += (tripleShootMultiplier*distanceFromGoal*(rapidFireCooldown/rapidFireDifference))-getTripleShootNerf;
        }
        if (increase) {
            desSpeed += 25;
        }
        if (decrease){
            desSpeed -= 25;
        }

        if (rev) {
            double shooterPower = (kS * Math.signum(desSpeed)) + (kV * desSpeed) + shooterPID.step(desSpeed, rotationsPerMinute);
            shooterUp.setPower(shooterPower);
            shooterDown.setPower(shooterPower);
//             shooterUp.setVelocity((desSpeed*numTicks)/60);
//             shooterDown.setVelocity((desSpeed*numTicks)/60);
            if (Math.abs(avgSpeed-desSpeed) < 200 && fire) {
                if (ControlManager.shot) {
                    rapidFireCooldown = rapidFireDifference;
                    QuickSpindexer.fullCycle();
                    RunLater.addAction(new DelayedAction(() -> {
                        ControlManager.shot = true;
                        rapidFireCooldown = -rapidFireDifference;
//                        ControlManager.spindexer.queueMessage(SpindexerMessage.EJECT);
                    }, 0.8));
                }
                ControlManager.shot = false;
                canFire = true;
            } else {
                canFire = false;
            }
        } else {
            canFire = false;
            if (intake){
                shooterUp.setPower(0.0);
                shooterDown.setPower(0.0);
            } else {
//                double shooterPower = (kS * Math.signum(idleSpeed)) + (kV * idleSpeed) + shooterPID.step(idleSpeed, rotationsPerMinute);
//                if (shooterPower > 0){
//                    shooterUp.setPower(shooterPower);
//                    shooterDown.setPower(shooterPower);
//                } else {
                    shooterUp.setPower(0.0);
                    shooterDown.setPower(0.0);
//                }
            }
        }
//        if (!fire && !rev && intake) {
//            shooterUp.setVelocity(-25*30);
//            shooterDown.setVelocity(-25*30);
//        }
//        opmode.telemetry.addData("Can fire? ", canFire);
//        opmode.telemetry.addData("Fire?", fire);
        opmode.telemetry.addData("Distance From Goal in inches", distanceFromGoal);
        opmode.telemetry.addData("Speed RPM RAW", rotationsPerMinute);
        opmode.telemetry.addData("Averaged RPM", avgSpeed);
        opmode.telemetry.addData("Desired Speed RPM", desSpeed);

        TelemetryPacket packet = new TelemetryPacket();
        packet.put("Current RPM", rotationsPerMinute);
        packet.put("Avg RPM", avgSpeed);
        packet.put("Desired RPM", desSpeed);
        packet.put("Goal Distance", distanceFromGoal);
        FtcDashboard.getInstance().sendTelemetryPacket(packet);
    }
}
//for feedworward
//tune kstatic first //done
//tune kv //done? maybe

//P //done
//D //idk what to do here, figure out later
//no I

//then test shooting speed
//test idle speed, close to lowest shooting as possible