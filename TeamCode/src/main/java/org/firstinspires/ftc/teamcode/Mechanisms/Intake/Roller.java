package org.firstinspires.ftc.teamcode.Mechanisms.Intake;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;


public class Roller { // Prefix for commands
    private static DcMotorEx roller; // init motor var
    private static OpMode opmode; // opmode var init
    public static double intakePower; //current intake power

    public static void initIntake(OpMode opmode) { // init motor
        roller = opmode.hardwareMap.get(DcMotorEx.class, "roller"); // motor config name
        roller.setZeroPowerBehavior(FLOAT);
        roller.setDirection(DcMotorSimple.Direction.REVERSE);

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
        //opmode.telemetry.addData("POWER FROM INTAKE THING", roller.getCurrent(CurrentUnit.AMPS));
    }
}