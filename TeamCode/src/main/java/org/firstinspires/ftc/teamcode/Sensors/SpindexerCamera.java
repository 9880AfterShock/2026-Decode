package org.firstinspires.ftc.teamcode.Sensors;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

public class SpindexerCamera {
    public static void initDetection(OpMode opmode){
        aprilTag = new AprilTagProcessor.Builder()
                // The following default settings are available to un-comment and edit as needed.
                //.setDrawAxes(false)
                //.setDrawCubeProjection(false)
                //.setDrawTagOutline(true)
                //.setTagFamily(AprilTagProcessor.TagFamily.TAG_36h11)
                //.setTagLibrary(AprilTagGameDatabase.getCenterStageTagLibrary())
                //.setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                // == CAMERA CALIBRATION ==
                // If you do not manually specify calibration parameters, the SDK will attempt
                // to load a predefined calibration for your camera.
                //.setLensIntrinsics(578.272, 578.272, 402.145, 221.506)
                // ... these parameters are fx, fy, cx, cy.
                .build();
        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(opmode.hardwareMap.get(WebcamName.class, "Webcam"));
        builder.addProcessor(aprilTag);
        VisionPortal visionPortal = builder.build();
        SpindexerCamera.opmode = opmode;
        //setManualExposure(6, 250);  // Use low exposure time to reduce motion blur
    }
}
