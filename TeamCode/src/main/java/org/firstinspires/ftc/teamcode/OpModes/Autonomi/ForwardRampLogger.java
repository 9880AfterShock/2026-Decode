//package org.firstinspires.ftc.teamcode.OpModes.Autonomi;
//
//import com.acmerobotics.roadrunner.ftc.DriveView;
//import com.acmerobotics.roadrunner.ftc.DriveViewFactory;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import java.util.ArrayList;
//import java.util.List;
//import static java.lang.Math.min;
//
//public class ForwardRampLogger extends LinearOpMode {
//    public static double POWER_PER_SEC = 0.1;
//    public static double POWER_MAX = 0.9;
//
//    private final DriveViewFactory dvf;
//
//    public ForwardRampLogger(DriveViewFactory dvf) {
//        this.dvf = dvf;
//    }
//
//    public double power(double seconds) {
//        return min(POWER_PER_SEC * seconds, POWER_MAX);
//    }
//
//    @Override
//    public void runOpMode() {
//        DriveView view = dvf.make(hardwareMap);
//        if (view.perpEncs.isEmpty()) {
//            throw new IllegalStateException("Only run this op mode if you're using dead wheels.");
//        }
//
//        class Data {
//            String type = view.type;
//            List<MutableSignal> powers = new ArrayList<>();
//            MutableSignal voltages = new MutableSignal();
//            List<MutableSignal> forwardEncPositions = new ArrayList<>();
//            List<MutableSignal> forwardEncVels = new ArrayList<>();
//            List<Boolean> forwardEncFixVels = new ArrayList<>();
//
//            Data() {
//                for (int i = 0; i < view.motors.size(); i++) {
//                    powers.add(new MutableSignal());
//                }
//                for (int i = 0; i < view.forwardEncs.size(); i++) {
//                    forwardEncPositions.add(new MutableSignal());
//                    forwardEncVels.add(new MutableSignal());
//                    forwardEncFixVels.add(shouldFixVels(view, view.forwardEncs.get(i)));
//                }
//            }
//        }
//
//        Data data = new Data();
//
//        waitForStart();
//
//        MidpointTimer t = new MidpointTimer();
//        while (opModeIsActive()) {
//            for (int i = 0; i < view.motors.size(); i++) {
//                double power = power(t.seconds());
//                view.motors.get(i).power = power;
//
//                MutableSignal s = data.powers.get(i);
//                s.times.add(t.addSplit());
//                s.values.add(power);
//            }
//
//            data.voltages.values.add(view.voltageSensor.voltage);
//            data.voltages.times.add(t.addSplit());
//
//            List<Double> encTimes = new ArrayList<>();
//            for (EncoderGroup group : view.encoderGroups) {
//                group.bulkRead();
//                encTimes.add(t.addSplit());
//            }
//
//            for (int i = 0; i < view.forwardEncs.size(); i++) {
//                recordUnwrappedEncoderData(
//                        view.encoderGroups,
//                        encTimes,
//                        view.forwardEncs.get(i),
//                        data.forwardEncPositions.get(i),
//                        data.forwardEncVels.get(i)
//                );
//            }
//        }
//
//        for (Motor m : view.motors) {
//            m.power = 0.0;
//        }
//
//        TuningFiles.save(TuningFiles.FileType.FORWARD_RAMP, data);
//    }
//}