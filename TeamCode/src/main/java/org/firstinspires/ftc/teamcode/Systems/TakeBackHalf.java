package org.firstinspires.ftc.teamcode.Systems;

public class TakeBackHalf extends ControllerAbstract {
    public double gain;
    public double integral;
    public TakeBackHalf(double gain) {
        this.gain = gain;
    }
    @Override
    public double step(double error) {
        if (error >= 0) {
            integral /= 2;
        } else {
            integral += error*gain;
        }
        return integral;
    }
}
