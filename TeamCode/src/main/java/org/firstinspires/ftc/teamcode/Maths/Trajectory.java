package org.firstinspires.ftc.teamcode.Maths;

public class Trajectory {

    public static LaunchInformation getOptimalVelocity(
            double x, double y, double wheel_radius,
            double anglemin, double anglemax) {

        LaunchInformation best = new LaunchInformation(30,0,Double.POSITIVE_INFINITY);

        for (double i = anglemin; i < anglemax; i += 1) {
            LaunchInformation candidate = getVelocity(x,y, wheel_radius, i);

            if ((candidate.rpm < best.rpm) && !Double.isNaN(candidate.rpm)) {
                best = candidate;
            }
        }
        best.rpm =  12079.12 - 9.939112*best.rpm + 0.003413314*Math.pow(best.rpm,2) - 3.69949e-7*Math.pow(best.rpm,3);
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
        return new LaunchInformation(angle, vel, rpm);
    }
}
