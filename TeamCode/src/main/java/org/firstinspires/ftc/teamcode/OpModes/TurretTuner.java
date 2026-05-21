package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Turret;
import org.firstinspires.ftc.teamcode.Systems.BulkWriterTracker;

import java.util.List;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Turret Tuner")
public class TurretTuner extends LinearOpMode {
    ElapsedTime runtime = new ElapsedTime();
    public static List<LynxModule> allHubs;

    @Override
    public void runOpMode() {
        Turret.initTurret(this, false);
        BulkWriterTracker.init(this);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        allHubs = hardwareMap.getAll(LynxModule.class);

        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }

        waitForStart();
        runtime.reset();

        int loops = 1;
        while (opModeIsActive()) {
            for (LynxModule hub : allHubs) {
                hub.clearBulkCache();
            }
            TelemetryPacket packet = new TelemetryPacket();
            Turret.updateTuner(packet);

            telemetry.addData("Run Time", runtime.toString());
            telemetry.addData("Looptime (MS per Loop)", runtime.milliseconds()/loops);
            loops += 1;
            FtcDashboard.getInstance().sendTelemetryPacket(packet);
            telemetry.update();

            BulkWriterTracker.update();
        }
    }
}
