package Mechanisms;

import org.firstinspires.ftc.teamcode.Enums.BallType;
import org.firstinspires.ftc.teamcode.Enums.Motif;
import org.firstinspires.ftc.teamcode.fakes.FakeDcMotorEx;
import org.firstinspires.ftc.teamcode.messages.SpindexerMessage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SpindexerTests {
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
        Assert.assertEquals(0, spindexer.index);
        spindexer.queueMessage(SpindexerMessage.RIGHT);
        spindexer.update();
        Assert.assertEquals(100, fakemotor.getTargetPosition());
        Assert.assertEquals(1, spindexer.index);
        spindexer.queueMessage(SpindexerMessage.RIGHT);
        spindexer.update();
        Assert.assertEquals(200, fakemotor.getTargetPosition());
        Assert.assertEquals(2, spindexer.index);
        spindexer.queueMessage(SpindexerMessage.RIGHT);
        spindexer.update();
        Assert.assertEquals(300, fakemotor.getTargetPosition());
        Assert.assertEquals(0, spindexer.index);
        spindexer.queueMessage(SpindexerMessage.RIGHT);
        spindexer.update();
        Assert.assertEquals(400, fakemotor.getTargetPosition());
        Assert.assertEquals(1, spindexer.index);
        spindexer.queueMessage(SpindexerMessage.LEFT);
        spindexer.update();
        Assert.assertEquals(300, fakemotor.getTargetPosition());
        Assert.assertEquals(0, spindexer.index);
        spindexer.queueMessage(SpindexerMessage.LEFT);
        spindexer.update();
        Assert.assertEquals(200, fakemotor.getTargetPosition());
        Assert.assertEquals(2, spindexer.index);
        spindexer.queueMessage(SpindexerMessage.LEFT);
        spindexer.update();
        Assert.assertEquals(100, fakemotor.getTargetPosition());
        Assert.assertEquals(1, spindexer.index);
        spindexer.queueMessage(SpindexerMessage.LEFT);
        spindexer.update();
        Assert.assertEquals(0, fakemotor.getTargetPosition());
        Assert.assertEquals(0, spindexer.index);
        spindexer.queueMessage(SpindexerMessage.LEFT);
        spindexer.update();
        Assert.assertEquals(-100, fakemotor.getTargetPosition());
        Assert.assertEquals(2, spindexer.index);
        spindexer.queueMessage(SpindexerMessage.LEFT);
        spindexer.update();
        Assert.assertEquals(-200, fakemotor.getTargetPosition());
        Assert.assertEquals(1, spindexer.index);
        spindexer.queueMessage(SpindexerMessage.LEFT);
        spindexer.update();
        Assert.assertEquals(-300, fakemotor.getTargetPosition());
        Assert.assertEquals(0, spindexer.index);
        spindexer.queueMessage(SpindexerMessage.LEFT);
        spindexer.update();
        Assert.assertEquals(-400, fakemotor.getTargetPosition());
        Assert.assertEquals(2, spindexer.index);
        spindexer.queueMessage(SpindexerMessage.RIGHT);
        spindexer.update();
        Assert.assertEquals(-300, fakemotor.getTargetPosition());
        Assert.assertEquals(0, spindexer.index);
        spindexer.queueMessage(SpindexerMessage.RIGHT);
        spindexer.update();
        Assert.assertEquals(-200, fakemotor.getTargetPosition());
        Assert.assertEquals(1, spindexer.index);
        spindexer.queueMessage(SpindexerMessage.RIGHT);
        spindexer.update();
        Assert.assertEquals(-100, fakemotor.getTargetPosition());
        Assert.assertEquals(2, spindexer.index);
        spindexer.queueMessage(SpindexerMessage.RIGHT);
        spindexer.update();
        Assert.assertEquals(0, fakemotor.getTargetPosition());
        Assert.assertEquals(0, spindexer.index);
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
        spindexer.queueMessage(SpindexerMessage.RIGHT);
        spindexer.update();
        Assert.assertEquals(1, spindexer.index);
        spindexer.queueMessage(SpindexerMessage.INPURPLE);
        spindexer.update();
        Assert.assertEquals(List.of(BallType.NONE, BallType.PURPLE, BallType.NONE), spindexer.balls);
        spindexer.queueMessage(SpindexerMessage.EJECT);
        spindexer.update();
        Assert.assertEquals(List.of(BallType.NONE, BallType.NONE, BallType.NONE), spindexer.balls);
        spindexer.queueMessage(SpindexerMessage.RIGHT);
        spindexer.update();
        Assert.assertEquals(2, spindexer.index);
        spindexer.queueMessage(SpindexerMessage.INUNKOWN);
        spindexer.update();
        Assert.assertEquals(List.of(BallType.NONE, BallType.NONE, BallType.UNKOWN), spindexer.balls);
        spindexer.queueMessage(SpindexerMessage.EJECT);
        spindexer.update();
        Assert.assertEquals(List.of(BallType.NONE, BallType.NONE, BallType.NONE), spindexer.balls);
        spindexer.queueMessage(SpindexerMessage.RIGHT);
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
        spindexer.queueMessage(SpindexerMessage.RIGHT);
        spindexer.update();
        Assert.assertEquals(1, spindexer.index);
        spindexer.gotoBallType(BallType.GREEN);
        spindexer.update();
        Assert.assertEquals(0, spindexer.index);
        spindexer.queueMessage(SpindexerMessage.LEFT);
        spindexer.update();
        Assert.assertEquals(2, spindexer.index);
        spindexer.gotoBallType(BallType.GREEN);
        spindexer.update();
        Assert.assertEquals(0, spindexer.index);
    }

    @Test
    public void check_motif_test() {
        fakemotor = new FakeDcMotorEx();
        spindexer = new org.firstinspires.ftc.teamcode.Mechanisms.Sorting.Spindexer(fakemotor, ticks, Arrays.asList(BallType.GREEN,BallType.PURPLE,BallType.PURPLE));
        Assert.assertTrue(spindexer.checkMotif(0, Motif.GPP));
        Assert.assertFalse(spindexer.checkMotif(0, Motif.PGP));
        Assert.assertFalse(spindexer.checkMotif(0, Motif.PPG));
        Assert.assertTrue(spindexer.checkMotif(1, Motif.PPG));
        Assert.assertFalse(spindexer.checkMotif(1, Motif.GPP));
        Assert.assertFalse(spindexer.checkMotif(1, Motif.PGP));
        Assert.assertTrue(spindexer.checkMotif(2, Motif.PGP));
        Assert.assertFalse(spindexer.checkMotif(2, Motif.PPG));
        Assert.assertFalse(spindexer.checkMotif(2, Motif.GPP));
        Assert.assertTrue(spindexer.checkMotif(-1, Motif.PGP));
        Assert.assertFalse(spindexer.checkMotif(-1, Motif.PPG));
        Assert.assertFalse(spindexer.checkMotif(-1, Motif.GPP));
    }

    @Test
    public void check_goto_motif_test() {
        fakemotor = new FakeDcMotorEx();
        spindexer = new org.firstinspires.ftc.teamcode.Mechanisms.Sorting.Spindexer(fakemotor, ticks, Arrays.asList(BallType.GREEN,BallType.PURPLE,BallType.PURPLE));
        spindexer.gotoMotif(Motif.GPP);
        spindexer.update();
        Assert.assertEquals(0, spindexer.index);
        spindexer.gotoMotif(Motif.PPG);
        Assert.assertEquals(1, spindexer.index);
        spindexer.gotoMotif(Motif.PGP);
        Assert.assertEquals(2, spindexer.index);
    }
}
