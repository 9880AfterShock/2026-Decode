package org.firstinspires.ftc.teamcode.Mechanisms.Scoring;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class Shooter { // Prefix for commands
    public static DcMotor shooter; // init motor var
    private static OpMode opmode; // opmode var init
    private static double p;
    private static double i;
    private static double d;
    public static void initShooter(OpMode opmode) { // init motor
        shooter = opmode.hardwareMap.get(DcMotor.class, "shooter"); // motor config name
        shooter.setZeroPowerBehavior(FLOAT);
        shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter.setDirection(DcMotorSimple.Direction.REVERSE);
        p = 0.1;
        i = 0.01;
        d = 0.01;
        Shooter.opmode = opmode;
    }

    public static void updateShooter(double speedRotationsMinutes,double currentSpeed) {
        shooter.setPower(speedRotationsMinutes/6000);

        opmode.telemetry.addData("Shooter Intended Power", shooter.getPower()); //make it check actual speed later
    }
}