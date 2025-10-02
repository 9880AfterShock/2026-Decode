package org.firstinspires.ftc.teamcode.OpModes;

import static org.firstinspires.ftc.teamcode.Sensors.SpindexerCamera.colorVisionPortal;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Sensors.SpindexerCamera;

@TeleOp(name = "vison testing")
//Toggle Disabled to make appear in list or not.
//@Disabled
public class VisionTest extends LinearOpMode {
    private final ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        //Add init msg
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        SpindexerCamera.initDetection(this);
        // init commands here

        //Wait for start
        //Running Loop
        while (opModeIsActive() || opModeInInit()) {
            //Tick Commands Here
            SpindexerCamera.update();
            telemetry.update();
        }
        colorVisionPortal.close();
    }
}