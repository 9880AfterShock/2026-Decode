package org.firstinspires.ftc.teamcode.Systems;

import java.util.ArrayList;

public class RunCountdown {
    private static final ArrayList<CountdownAction> actions = new ArrayList<>();

    public static void addAction(CountdownAction action) {
        actions.add(action);
    }

    public static void update() {
        for (CountdownAction action : actions) {
            action.tryRun();
        }
    }
}
