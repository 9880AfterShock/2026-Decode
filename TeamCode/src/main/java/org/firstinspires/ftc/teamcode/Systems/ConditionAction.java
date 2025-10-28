package org.firstinspires.ftc.teamcode.Systems;

import java.util.function.Supplier;

public class ConditionAction {
    public Supplier<Boolean> condition;
    public Runnable func;
    public boolean repeat;
    public ConditionAction(Runnable func, Supplier<Boolean> condition) {
        this.repeat = false;
        this.func = func;
        this.condition = condition;
    }

    public ConditionAction(Runnable func, Supplier<Boolean> condition, boolean repeat) {
        this.repeat = repeat;
        this.func = func;
        this.condition = condition;
    }
}
