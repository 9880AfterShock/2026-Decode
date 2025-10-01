package Mechanisms;

import org.firstinspires.ftc.teamcode.Enums.BallType;
import org.firstinspires.ftc.teamcode.fakes.FakeDcMotorEx;
import org.firstinspires.ftc.teamcode.messages.SpindexerMessage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class Spindexer {
    public org.firstinspires.ftc.teamcode.Mechanisms.Sorting.Spindexer spindexer;
    public FakeDcMotorEx fakemotor;
    public double ticks = 300;
    @Before
    public void setup() {
        fakemotor = new FakeDcMotorEx();
        spindexer = new org.firstinspires.ftc.teamcode.Mechanisms.Sorting.Spindexer(fakemotor, ticks);
    }

    @Test
    public void rotation_test() {
        // is in starting position
        Assert.assertEquals(0, fakemotor.getTargetPosition());
        spindexer.queueMessage(SpindexerMessage.LEFT);
        spindexer.update();
        Assert.assertEquals(100, fakemotor.getTargetPosition());
        spindexer.queueMessage(SpindexerMessage.LEFT);
        spindexer.update();
        Assert.assertEquals(200, fakemotor.getTargetPosition());
        spindexer.queueMessage(SpindexerMessage.LEFT);
        spindexer.update();
        Assert.assertEquals(300, fakemotor.getTargetPosition());
        spindexer.queueMessage(SpindexerMessage.LEFT);
        spindexer.update();
        Assert.assertEquals(400, fakemotor.getTargetPosition());
        spindexer.queueMessage(SpindexerMessage.RIGHT);
        spindexer.update();
        Assert.assertEquals(300, fakemotor.getTargetPosition());
        spindexer.queueMessage(SpindexerMessage.RIGHT);
        spindexer.update();
        Assert.assertEquals(200, fakemotor.getTargetPosition());
        spindexer.queueMessage(SpindexerMessage.RIGHT);
        spindexer.update();
        Assert.assertEquals(100, fakemotor.getTargetPosition());
        spindexer.queueMessage(SpindexerMessage.RIGHT);
        spindexer.update();
        Assert.assertEquals(0, fakemotor.getTargetPosition());
        spindexer.queueMessage(SpindexerMessage.RIGHT);
        spindexer.update();
        Assert.assertEquals(-100, fakemotor.getTargetPosition());
        spindexer.queueMessage(SpindexerMessage.RIGHT);
        spindexer.update();
        Assert.assertEquals(-200, fakemotor.getTargetPosition());
        spindexer.queueMessage(SpindexerMessage.RIGHT);
        spindexer.update();
        Assert.assertEquals(-300, fakemotor.getTargetPosition());
        spindexer.queueMessage(SpindexerMessage.RIGHT);
        spindexer.update();
        Assert.assertEquals(-400, fakemotor.getTargetPosition());
        spindexer.queueMessage(SpindexerMessage.LEFT);
        spindexer.update();
        Assert.assertEquals(-300, fakemotor.getTargetPosition());
        spindexer.queueMessage(SpindexerMessage.LEFT);
        spindexer.update();
        Assert.assertEquals(-200, fakemotor.getTargetPosition());
        spindexer.queueMessage(SpindexerMessage.LEFT);
        spindexer.update();
        Assert.assertEquals(-100, fakemotor.getTargetPosition());
        spindexer.queueMessage(SpindexerMessage.LEFT);
        spindexer.update();
        Assert.assertEquals(0, fakemotor.getTargetPosition());
    }

    @Test
    public void intake_shoot_test() {
        Assert.assertEquals(List.of(BallType.NONE, BallType.NONE, BallType.NONE), spindexer.balls);
        Assert.assertEquals(0, spindexer.index);
        spindexer.queueMessage(SpindexerMessage.INGREEN);
        spindexer.update();
        Assert.assertEquals(List.of(BallType.GREEN, BallType.NONE, BallType.NONE), spindexer.balls);
        spindexer.queueMessage(SpindexerMessage.EJECT);
        spindexer.update();
        Assert.assertEquals(List.of(BallType.NONE, BallType.NONE, BallType.NONE), spindexer.balls);
        spindexer.queueMessage(SpindexerMessage.LEFT);
        spindexer.update();
        Assert.assertEquals(1, spindexer.index);
        spindexer.queueMessage(SpindexerMessage.INPURPLE);
        spindexer.update();
        Assert.assertEquals(List.of(BallType.NONE, BallType.PURPLE, BallType.NONE), spindexer.balls);
        spindexer.queueMessage(SpindexerMessage.EJECT);
        spindexer.update();
        Assert.assertEquals(List.of(BallType.NONE, BallType.NONE, BallType.NONE), spindexer.balls);
        spindexer.queueMessage(SpindexerMessage.LEFT);
        spindexer.update();
        Assert.assertEquals(2, spindexer.index);
        spindexer.queueMessage(SpindexerMessage.INUNKOWN);
        spindexer.update();
        Assert.assertEquals(List.of(BallType.NONE, BallType.NONE, BallType.UNKOWN), spindexer.balls);
        spindexer.queueMessage(SpindexerMessage.EJECT);
        spindexer.update();
        Assert.assertEquals(List.of(BallType.NONE, BallType.NONE, BallType.NONE), spindexer.balls);
        spindexer.queueMessage(SpindexerMessage.LEFT);
        spindexer.update();
        Assert.assertEquals(0, spindexer.index);
    }

    @Test
    public void goto_ball_type_test() {
        Assert.assertEquals(List.of(BallType.NONE, BallType.NONE, BallType.NONE), spindexer.balls);
        Assert.assertEquals(0, spindexer.index);
        spindexer.queueMessage(SpindexerMessage.INGREEN);
        spindexer.update();
        spindexer.gotoBallType(BallType.GREEN);
        spindexer.update();
        Assert.assertEquals(0, spindexer.index);
        spindexer.queueMessage(SpindexerMessage.LEFT);
        spindexer.update();
        Assert.assertEquals(1, spindexer.index);
        spindexer.gotoBallType(BallType.GREEN);
        spindexer.update();
        Assert.assertEquals(0, spindexer.index);
        spindexer.queueMessage(SpindexerMessage.RIGHT);
        spindexer.update();
        Assert.assertEquals(2, spindexer.index);
        spindexer.gotoBallType(BallType.GREEN);
        spindexer.update();
        Assert.assertEquals(0, spindexer.index);
    }
}
