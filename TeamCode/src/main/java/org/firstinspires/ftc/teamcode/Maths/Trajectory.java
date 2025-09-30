package org.firstinspires.ftc.teamcode.Maths;

public class Trajectory {
    public static LaunchInformation getVelocity(double x, double y, double wheel_radius, double angle) {
        double gravity = 9.81;
        double vel = Math.sqrt(
                gravity*Math.pow(x,2)
                / //-----------------
                ((Math.tan(angle)*x)-y)*2*Math.pow(Math.cos(angle),2)

        );
        double rpm = ((60*vel)/(2*Math.PI*wheel_radius));
        return new LaunchInformation(angle,vel,rpm);
    }
}
