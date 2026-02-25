package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
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

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses START)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            SensOrange.updateEncoder();
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
    }
}
