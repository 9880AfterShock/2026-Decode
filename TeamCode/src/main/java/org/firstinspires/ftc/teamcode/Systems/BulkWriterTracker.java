package org.firstinspires.ftc.teamcode.Systems;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.WrapperClasses.BulkWriter;

import java.util.ArrayList;

public class BulkWriterTracker {

    private static OpMode opMode;
    private static ArrayList<BulkWriter> bulkWriters = new ArrayList<> ();

    public static void init(OpMode opMode) {
        BulkWriterTracker.opMode = opMode;
        bulkWriters = new ArrayList<> ();
    }

    public static void update() {
        for (BulkWriter bulkWriter : bulkWriters) {
            bulkWriter.bulkWrite();
        }
        opMode.telemetry.addData("num of bulk writers",bulkWriters.size());
    }

    public static void addWriter(BulkWriter writer) {
        bulkWriters.add(writer);
    }
}
