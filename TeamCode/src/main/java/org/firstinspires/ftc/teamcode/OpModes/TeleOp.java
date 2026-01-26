package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Aiming.Alignment;
import org.firstinspires.ftc.teamcode.Aiming.DriverTest;
import org.firstinspires.ftc.teamcode.Aiming.GoalVision;
import org.firstinspires.ftc.teamcode.Enums.Alliance;
import org.firstinspires.ftc.teamcode.Mechanisms.DriveTrain;
import org.firstinspires.ftc.teamcode.Mechanisms.Hinge;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Arm;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Roller;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Shield;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Hood;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Transfer;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.BallColorDetectinator;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.Prongs;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.QuickSpindexer;
import org.firstinspires.ftc.teamcode.Sensors.Distance;
import org.firstinspires.ftc.teamcode.Sensors.Gyroscope;
import org.firstinspires.ftc.teamcode.Sensors.Limelight;
import org.firstinspires.ftc.teamcode.Systems.ControlManager;
import org.firstinspires.ftc.teamcode.Systems.RunCondition;
import org.firstinspires.ftc.teamcode.Systems.RunLater;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="9880 Decode TeleOp")
public class TeleOp extends LinearOpMode {
    public static Alliance alliance;
    public static Pose2d autoEndPosition;
    public static boolean autoHasBalls;

    // Declare OpMode members.
    ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        //Init Functions
        DriveTrain.initDrive(this);
        RunLater.setup(this);
        //FieldCentricDrive.initDrive(this);
        //Obelisk.initDetection(this);
        //SpindexerCamera.initDetection(this);
        BallColorDetectinator.setup();
        Alignment.initAlignment(this);
        Roller.initIntake(this);
        Arm.initIntake(this);
//        Transfer.initTransfer(this);
//        Distance.initSensor(this);
        //ColorSensor.initSensor(this);
        //Wall_E.initWebcam(this);
        DriverTest.initControls(this);
        Hood.initAim(this);
        //QuickAlignment.initAprilTag(this);
        GoalVision.initAprilTag(this);
//        Shield.initLocking(this);
        Prongs.initGrate(this);
        Distance.initSensor(this);
        Limelight.initDetection(this);
        Gyroscope.initSensor(this);
        Hinge.initBase(this);

        QuickSpindexer.initSpindexer(this);
//        QuickBallRamp.initTransfer(this);

//        RRTeleOp RRdrive = new RRTeleOp(hardwareMap);


        telemetry.addData("Status", "Initialized");
        telemetry.update();


        // Wait for the game to start (driver presses START)
        waitForStart();
        runtime.reset();
        resetRuntime();
        ControlManager.setup(this);
        Hood.goNear();

        if (alliance == null){
            alliance = Alliance.BLUE;
        }
        if (autoEndPosition == null) {
            autoEndPosition = new Pose2d(0.0, 0.0, 0.0);
        }
        if (autoHasBalls){
            QuickSpindexer.hasBall[0] = true;
            QuickSpindexer.hasBall[1] = true;
            QuickSpindexer.hasBall[2] = true;
            autoHasBalls = false;
        }
        Gyroscope.setRotation(Math.toDegrees(autoEndPosition.heading.toDouble()));
        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            Gyroscope.updateGyro(gamepad1.backWasPressed());
            Limelight.update();
            ControlManager.update(alliance == Alliance.RED);
            RunLater.update();
            RunCondition.update();
//            BallColorDetectinator.update();
            Distance.updateSensor();
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Alliance", alliance);
            telemetry.update();
        }
        //SpindexerCamera.stopVision();
        //Obelisk.stopVision();
    }
}