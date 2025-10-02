package org.firstinspires.ftc.teamcode.Sensors;

import static android.os.SystemClock.sleep;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Obelisk {
    private static OpMode opmode; // opmode var init
    private static AprilTagProcessor aprilTag;
    public static VisionPortal visionPortal;
    public enum Motifs{GPP, PGP, PPG, unknown}; // 21,22,23
    public static Motifs Motif;

    public static void initDetection(OpMode opmode){
        Motif = Motifs.unknown;
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
        visionPortal = builder.build();
        Obelisk.opmode = opmode;
        //setManualExposure(6, 250);  // Use low exposure time to reduce motion blur
    }

    public static void update(){
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        for (AprilTagDetection detection : currentDetections) {
            if (detection.id == 21){
                Motif = Motifs.GPP;
                break;
            }
            if (detection.id == 22){
                Motif = Motifs.PGP;
                break;
            }
            if (detection.id == 23){
                Motif = Motifs.PPG;
                break;
            }
        }
        opmode.telemetry.addData("Motif Pattern", Motif);
    }

    public static void stopVision() {
        visionPortal.close();
    }






    /*
     From RobotAutoDriveToAprilTagOmni.java
    */
    private static void setManualExposure(int exposureMS, int gain) {
        // Wait for the camera to be open, then use the controls

        if (visionPortal == null) {
            return;
        }

        // Make sure camera is streaming before we try to set the exposure controls
        if (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            opmode.telemetry.addData("Camera", "Waiting");
            opmode.telemetry.update();
            while (/*!isStopRequested() && */(visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING)) {
                sleep(20);
            }
            opmode.telemetry.addData("Camera", "Ready");
            opmode.telemetry.update();
        }

        // Set camera controls unless we are stopping.
        /*if (!isStopRequested())
        { */
            ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
            if (exposureControl.getMode() != ExposureControl.Mode.Manual) {
                exposureControl.setMode(ExposureControl.Mode.Manual);
                sleep(50);
            }
            exposureControl.setExposure((long)exposureMS, TimeUnit.MILLISECONDS);
            sleep(20);
            GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
            gainControl.setGain(gain);
            sleep(20);
        }
}
