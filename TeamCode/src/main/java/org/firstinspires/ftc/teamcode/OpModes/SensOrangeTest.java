package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Turret;
import org.firstinspires.ftc.teamcode.Sensors.SensOrange;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="SensOrange Test")
public class SensOrangeTest extends LinearOpMode {
    ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        //Init Functions
        SensOrange.initSensor(this);
//        DcMotorEx shooterUp = hardwareMap.get(DcMotorEx.class, "shooterUp"); //Port 3 or 4 on the expansion hub //one with encoder
//        DcMotorEx shooterDown = hardwareMap.get(DcMotorEx.class, "shooterDown"); //Port 3 or 4 on the expansion hub //one without encoder

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses START)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
//            shooterUp.setPower(gamepad1.left_stick_x);
//            shooterDown.setPower(gamepad1.right_stick_x);
            SensOrange.updateEncoder();
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
    }
}
