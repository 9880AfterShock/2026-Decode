package org.firstinspires.ftc.teamcode.Maths;

public class Trajectory {

    public static LaunchInformation getOptimalVelocity(double x, double y, double wheel_radius, double anglemin, double anglemax, double maxIter) {
        LaunchInformation velmax = getVelocity(x,y,wheel_radius,anglemax);
        LaunchInformation velmin = getVelocity(x,y,wheel_radius,anglemin);
        if (velmin.rpm < velmax.rpm) {
            return getOptimalVelocity(x,y,wheel_radius,anglemin,((anglemin-anglemax)/2)+anglemin,maxIter-1);
        } else {
            return getOptimalVelocity(x,y,wheel_radius,((anglemin-anglemax)/2)+anglemin,anglemax,maxIter-1);
        }
    }
    public static LaunchInformation getVelocity(double x, double y, double wheel_radius, double angle) {
        double gravity = -9.81;
        double vel = Math.sqrt(
                (gravity*Math.pow(x,2))
                / //-----------------
                ((Math.tan(angle)-y) * Math.pow(Math.cos(angle),2))
        );
        double rpm = ((60)
                /
                ((2*Math.PI)*wheel_radius))*vel;
        return new LaunchInformation(angle,vel,rpm);
    }
}
