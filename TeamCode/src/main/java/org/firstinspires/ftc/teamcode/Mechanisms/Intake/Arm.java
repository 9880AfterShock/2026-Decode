package org.firstinspires.ftc.teamcode.Mechanisms.Intake;

import static java.lang.Math.abs;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Systems.DelayedAction;
import org.firstinspires.ftc.teamcode.Systems.RunLater;


public class Arm { // Prefix for commands

    private static Servo arm; // init motor var
    private static OpMode opmode; // opmode var init
    public static double intakePosition = 0.36;
    public static double neutralPosition = 0.57;
    public static double revPosition = 0.62;
    public static String intakeState = "Intaking";
    public static double lastTransition = -9880.0;
    public final static double transitionTime = 0.5;

    public static void initIntake(OpMode opmode) { // init motor
        arm = opmode.hardwareMap.get(Servo.class, "arm"); //Port 0 on control hub
        Arm.opmode = opmode;
        lastTransition = -9880.0;
    }

    public static void updateIntake(boolean intakeButtonCurrentlyPressed, boolean outTakeButtonCurrentlyPressed, boolean revving) {
        if ((intakeButtonCurrentlyPressed || outTakeButtonCurrentlyPressed) && !revving && lastTransition == -9880.0){
            intakeState = "Intaking";
            arm.setPosition(intakePosition);
        } else {
            if (revving) {
                if (intakeState != "revving") {
                    intakeState = "revving";
                    arm.setPosition(intakePosition);
                    lastTransition = opmode.getRuntime();
                } else
                if (lastTransition != -9880.0 && lastTransition + transitionTime > opmode.getRuntime() && intakeState == "revving") {
                    arm.setPosition(revPosition);
                    lastTransition = -9880.0;
                }
            } else {
                if (intakeState == "revving" ) {
                    intakeState = "Neutral";
                    arm.setPosition(intakePosition);
                    lastTransition = opmode.getRuntime();
                } else {
                    if (intakeState == "Neutral" && lastTransition != -9880.0 && lastTransition + transitionTime > opmode.getRuntime()) {
                        arm.setPosition(revPosition);
                        lastTransition = -9880.0;
                    }
                }
            }
        }

        opmode.telemetry.addData("Intake Arm", intakeState);
//        opmode.telemetry.addData("Intake Pos", arm.getPosition());
    }



    public static Action AutoArmInWait() {
        return new Action() {
            private boolean first = true;
            public boolean run(@NonNull TelemetryPacket packet) {
                if (first) {
                    RunLater.addAction(new DelayedAction(() -> {},0.2));
                    first = false;
                }
                arm.setPosition(neutralPosition);
                intakeState = "Neutral";
                RunLater.update();
                return !RunLater.isEmpty();
            }
        };
    }

    public static Action AutoLaunchStart() {
        return new Action() {
            private boolean first = true;
            public boolean run(@NonNull TelemetryPacket packet) {
                if (first) {
                    RunLater.addAction(new DelayedAction(() -> {arm.setPosition(revPosition);}, 0.2));
                    Roller.updateIntake(false, false, true, 1.0);
                    first = false;
                }
                RunLater.update();
                return !RunLater.isEmpty();
            }
        };
    }

    public static Action AutoLaunchEnd() {
        return new Action() {
            public boolean run(@NonNull TelemetryPacket packet) {
                Roller.updateIntake(false, false, false, 1.0);
                arm.setPosition(neutralPosition);
                return false;
            }
        };
    }

    public static Action AutoArmRev() {
        return new Action() {
            public boolean run(@NonNull TelemetryPacket packet) {
                arm.setPosition(revPosition);
                intakeState = "revving";
                return false;
            }
        };
    }

    public static Action AutoArmOut() {
        return new Action() {
            public boolean run(@NonNull TelemetryPacket packet) {
                arm.setPosition(intakePosition);
                intakeState = "Intaking";
                return false;
            }
        };
    }

    public static Action AutoArmIn() {
        return new Action() {
            public boolean run(@NonNull TelemetryPacket packet) {
                arm.setPosition(revPosition);
                intakeState = "Neutral";
                return false;
            }
        };
    }
}