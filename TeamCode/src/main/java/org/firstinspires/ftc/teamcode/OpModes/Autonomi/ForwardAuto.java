package org.firstinspires.ftc.teamcode.OpModes.Autonomi;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.QuickSpindexer;

//@Disabled
@Autonomous(name="Go forward 1 second after 25 seconds")
public class ForwardAuto extends LinearOpMode {
    private DcMotor frontLeftDrive   = null;
    private DcMotor frontRightDrive  = null;
    private DcMotor backLeftDrive   = null;
    private DcMotor backRightDrive  = null;
    static final double FORWARD_SPEED = 0.6;
    static final double STOP_SPEED = 0.0;
    private ElapsedTime runtime = new ElapsedTime();
    @Override
    public void runOpMode() {
        QuickSpindexer.initSpindexer(this); //us only
        frontLeftDrive  = hardwareMap.get(DcMotor.class, "leftFront");
        frontRightDrive = hardwareMap.get(DcMotor.class, "rightFront");
        backLeftDrive  = hardwareMap.get(DcMotor.class, "leftRear");
        backRightDrive = hardwareMap.get(DcMotor.class, "rightRear");

        frontLeftDrive.setDirection(DcMotor.Direction.REVERSE);
        backLeftDrive.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();
        runtime.reset();

        while (opModeIsActive() && (runtime.seconds() < 25)) {
            sleep(1);
        }

        //move forward
        frontLeftDrive.setPower(FORWARD_SPEED);
        frontRightDrive.setPower(FORWARD_SPEED);
        backLeftDrive.setPower(FORWARD_SPEED);
        backRightDrive.setPower(FORWARD_SPEED);
        while (opModeIsActive() && (runtime.seconds() < 1)) {
            sleep(10);
        }

        frontLeftDrive.setPower(STOP_SPEED);
        frontRightDrive.setPower(STOP_SPEED);
        backLeftDrive.setPower(STOP_SPEED);
        backRightDrive.setPower(STOP_SPEED);

        QuickSpindexer.spindexer.setTargetPosition(0); //us only

        while (opModeIsActive()) {
            sleep(1);
        }
    }
}