import org.firstinspires.ftc.teamcode.Systems.ConditionAction;
import org.firstinspires.ftc.teamcode.Systems.RunCondition;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

public class RunStuffTest {
    public static boolean condition = false;
    @Test
    public void runCondition() {
        AtomicReference<Double> value = new AtomicReference<>((double) 20);
        ConditionAction action = new ConditionAction(() -> {value.set(30.0);},() -> condition);
        RunCondition.addAction(action);
        RunCondition.update();
        Assert.assertEquals(20.0, value.get(), 0.0);
        condition = true;
        RunCondition.update();
        Assert.assertEquals(30.0, value.get(), 0.0);
    }
}
