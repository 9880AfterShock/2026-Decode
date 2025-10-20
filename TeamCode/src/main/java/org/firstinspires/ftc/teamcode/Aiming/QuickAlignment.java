package org.firstinspires.ftc.teamcode.Aiming;

import static android.os.SystemClock.sleep;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;
public class QuickAlignment {
    final double DESIRED_DISTANCE = 12.0; //  this is how close the camera should get to the target (inches)

    //  Set the GAIN constants to control the relationship between the measured position error, and how much power is
    //  applied to the drive motors to correct the error.
    //  Drive = Error * Gain    Make these values smaller for smoother control, or larger for a more aggressive response.
    final double SPEED_GAIN  =  0.02  ;   //  Forward Speed Control "Gain". e.g. Ramp up to 50% power at a 25 inch error.   (0.50 / 25.0)
    final double STRAFE_GAIN =  0.015 ;   //  Strafe Speed Control "Gain".  e.g. Ramp up to 37% power at a 25 degree Yaw error.   (0.375 / 25.0)
    final double TURN_GAIN   =  0.01  ;   //  Turn Control "Gain".  e.g. Ramp up to 25% power at a 25 degree error. (0.25 / 25.0)

    final double MAX_AUTO_SPEED = 0.5;   //  Clip the approach speed to this max value (adjust for your robot)
    final double MAX_AUTO_STRAFE= 0.5;   //  Clip the strafing speed to this max value (adjust for your robot)
    final double MAX_AUTO_TURN  = 0.3;   //  Clip the turn speed to this max value (adjust for your robot)

    private static DcMotor leftFront;
    private static DcMotor rightFront;
    private static DcMotor leftRear;
    private static DcMotor rightRear;

    private static final int DESIRED_TAG_ID = -1;     // Choose the tag you want to approach or set to -1 for ANY tag.
    private static AprilTagProcessor aprilTag;
    private AprilTagDetection targetTag;

    boolean targetFound = false;
    double  drive = 0;
    double  strafe = 0;
    double  turn = 0;

    private static OpMode opmode;

    /**
     * Move robot according to desired axes motions
     * <p>
     * Positive X is forward
     * <p>
     * Positive Y is strafe left
     * <p>
     * Positive Yaw is counter-clockwise
     */

    public void moveRobot(double x, double y, double yaw) {
        // Calculate wheel powers.
        double frontLeftPower    =  x - y - yaw;
        double frontRightPower   =  x + y + yaw;
        double backLeftPower     =  x + y - yaw;
        double backRightPower    =  x - y + yaw;

        // Normalize wheel powers to be less than 1.0
        double max = Math.max(Math.abs(frontLeftPower), Math.abs(frontRightPower));
        max = Math.max(max, Math.abs(backLeftPower));
        max = Math.max(max, Math.abs(backRightPower));

        if (max > 1.0) {
            frontLeftPower /= max;
            frontRightPower /= max;
            backLeftPower /= max;
            backRightPower /= max;
        }

        // Send powers to the wheels.
        leftFront.setPower(frontLeftPower);
        rightFront.setPower(frontRightPower);
        leftRear.setPower(backLeftPower);
        rightRear.setPower(backRightPower);
    }

    public static void initAprilTag(OpMode opmode) {
        aprilTag = new AprilTagProcessor.Builder().build();

        // Adjust Image Decimation to trade-off detection-range for detection-rate.
        // e.g. Some typical detection data using a Logitech C920 WebCam
        // Decimation = 1 ..  Detect 2" Tag from 10 feet away at 10 Frames per second
        // Decimation = 2 ..  Detect 2" Tag from 6  feet away at 22 Frames per second
        // Decimation = 3 ..  Detect 2" Tag from 4  feet away at 30 Frames Per Second
        // Decimation = 3 ..  Detect 5" Tag from 10 feet away at 30 Frames Per Second
        // Note: Decimation can be changed on-the-fly to adapt during a match.
        aprilTag.setDecimation(2);

        VisionPortal visionPortal = new VisionPortal.Builder()
                .setCamera(opmode.hardwareMap.get(WebcamName.class, "Webcam"))
                .addProcessor(aprilTag)
                .build();

        leftFront = opmode.hardwareMap.get(DcMotor.class, "front_left_drive");
        rightFront = opmode.hardwareMap.get(DcMotor.class, "front_right_drive");
        leftRear = opmode.hardwareMap.get(DcMotor.class, "back_left_drive");
        rightRear = opmode.hardwareMap.get(DcMotor.class, "back_right_drive");

        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftRear.setDirection(DcMotor.Direction.REVERSE);
    }

