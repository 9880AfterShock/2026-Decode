package org.firstinspires.ftc.teamcode.Sensors;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Gyroscope { // Prefix for commands
    private static OpMode opmode; // opmode var init
    private static IMU imu;
    private static double offset = 0.0;

    public static void initSensor(OpMode opmode) {
        imu = opmode.hardwareMap.get(IMU.class, "imu");
        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.LEFT));
        imu.initialize(parameters);
        imu.resetYaw();
        offset = 0.0;
        Gyroscope.opmode = opmode;
    }

    public static double getRotationDegrees() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) + offset;
    }

    public static void updateGyro(boolean resetOrientation){
        if (resetOrientation) {
            setRotation(0.0);
        }
        opmode.telemetry.addData("IMU ROTATION after offset is applied", getRotationDegrees());
    }

    public static void setRotation(double currentRotation) {
        offset = currentRotation;
        imu.resetYaw();
    }
}
