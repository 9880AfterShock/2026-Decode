package org.firstinspires.ftc.teamcode.Systems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.Spindexer;
import org.firstinspires.ftc.teamcode.messages.SpindexerMessage;

import java.util.PriorityQueue;

public class RunLater {
    private static OpMode opMode;
    private static PriorityQueue<DelayedAction> queue = new PriorityQueue<>();
    public static void setup(OpMode opMode) {
        RunLater.opMode =opMode;
        queue = new PriorityQueue<>();
    }

    public static void clearQueue() {
        queue = new PriorityQueue<>();
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
    public static boolean isEmpty() {
        return queue.isEmpty();
    }
    public static int getCount() {return queue.size();}
    public static class Finish implements Action {

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            update();
            return queue.isEmpty();
        }
    }
    public static Action finish() {
        return new Finish();
    }
}
