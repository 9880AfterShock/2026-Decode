package org.firstinspires.ftc.teamcode.Systems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.teamcode.Aiming.Alignment;
import org.firstinspires.ftc.teamcode.Aiming.DriverTest;
import org.firstinspires.ftc.teamcode.Color;
import org.firstinspires.ftc.teamcode.Enums.BallType;
import org.firstinspires.ftc.teamcode.Enums.ColorType;
import org.firstinspires.ftc.teamcode.Mechanisms.DriveTrain;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Arm;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Roller;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.BallRamp;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Hood;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.BallColorDetectinator;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.ColorClassifier;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.Spindexer;
import org.firstinspires.ftc.teamcode.Sensors.Distance;
import org.firstinspires.ftc.teamcode.States.BallRampState;
import org.firstinspires.ftc.teamcode.messages.BallRampMessage;
import org.firstinspires.ftc.teamcode.messages.SpindexerMessage;

import java.util.function.Supplier;

public class ControlManager {
    private static OpMode opMode;
    public static Spindexer spindexer;
    public static BallRamp ballRamp;
    private static Gamepad driver;
    private static Gamepad operator;

    private static boolean prevInstake;

    public static boolean shot = false;
    public static boolean canSpin = true;
    public static Supplier<Color> sensor;
    public static com.qualcomm.robotcore.hardware.NormalizedColorSensor color_sensor;
    public static ColorClassifier<BallType> classifier;
    public static double intake_speed_revving = 0.5;
    public static double intake_speed_default = 1.0;
    public static void setup(OpMode opMode) {
        ballRamp = new BallRamp(opMode, "ramp",0.08,0.25);
        color_sensor = opMode.hardwareMap.get(com.qualcomm.robotcore.hardware.NormalizedColorSensor.class,"sensorColor");
        ballRamp.queueMessage(BallRampMessage.UP);
        ControlManager.opMode=opMode;
        driver = opMode.gamepad1;
        operator = opMode.gamepad2;
        spindexer = new Spindexer("spindexer", opMode, 1425.1, 10, () -> operator.a);
        classifier = new ColorClassifier<>(BallType.NONE,0.2);
        classifier.addColor(new Color((double) 44 /255, (double) 178 /255, (double) 51 /255,ColorType.RGB), BallType.GREEN);
        classifier.addColor(new Color((double) 138 /255, (double) 44 /255, (double) 178 /255,ColorType.RGB), BallType.PURPLE);
    }

    public static void update(boolean flipField) { //false is blue, true is red
        //Spindexer
        boolean spinLeft = operator.dpadLeftWasPressed();
        boolean spinRight = operator.dpadRightWasPressed();

        //Drive Train
        float strafe = driver.left_stick_x;
        float drive = -driver.left_stick_y;
        float turn = driver.right_stick_x*0.5f;
        boolean slowMode = driver.right_trigger > 0.1;
        boolean align = driver.x;

        //Intaking
        boolean intaking = driver.left_trigger > 0.1;
        boolean ejecting = driver.left_bumper;

        //DriverTest
        boolean increase = driver.dpadUpWasPressed();
        boolean decrease = driver.dpadDownWasPressed();
        boolean rev = operator.a;
        boolean fire = driver.right_bumper;
        boolean intake_shooter = operator.x;

        //BallRamp
        boolean cycleRamp = driver.bWasPressed();



        double speed = intake_speed_default;
        if (rev) {
            speed = intake_speed_revving;
        }


        DriveTrain.updateDrive(strafe, drive, turn, slowMode, align, flipField);

        //Obelisk.update();
        //SpindexerCamera.update();
        Alignment.updateAlignment();

        Roller.updateIntake(intaking, ejecting, speed);

        Arm.updateIntake(intaking, ejecting);

        Distance.updateSensor();

        //ColorSensor.updateSensor(2.5F);

        Hood.updateAim(operator.yWasPressed());

        //Wall_E.updateTarget(operator.left_bumper, operator.right_bumper);
        if (prevInstake != intaking && intaking) {
            sensor = () -> {
                NormalizedRGBA color = color_sensor.getNormalizedColors();
                return new Color(color.red, color.green, color.blue, ColorType.RGB);
            };

            BallColorDetectinator.addSensor(sensor);

            RunLater.addAction(new DelayedAction(() -> {
                Color data = BallColorDetectinator.pullData(sensor);
                BallType classification = classifier.classify(data);
                if (classification == BallType.GREEN) {
                    spindexer.queueMessage(SpindexerMessage.INGREEN);
                } else if (classification == BallType.PURPLE) {
                    spindexer.queueMessage(SpindexerMessage.INPURPLE);
                }
            }, 0.5));
        }

        DriverTest.update(increase, decrease, fire && (ballRamp.state == BallRampState.DOWN) ,rev, intake_shooter);
        if (cycleRamp) {
            canSpin = false;
            spindexer.queueMessage(SpindexerMessage.LINEUP);
            RunCondition.addAction(new ConditionAction(() -> {ballRamp.queueMessage(BallRampMessage.CYCLE); RunLater.addAction(new DelayedAction(() -> canSpin = true, 0.9));}, spindexer::isLinedUp));
        }

        if (spinLeft && ((ballRamp.state == BallRampState.DOWN && shot)||ballRamp.state == BallRampState.UP) && canSpin) {
            shot = false;
            spindexer.queueMessage(SpindexerMessage.RIGHT);
        }
        if (spinRight && ballRamp.state == BallRampState.UP && canSpin) {
            spindexer.queueMessage(SpindexerMessage.LEFT);
        }
        spindexer.update();
        ballRamp.update();

        prevInstake = intaking;
        opMode.telemetry.addData("BallRamp mesages",ballRamp.messageQueue.size());
        opMode.telemetry.addData("Current Ball",spindexer.getCurrentBall());
    }
}

