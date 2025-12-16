package org.firstinspires.ftc.teamcode.Systems;

public abstract class PIDAbstract {

    public abstract double step(double error);
    public double step(double goal, double current) {
        return step(goal-current);
    }
}
