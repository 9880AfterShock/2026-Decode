package org.firstinspires.ftc.teamcode.Systems;

import java.util.function.Supplier;

public class ConditionAction {
    public Supplier<Boolean> condition;
    public Runnable func;
    public ConditionAction(Runnable func, Supplier<Boolean> condition) {
        this.func = func;
        this.condition = condition;
    }
}
