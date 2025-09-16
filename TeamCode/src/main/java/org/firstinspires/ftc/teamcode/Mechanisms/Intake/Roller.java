package org.firstinspires.ftc.teamcode.Mechanisms.Intake;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


public class Roller { // Prefix for commands
    private static DcMotor roller; // init motor var
    private static OpMode opmode; // opmode var init
    public static double intakePower; //current intake power

    public static void initIntake(OpMode opmode) { // init motor
        roller = opmode.hardwareMap.get(DcMotor.class, "roller"); // motor config name
        roller.setZeroPowerBehavior(FLOAT);

        Roller.opmode = opmode;
    }

    public static void updateIntake(boolean intaking, boolean ejecting, double speed) {
        if (intaking){
            intakePower = speed;
        } else {
            if (ejecting){
                intakePower = -speed;
            } else {
                intakePower = 0.0;
            }
        }

        roller.setPower(intakePower);

        opmode.telemetry.addData("Intake Roller", intakePower);
    }
}