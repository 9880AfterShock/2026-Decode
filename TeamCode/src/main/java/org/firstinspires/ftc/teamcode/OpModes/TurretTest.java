package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Turret;

@Disabled
@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Turret Test")
public class TurretTest extends LinearOpMode {
    ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        //Init Functions
        Turret.initTurret(this);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses START)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            if (gamepad1.left_stick_y == 0 && gamepad1.left_stick_x == 0){
                Turret.updateTurret(gamepad1.a, 0.0);
            } else {
                if (gamepad1.left_stick_y < 0){
                    Turret.updateTurret(gamepad1.a, 90+Math.toDegrees(Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x)));
                } else{
                    Turret.updateTurret(gamepad1.a, 90+Math.toDegrees(Math.atan2(-gamepad1.left_stick_y, -gamepad1.left_stick_x)));
                }
            }
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }
    }
}
