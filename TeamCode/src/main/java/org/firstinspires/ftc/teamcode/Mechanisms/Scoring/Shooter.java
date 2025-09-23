package org.firstinspires.ftc.teamcode.Mechanisms.Scoring;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class Shooter { // Prefix for commands
    public static DcMotor shooter; // init motor var
    private static OpMode opmode; // opmode var init
    public static void initShooter(OpMode opmode) { // init motor
        shooter = opmode.hardwareMap.get(DcMotor.class, "shooter"); // motor config name
        shooter.setZeroPowerBehavior(FLOAT);
        shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooter.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        shooter.setDirection(DcMotorSimple.Direction.REVERSE);

        Shooter.opmode = opmode;
    }

    public static void updateShooter(double speedRadianMinutes) {
        shooter.setPower((speedRadianMinutes/(Math.PI*2))/6000);

        opmode.telemetry.addData("Shooter Intended Power", shooter.getPower()); //make it check actual speed later
    }
}