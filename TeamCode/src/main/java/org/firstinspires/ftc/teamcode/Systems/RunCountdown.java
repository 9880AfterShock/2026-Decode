package org.firstinspires.ftc.teamcode.Systems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.HashMap;
import java.util.Map;

public class RunCountdown {
    private static OpMode opMode;
    private static HashMap<String,CountdownAction> countDowns;

    public static void init(OpMode opMode) {
        RunCountdown.opMode = opMode;
    }

    public static void addAction(CountdownAction action,String id) {
        if (!countDowns.containsKey(id)) {
            countDowns.put(id, action);
        }
    }

    public static CountdownAction getAction(String id) {
        return countDowns.get(id);
    }

    public static void resetCountdown(double secsLeft,String id) {
        CountdownAction countdown = countDowns.get(id);
        if (countdown != null) {
            countdown.countdownTimestamp = opMode.getRuntime() + secsLeft;
            countdown.hasRun = false;
            countDowns.put(id,countdown);
        }
    }

    public static void update() {
        for (CountdownAction countdownAction : countDowns.values()) {
            if (!countdownAction.hasRun && countdownAction.countdownTimestamp <= opMode.getRuntime()) {
                countdownAction.func.run();
                countdownAction.hasRun = true;
            }
        }
    }
}
