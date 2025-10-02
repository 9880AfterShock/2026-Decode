package org.firstinspires.ftc.teamcode.Sensors;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class ColorDetectionPipeline extends OpenCvPipeline {

    // Define the square ROI (top-left corner and size)
    private final int REGION_TOPLEFT_X = 100;
    private final int REGION_TOPLEFT_Y = 100;
    private final int REGION_WIDTH = 50;
    private final int REGION_HEIGHT = 50;

    // HSV color bounds for detection (example: red)
    private final Scalar lowerHSV = new Scalar(0, 100, 100);  // Lower bound of color
    private final Scalar upperHSV = new Scalar(10, 255, 255); // Upper bound of color

    private double colorPercentage = 0;

    public double getColorPercentage() {
        return colorPercentage;
    }

    @Override
    public Mat processFrame(Mat input) {
        // Convert to HSV
        Mat hsv = new Mat();
        Imgproc.cvtColor(input, hsv, Imgproc.COLOR_RGB2HSV);

        // Define region of interest
        Rect roi = new Rect(REGION_TOPLEFT_X, REGION_TOPLEFT_Y, REGION_WIDTH, REGION_HEIGHT);
        Mat region = hsv.submat(roi);

        // Apply mask
        Mat mask = new Mat();
        Core.inRange(region, lowerHSV, upperHSV, mask);

        // Count non-zero pixels (i.e., matching color)
        int matchingPixels = Core.countNonZero(mask);
        int totalPixels = REGION_WIDTH * REGION_HEIGHT;

        // Calculate percentage of matching color
        colorPercentage = (double) matchingPixels / totalPixels;

        // Optional: draw rectangle on screen
        Imgproc.rectangle(input, roi.tl(), roi.br(), new Scalar(0, 255, 0), 2);

        return input; // return input to display it on screen
    }
}
