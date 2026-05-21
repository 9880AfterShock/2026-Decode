package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Aiming.Alignment;
import org.firstinspires.ftc.teamcode.Aiming.DriverTest;
import org.firstinspires.ftc.teamcode.Aiming.GoalVision;
import org.firstinspires.ftc.teamcode.Enums.Alliance;
import org.firstinspires.ftc.teamcode.Mechanisms.DriveTrain;
import org.firstinspires.ftc.teamcode.Mechanisms.Hinge;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Arm;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Roller;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Hood;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Turret;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.BallColorDetectinator;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.Prongs;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.QuickSpindexer;
import org.firstinspires.ftc.teamcode.Sensors.Distance;
import org.firstinspires.ftc.teamcode.Sensors.Gyroscope;
import org.firstinspires.ftc.teamcode.Sensors.Limelight;
import org.firstinspires.ftc.teamcode.Sensors.SensOrange;
import org.firstinspires.ftc.teamcode.Systems.BulkWriterTracker;
import org.firstinspires.ftc.teamcode.Systems.ControlManager;
import org.firstinspires.ftc.teamcode.Systems.RunCondition;
import org.firstinspires.ftc.teamcode.Systems.RunCountdown;
import org.firstinspires.ftc.teamcode.Systems.RunLater;
import java.util.List;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="9880 Decode TeleOp")
public class TeleOp extends LinearOpMode {
    public static Alliance alliance;
    public static Pose2d autoEndPosition;
    public static boolean afterAuto;
    public static boolean autoHasBalls;
    public static List<LynxModule> allHubs;

    // Declare OpMode members.
    ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        SensOrange.initSensor(this);

        //Init Functions
        DriveTrain.initDrive(this);
        RunLater.setup(this);
        RunCountdown.setup(this);
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
        Turret.initTurret(this, afterAuto);

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
        double loops = 1;

        allHubs = hardwareMap.getAll(LynxModule.class);

        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        while (opModeIsActive()) {
            afterAuto = false;
            SensOrange.updateEncoder();
            Gyroscope.updateGyro(gamepad1.backWasPressed());
            for (LynxModule hub : allHubs) {
                hub.clearBulkCache();
            }
            Distance.updateSensor();
            Limelight.update();
            ControlManager.update(alliance == Alliance.RED);
            RunLater.update();
            RunCondition.update();
            RunCountdown.update();
//            BallColorDetectinator.update();


            if (gamepad2.left_stick_button){
                if (gamepad2.left_stick_y == 0 && gamepad2.left_stick_x == 0){
                    Turret.updateTurret(true, 0.0);
                } else {
                    if (gamepad2.left_stick_y < 0){
                        Turret.updateTurret(true, 90+Math.toDegrees(Math.atan2(gamepad2.left_stick_y, gamepad2.left_stick_x)));
                    } else{
                        Turret.updateTurret(true, 90+Math.toDegrees(Math.atan2(-gamepad2.left_stick_y, -gamepad2.left_stick_x)));
                    }
                }
            } else {
                Turret.updateTurret(false, 0.0);
            }

//            Distance.updateSensor();
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Alliance", alliance);
            telemetry.addData("Looptime (MS per Loop)", runtime.milliseconds()/loops);
            loops += 1;
            telemetry.update();
//            for (LynxModule hub : allHubs) {
//                hub.clearBulkCache();
//            }
            BulkWriterTracker.update();
        }
        //SpindexerCamera.stopVision();
        //Obelisk.stopVision();
    }
}