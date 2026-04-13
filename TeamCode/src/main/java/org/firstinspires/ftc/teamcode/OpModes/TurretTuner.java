package org.firstinspires.ftc.teamcode.OpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Mechanisms.Scoring.Turret;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Turret Tuner")
public class TurretTuner extends LinearOpMode {
    ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        Turret.initTurret(this);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        int loops = 1;
        while (opModeIsActive()) {
            TelemetryPacket packet = new TelemetryPacket();
            Turret.updateTuner(packet);

            telemetry.addData("Run Time", runtime.toString());
            telemetry.addData("Looptime (MS per Loop)", runtime.milliseconds()/loops);
            loops += 1;
            FtcDashboard.getInstance().sendTelemetryPacket(packet);
            telemetry.update();
        }
    }
}
