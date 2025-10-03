package org.firstinspires.ftc.teamcode.Systems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Aiming.Alignment;
import org.firstinspires.ftc.teamcode.Aiming.DriverTest;
import org.firstinspires.ftc.teamcode.Mechanisms.DriveTrain;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Arm;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Roller;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Transfer;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.Spindexer;
import org.firstinspires.ftc.teamcode.Mechanisms.Wall_E;
import org.firstinspires.ftc.teamcode.Sensors.Color;
import org.firstinspires.ftc.teamcode.Sensors.Distance;
import org.firstinspires.ftc.teamcode.Sensors.Obelisk;
import org.firstinspires.ftc.teamcode.Sensors.SpindexerCamera;
import org.firstinspires.ftc.teamcode.messages.SpindexerMessage;

public class ControlManager {
    private static OpMode opMode;
    public static Spindexer spindexer;
    private static Gamepad driver;
    private static Gamepad operator;
    public static void setup(OpMode opMode, Spindexer spindexer) {
        ControlManager.spindexer = spindexer;
        ControlManager.opMode=opMode;
        driver = opMode.gamepad1;
        operator = opMode.gamepad2;
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
        boolean fire = driver.a;





        //other stuff
        DriveTrain.updateDrive(strafe, drive, turn, slowMode);

        //Obelisk.update();
        SpindexerCamera.update();
        Alignment.updateAlignment();

        Roller.updateIntake(intaking, ejecting, 1.0);

        Arm.updateIntake(intaking, ejecting);

        Distance.updateSensor();

        Color.updateSensor(2.5F);

        Wall_E.updateTarget(operator.left_bumper, operator.right_bumper);

        DriverTest.update(increase, decrease, fire ,rev);

        if (spinLeft && Transfer.spindexerSafe) {
            spindexer.queueMessage(SpindexerMessage.LEFT);
        }
        if (spinRight && Transfer.spindexerSafe) {
            spindexer.queueMessage(SpindexerMessage.RIGHT);
        }
        spindexer.update();
    }
}
