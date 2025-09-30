package org.firstinspires.ftc.teamcode.Systems;

import android.os.Build;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

public class DelayedAction implements Comparable<DelayedAction> {
    public double runOn;
    public Runnable func;
    public DelayedAction(Runnable func, Duration delay) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            this.runOn = RunLater.getRuntime()+((double) delay.toNanos())/1000000000;
        }
        this.func = func;
    }

    public DelayedAction(Runnable func, double delaySecs) {
        this.runOn = RunLater.getRuntime()+delaySecs;
        this.func = func;
    }

    @Override
    public int compareTo(DelayedAction delayedAction) {
        return Double.compare(this.runOn,delayedAction.runOn);
    }
}
