package org.firstinspires.ftc.teamcode.Systems;

import androidx.core.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MultiPID extends PIDAbstract {

    private final List<Pair<Double,PIDAbstract>> pids;
    private final PIDAbstract fallback;
    public MultiPID(PIDAbstract fallbackPID) {
        fallback = fallbackPID;
        pids = new ArrayList<>();
    }

    public MultiPID addPID(PIDAbstract pid, double maxErr) {
        pids.add(new Pair<>(maxErr, pid));
        pids.sort(Comparator.comparingDouble((Pair<Double,PIDAbstract> a) -> a.first).reversed());
        return this;
    }

    @Override
    public double step(double error) {
        PIDAbstract currentPID = fallback;
        for (Pair<Double, PIDAbstract> pid : pids) {
            if (error <= pid.first) {
                currentPID = pid.second;
            }
        }
        return currentPID.step(error);
    }
}
