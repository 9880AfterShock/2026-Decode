package org.firstinspires.ftc.teamcode.Systems;

import static org.firstinspires.ftc.teamcode.Aiming.DriverTest.canFire;
import static org.firstinspires.ftc.teamcode.OpModes.TeleOp.alliance;

//import android.sax.StartElementListener;

import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.hardware.ams.AMSColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.teamcode.Aiming.Alignment;
import org.firstinspires.ftc.teamcode.Aiming.DriverTest;
import org.firstinspires.ftc.teamcode.Color;
import org.firstinspires.ftc.teamcode.Enums.Alliance;
import org.firstinspires.ftc.teamcode.Enums.BallType;
import org.firstinspires.ftc.teamcode.Enums.ColorType;
import org.firstinspires.ftc.teamcode.Mechanisms.DriveTrain;
import org.firstinspires.ftc.teamcode.Mechanisms.Hinge;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Arm;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Roller;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Shield;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.BallRamp;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Hood;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.BallColorDetectinator;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.ColorClassifier;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.Prongs;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.QuickSpindexer;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.Spindexer;
import org.firstinspires.ftc.teamcode.Sensors.Distance;
import org.firstinspires.ftc.teamcode.Sensors.Obelisk;
import org.firstinspires.ftc.teamcode.Sensors.SpindexerCamera;
import org.firstinspires.ftc.teamcode.States.BallRampState;
import org.firstinspires.ftc.teamcode.Systems.DelayedAction;
import org.firstinspires.ftc.teamcode.Systems.RunLater;
import org.firstinspires.ftc.teamcode.messages.BallRampMessage;
import org.firstinspires.ftc.teamcode.messages.SpindexerMessage;

import java.util.function.Supplier;

public class ControlManager {
    private static OpMode opMode;
    //    public static Spindexer spindexer;
//    public static BallRamp ballRamp;
    private static Gamepad driver;
    private static Gamepad operator;

    private static boolean prevInstake;

    public static boolean shot = true;
    //    public static boolean canSpin = true;
    public static Supplier<Color> sensor;
    public static AdafruitI2cColorSensor color_sensor;
    public static ColorClassifier<BallType> classifier;
    public static double intake_speed_revving = 0.5;
    public static double intake_speed_default = 1.0;
    private static int tripleIncrement = 0;
    private final static double delayTime = 1.0;
    private static boolean armOverride = false;
    private static boolean cyclePrepped = false;
    public static void setup(OpMode opMode) {
        color_sensor = opMode.hardwareMap.get(AdafruitI2cColorSensor.class,"sensorColor");
        color_sensor.initialize(AMSColorSensor.Parameters.createForTCS34725());
        color_sensor.setGain(10);
        ControlManager.opMode=opMode;
        driver = opMode.gamepad1;
        operator = opMode.gamepad2;
//        spindexer = new Spindexer("spindexer", opMode, 1425.1, 10, () -> operator.a);
//        classifier = new ColorClassifier<>(BallType.NONE,3);
//        classifier.addColor((new Color((double) 0.278, (double) 0.465, (double) 0.432, ColorType.RGB)).asHSV(), BallType.GREEN);
//        classifier.addColor((new Color((double) 0.28417, (double) 0.455, (double) 0.43,ColorType.RGB)).asHSV(), BallType.PURPLE);

        tripleIncrement = 0;
        armOverride = false;
        cyclePrepped = false;
    }

