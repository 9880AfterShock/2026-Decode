package org.firstinspires.ftc.teamcode.Aiming;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.ArrayList;
import java.util.List;

public class Alignment {
    private static OpMode opmode;
    private static FtcDashboard dash = FtcDashboard.getInstance();
    private static List<Action> runningActions = new ArrayList<>();

    public static void initAlignment(OpMode opmode) { // init motors
        Alignment.opmode = opmode;
    }

    public static void updateAlignment(){
        TelemetryPacket packet = new TelemetryPacket();

        // updated based on gamepads

        // update running actions
        List<Action> newActions = new ArrayList<>();
        for (Action action : runningActions) {
            action.preview(packet.fieldOverlay());
            if (action.run(packet)) {
                newActions.add(action);
            }
        }
        runningActions = newActions;

        dash.sendTelemetryPacket(packet);




        if (opmode.gamepad1.a) {
            runningActions.add(new SequentialAction(
                    new SleepAction(0.5)//,
                    //new InstantAction(() -> servo.setPosition(0.5))
            ));
        }
    }

    public static void rotate(float Radians){

    }
}
