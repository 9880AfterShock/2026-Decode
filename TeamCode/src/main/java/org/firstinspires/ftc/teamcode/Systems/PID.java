package org.firstinspires.ftc.teamcode.Systems;

public class PID {
    public double maxi;
    public double p;
    public double i;
    public double iaccum;
    public double d;
    public double lasterr;
    public PID(double p, double i, double d) {
        this.p = p;
        this.i = i;
        this.d = d;
        this.lasterr = 0;
        this.iaccum = 0;
        this.maxi = 0;
    }
    public PID(double p, double i, double d, double maxi) {
        this.p = p;
        this.i = i;
        this.d = d;
        this.lasterr = 0;
        this.iaccum = 0;
        this.maxi = maxi;
    }

    public double step(double error) {
        double out =  (error*p)+((this.lasterr-error)*d)+(this.iaccum*i);
        this.lasterr = error;
        this.iaccum += error;
        if (this.maxi != 0) {
            this.iaccum = Math.min(Math.max(this.iaccum,-this.maxi),this.maxi);
        }
        return out;
    }

    public double step(double goal, double current) {
        return step(goal-current);
    }
}
