package org.firstinspires.ftc.teamcode.Systems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.BallRamp;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.Spindexer;
import org.firstinspires.ftc.teamcode.messages.BallRampMessage;
import org.firstinspires.ftc.teamcode.messages.SpindexerMessage;

public class ActionManager {

    private final Spindexer spindexer;
    private final BallRamp ballRamp;

    public ActionManager(Spindexer spindexer, BallRamp ballRamp) {
        this.spindexer = spindexer;
        this.ballRamp = ballRamp;
    }
    public Action cycleRamp() {
        spindexer.queueMessage(SpindexerMessage.LINEUP);
        RunLater.addAction(new DelayedAction(() -> ballRamp.queueMessage(BallRampMessage.CYCLE), 0.2));
        return new Action() {

            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                spindexer.update();
                RunLater.update();
                return RunLater.isEmpty();
            }
        };
    }

    public Action launch() {

        return new Action() {
            @Override
            public boolean run(@NonNull TelemetryPacket telemetryPacket) {
                return false;
            }
        };
    }
}
