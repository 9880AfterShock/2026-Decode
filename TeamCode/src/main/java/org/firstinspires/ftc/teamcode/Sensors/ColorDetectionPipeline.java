package org.firstinspires.ftc.teamcode.Sensors;

import android.graphics.Canvas;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.firstinspires.ftc.vision.VisionProcessor;

public class ColorDetectionPipeline implements VisionProcessor {
    public enum DetectedColor { PURPLE, GREEN, NONE }

    private volatile DetectedColor[] slotColors = new DetectedColor[]{DetectedColor.NONE, DetectedColor.NONE, DetectedColor.NONE};

    // Example ROI settings (tweak for your camera view)
    private final Rect roiFront = new Rect(100, 100, 100, 100);       // x, y, width, height
    private final Rect roiBackLeft = new Rect(50, 250, 100, 100);
    private final Rect roiBackRight = new Rect(150, 250, 100, 100);

    public void init(int width, int height) {
        // You can adjust ROIs here if needed, based on width/height
    }

    @Override
    public void init(int width, int height, CameraCalibration calibration) {
    }

    @Override
    public Mat processFrame(Mat input, long captureTimeNanos) {
        Mat hsv = new Mat();
        Imgproc.cvtColor(input, hsv, Imgproc.COLOR_RGB2HSV);

        // HSV Ranges
        Scalar purpleLower = new Scalar(200, 50, 50);
        Scalar purpleUpper = new Scalar(320, 255, 255);

        Scalar greenLower = new Scalar(70, 50, 50);
        Scalar greenUpper = new Scalar(160, 255, 255);

        // Process each slot
        slotColors[0] = detectColorInRegion(hsv.submat(roiFront), purpleLower, purpleUpper, greenLower, greenUpper);
        slotColors[1] = detectColorInRegion(hsv.submat(roiBackLeft), purpleLower, purpleUpper, greenLower, greenUpper);
        slotColors[2] = detectColorInRegion(hsv.submat(roiBackRight), purpleLower, purpleUpper, greenLower, greenUpper);

        // Optional: draw rectangles to show ROI zones
        Imgproc.rectangle(input, roiFront.tl(), roiFront.br(), new Scalar(255, 0, 0), 2);     // Blue
        Imgproc.rectangle(input, roiBackLeft.tl(), roiBackLeft.br(), new Scalar(0, 255, 0), 2); // Green
        Imgproc.rectangle(input, roiBackRight.tl(), roiBackRight.br(), new Scalar(0, 0, 255), 2); // Red

        hsv.release();
        return input;
    }

    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {

    }

    private DetectedColor detectColorInRegion(Mat region, Scalar purpleLow, Scalar purpleHigh, Scalar greenLow, Scalar greenHigh) {
        Mat purpleMask = new Mat();
        Mat greenMask = new Mat();

        Core.inRange(region, purpleLow, purpleHigh, purpleMask);
        Core.inRange(region, greenLow, greenHigh, greenMask);

        double purpleCount = Core.countNonZero(purpleMask);
        double greenCount = Core.countNonZero(greenMask);

        purpleMask.release();
        greenMask.release();

        if (purpleCount > greenCount && purpleCount > 200) {
            return DetectedColor.PURPLE;
        } else if (greenCount > purpleCount && greenCount > 200) {
            return DetectedColor.GREEN;
        } else {
            return DetectedColor.NONE;
        }
    }

    public DetectedColor[] getSlotColors() {
        return slotColors;
    }
}
