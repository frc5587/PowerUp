package org.frc5587.robot2018;

public class Constants {

    public class Elevator {
        // Which PID slot to pull gains from. Starting 2018, you can choose from 0,1,2 or 3.
        public static final int kSlotIdx = 0;
        // Talon SRX/Victor SPX will supported multiple (cascaded) PID loops. For now we just want the primary one
        public static final int kPIDLoopIdx = 0;
        // set to zero to skip waiting for confirmation, set to nonzero to wait and report to DS if action fails
        public static final int kTimeoutMs = 10;

        // The tolerance for the target position (see Elevator.isDone())
        public static final double kDeadband = 10;
        //PID Constants
        public static final double kF = 2.495,
            kP = 0.0,
            kI = 0.0,
            kD = 0.0;
        //Safety limits for testing
        public static final double minPercentOut = 0,
            maxPercentOut = .5;
        //System Constraints
        public static final int maxVelocity = 300, //measured in native units/100ms
            maxAcceleration = 600; //measured in native units/100ms/sec
        //Unit Conversion
        public static final int ticksPerInch = 874;

        //Hall effect sensor height in native units MEASURED FROM BOTTOM OF INTAKE
        public static final int hallHeight = 25326;
    }
}
