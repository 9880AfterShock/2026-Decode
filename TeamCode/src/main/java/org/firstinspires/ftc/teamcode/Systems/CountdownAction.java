package org.firstinspires.ftc.teamcode.Systems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class CountdownAction {
    private boolean hasRun = true;
    private double timeStamp = 0;
    private OpMode opMode;
    private Runnable func;


    public CountdownAction(Runnable func) {
        this.func = func;
    }
    public void resetCountdown(double seconds) {
        timeStamp = this.opMode.getRuntime()+seconds;
        hasRun = false;
    }

    public void tryRun() {
        if (!hasRun&&(timeStamp<=this.opMode.getRuntime())) {
            func.run();
            hasRun = true;
        }
    }
}
