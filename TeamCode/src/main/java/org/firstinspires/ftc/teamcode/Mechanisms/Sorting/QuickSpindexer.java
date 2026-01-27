package org.firstinspires.ftc.teamcode.Mechanisms.Sorting;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import static java.lang.Math.abs;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Enums.Motif;
import org.firstinspires.ftc.teamcode.Sensors.Limelight;
import org.firstinspires.ftc.teamcode.Sensors.Obelisk;

import java.util.Arrays;

public class QuickSpindexer { // Prefix for commands
    public static DcMotor spindexer; // init motor var
    private static OpMode opmode; // opmode var init
    private static double targetPosition;
    private static boolean wasClockwise;
    private static boolean wasCounterclockwise;
    private static final double errorMargin = 10.0; //in encoder ticks
    public static boolean[] hasBall = new boolean[3];
    public static int currentSlot = 1; //1 2 3 going clockwise
    public static boolean spindexerOffset = false;
    final private static int offsetDivider = 12;

    public static void initSpindexer(OpMode opmode) { // init motor
        spindexer = opmode.hardwareMap.get(DcMotor.class, "spindexer"); //Port 1 on expansion hub
        spindexer.setZeroPowerBehavior(BRAKE);
        spindexer.setTargetPosition(0);
        spindexer.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spindexer.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        targetPosition = 0;
        spindexer.setPower(1.0);

        QuickSpindexer.opmode = opmode;
        hasBall = new boolean[3];
        currentSlot = 1;
        spindexerOffset = false;
    }

    public static void updateSpindexer(boolean clockwise, boolean counterclockwise) {

        if (clockwise && !wasClockwise){
            targetPosition += 1425.1/3;
        }
        if (counterclockwise && !wasCounterclockwise) {
            targetPosition -= 1425.1/3;
        }

        spindexer.setTargetPosition((int) targetPosition);
        wasClockwise = clockwise;
        wasCounterclockwise = counterclockwise;

        spindexer.setPower(1.0);
    }

    public static void updateSpindexerResetIncluded(boolean clockwise, boolean counterclockwise, boolean reseting, boolean reset) {

        if (clockwise && !wasClockwise){
            targetPosition += 1425.1/3;
            spindexer.setPower(1.0);
            currentSlot += 1;
            if (currentSlot > 3) currentSlot = 1;
        }
        if (counterclockwise && !wasCounterclockwise) {
            targetPosition -= 1425.1/3;
            spindexer.setPower(1.0);
            currentSlot -= 1;
            if (currentSlot < 1) currentSlot = 3;
        }
        if (reseting) {
            targetPosition += 30;
            spindexer.setPower(1.0);
        }
        if (reset){
            spindexer.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            spindexer.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            targetPosition = 0;
            spindexer.setPower(1.0);
        }

        if (spindexerOffset){
            spindexer.setTargetPosition((int) (targetPosition - 1425.1/offsetDivider));
        } else {
            spindexer.setTargetPosition((int) targetPosition);
        }

        wasClockwise = clockwise;
        wasCounterclockwise = counterclockwise;
        opmode.telemetry.addData("Current Spindexer Slot", currentSlot);
//        opmode.telemetry.addData("Ball in ACTIVE slot?", hasBall[currentSlot-1]);
        opmode.telemetry.addData("Spindexer Array", Arrays.toString(hasBall));

//        opmode.telemetry.addData("DEXER raw ticks", spindexer.getTargetPosition());
//        opmode.telemetry.addData("DEXER target", targetPosition);
//        opmode.telemetry.addData("DEXER int target", (int) targetPosition);

    }

    public static void fullCycle(){
        spindexerOffset = false;
        targetPosition += 1425.1;
        spindexer.setTargetPosition((int) (targetPosition - 1425.1/offsetDivider));
        spindexer.setPower(0.7);
        hasBall = new boolean[3];
    }

