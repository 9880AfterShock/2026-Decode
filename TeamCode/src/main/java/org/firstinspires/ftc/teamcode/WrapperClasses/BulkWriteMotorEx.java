package org.firstinspires.ftc.teamcode.WrapperClasses;

import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.teamcode.Systems.BulkWriterTracker;

public class BulkWriteMotorEx implements DcMotorEx,BulkWriter {
    private final DcMotorEx rootMotor;
    private int targetPos;
    private boolean targetPosDirtied = false;
    private double power;
    private boolean powerDirtied = false;
    private double velocity;
    private AngleUnit velocityUnit;
    private boolean velocityDirtied = false;
    public BulkWriteMotorEx(DcMotorEx rootMotor) {
        this.rootMotor = rootMotor;
        BulkWriterTracker.addWriter(this);
    }

    public void bulkWrite() {
        if (targetPosDirtied) {
            rootMotor.setTargetPosition(targetPos);
            targetPosDirtied = false;
        }
        if (powerDirtied) {
            rootMotor.setPower(power);
            powerDirtied = false;
        }
        if (velocityDirtied) {
            if (velocityUnit != null) {
                rootMotor.setVelocity(velocity,velocityUnit);
            } else {
                rootMotor.setVelocity(velocity);
            }
            velocityDirtied = false;
        }
    }

    @Override
    public void setMotorEnable() {
        rootMotor.setMotorEnable();
    }

    @Override
    public void setMotorDisable() {
        rootMotor.setMotorDisable();
    }

    @Override
    public boolean isMotorEnabled() {
        return rootMotor.isMotorEnabled();
    }

    @Override
    public void setVelocity(double angularRate) {
        if (velocity != angularRate || velocityUnit != null) {
            velocity = angularRate;
            velocityUnit = null;
            velocityDirtied = true;
        }
    }

    @Override
    public void setVelocity(double angularRate, AngleUnit unit) {
        if (velocity != angularRate || velocityUnit != unit) {
            velocity = angularRate;
            velocityUnit = unit;
            velocityDirtied = true;
        }
    }

    @Override
    public double getVelocity() {
        return rootMotor.getVelocity();
    }

    @Override
    public double getVelocity(AngleUnit unit) {
        return rootMotor.getVelocity(unit);
    }

    @Override
    public void setPIDCoefficients(RunMode mode, PIDCoefficients pidCoefficients) {
        rootMotor.setPIDCoefficients(mode,pidCoefficients);
    }

    @Override
    public void setPIDFCoefficients(RunMode mode, PIDFCoefficients pidfCoefficients) throws UnsupportedOperationException {
        rootMotor.setPIDFCoefficients(mode, pidfCoefficients);
    }

    @Override
    public void setVelocityPIDFCoefficients(double p, double i, double d, double f) {
        rootMotor.setVelocityPIDFCoefficients(p, i, d, f);
    }

    @Override
    public void setPositionPIDFCoefficients(double p) {
        rootMotor.setPositionPIDFCoefficients(p);
    }

    @Override
    public PIDCoefficients getPIDCoefficients(RunMode mode) {
        return rootMotor.getPIDCoefficients(mode);
    }

    @Override
    public PIDFCoefficients getPIDFCoefficients(RunMode mode) {
        return rootMotor.getPIDFCoefficients(mode);
    }

    @Override
    public void setTargetPositionTolerance(int tolerance) {
        rootMotor.setTargetPositionTolerance(tolerance);
    }

    @Override
    public int getTargetPositionTolerance() {
        return rootMotor.getTargetPositionTolerance();
    }

    @Override
    public double getCurrent(CurrentUnit unit) {
        return rootMotor.getCurrent(unit);
    }

    @Override
    public double getCurrentAlert(CurrentUnit unit) {
        return rootMotor.getCurrentAlert(unit);
    }

    @Override
    public void setCurrentAlert(double current, CurrentUnit unit) {
        rootMotor.setCurrentAlert(current,unit);
    }

    @Override
    public boolean isOverCurrent() {
        return rootMotor.isOverCurrent();
    }

    @Override
    public MotorConfigurationType getMotorType() {
        return rootMotor.getMotorType();
    }

    @Override
    public void setMotorType(MotorConfigurationType motorType) {
        rootMotor.setMotorType(motorType);
    }

    @Override
    public DcMotorController getController() {
        return rootMotor.getController();
    }

    @Override
    public int getPortNumber() {
        return rootMotor.getPortNumber();
    }

    @Override
    public void setZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {
        rootMotor.setZeroPowerBehavior(zeroPowerBehavior);
    }

    @Override
    public ZeroPowerBehavior getZeroPowerBehavior() {
        return rootMotor.getZeroPowerBehavior();
    }

    @Override
    public void setPowerFloat() {
        this.setZeroPowerBehavior(ZeroPowerBehavior.FLOAT);
        this.setPower(0);
    }

    @Override
    public boolean getPowerFloat() {
        return this.getZeroPowerBehavior() == ZeroPowerBehavior.FLOAT && this.getPower() == 0;
    }

    @Override
    public void setTargetPosition(int position) {
        if (targetPos != position) {
            targetPos = position;
            targetPosDirtied = true;
        }
    }

    @Override
    public int getTargetPosition() {
        return targetPos;
    }

    @Override
    public boolean isBusy() {
        return rootMotor.isBusy();
    }

    @Override
    public int getCurrentPosition() {
        return rootMotor.getCurrentPosition();
    }

    @Override
    public void setMode(RunMode mode) {
        rootMotor.setMode(mode);
    }

    @Override
    public RunMode getMode() {
        return rootMotor.getMode();
    }

    @Override
    public void setDirection(Direction direction) {
        rootMotor.setDirection(direction);
    }

    @Override
    public Direction getDirection() {
        return rootMotor.getDirection();
    }

    @Override
    public void setPower(double power) {
        if (this.power != power) {
            this.power = power;
            this.powerDirtied = true;
        }
    }

    @Override
    public double getPower() {
        return this.power;
    }

    @Override
    public Manufacturer getManufacturer() {
        return rootMotor.getManufacturer();
    }

    @Override
    public String getDeviceName() {
        return rootMotor.getDeviceName();
    }

    @Override
    public String getConnectionInfo() {
        return rootMotor.getConnectionInfo();
    }

    @Override
    public int getVersion() {
        return rootMotor.getVersion();
    }

    @Override
    public void resetDeviceConfigurationForOpMode() {
        rootMotor.resetDeviceConfigurationForOpMode();
    }

    @Override
    public void close() {
        rootMotor.close();
    }
}
