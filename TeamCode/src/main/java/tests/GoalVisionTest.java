import org.firstinspires.ftc.teamcode.Aiming.GoalVision;
import org.junit.Test;
import static org.junit.Assert.*;

public class GoalVisionTest {

    private static final double EPSILON = 1e-6;

    @Test
    public void testGetTrueDistance() {
        // Example slant distance from ftcPose.range
        double range = 116.35; // inches (or cm — unit doesn't matter)

        // Expected ground distance using correct math
        double expected = range * Math.cos(Math.toRadians(GoalVision.webcamAngle));

        // ACTUAL (your function under test)
        double result = GoalVision.getTrueDistance(range);

        // Comparison
        assertEquals(expected, result, EPSILON);
    }
}
