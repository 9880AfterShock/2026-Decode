package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Color;
import org.firstinspires.ftc.teamcode.Enums.BallType;
import org.firstinspires.ftc.teamcode.Enums.ColorType;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.BallColorDetectinator;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.ColorClassifier;
import org.firstinspires.ftc.teamcode.Systems.RunLater;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Color sensor test")
public class ColorSensorTest extends LinearOpMode {

    // Declare OpMode members.
    ElapsedTime runtime = new ElapsedTime();

    //TwoDeadWheelLocalizer myLocalizer = new TwoDeadWheelLocalizer(hardwareMap, MecanumDrive.lazyImu.get(), PARAMS.inPerTick, pose);

    @Override
    public void runOpMode() throws InterruptedException {
        RunLater.setup(this);
        BallColorDetectinator.setup();

//        QuickSpindexer.initSpindexer(this);
//        QuickBallRamp.initTransfer(this);

        telemetry.addData("Status", "Initialized");
        telemetry.update();


        // Wait for the game to start (driver presses START)
        waitForStart();
        runtime.reset();
        ColorClassifier<BallType> classifier = new ColorClassifier<>(BallType.NONE, 1);
        classifier.addColor((new Color((double) 0.37, (double) 0.57, (double) 0.42, ColorType.RGB)).asHSV(), BallType.GREEN);
        classifier.addColor((new Color((double) 0.375, (double) 0.29, (double) 0.29,ColorType.RGB)).asHSV(), BallType.PURPLE);
        NormalizedColorSensor colorSensor = hardwareMap.get(NormalizedColorSensor.class, "sensorColor");
        colorSensor.setGain(40);

        //Hood.goUp();


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            NormalizedRGBA color = colorSensor.getNormalizedColors();
            Color newcolor= new Color(color.red,color.green,color.blue,ColorType.RGB);
            telemetry.addData("Found Color",classifier.classify(newcolor));
            telemetry.addData("Color",color.red+" "+color.green+" "+color.blue);
            telemetry.addData("Hue", newcolor.asHSV().getHue());
            RunLater.update();
            BallColorDetectinator.update();
            telemetry.update();
        }
        //SpindexerCamera.stopVision();
        //Obelisk.stopVision();
    }
}