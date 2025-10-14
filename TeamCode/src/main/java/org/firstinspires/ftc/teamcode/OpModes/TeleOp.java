package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Aiming.Alignment;
import org.firstinspires.ftc.teamcode.Aiming.DriverTest;
import org.firstinspires.ftc.teamcode.Mechanisms.DriveTrain;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Arm;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Roller;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.QuickBallRamp;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Shooter;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Transfer;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.QuickSpindexer;
import org.firstinspires.ftc.teamcode.Sensors.Color;
import org.firstinspires.ftc.teamcode.Sensors.Distance;
import org.firstinspires.ftc.teamcode.Sensors.Obelisk;
import org.firstinspires.ftc.teamcode.Systems.ControlManager;
import org.firstinspires.ftc.teamcode.Systems.RunCondition;
import org.firstinspires.ftc.teamcode.Systems.RunLater;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="9880 Decode TeleOp")
public class TeleOp extends LinearOpMode {

    // Declare OpMode members.
    ElapsedTime runtime = new ElapsedTime();

    //TwoDeadWheelLocalizer myLocalizer = new TwoDeadWheelLocalizer(hardwareMap, MecanumDrive.lazyImu.get(), PARAMS.inPerTick, pose);

    @Override
    public void runOpMode() {

        //Init Functions
        DriveTrain.initDrive(this);
        RunLater.setup(this);
        //FieldCentricDrive.initDrive(this);
        Obelisk.initDetection(this);
        //SpindexerCamera.initDetection(this);
        Alignment.initAlignment(this);
        Roller.initIntake(this);
        Arm.initIntake(this);
        Shooter.initShooter(this);
        Transfer.initTransfer(this);
        Distance.initSensor(this);
        Color.initSensor(this);
        //Wall_E.initWebcam(this);
        DriverTest.initControls(this);

//        QuickSpindexer.initSpindexer(this);
//        QuickBallRamp.initTransfer(this);

        telemetry.addData("Status", "Initialized");
        telemetry.update();


        // Wait for the game to start (driver presses START)
        waitForStart();
        runtime.reset();
        ControlManager.setup(this);


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            ControlManager.update();
            RunLater.update();
            RunCondition.update();
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
        //SpindexerCamera.stopVision();
        Obelisk.stopVision();
    }
}