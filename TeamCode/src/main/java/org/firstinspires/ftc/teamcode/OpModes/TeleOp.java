package org.firstinspires.ftc.teamcode.OpModes;
import static org.firstinspires.ftc.teamcode.Sensors.Obelisk.visionPortal;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Aiming.Alignment;
import org.firstinspires.ftc.teamcode.Mechanisms.DriveTrain;
import org.firstinspires.ftc.teamcode.Mechanisms.FieldCentricDrive;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Arm;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Roller;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Shooter;
import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Transfer;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.QuickSpindexer;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.Spindexer;
import org.firstinspires.ftc.teamcode.Sensors.Obelisk;
import org.firstinspires.ftc.teamcode.messages.SpindexerMessage;

import java.lang.reflect.Field;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="9880 Decode TeleOp")
public class TeleOp extends LinearOpMode {

    // Declare OpMode members.
    ElapsedTime runtime = new ElapsedTime();
    //public Spindexer spindexer = new Spindexer("spindexer", this, 537.7);

    @Override
    public void runOpMode() {

        //Init Functions
        DriveTrain.initDrive(this);
        FieldCentricDrive.initDrive(this);
        Obelisk.initDetection(this);
        Alignment.initAlignment(this);
        Roller.initIntake(this);
        Arm.initIntake(this);
        Shooter.initShooter(this);
        //Transfer.initTransfer(this);

        QuickSpindexer.initSpindexer(this);

        telemetry.addData("Status", "Initialized");
        telemetry.update();


        // Wait for the game to start (driver presses START)
        waitForStart();
        runtime.reset();


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            //Loop Functions
            DriveTrain.updateDrive(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x, gamepad1.right_trigger > 0.1);
            FieldCentricDrive.updateDrive(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x, gamepad1.right_trigger > 0.1, gamepad1.optionsWasPressed());
            Obelisk.update();
            Alignment.updateAlignment();
            Roller.updateIntake(gamepad1.left_trigger > 0.1, gamepad1.left_bumper, 1.0);
            Arm.updateIntake(gamepad1.left_trigger > 0.1, gamepad1.left_bumper);
            Shooter.updateShooter(gamepad1.a);
            //Transfer.updateTransfer(gamepad1.a);


            QuickSpindexer.updateSpindexer(gamepad1.dpad_right, gamepad1.dpad_left);
//            if (gamepad1.dpadLeftWasPressed()) {
//                spindexer.queueMessage(SpindexerMessage.LEFT);
//            }
//            if (gamepad1.dpadRightWasPressed()) {
//                spindexer.queueMessage(SpindexerMessage.RIGHT);
//            }
//            spindexer.update();
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
        visionPortal.close();
    }
}