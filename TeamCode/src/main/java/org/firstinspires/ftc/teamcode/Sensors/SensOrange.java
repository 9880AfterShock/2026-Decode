package org.firstinspires.ftc.teamcode.Sensors;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;

public class SensOrange {
    private static OpMode opmode;
    private static int currentPosition = 0; //
    private static AnalogInput absEncoder; //Right servo feedback wire
    private static int offset = 0;
    public static void initSensor(OpMode opmode) {
        absEncoder = opmode.hardwareMap.get(AnalogInput.class, "sensOrange"); // plugged into ___
        currentPosition = 0;
        SensOrange.opmode = opmode;
    }

    public static int getCurrentPosition() {
        return currentPosition;
    }

    public static int getCurrentAngle(){
        return (int) ((absEncoder.getVoltage()-0.043)/3.1*360 + offset);
    }

    public static void updateEncoder(){
        int diff = getCurrentAngle() - ((currentPosition % 360 + 360) % 360);

        if (diff > 180) {
            diff -= 360;
        } else if (diff < -180) {
            diff += 360;
        }

        currentPosition += diff;

        opmode.telemetry.addData("SensOrange total ticks", getCurrentPosition());
        opmode.telemetry.addData("SensOrange current angle ticks", getCurrentAngle());
    }

    public static void setRotation(int newOffset) {
        offset = newOffset;
    }
}
