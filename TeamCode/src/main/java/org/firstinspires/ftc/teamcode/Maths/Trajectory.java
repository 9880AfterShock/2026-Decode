package org.firstinspires.ftc.teamcode.Maths;

public class Trajectory {
    public static LaunchInformation getVelocity(double distance, double height, double wheel_radius, double angle) {
        double gravity = 9.81;
        double dividen = gravity*Math.pow(distance,2);
        double divisor = (Math.tan(angle)*distance-height)*2*Math.pow(Math.cos(angle),2);
        double magnitude = Math.sqrt(dividen/divisor);
        double rpm = (6/(2*Math.PI*wheel_radius))*Math.sqrt(dividen/((Math.tan(angle)-height)* Math.pow(Math.cos(angle),2)));
        return new LaunchInformation(angle,magnitude,rpm);
    }
}
