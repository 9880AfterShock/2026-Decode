package org.firstinspires.ftc.teamcode.OpModes.Autonomi;
import static org.firstinspires.ftc.teamcode.Sensors.Obelisk.visionPortal;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="KStatic Tuning Test")
public class KStaticTuningTest extends LinearOpMode {

    private static DcMotor leftRear; // init motor vars
    private static DcMotor leftFront;
    private static DcMotor rightRear;
    private static DcMotor rightFront;
    private static Double power;

    // Declare OpMode members.
    ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        power = 0.0;

        leftRear = hardwareMap.get(DcMotor.class, "leftRear"); // motor config names
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        rightRear = hardwareMap.get(DcMotor.class, "rightRear");
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");

        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftRear.setDirection(DcMotorSimple.Direction.REVERSE); // motor directions
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Wait for the game to start (driver presses START)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            if (gamepad1.aWasPressed()){
                power += 0.01;
            }
            if (gamepad1.bWasPressed()){
                power -= 0.01;
            }

            leftFront.setPower(power);
            leftRear.setPower(power);
            rightFront.setPower(power);
            rightRear.setPower(power);

            telemetry.addData("Power", power);
            telemetry.addData("Run Time: ", runtime.toString());
            telemetry.update();
        }
    }
}