    public static void update(boolean flipField) {//false is blue, true is red
        if (driver.startWasPressed()) {
            if (alliance == Alliance.BLUE) {
                alliance = Alliance.RED;
            } else {
                alliance = Alliance.BLUE;
            }
        }

        if (operator.left_trigger > 0.5) {
            int nextSlot = QuickSpindexer.currentSlot-1;
            if (nextSlot < 1) nextSlot = 3;
            if (QuickSpindexer.aligned()){
                if (!armOverride && (QuickSpindexer.hasBall[QuickSpindexer.currentSlot-1] || Distance.ballInIntake())){
                    armOverride = true;
                    if (QuickSpindexer.hasBall[nextSlot-1]) {
                        QuickSpindexer.hasBall[QuickSpindexer.currentSlot-1] = true;
                    }
                }
                if (!QuickSpindexer.hasBall[nextSlot-1] && QuickSpindexer.hasBall[QuickSpindexer.currentSlot-1] && !cyclePrepped) {
                    cyclePrepped = true;
                    RunLater.addAction(new DelayedAction(() -> armOverride = false, 0.2));
                    RunLater.addAction(new DelayedAction(() -> cyclePrepped = false, 0.2));
                    RunLater.addAction(new DelayedAction(QuickSpindexer::turnIntake, 0.2));
                }
            }


//
//            if(QuickSpindexer.hasBall[QuickSpindexer.currentSlot-1]){
//                int nextSlot = QuickSpindexer.currentSlot-1;
//                if (nextSlot < 1) nextSlot = 3;
//                if ((!QuickSpindexer.hasBall[nextSlot-1]) && QuickSpindexer.aligned() && !armOverride){
//                    armOverride = true;
//                    RunLater.addAction(new DelayedAction(QuickSpindexer::turnIntake, 0.2));
//                    RunLater.addAction(new DelayedAction(() -> armOverride = false, 0.2));
//                }
//            }
        } else {
            armOverride = false;
            cyclePrepped = false;
        }

        if (operator.aWasPressed()){
            QuickSpindexer.spindexerOffset = true;
        } else if (operator.aWasReleased()) {
            QuickSpindexer.spindexerOffset = false;
        }


        //Spindexer
        boolean spinLeft = operator.dpadLeftWasPressed();
        boolean spinRight = operator.dpadRightWasPressed();

        //Drive Train
        float strafe = driver.left_stick_x;
        float drive = -driver.left_stick_y;
        float turn = driver.right_stick_x;
        boolean slowMode = driver.right_trigger > 0.1;
        boolean align = driver.x;

        //Intaking
        boolean intaking = driver.left_trigger > 0.1;
        boolean ejecting = driver.left_bumper;

        //DriverTest
        // boolean auto_shoot = operator.left_bumper;
        boolean increase = driver.dpadUpWasPressed();
        boolean decrease = driver.dpadDownWasPressed();
        boolean rev = operator.a;
        boolean fire = driver.right_bumper;
        boolean intake_shooter = operator.x;


        //Hood
        // boolean change_mode = operator.yWasPressed();

        //BallRamp
//        boolean cycleRamp = driver.bWasPressed();

        double speed = intake_speed_default;
        if (rev) {
            speed = intake_speed_revving;
        }

        DriveTrain.updateDrive(strafe, drive, turn, slowMode, align, flipField);

        //Obelisk.update();
        //SpindexerCamera.update();
        Alignment.updateAlignment();
//        opMode.telemetry.addData("Auto Shoot", (auto_shoot&&(spindexer.getCurrentBall() != BallType.NONE)));
//        Roller.updateIntake(intaking, ejecting, (fire||(auto_shoot&&(spindexer.getCurrentBall() != BallType.NONE)&&spindexer.isLinedUp())) && canFire, speed);
        Roller.updateIntake(intaking && !armOverride, ejecting, false, speed);

        if (!armOverride){
            Arm.updateIntake(intaking, ejecting, rev);
        } else {
            Arm.updateIntake(false, false, false);
        }

//        Distance.updateSensor();

//        ColorSensor.updateSensor(2.5F);

        Hood.updateAim(false);

        //Wall_E.updateTarget(operator.left_bumper, operator.right_bumper);
//        NormalizedRGBA color_test = color_sensor.getNormalizedColors();
//        opMode.telemetry.addData("sensed color", color_test.red+", "+color_test.green+", "+color_test.blue);
//        if (Distance.ballInSpindexer() && spindexer.isLinedUp() && !auto_shoot) {
//                NormalizedRGBA color = color_sensor.getNormalizedColors();
//                Color data = new Color(color.red, color.green, color.blue, ColorType.RGB);
//                opMode.telemetry.addData("color: ", data.getRed()+", "+data.getGreen()+", "+data.getBlue());
//                BallType classification = classifier.classify(data);
//                if (classification == BallType.GREEN) {
//                    spindexer.queueMessage(SpindexerMessage.INGREEN);
//                } else if (classification == BallType.PURPLE) {
//                    spindexer.queueMessage(SpindexerMessage.INPURPLE);
//                }
//        } else if (spindexer.isLinedUp() && !auto_shoot) {
//            spindexer.queueMessage(SpindexerMessage.EJECT);
//        }
//
//        if (spindexer.isLinedUp() && auto_shoot && spindexer.getCurrentBall() == BallType.NONE) {
//            spindexer.queueMessage(SpindexerMessage.LEFT);
//        }

        DriverTest.update(increase, decrease, fire, rev, /*intaking*/ true, false);
//        DriverTest.update(increase, decrease, fire||(auto_shoot&&spindexer.isLinedUp()&&(spindexer.getCurrentBall() != BallType.NONE)), rev, intake_shooter, false);
//        Shield.updateLocking(rev);
        Prongs.updateGrate(rev, operator.right_trigger > 0.5 || operator.left_trigger > 0.5 || (QuickSpindexer.hasBall[0] && QuickSpindexer.hasBall[1] && QuickSpindexer.hasBall[2]));

//        if (spinLeft) {
//            spindexer.queueMessage(SpindexerMessage.RIGHT);
//        }
//        if (spinRight) {
//            spindexer.queueMessage(SpindexerMessage.LEFT);
//        }
        QuickSpindexer.updateSpindexerResetIncluded(spinLeft && !(rev || driver.dpad_left || operator.left_trigger > 0.5 || operator.right_trigger > 0.5 || (QuickSpindexer.hasBall[0] && QuickSpindexer.hasBall[1] && QuickSpindexer.hasBall[2])), spinRight && !(!rev && (QuickSpindexer.hasBall[0] && QuickSpindexer.hasBall[1] && QuickSpindexer.hasBall[2])), operator.start && operator.back, Spindexer.reset && operator.backWasReleased());
//        if (operator.start && operator.back) {
//            Spindexer.reset = true;
//        }
//        if (Spindexer.reset && operator.backWasReleased()) {
//            Spindexer.reset = false;
//            spindexer.resetEncoder();
//        }
//        spindexer.update();
//        ballRamp.update();

        prevInstake = intaking;
//        opMode.telemetry.addData("BallRamp mesages",ballRamp.messageQueue.size());
//        opMode.telemetry.addData("Current Ball",spindexer.getCurrentBall());

        Hinge.updateBase(operator.yWasPressed());
//        opMode.telemetry.addData("ARM OVERRIDE", armOverride);
    }

    // private static boolean autoShootSpindex(double startTime, double currentTime){
    //     if (((currentTime-startTime)-(delayTime*tripleIncrement)) > 0) {
    //         tripleIncrement += 1;
    //         return true;
    //     } else {
    //         return false;
    //     }
    // }
}