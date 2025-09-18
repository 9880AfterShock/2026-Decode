package org.firstinspires.ftc.teamcode.Maths;

import com.acmerobotics.roadrunner.Vector2d;

public class Trajectory {
    public static RadialVector getVelocity(double distance, double height,double gravity) {
        double theta = Math.atan2(height,distance);
        double dividen = Math.tan(theta)*distance-gravity*Math.pow(distance,2);
        double divisor = 2*Math.pow(Math.cos(theta),2);
        double speed = Math.sqrt(dividen/divisor);
        return new RadialVector(theta,speed);
    }
}