    public void updateApriltags(){
        targetFound = false;
        targetTag = null;

        // Step through the list of detected tags and look for a matching tag
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        for (AprilTagDetection detection : currentDetections) {
            // Look to see if we have size info on this tag.
            if (detection.metadata != null) {
                //  Check to see if we want to track towards this tag.
                if ((DESIRED_TAG_ID < 0) || (detection.id == DESIRED_TAG_ID)) {
                    // Yes, we want to use this tag.
                    targetFound = true;
                    targetTag = detection;
                    break;
                } else {
                    opmode.telemetry.addData("Skipping", "Tag ID %d is not desired", detection.id);
                }
            } else {
                opmode.telemetry.addData("Unknown", "Tag ID %d is not in TagLibrary", detection.id);
            }
        }

        if (targetFound) {
            opmode.telemetry.addData("\n>","HOLD Left-Bumper to Drive to Target\n");
            opmode.telemetry.addData("Found", "ID %d (%s)", targetTag.id, targetTag.metadata.name);
            opmode.telemetry.addData("Range",  "%5.1f inches", targetTag.ftcPose.range);
            opmode.telemetry.addData("Bearing","%3.0f degrees", targetTag.ftcPose.bearing);
            opmode.telemetry.addData("Yaw","%3.0f degrees", targetTag.ftcPose.yaw);
        } else {
            opmode.telemetry.addData("\n>","Drive using joysticks to find valid target\n");
        }

        // If Left Bumper is being pressed, AND we have found the desired target, Drive to target Automatically .
        if (opmode.gamepad1.left_bumper && targetFound) {

            // Determine heading, range and Yaw (tag image rotation) error so we can use them to control the robot automatically.
            double  rangeError      = (targetTag.ftcPose.range - DESIRED_DISTANCE);
            double  headingError    = targetTag.ftcPose.bearing;
            double  yawError        = targetTag.ftcPose.yaw;

            drive  = Range.clip(rangeError * SPEED_GAIN, -MAX_AUTO_SPEED, MAX_AUTO_SPEED);
            turn   = Range.clip(headingError * TURN_GAIN, -MAX_AUTO_TURN, MAX_AUTO_TURN) ;
            strafe = Range.clip(-yawError * STRAFE_GAIN, -MAX_AUTO_STRAFE, MAX_AUTO_STRAFE);

            opmode.telemetry.addData("Auto","Drive %5.2f, Strafe %5.2f, Turn %5.2f ", drive, strafe, turn);
        } else {

            // drive using manual POV Joystick mode.  Slow things down to make the robot more controlable.
            drive  = -opmode.gamepad1.left_stick_y  / 2.0;  // Reduce drive rate to 50%.
            strafe = -opmode.gamepad1.left_stick_x  / 2.0;  // Reduce strafe rate to 50%.
            turn   = -opmode.gamepad1.right_stick_x / 3.0;  // Reduce turn rate to 33%.
            opmode.telemetry.addData("Manual","Drive %5.2f, Strafe %5.2f, Turn %5.2f ", drive, strafe, turn);
        }
        opmode.telemetry.update();

        // Apply desired axes motions to the drivetrain.
        moveRobot(drive, strafe, turn);
        sleep(10);
    }
}
