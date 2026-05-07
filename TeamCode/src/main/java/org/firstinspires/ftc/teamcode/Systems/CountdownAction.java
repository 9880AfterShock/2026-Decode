package org.firstinspires.ftc.teamcode.Systems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class CountdownAction {
    public double countdownTimestamp;
    public boolean hasRun;
    public Runnable func;
    public String id;
    public CountdownAction(Runnable func, String id) {
        this.func = func;
        this.hasRun = false;
        this.countdownTimestamp = 0;
        this.id = id;
        RunCountdown.addAction(this,id);
    }

    public void resetCountdown(double secsLeft) {
        RunCountdown.resetCountdown(secsLeft,id);
    }
}
