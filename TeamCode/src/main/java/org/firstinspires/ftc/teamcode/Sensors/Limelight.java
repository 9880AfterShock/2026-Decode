package org.firstinspires.ftc.teamcode.Sensors;

import static org.firstinspires.ftc.teamcode.Enums.Motif.*;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.LLStatus;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Enums.Alliance;
import org.firstinspires.ftc.teamcode.Enums.Motif;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.IMU;

import java.util.List;

public class Limelight {
    private static OpMode opmode;
    public static Motif motif;
    private static Limelight3A limelight;
    private static final double METER_TO_INCH = 39.3701;

    public static Pose2d currentPosShoot1;
    public static Pose2d currentPosShoot2;
    public static TrajectoryActionBuilder alignShoot1;
    public static TrajectoryActionBuilder alignShoot2;

    public static void initDetection(OpMode opmode) {
        limelight = opmode.hardwareMap.get(Limelight3A.class, "limelight");
        // telemetry.setMsTransmissionInterval(11); //idk what this does but its in the docs
        limelight.pipelineSwitch(0);

        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)

        //Start up data fetching
        limelight.start();

        motif = unknown;
        Limelight.opmode = opmode;
    }

    public static void update(){
        updatePosition();
        updateMotif();

        LLStatus status = limelight.getStatus();
        opmode.telemetry.addData("Name", "%s",
                status.getName());
        opmode.telemetry.addData("LL", "Temp: %.1fC, CPU: %.1f%%, FPS: %d",
                status.getTemp(), status.getCpu(),(int)status.getFps());
        opmode.telemetry.addData("Pipeline", "Index: %d, Type: %s",
                status.getPipelineIndex(), status.getPipelineType());
    }

    public static void updateMotif() {
        LLResult result = limelight.getLatestResult();
        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
        int validTagsSeen = 0;
        for (LLResultTypes.FiducialResult fiducial : fiducials) {
            if (fiducial.getFiducialId() == 21) {
                motif = GPP;
                validTagsSeen += 1;
            }
            if (fiducial.getFiducialId() == 22) {
                motif = PGP;
                validTagsSeen += 1;
            }
            if (fiducial.getFiducialId() == 23) {
                motif = PPG;
                validTagsSeen += 1;
            }
        }
        if (validTagsSeen != 1) {
            motif = Motif.unknown;
        }

        opmode.telemetry.addData("Motif Pattern", motif);
    }

    public static void updatePosition() {
        Pose2d botpose = getPosition();
        if (botpose != null) {
            opmode.telemetry.addData("LIMELIGHT POSITION: MT2 Location:", "(" + botpose.position.x + ", " + botpose.position.y + ", " + Math.toDegrees(botpose.heading.toDouble()) + ")");
        }
//
//        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
//        for (LLResultTypes.FiducialResult fiducial : fiducials) {
//            int id = fiducial.getFiducialId(); // The ID number of the fiducial
//            opmode.telemetry.addData("ROBOT IS AT" + fiducial.getRobotPoseFieldSpace(), "AT TAG ID" + id);
//        }
    }

    public static Pose2d getPosition() { //MetaTag2
        limelight.updateRobotOrientation(Gyroscope.getRotationDegrees());
        LLResult result = limelight.getLatestResult();

        if (result != null && result.isValid()) {
            return new Pose2d(result.getBotpose_MT2().getPosition().y*METER_TO_INCH, result.getBotpose_MT2().getPosition().x*METER_TO_INCH, Math.toRadians(result.getBotpose_MT2().getOrientation().getYaw() /*- 90 */)); //convert from wipilib cords in meters to ftc cords in inches
        }
        return null;
    }

    public static Pose2d getPositionAuto() { //MetaTag2
        limelight.updateRobotOrientation(Gyroscope.getRotationDegrees());
        LLResult result = limelight.getLatestResult();

        if (result != null && result.isValid()) {
            return new Pose2d(result.getBotpose_MT2().getPosition().x*METER_TO_INCH, result.getBotpose_MT2().getPosition().y*METER_TO_INCH, Math.toRadians(result.getBotpose_MT2().getOrientation().getYaw() /*- 90 */)); //convert from wipilib cords in meters to ftc cords in inches
        }
        return null;
    }
    //
    // public static List<LLResultTypes.FiducialResult> getFiducial() { //
    //     YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
    //     limelight.updateRobotOrientation(orientation.getYaw(AngleUnit.DEGREES));
    //     LLResult result = limelight.getLatestResult();
    //     if (result != null && result.isValid()) {
    //         return result.getFiducialResults();
    //     }
    //     return null;
    // }
    //
    // public static Pose2d getFieldPosition() { //
    //     List<LLResultTypes.FiducialResult> fiducials = getFiducial();
    //     for (LLResultTypes.FiducialResult fiducial : fiducials) {
    //         int id = fiducial.getFiducialId(); // The ID number of the fiducial
    //         opmode.telemetry.addData("ROBOT IS AT" + fiducial.getRobotPoseFieldSpace(), "AT TAG ID" + id);
    //     }
    //     return null;
    // }
    public static void takeScreenShot(String fileName) {
        limelight.captureSnapshot(fileName);
    }
    public static void clearScreenShots() {
        limelight.deleteSnapshots();
    }





    public static Action AutoScan() {
        return new Action() {
            private boolean first = true;
            double scanTime;
            public boolean run(@NonNull TelemetryPacket packet) {
                if (first){
                    scanTime = opmode.getRuntime();
                    first = false;
                }

                List<LLResultTypes.FiducialResult> fiducials = limelight.getLatestResult().getFiducialResults();
                int validTagsSeen = 0;
                for (LLResultTypes.FiducialResult fiducial : fiducials) {
                    if (fiducial.getFiducialId() == 21) {
                        motif = GPP;
                        validTagsSeen += 1;
                    }
                    if (fiducial.getFiducialId() == 22) {
                        motif = PGP;
                        validTagsSeen += 1;
                    }
                    if (fiducial.getFiducialId() == 23) {
                        motif = PPG;
                        validTagsSeen += 1;
                    }
                }
                if (validTagsSeen != 1) {
                    motif = Motif.unknown;
                }

                packet.put("Motif", motif);

                if (opmode.getRuntime() - scanTime >= 2.0) {
                    motif = GPP;
                    return false;
                }
                return (motif == Motif.unknown);
            }
        };
    }

    public static Action AutoScanWithInit() {
        return new Action() {
            private boolean first = true;
            double scanTime;
            public boolean run(@NonNull TelemetryPacket packet) {
                if (first && motif != unknown){ //check if got during init
                    return false;
                }
                if (first){
                    scanTime = opmode.getRuntime();
                    first = false;
                }

                List<LLResultTypes.FiducialResult> fiducials = limelight.getLatestResult().getFiducialResults();
                int validTagsSeen = 0;
                for (LLResultTypes.FiducialResult fiducial : fiducials) {
                    if (fiducial.getFiducialId() == 21) {
                        motif = GPP;
                        validTagsSeen += 1;
                    }
                    if (fiducial.getFiducialId() == 22) {
                        motif = PGP;
                        validTagsSeen += 1;
                    }
                    if (fiducial.getFiducialId() == 23) {
                        motif = PPG;
                        validTagsSeen += 1;
                    }
                }
                if (validTagsSeen != 1) {
                    motif = Motif.unknown;
                }

                packet.put("Motif", motif);

                if (opmode.getRuntime() - scanTime >= 2.0) {
                    motif = GPP;
                    return false;
                }
                return (motif == Motif.unknown);
            }
        };
    }

    public static Action Relocalize(MecanumDrive drive) {
        return new Action() {
            public boolean run(@NonNull TelemetryPacket packet) {
                Pose2d currentPos = getPosition();
                if (currentPos == null) {
                    return true;
                } else {
                    packet.put("old pose", drive.localizer.getPose());
                    drive.localizer.setPose(currentPos);
                    packet.put("new pose", drive.localizer.getPose());
                    return false;
                }
            }
        };
    }
}