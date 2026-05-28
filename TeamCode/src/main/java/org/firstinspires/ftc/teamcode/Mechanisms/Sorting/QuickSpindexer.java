package org.firstinspires.ftc.teamcode.Mechanisms.Sorting;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import static java.lang.Math.abs;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Enums.Motif;
import org.firstinspires.ftc.teamcode.Sensors.Limelight;
import org.firstinspires.ftc.teamcode.Sensors.Obelisk;
import org.firstinspires.ftc.teamcode.Sensors.SensOrange;
import org.firstinspires.ftc.teamcode.WrapperClasses.BulkWriteMotor;
import org.firstinspires.ftc.teamcode.WrapperClasses.BulkWriteMotorEx;

import java.util.Arrays;

public class QuickSpindexer { // Prefix for commands
    public static DcMotorEx spindexerMotor; // init motor var
    public static BulkWriteMotorEx spindexer;
    private static OpMode opmode; // opmode var init
    private static double targetPosition;
    private static boolean wasClockwise;
    private static boolean wasCounterclockwise;
    private static final double errorMargin = 10.0; //in encoder ticks
    public static boolean[] hasBall = new boolean[3];
    public static int currentSlot = 1; //1 2 3 going clockwise
    public static boolean spindexerOffset = false;
    final private static int offsetDivider = 8;
    private static int lastTarget = 0;
    private static int preLastTarget = 0;
    private static double lastGoodTarget = 0;
    private static int lastPos = 0;
    private static boolean aborting = false;
    private static int jamCount = 0;
    private static int jamExcuses = 3;
    private static double offset = 0.0;

    public static void initSpindexer(OpMode opmode) { // init motor
        spindexerMotor = opmode.hardwareMap.get(DcMotorEx.class, "spindexer"); //Port 1 on expansion hub
        spindexerMotor.setZeroPowerBehavior(BRAKE);
        spindexerMotor.setTargetPosition(0);
        spindexerMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        spindexerMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        targetPosition = 0;
        spindexerMotor.setPower(1.0);
        spindexer = new BulkWriteMotorEx(spindexerMotor);

        QuickSpindexer.opmode = opmode;
        hasBall = new boolean[3];
        currentSlot = 1;
        spindexerOffset = false;

        lastTarget = 0;
        preLastTarget = 0;
        lastPos = 0;

        aborting = false;
        jamCount = 0;
        jamExcuses = 3;

        offset = ((SensOrange.getCurrentAngle()%120)/360.0)*1425.1;
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
        if (clockwise && !wasClockwise && !spindexerStuck()){
            targetPosition += 1425.1/3;
            spindexer.setPower(1.0);
            currentSlot += 1;
            if (currentSlot > 3) currentSlot = 1;
        }
        if (counterclockwise && !wasCounterclockwise && !spindexerStuck()) {
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
            offset = 0;
            targetPosition = 0;
            spindexer.setPower(1.0);
        }

        if (spindexerOffset){
            spindexer.setTargetPosition((int) (((targetPosition) - (1425.1/offsetDivider)) + offset));
        } else {
            spindexer.setTargetPosition((int) (targetPosition + offset));
        }

        //Logic for canceling, and retrying if we get stuck
        if (abs(spindexer.getTargetPosition() - spindexer.getCurrentPosition()) < 40){
            aborting = false;
        }
        if (spindexerStuck() && !aborting && !reseting){
            jamExcuses -= 1;
            if (jamExcuses < 0){
                jamExcuses = 8;
                abortTurn();
                jamCount = 3;
                aborting = true;
            }
        }

        wasClockwise = clockwise;
        wasCounterclockwise = counterclockwise;
        opmode.telemetry.addData("Current Spindexer Slot", currentSlot);
//        opmode.telemetry.addData("Ball in ACTIVE slot?", hasBall[currentSlot-1]);
        opmode.telemetry.addData("Spindexer Array", Arrays.toString(hasBall));

        opmode.telemetry.addData("DEXER raw ticks", spindexer.getTargetPosition());
        opmode.telemetry.addData("DEXER target", targetPosition);
        opmode.telemetry.addData("Attempt Offset", spindexerOffset);

        opmode.telemetry.addData("SPINDEXER JAM:", spindexerStuck());
        opmode.telemetry.addData("Spindexer Current", spindexer.getCurrent(CurrentUnit.AMPS));
        TelemetryPacket packet = new TelemetryPacket();
        packet.put("Spindexer Current", spindexer.getCurrent(CurrentUnit.AMPS));
        FtcDashboard.getInstance().sendTelemetryPacket(packet);
        opmode.telemetry.addData("Last JAM pos:", lastPos);
        opmode.telemetry.addData("JAM count", jamCount);
        opmode.telemetry.addData("Last Pos Difference", spindexer.getCurrentPosition() - lastPos);
//        opmode.telemetry.addData("DEXER int target", (int) targetPosition);
        logTargets((int) targetPosition, spindexer.getCurrentPosition());
        if (abs(spindexer.getCurrentPosition() - targetPosition) < 70 && !spindexerStuck()) {
            lastGoodTarget = targetPosition;
        }
    }

