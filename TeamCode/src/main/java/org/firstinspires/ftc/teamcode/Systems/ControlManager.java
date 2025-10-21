package org.firstinspires.ftc.teamcode.Systems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Aiming.Alignment;
import org.firstinspires.ftc.teamcode.Aiming.DriverTest;
import org.firstinspires.ftc.teamcode.Aiming.QuickAlignment;
import org.firstinspires.ftc.teamcode.Mechanisms.DriveTrain;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Arm;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Roller;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.BallRamp;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Hood;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.Spindexer;
import org.firstinspires.ftc.teamcode.Sensors.Color;
import org.firstinspires.ftc.teamcode.Sensors.Distance;
import org.firstinspires.ftc.teamcode.Sensors.Obelisk;
import org.firstinspires.ftc.teamcode.States.BallRampState;
import org.firstinspires.ftc.teamcode.messages.BallRampMessage;
import org.firstinspires.ftc.teamcode.messages.SpindexerMessage;

public class ControlManager {
    private static OpMode opMode;
    public static Spindexer spindexer;
    public static BallRamp ballRamp;
    private static Gamepad driver;
    private static Gamepad operator;

    private static boolean prevInstake;

    public static boolean shot = false;
    public static void setup(OpMode opMode) {
        ballRamp = new BallRamp(opMode, "ramp",0.04,0.22);
        ballRamp.queueMessage(BallRampMessage.UP);
        ControlManager.opMode=opMode;
        driver = opMode.gamepad1;
        operator = opMode.gamepad2;
        spindexer = new Spindexer("spindexer", opMode, 1425.1, 10, () -> operator.a);
    }

    public static void update() {
        //Spindexer
        boolean spinLeft = operator.dpadLeftWasPressed();
        boolean spinRight = operator.dpadRightWasPressed();

        //Drive Train
        float strafe = driver.left_stick_x;
        float drive = -driver.left_stick_y;
        float turn = driver.right_stick_x;
        boolean slowMode = driver.right_trigger > 0.1;

        //Intaking
        boolean intaking = driver.left_trigger > 0.1;
        boolean ejecting = driver.left_bumper;

        //DriverTest
        boolean increase = driver.dpadUpWasPressed();
        boolean decrease = driver.dpadDownWasPressed();
        boolean rev = operator.a;
        boolean fire = driver.right_bumper;

        //BallRamp
        boolean cycleRamp = driver.bWasPressed();




        //other stuff
        if (driver.x) {
            QuickAlignment.updateApriltags();
        } else {
            DriveTrain.updateDrive(strafe, drive, turn, slowMode);
        }

        Obelisk.update();
        //SpindexerCamera.update();
        Alignment.updateAlignment();

        Roller.updateIntake(intaking, ejecting, 1.0);

        Arm.updateIntake(intaking, ejecting);

        Distance.updateSensor();

        Color.updateSensor(2.5F);

        Hood.updateAim(operator.yWasPressed());

        //Wall_E.updateTarget(operator.left_bumper, operator.right_bumper);

        DriverTest.update(increase, decrease, fire ,rev);
        if (cycleRamp || (prevInstake != intaking && ballRamp.state == BallRampState.DOWN && intaking)) {
            spindexer.queueMessage(SpindexerMessage.LINEUP);
            RunLater.addAction(new DelayedAction(() -> ballRamp.queueMessage(BallRampMessage.CYCLE), 0.2));
        }

        if (spinLeft && ((ballRamp.state == BallRampState.DOWN && shot)||ballRamp.state == BallRampState.UP)) {
            shot = false;
            spindexer.queueMessage(SpindexerMessage.RIGHT);
        }
        if (spinRight && ballRamp.state == BallRampState.UP) {
            spindexer.queueMessage(SpindexerMessage.LEFT);
        }
        spindexer.update();
        ballRamp.update();

        prevInstake = intaking;
    }
}
