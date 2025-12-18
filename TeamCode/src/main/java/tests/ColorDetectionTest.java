import org.firstinspires.ftc.teamcode.Color;
import org.firstinspires.ftc.teamcode.Enums.BallType;
import org.firstinspires.ftc.teamcode.Enums.ColorType;
import org.firstinspires.ftc.teamcode.Mechanisms.Sorting.ColorClassifier;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

public class
ColorDetectionTest {
    public ColorClassifier<BallType> classifier;
    @Before
    public void setup() {
        classifier = new ColorClassifier<>(BallType.NONE,0.3);
        classifier.addColor((new Color((double) 44 /255, (double) 178 /255, (double) 51 /255, ColorType.RGB)).asHSV(), BallType.GREEN);
        classifier.addColor((new Color((double) 138 /255, (double) 44 /255, (double) 178 /255,ColorType.RGB)).asHSV(), BallType.PURPLE);
    }

    @Test
    public void exactColorTest() {
        Assert.assertEquals(BallType.GREEN,classifier.classify((new Color((double) 44 /255, (double) 178 /255, (double) 51 /255, ColorType.RGB)).asHSV()));
        Assert.assertEquals(BallType.PURPLE,classifier.classify((new Color((double) 138 /255, (double) 44 /255, (double) 178 /255, ColorType.RGB)).asHSV()));
    }
    @Test
    public void roughColorTest() {
        double change = 0.3;
        Random random = new Random();
        for (int i = 1; i < 500; i++) {
            Assert.assertEquals(BallType.GREEN, classifier.classify((new Color(((double) 44 / 255) + random.nextDouble() * change, ((double) 178 / 255) + random.nextDouble() * change, ((double) 51 / 255) + random.nextDouble() * change, ColorType.RGB)).asHSV()));
            Assert.assertEquals(BallType.PURPLE, classifier.classify((new Color(((double) 138 / 255) + random.nextDouble() * change, ((double) 44 / 255) + random.nextDouble() * change, ((double) 178 / 255) + random.nextDouble() * change, ColorType.RGB)).asHSV()));
        }
    }
}
