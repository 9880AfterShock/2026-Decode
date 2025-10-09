package org.firstinspires.ftc.teamcode.Systems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.PriorityQueue;

public class RunLater {
    private static OpMode opMode;
    private static final PriorityQueue<DelayedAction> queue = new PriorityQueue<>();
    public static void setup(OpMode opMode) {
        RunLater.opMode =opMode;
    }

    public static double getRuntime() {
        return RunLater.opMode.getRuntime();
    }

    public static void addAction(DelayedAction action) {
        queue.add(action);
    }
    public static void update() {
        opMode.telemetry.addData("Time",opMode.getRuntime());
        while (!queue.isEmpty() && queue.peek().runOn <= opMode.getRuntime()) {
            DelayedAction action = queue.remove();
            action.func.run();
        }
    }
}
