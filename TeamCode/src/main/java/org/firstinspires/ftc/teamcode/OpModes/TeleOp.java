package org.firstinspires.ftc.teamcode.OpModes;

import static org.firstinspires.ftc.teamcode.MecanumDrive.PARAMS;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Aiming.Alignment;
import org.firstinspires.ftc.teamcode.Aiming.DriverTest;
import org.firstinspires.ftc.teamcode.Aiming.GoalVision;
import org.firstinspires.ftc.teamcode.Mechanisms.DriveTrain;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Arm;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Roller;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Shield;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Hood;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Transfer;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.BallColorDetectinator;
import org.firstinspires.ftc.teamcode.Sensors.Distance;
import org.firstinspires.ftc.teamcode.Systems.ControlManager;
import org.firstinspires.ftc.teamcode.Systems.RunCondition;
import org.firstinspires.ftc.teamcode.Systems.RunLater;
import org.firstinspires.ftc.teamcode.TwoDeadWheelLocalizer;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="9880 Decode TeleOp")
public class TeleOp extends LinearOpMode {

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
        Distance.initSensor(this);
        //ColorSensor.initSensor(this);
        //Wall_E.initWebcam(this);
        DriverTest.initControls(this);
        Hood.initAim(this);
        //QuickAlignment.initAprilTag(this);
        GoalVision.initAprilTag(this);
        Shield.initLocking(this);

//        QuickSpindexer.initSpindexer(this);
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


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            ControlManager.update(false); //will need to add inpute using speical buttons for this later, not using atm
            RunLater.update();
            RunCondition.update();
            BallColorDetectinator.update();
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
        //SpindexerCamera.stopVision();
        //Obelisk.stopVision();
    }
}