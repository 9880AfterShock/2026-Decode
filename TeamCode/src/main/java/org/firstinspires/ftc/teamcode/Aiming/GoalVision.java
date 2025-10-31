package org.firstinspires.ftc.teamcode.Aiming;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.teamcode.Maths.Trajectory;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

public class GoalVision {
    private static AprilTagProcessor aprilTag;
    private static AprilTagDetection targetTag;

    static boolean targetFound = false;

    private static OpMode opmode;
    private final static double webcamAngle = 12.0; //degrees
    private final static double aprilTagHeight = 29.5; //inches
    private static VisionPortal visionPortal;

    public static void initAprilTag(OpMode opmode) {
        aprilTag = new AprilTagProcessor.Builder().build();

        /* Adjust Image Decimation to trade-off detection-range for detection-rate.
        e.g. Some typical detection data using a Logitech C920 WebCam
        Decimation = 1 ..  Detect 2" Tag from 10 feet away at 10 Frames per second
        Decimation = 2 ..  Detect 2" Tag from 6  feet away at 22 Frames per second
        Decimation = 3 ..  Detect 2" Tag from 4  feet away at 30 Frames Per Second
        Decimation = 3 ..  Detect 5" Tag from 10 feet away at 30 Frames Per Second
        Note: Decimation can be changed on-the-fly to adapt during a match. */

        aprilTag.setDecimation(2);

        visionPortal = new VisionPortal.Builder()
                .setCamera(opmode.hardwareMap.get(WebcamName.class, "Webcam"))
                .addProcessor(aprilTag)
                .build();

        GoalVision.opmode = opmode;
    }

    public static void stopVision() {
        visionPortal.close();
    }

    public static double getRotation(){
        targetFound = false;
        targetTag = null;

        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                if ((detection.id == 20) || (detection.id == 24)) { //20 is blue, 24 is red
                    targetFound = true;
                    targetTag = detection;
                    break;
                }
            }
        }

        if (targetFound) {
            //Webcam is upside down lol
            double headingError = targetTag.ftcPose.bearing;
            opmode.telemetry.addData("Heading Error APRILTAG", headingError);
            double distanceDirect = sqrt((targetTag.ftcPose.x*targetTag.ftcPose.x) + (targetTag.ftcPose.x*targetTag.ftcPose.x));
            double distanceGround = (-targetTag.ftcPose.z * cos(toRadians(webcamAngle))) - (-targetTag.ftcPose.y * sin(toRadians(webcamAngle)));
            opmode.telemetry.addData("Distance Apriltag UNCORRECTED?", distanceDirect);
            opmode.telemetry.addData("Distance Apriltag CORRECTED", distanceGround);
            return headingError;
        } else {
            opmode.telemetry.addData("Heading Error APRILTAG", "NO TAG FOUND");
            return -9880.0;
        }
    }
}