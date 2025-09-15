package org.firstinspires.ftc.teamcode.OpModes;
import static org.firstinspires.ftc.teamcode.Sensors.Obelisk.visionPortal;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Aiming.Alignment;
import org.firstinspires.ftc.teamcode.Mechanisms.DriveTrain;
import org.firstinspires.ftc.teamcode.Mechanisms.Intake.Roller;
import org.firstinspires.ftc.teamcode.Sensors.Obelisk;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="9880 Decode TeleOp")
public class TeleOp extends LinearOpMode {

    // Declare OpMode members.
    ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        //Init Functions
        DriveTrain.initDrive(this);
        Obelisk.initDetection(this);
        Alignment.initAlignment(this);
        Roller.initIntake(this);

        telemetry.addData("Status", "Initialized");
        telemetry.update();


        // Wait for the game to start (driver presses START)
        waitForStart();
        runtime.reset();


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            //Loop Functions
            DriveTrain.updateDrive(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x, gamepad1.right_trigger > 0.1);
            Obelisk.update();
            Alignment.updateAlignment();
            Roller.updateIntake(gamepad2.right_trigger > 0.1, gamepad2.left_trigger > 0.1, 1.0);

            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
        visionPortal.close();
    }
}