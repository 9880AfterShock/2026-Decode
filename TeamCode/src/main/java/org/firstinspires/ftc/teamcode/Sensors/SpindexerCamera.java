package org.firstinspires.ftc.teamcode.Sensors;

import static org.firstinspires.ftc.teamcode.Enums.BallType.GREEN;
import static org.firstinspires.ftc.teamcode.Sensors.BallProcessor.ballColor.NONE;
import static org.firstinspires.ftc.teamcode.Sensors.BallProcessor.ballColor.PURPLE;
import static org.firstinspires.ftc.teamcode.Sensors.BallProcessor.ballColor.UNKNOWN;

import android.graphics.Paint;
import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionPortal;

import android.graphics.Canvas;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;


public class SpindexerCamera {

    public static VisionPortal visionPortal = null;
    public static BallProcessor ballProcessor;

    private static OpMode opmode; // opmode var init

    public static void initDetection(OpMode opmode){
        ballProcessor = new BallProcessor();

        VisionPortal.Builder builder = new VisionPortal.Builder();

        builder.setCamera(opmode.hardwareMap.get(WebcamName.class, "Webcam"));


        builder.addProcessor(ballProcessor);

        visionPortal = builder.build();

        SpindexerCamera.opmode = opmode;
    }

    public void updateSpindexerCamera() {
        opmode.telemetry.addData("Front", ballProcessor.front);
        opmode.telemetry.addData("Back Left", ballProcessor.backLeft);
        opmode.telemetry.addData("Back Right", ballProcessor.backRight);
    }
    public void stopStreaming() {
        if (visionPortal != null) {
            visionPortal.stopStreaming();
        }
    }
}










class BallProcessor implements VisionProcessor {
    public Rect rectFront = new Rect(20, 210, 80, 80);
    public Rect rectBackLeft = new Rect(200, 80, 80, 80);
    public Rect rectBackRight = new Rect(520, 100, 80, 80);
    public Mat submat = new Mat();
    public Mat hsvMat = new Mat();
    public double front = 0.0;
    public double backLeft = 0.0;
    public double backRight = 0.0;

    @Override
    public void init(int width, int height, CameraCalibration calibration) {
    }

    public Object processFrame(Mat frame, long captureTimeNanos) {
        Imgproc.cvtColor(frame, hsvMat, Imgproc.COLOR_RGB2HSV);
        front = getAvgSaturation(hsvMat, rectFront);
        backLeft = getAvgSaturation(hsvMat, rectBackLeft);
        backRight = getAvgSaturation(hsvMat, rectBackRight);

        frontColor = classifyColor(hsvMat, rectFront);
        backLeftColor = classifyColor(hsvMat, rectBackLeft);
        backRightColor = classifyColor(hsvMat, rectBackRight);
        return null;
    }

    private double getAvgSaturation(Mat input, Rect rect) {
        submat = input.submat(rect);
        org.opencv.core.Scalar color = Core.mean(submat);
        return color.val[1];
    }

    private android.graphics.Rect makeGraphicsRect(Rect rect, float scaleBmpPxToCanvasPx) {
        int left = Math.round(rect.x * scaleBmpPxToCanvasPx);
        int top = Math.round(rect.y * scaleBmpPxToCanvasPx);
        int right = left + Math.round(rect.width * scaleBmpPxToCanvasPx);
        int bottom = top + Math.round(rect.height * scaleBmpPxToCanvasPx);
        return new android.graphics.Rect(left, top, right, bottom);
    }

    @Override
    public void onDrawFrame(
            Canvas canvas,
            int onscreenWidth,
            int onscreenHeight,
            float scaleBmpPxToCanvasPx,
            float scaleCanvasDensity,
            Object userContext
    ) {
        Paint nonePaint = new Paint();
        nonePaint.setColor(Color.RED);
        nonePaint.setStyle(Paint.Style.STROKE);
        nonePaint.setStrokeWidth(scaleCanvasDensity * 4);
        Paint greenPaint = new Paint(nonePaint);
        Paint purplePaint = new Paint(nonePaint);
        greenPaint.setColor(Color.GREEN);
        purplePaint.setColor(Color.MAGENTA);



        android.graphics.Rect drawRectangleFront = makeGraphicsRect(rectFront, scaleBmpPxToCanvasPx);
        android.graphics.Rect drawRectangleBackLeft = makeGraphicsRect(rectBackLeft, scaleBmpPxToCanvasPx);
        android.graphics.Rect drawRectangleBackRight = makeGraphicsRect(rectBackRight, scaleBmpPxToCanvasPx);
        switch (ballColor) {
            case PURPLE:
                canvas.drawRect(drawRectangleFront, purplePaint);
                canvas.drawRect(drawRectangleBackLeft, purplePaint);
                canvas.drawRect(drawRectangleBackRight, purplePaint);
                break;

            case GREEN:
                canvas.drawRect(drawRectangleFront, greenPaint);
                canvas.drawRect(drawRectangleBackLeft, greenPaint);
                canvas.drawRect(drawRectangleBackRight, greenPaint);
                break;

            case NONE:
                canvas.drawRect(drawRectangleFront, nonePaint);
                canvas.drawRect(drawRectangleBackLeft, nonePaint);
                canvas.drawRect(drawRectangleBackRight, nonePaint);
                break;

            case UNKNOWN:
                canvas.drawRect(drawRectangleFront, nonePaint);
                canvas.drawRect(drawRectangleBackLeft, nonePaint);
                canvas.drawRect(drawRectangleBackRight, nonePaint);
                break;
        }
    }
    public enum ballColor {
        NONE, PURPLE, GREEN, UNKNOWN
    }
}