    public static void turnIntake(){
        targetPosition -= 1425.1/3;
        currentSlot -= 1;
        if (currentSlot < 1) currentSlot = 3;
        spindexer.setTargetPosition((int) targetPosition);
        spindexer.setPower(1.0);
    }

    public static Action goToMotif(){
        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (Obelisk.motif == Motif.GPP) {
                    spindexer.setTargetPosition((int) (10.0/360*1425.1)); //10.0 is offset degrees
                }
                if (Obelisk.motif == Motif.PGP) {
                    spindexer.setTargetPosition((int) ((10.0/360*1425.1) - (1425.1/3))); //10.0 is offset degrees
                }
                if (Obelisk.motif == Motif.PPG) {
                    spindexer.setTargetPosition((int) ((10.0/360*1425.1) + (1425.1/3))); //10.0 is offset degrees
                }
                return abs(spindexer.getCurrentPosition()- spindexer.getTargetPosition()) > 40; //40 is tick margin of error
            }
        };
    }

    public static Action turnRight(){
        return new Action() {
            private boolean first = true;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (first) {
                    spindexer.setTargetPosition((int) (spindexer.getTargetPosition()+(1425.1/3)));
                    first = false;
                }
                telemetryPacket.put("Spin Pose", spindexer.getCurrentPosition());
                telemetryPacket.put("Spin Target Pose", spindexer.getTargetPosition());
                return abs(spindexer.getCurrentPosition() - spindexer.getTargetPosition()) > errorMargin; //40 is tick margin of error
            }
        };
    }

    public static Action autoFullCycle(boolean removeOffset){
        return new Action() {
            private boolean first = true;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (first) {
                    if (removeOffset) {
                        spindexer.setTargetPosition((int) (spindexer.getTargetPosition()+1425.1+(1425.1/offsetDivider)));
                    }else {
                        spindexer.setTargetPosition((int) (spindexer.getTargetPosition()+1425.1));
                    }
                    first = false;
                }
                telemetryPacket.put("Spin Pose", spindexer.getCurrentPosition());
                telemetryPacket.put("Spin Target Pose", spindexer.getTargetPosition());
                return abs(spindexer.getCurrentPosition() - spindexer.getTargetPosition()) > errorMargin;
            }
        };
    }

    public static Action turnLeft(){
        return new Action() {
            private boolean first = true;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (first) {
                    spindexer.setTargetPosition((int) (spindexer.getTargetPosition()-(1425.1/3)));
                    first = false;
                }
                telemetryPacket.put("Spin Pose", spindexer.getCurrentPosition());
                telemetryPacket.put("Spin Target Pose", spindexer.getTargetPosition());
                return abs(spindexer.getCurrentPosition() - spindexer.getTargetPosition()) > errorMargin; //40 is tick margin of error
            }
        };
    }

    public static Action addRevOffset(){
        return new Action() {
            private boolean first = true;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (first) {
                    spindexer.setTargetPosition((int) (spindexer.getTargetPosition()-(1425.1/offsetDivider)));
                    first = false;
                }
                telemetryPacket.put("Spin Pose", spindexer.getCurrentPosition());
                telemetryPacket.put("Spin Target Pose", spindexer.getTargetPosition());
                return abs(spindexer.getCurrentPosition() - spindexer.getTargetPosition()) > errorMargin;
            }
        };
    }

    public static Action removeRevOffset(){ //no delay
        return new Action() {
//            private boolean first = true;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
//                if (first) {
                    spindexer.setTargetPosition((int) (spindexer.getTargetPosition()+(1425.1/offsetDivider)));
//                    first = false;
//                }
//                telemetryPacket.put("Spin Pose", spindexer.getCurrentPosition());
//                telemetryPacket.put("Spin Target Pose", spindexer.getTargetPosition());
//                return abs(spindexer.getCurrentPosition() - spindexer.getTargetPosition()) > errorMargin;
                return false;
            }
        };
    }

    public static Action removeBias(){
        return new Action() {
            private boolean first = true;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (first) {
                    spindexer.setTargetPosition((int) (spindexer.getTargetPosition()-(1425.1/360*6)));
                    first = false;
                }
                telemetryPacket.put("Spin Pose", spindexer.getCurrentPosition());
                telemetryPacket.put("Spin Target Pose", spindexer.getTargetPosition());
                return abs(spindexer.getCurrentPosition() - spindexer.getTargetPosition()) > 20; //20 is tick margin of error
            }
        };
    }

    public static Action addBias(){
        return new Action() {
            private boolean first = true;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (first) {
                    spindexer.setTargetPosition((int) (spindexer.getTargetPosition()+(1425.1/360*6)));
                    first = false;
                }
                telemetryPacket.put("Spin Pose", spindexer.getCurrentPosition());
                telemetryPacket.put("Spin Target Pose", spindexer.getTargetPosition());
                return abs(spindexer.getCurrentPosition() - spindexer.getTargetPosition()) > 20; //20 is tick margin of error
            }
        };
    }

    public static Action cycleRampStart(){
        return new Action() {
            private boolean first = true;
            @Override
            public  boolean run (@NonNull TelemetryPacket telemetryPacket) {
                if (first){
                    spindexer.setTargetPosition((int) (spindexer.getCurrentPosition()-((1425.1/3)/2.5)));
                    first = false;
                }
                return abs(spindexer.getCurrentPosition() - spindexer.getTargetPosition()) > 10; //10 is tick margin of error
            }
        };
    }

    public static Action cycleRampEnd(){
        return new Action() {
            private boolean first = true;
            @Override
            public  boolean run (@NonNull TelemetryPacket telemetryPacket) {
                if (first) {
                    spindexer.setTargetPosition((int) (spindexer.getCurrentPosition()+((1425.1/3)/2.5)));
                    first = false;
                }
                return abs(spindexer.getCurrentPosition() - spindexer.getTargetPosition()) > 10; //10 is tick margin of error
            }
        };
    }

    public static Action resetForTele(){
        return new Action() {
            @Override
            public  boolean run (@NonNull TelemetryPacket telemetryPacket) {
                spindexer.setTargetPosition(0);
                return abs(spindexer.getCurrentPosition() - spindexer.getTargetPosition()) > 10; //10 is tick margin of error
            }
        };
    }

    public static Action toMotifFrom(Motif currentInventory){
        return new Action() {
            private boolean first = true;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (first) {
                    if (Limelight.motif != currentInventory) {
                        if ((Limelight.motif == Motif.GPP && currentInventory == Motif.PPG) ||
                                (Limelight.motif == Motif.PGP && currentInventory == Motif.GPP) ||
                                (Limelight.motif == Motif.PPG && currentInventory == Motif.PGP)){
                            spindexer.setTargetPosition((int) (spindexer.getTargetPosition()-(1425.1/3)));
                        }
                        if ((Limelight.motif == Motif.GPP && currentInventory == Motif.PGP) ||
                                (Limelight.motif == Motif.PGP && currentInventory == Motif.PPG) ||
                                (Limelight.motif == Motif.PPG && currentInventory == Motif.GPP)){
                            spindexer.setTargetPosition((int) (spindexer.getTargetPosition()+(1425.1/3)));
                        }
                    }
                    first = false;
                }
                telemetryPacket.put("Spin Pose", spindexer.getCurrentPosition());
                telemetryPacket.put("Spin Target Pose", spindexer.getTargetPosition());
                return abs(spindexer.getCurrentPosition() - spindexer.getTargetPosition()) > errorMargin; //40 is tick margin of error
            }
        };
    }

    public static boolean aligned(){
        return abs(spindexer.getCurrentPosition() - spindexer.getTargetPosition()) < errorMargin && spindexer.getTargetPosition() == (int) targetPosition;
    }
}