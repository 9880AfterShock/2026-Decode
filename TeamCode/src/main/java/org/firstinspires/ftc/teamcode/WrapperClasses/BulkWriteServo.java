package org.firstinspires.ftc.teamcode.WrapperClasses;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

import org.firstinspires.ftc.teamcode.Systems.BulkWriterTracker;

public class BulkWriteServo implements Servo,BulkWriter {
    private final Servo rootServo;

    private double position;
    private boolean positionDirtied;

    public BulkWriteServo(Servo rootServo) {
        this.rootServo = rootServo;
        BulkWriterTracker.addWriter(this);
    }

    public void bulkWrite() {
        if (positionDirtied) {
            rootServo.setPosition(position);
            positionDirtied = false;
        }
    }

    @Override
    public ServoController getController() {
        return rootServo.getController();
    }

    @Override
    public int getPortNumber() {
        return rootServo.getPortNumber();
    }

    @Override
    public void setDirection(Direction direction) {
        rootServo.setDirection(direction);
    }

    @Override
    public Direction getDirection() {
        return rootServo.getDirection();
    }

    @Override
    public void setPosition(double position) {
        if (this.position != position) {
            this.position = position;
            positionDirtied = true;
        }
    }

    @Override
    public double getPosition() {
        return position;
    }

    @Override
    public void scaleRange(double min, double max) {
        rootServo.scaleRange(min,max);
    }

    @Override
    public Manufacturer getManufacturer() {
        return rootServo.getManufacturer();
    }

    @Override
    public String getDeviceName() {
        return rootServo.getDeviceName();
    }

    @Override
    public String getConnectionInfo() {
        return rootServo.getConnectionInfo();
    }

    @Override
    public int getVersion() {
        return rootServo.getVersion();
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {
        rootServo.resetDeviceConfigurationForOpMode();
    }

    @Override
    public void close() {
        rootServo.close();
    }
}
