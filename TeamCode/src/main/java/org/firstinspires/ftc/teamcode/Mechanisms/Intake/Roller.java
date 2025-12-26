package org.firstinspires.ftc.teamcode.Mechanisms.Intake;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.FLOAT;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;


public class Roller { // Prefix for commands
    private static DcMotorEx roller; // init motor var
    private static OpMode opmode; // opmode var init
    public static double intakePower; //current intake power

    public static void initIntake(OpMode opmode) { // init motor
        roller = opmode.hardwareMap.get(DcMotorEx.class, "roller"); //Port 0 on expansion hub
        roller.setZeroPowerBehavior(FLOAT);
        roller.setDirection(DcMotorEx.Direction.REVERSE);

        Roller.opmode = opmode;
    }

    public static void updateIntake(boolean intaking, boolean ejecting, boolean transferring, double speed) {
        if (intaking){
            intakePower = speed;
        } else {
            if (ejecting){
                intakePower = -speed;
            } else {
                if (transferring){
                    intakePower = speed;
                } else {
                    intakePower = 0.0;
                }
            }
        }

        roller.setPower(intakePower);

        opmode.telemetry.addData("Intake Roller", intakePower);
        //opmode.telemetry.addData("POWER FROM INTAKE THING", roller.getCurrent(CurrentUnit.AMPS));
    }

    public static Action AutoIntakeOn() {
        return new Action() {
            public boolean run(@NonNull TelemetryPacket packet) {
                updateIntake(true, false, false, 1.0);
                return false;
            }
        };
    }

    public static Action AutoIntakeOff() {
        return new Action() {
            public boolean run(@NonNull TelemetryPacket packet) {
                updateIntake(false, false, false, 1.0);
                return false;
            }
        };
    }

    public static Action AutoIntakeSlow() {
        return new Action() {
            public boolean run(@NonNull TelemetryPacket packet) {
                updateIntake(true, false, false, 0.3);
                return false;
            }
        };
    }

    public static Action AutoIntakeEject() {
        return new Action() {
            public boolean run(@NonNull TelemetryPacket packet) {
                updateIntake(false, true, false, 1.0);
                return false;
            }
        };
    }
}