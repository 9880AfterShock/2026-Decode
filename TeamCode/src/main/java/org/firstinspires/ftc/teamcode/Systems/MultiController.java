package org.firstinspires.ftc.teamcode.Systems;

import androidx.core.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MultiController extends ControllerAbstract {

    private final List<Pair<Double, ControllerAbstract>> pids;
    private final ControllerAbstract fallback;
    public MultiController(ControllerAbstract fallbackPID) {
        fallback = fallbackPID;
        pids = new ArrayList<>();
    }

    public MultiController addPID(ControllerAbstract pid, double maxErr) {
        pids.add(new Pair<>(maxErr, pid));
        pids.sort(Comparator.comparingDouble((Pair<Double, ControllerAbstract> a) -> a.first).reversed());
        return this;
    }

    @Override
    public double step(double error) {
        ControllerAbstract currentPID = fallback;
        for (Pair<Double, ControllerAbstract> pid : pids) {
            if (error <= pid.first) {
                currentPID = pid.second;
            }
        }
        return currentPID.step(error);
    }
}