    public static boolean has2Balls(){
        return ((!hasBall[0] && hasBall[1] && hasBall [2]) || (hasBall[0] && !hasBall[1] && hasBall [2]) || (hasBall[0] && hasBall[1] && !hasBall [2]));
    }

    public static void fullCycle(){
        if (!spindexerStuck()) {
        jamExcuses = 3;
        spindexerOffset = false;
        lastGoodTarget = targetPosition;
        targetPosition += 1425.1;
        spindexer.setTargetPosition((int) (targetPosition));
        spindexer.setPower(0.7);
        hasBall = new boolean[3];
        }
    }

    public static void turnIntake(){
        jamExcuses = 3;
        targetPosition -= 1425.1/3;
        currentSlot -= 1;
        if (currentSlot < 1) currentSlot = 3;
        spindexer.setTargetPosition((int) targetPosition);
        spindexer.setPower(0.9);
    }

    public static void logTargets(int currentTarget, int currentPos){
        lastPos = currentPos;
        if (currentTarget != lastTarget){
            preLastTarget = lastTarget;
            lastTarget = currentTarget;
        }
    }

    public static boolean spindexerStuck(){
        return spindexer.getCurrent(CurrentUnit.AMPS) > 7;
    }

    public static void abortTurn(){
        targetPosition = lastGoodTarget;
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
                    spindexerMotor.setTargetPosition((int) (spindexerMotor.getTargetPosition()+(1425.1/3)));
                    first = false;
                    spindexerMotor.setPower(1.0);
                }
                telemetryPacket.put("Spin Pose", spindexerMotor.getCurrentPosition());
                telemetryPacket.put("Spin Target Pose", spindexerMotor.getTargetPosition());
                return abs(spindexerMotor.getCurrentPosition() - spindexerMotor.getTargetPosition()) > errorMargin; //40 is tick margin of error
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
                        spindexerMotor.setTargetPosition((int) (spindexerMotor.getTargetPosition()+1425.1+(1425.1/offsetDivider)));
                    }else {
                        spindexerMotor.setTargetPosition((int) (spindexerMotor.getTargetPosition()+1425.1));
                    }
                    spindexerMotor.setPower(0.9);
                    first = false;
                }
                telemetryPacket.put("Spin Pose", spindexerMotor.getCurrentPosition());
                telemetryPacket.put("Spin Target Pose", spindexerMotor.getTargetPosition());
                return abs(spindexerMotor.getCurrentPosition() - spindexerMotor.getTargetPosition()) > errorMargin;
            }
        };
    }

    public static Action turnLeft(){
        return new Action() {
            private boolean first = true;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (first) {
                    spindexerMotor.setTargetPosition((int) (spindexerMotor.getTargetPosition()-(1425.1/3)));
                    first = false;
                    spindexerMotor.setPower(0.6);
                }
                telemetryPacket.put("Spin Pose", spindexerMotor.getCurrentPosition());
                telemetryPacket.put("Spin Target Pose", spindexerMotor.getTargetPosition());
                return abs(spindexerMotor.getCurrentPosition() - spindexerMotor.getTargetPosition()) > errorMargin; //40 is tick margin of error
            }
        };
    }

    public static Action turnLeftHalfTime(){
        return new Action() {
            private boolean first = true;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (first) {
                    spindexerMotor.setTargetPosition((int) (spindexerMotor.getTargetPosition()-(1425.1/3)));
                    first = false;
                    spindexerMotor.setPower(1.0);
                }
                telemetryPacket.put("Spin Pose", spindexerMotor.getCurrentPosition());
                telemetryPacket.put("Spin Target Pose", spindexerMotor.getTargetPosition());
                return abs(spindexerMotor.getCurrentPosition() - spindexerMotor.getTargetPosition())/2.0 > errorMargin; //40 is tick margin of error
            }
        };
    }

    public static Action addRevOffset(){
        return new Action() {
            private boolean first = true;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (first) {
                    spindexerMotor.setTargetPosition((int) (spindexerMotor.getTargetPosition()-(1425.1/offsetDivider)));
                    first = false;
                    spindexerMotor.setPower(1.0);
                }
                telemetryPacket.put("Spin Pose", spindexerMotor.getCurrentPosition());
                telemetryPacket.put("Spin Target Pose", spindexerMotor.getTargetPosition());
                return abs(spindexerMotor.getCurrentPosition() - spindexerMotor.getTargetPosition()) > errorMargin;
            }
        };
    }

    public static Action removeRevOffset(){ //no delay
        return new Action() {
//            private boolean first = true;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
//                if (first) {
                spindexerMotor.setTargetPosition((int) (spindexerMotor.getTargetPosition()+(1425.1/offsetDivider)));
                spindexerMotor.setPower(1.0);
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
                    spindexerMotor.setTargetPosition((int) (spindexerMotor.getTargetPosition()-(1425.1/360*6)));
                    first = false;
                }
                telemetryPacket.put("Spin Pose", spindexerMotor.getCurrentPosition());
                telemetryPacket.put("Spin Target Pose", spindexerMotor.getTargetPosition());
                return abs(spindexerMotor.getCurrentPosition() - spindexerMotor.getTargetPosition()) > 20; //20 is tick margin of error
            }
        };
    }

    public static Action addBias(){
        return new Action() {
            private boolean first = true;
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                if (first) {
                    spindexerMotor.setTargetPosition((int) (spindexerMotor.getTargetPosition()+(1425.1/360*6)));
                    first = false;
                }
                telemetryPacket.put("Spin Pose", spindexerMotor.getCurrentPosition());
                telemetryPacket.put("Spin Target Pose", spindexerMotor.getTargetPosition());
                return abs(spindexerMotor.getCurrentPosition() - spindexerMotor.getTargetPosition()) > 20; //20 is tick margin of error
            }
        };
    }

    public static Action cycleRampStart(){
        return new Action() {
            private boolean first = true;
            @Override
            public  boolean run (@NonNull TelemetryPacket telemetryPacket) {
                if (first){
                    spindexerMotor.setTargetPosition((int) (spindexerMotor.getCurrentPosition()-((1425.1/3)/2.5)));
                    first = false;
                }
                return abs(spindexerMotor.getCurrentPosition() - spindexerMotor.getTargetPosition()) > 10; //10 is tick margin of error
            }
        };
    }

    public static Action cycleRampEnd(){
        return new Action() {
            private boolean first = true;
            @Override
            public  boolean run (@NonNull TelemetryPacket telemetryPacket) {
                if (first) {
                    spindexerMotor.setTargetPosition((int) (spindexerMotor.getCurrentPosition()+((1425.1/3)/2.5)));
                    first = false;
                }
                return abs(spindexerMotor.getCurrentPosition() - spindexerMotor.getTargetPosition()) > 10; //10 is tick margin of error
            }
        };
    }

    public static Action resetForTele(){
        return new Action() {
            @Override
            public  boolean run (@NonNull TelemetryPacket telemetryPacket) {
                spindexer.setTargetPosition(0);
                return abs(spindexerMotor.getCurrentPosition() - spindexerMotor.getTargetPosition()) > 10; //10 is tick margin of error
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
                            spindexerMotor.setTargetPosition((int) (spindexerMotor.getTargetPosition()-(1425.1/3)));
                        }
                        if ((Limelight.motif == Motif.GPP && currentInventory == Motif.PGP) ||
                                (Limelight.motif == Motif.PGP && currentInventory == Motif.PPG) ||
                                (Limelight.motif == Motif.PPG && currentInventory == Motif.GPP)){
                            spindexerMotor.setTargetPosition((int) (spindexerMotor.getTargetPosition()+(1425.1/3)));
                        }
                    }
                    first = false;
                }
                telemetryPacket.put("Spin Pose", spindexerMotor.getCurrentPosition());
                telemetryPacket.put("Spin Target Pose", spindexerMotor.getTargetPosition());
                return abs(spindexerMotor.getCurrentPosition() - spindexerMotor.getTargetPosition()) > errorMargin; //40 is tick margin of error
            }
        };
    }

    public static boolean aligned(){
        return abs(spindexer.getCurrentPosition() - spindexer.getTargetPosition()) < errorMargin && spindexer.getTargetPosition() == (int) (targetPosition + offset);
    }
}