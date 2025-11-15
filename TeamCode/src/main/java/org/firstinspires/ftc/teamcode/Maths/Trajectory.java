package org.firstinspires.ftc.teamcode.Maths;

public class Trajectory {

    public static LaunchInformation getOptimalVelocity(
            double x, double y, double wheel_radius,
            double anglemin, double anglemax) {

        LaunchInformation best = new LaunchInformation(30,0,Double.POSITIVE_INFINITY, 0.0);

        for (double i = anglemin; i < anglemax; i += 0.01) {
            LaunchInformation candidate = getVelocity(x,y, wheel_radius, i);

            if ((candidate.rpm < best.rpm) && (!Double.isNaN(candidate.rpm) || Double.isInfinite(best.rpm))) {
                best = candidate;
            }
        }
        return best;
    }

    public static LaunchInformation getVelocity(double x, double y, double wheel_radius, double angle) {
        double gravity = -9.81;
        double vel = Math.sqrt(
                (gravity * Math.pow(x, 2))
                        / //-----------------
                        ((Math.tan(angle) - y) * Math.pow(Math.cos(angle), 2))
        );
        double rpm = ((60)
                /
                ((2 * Math.PI) * wheel_radius)) * vel;
        double adjustment = Math.max((2.6+(3*(-x/(x+1))))+0.16,0); //wierd ass equation
        return new LaunchInformation(angle, vel, rpm*adjustment, adjustment);
    }
}
