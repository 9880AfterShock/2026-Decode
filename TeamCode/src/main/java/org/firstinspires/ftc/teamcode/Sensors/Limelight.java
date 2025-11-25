package org.firstinspires.ftc.teamcode.Sensors;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.Enums.Motif;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.IMU;

import java.util.List;

public class Limelight {
    private static OpMode opmode;
    public static Motif motif;
    private static Limelight3A limelight;
    private static IMU imu;

    public static void initDetection(OpMode opmode) {
        limelight = opmode.hardwareMap.get(Limelight3A.class, "limelight");
        imu = opmode.hardwareMap.get(IMU.class, "imu");
        // telemetry.setMsTransmissionInterval(11); //idk what this does but its in the docs
        limelight.pipelineSwitch(0);

        limelight.setPollRateHz(100); // This sets how often we ask Limelight for data (100 times per second)

        //Start up data fetching
        limelight.start();

        motif = Motif.unknown;
        Limelight.opmode = opmode;
    }

    public static void update() {
        updatePosition();
//        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
//        for (AprilTagDetection detection : currentDetections) {
//            if (detection.id == 21) {
//                motif = Motif.GPP;
//                break;
//            }
//            if (detection.id == 22) {
//                motif = Motif.PGP;
//                break;
//            }
//            if (detection.id == 23) {
//                motif = Motif.PPG;
//                break;
//            }
//        }
        opmode.telemetry.addData("Motif Pattern", motif);
    }

    public static void updatePosition() {
        Pose3D botpose_mt2 = getPosition();
        if (botpose_mt2 != null) {
            double x = botpose_mt2.getPosition().x;
            double y = botpose_mt2.getPosition().y;
            opmode.telemetry.addData("MT2 Location:", "(" + x + ", " + y + ")");
        }

        Pose2d fieldPos = getFieldPosition();
        if (fieldPos != null) {
            opmode.telemetry.addData("Field Pos Location:", "(" + fieldPos.position.x + ", " + fieldPos.position.y + ")");
        }
//
//        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
//        for (LLResultTypes.FiducialResult fiducial : fiducials) {
//            int id = fiducial.getFiducialId(); // The ID number of the fiducial
//            opmode.telemetry.addData("ROBOT IS AT" + fiducial.getRobotPoseFieldSpace(), "AT TAG ID" + id);
//        }
    }

    public static Pose3D getPosition() { //MetaTag2
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        limelight.updateRobotOrientation(orientation.getYaw(AngleUnit.DEGREES));
        LLResult result = limelight.getLatestResult();

        if (result != null && result.isValid()) {
            return result.getBotpose_MT2();
        }
        return null;
    }

    public static List<LLResultTypes.FiducialResult> getFiducial() { //
        YawPitchRollAngles orientation = imu.getRobotYawPitchRollAngles();
        limelight.updateRobotOrientation(orientation.getYaw(AngleUnit.DEGREES));
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()) {
            return result.getFiducialResults();
        }
        return null;
    }

    public static Pose2d getFieldPosition() { //
        List<LLResultTypes.FiducialResult> fiducials = getFiducial();
        for (LLResultTypes.FiducialResult fiducial : fiducials) {
            int id = fiducial.getFiducialId(); // The ID number of the fiducial
            opmode.telemetry.addData("ROBOT IS AT" + fiducial.getRobotPoseFieldSpace(), "AT TAG ID" + id);
        }
        return null;
    }
}