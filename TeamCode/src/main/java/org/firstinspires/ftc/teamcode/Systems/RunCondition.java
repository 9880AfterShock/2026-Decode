package org.firstinspires.ftc.teamcode.Systems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class RunCondition {
    private static final ArrayList<ConditionAction> list = new ArrayList<>();
    public static void addAction(ConditionAction action) {
        list.add(action);
    }
    public static void update() {
        for (ConditionAction action : (ArrayList<ConditionAction>) list.clone()) {
            if (action.condition.get()) {
                action.func.run();
                list.remove(action);
            }
        }
    }
}
