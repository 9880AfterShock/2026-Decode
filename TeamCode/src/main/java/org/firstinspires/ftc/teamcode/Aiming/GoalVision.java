package org.firstinspires.ftc.teamcode.Aiming;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
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
    public final static double webcamAngle = 12.0; //degrees
    public static double goalDistance;
//    public static Pose2d lastSeen;
//    private final static double aprilTagHeight = 29.5; //inches
    private static VisionPortal visionPortal;

    private static final Position cameraPosition = new Position(DistanceUnit.INCH,
            0, -5.118, 13.0315, 0);
    private static final YawPitchRollAngles cameraOrientation = new YawPitchRollAngles(AngleUnit.DEGREES,
            180, -90+webcamAngle, 180, 0);

    public static void initAprilTag(OpMode opmode) {
        aprilTag = new AprilTagProcessor.Builder()
        .setCameraPose(cameraPosition, cameraOrientation)
        .build();

        /* Adjust Image Decimation to trade-off detection-range for detection-rate.
        e.g. Some typical detection data using a Logitech C920 WebCam
        Decimation = 1 ..  Detect 2" Tag from 10 feet away at 10 Frames per second
        Decimation = 2 ..  Detect 2" Tag from 6  feet away at 22 Frames per second
        Decimation = 3 ..  Detect 2" Tag from 4  feet away at 30 Frames Per Second
        Decimation = 3 ..  Detect 5" Tag from 10 feet away at 30 Frames Per Second
        Note: Decimation can be changed on-the-fly to adapt during a match. */

        //see the localization sample for more options

        aprilTag.setDecimation(2);

        visionPortal = new VisionPortal.Builder()
                .setCamera(opmode.hardwareMap.get(WebcamName.class, "Webcam"))
                .addProcessor(aprilTag)
                .build();

        GoalVision.opmode = opmode;
        goalDistance = 0.0;

//        lastSeen = null;
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
            opmode.telemetry.addData("Heading Apriltag ERROR", headingError);

            goalDistance = getTrueDistance(targetTag.ftcPose.range);
            opmode.telemetry.addData("Distance Apriltag RANGE CORRECTED", goalDistance);

            //Can re-orient from either tag.
//            lastSeen = new Pose2d(targetTag.robotPose.getPosition().x, targetTag.robotPose.getPosition().y, targetTag.robotPose.getOrientation().getYaw());

            return headingError;
        } else {
            opmode.telemetry.addData("Heading Error APRILTAG", "NO TAG FOUND");
            return -9880.0;
        }
    }

    public static double getTrueDistance(double range){
        return range * Math.cos(Math.toRadians(webcamAngle));
    }

    public static Pose2d getPos() {

        return null;
    }
}