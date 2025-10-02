package org.firstinspires.ftc.teamcode.Sensors;

import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.openftc.easyopencv.OpenCvWebcam;

public class SpindexerCamera {
    private static OpMode opmode; // opmode var init
    static ColorDetectionPipeline pipeline;
    static OpenCvWebcam webcam;

    public static void initDetection(OpMode opmode){
        int cameraMonitorViewId = opmode.hardwareMap.appContext.getResources().getIdentifier(
                "cameraMonitorViewId", "id", opmode.hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(
                opmode.hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

        // Set pipeline
        pipeline = new ColorDetectionPipeline();
        webcam.setPipeline(pipeline);

        // Open camera asynchronously
        webcam.openCameraDeviceAsync(() -> webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT));





        VisionPortal.Builder builder = new VisionPortal.Builder();

        builder.setCamera(opmode.hardwareMap.get(WebcamName.class, "Webcam"));

        visionPortal = builder.build();

        SpindexerCamera.opmode = opmode;
    }

    public void updateSpindexerCamera() {
        opmode.telemetry.addData("Color %", pipeline.getColorPercentage() * 100);
        opmode.telemetry.update();
    }
    public void stopStreaming() {
        if (visionPortal != null) {
            visionPortal.stopStreaming();
        }
    }
}
