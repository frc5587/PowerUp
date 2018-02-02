package org.frc5587.robot2018;

public class Constants {

    public class ElevatorTalon {
        // Which PID slot to pull gains from. Starting 2018, you can choose from 0,1,2 or 3.
        public static final int kSlotIdx = 0;
        // Talon SRX/Victor SPX will supported multiple (cascaded) PID loops. For now we just want the primary one
        public static final int kPIDLoopIdx = 0;
        // set to zero to skip waiting for confirmation, set to nonzero to wait and report to DS if action fails
        public static final int kTimeoutMs = 10;

        // The tolerance for the target position (see Elevator.isDone())
        public static final double kDeadband = 10;

        public static final double kF = 2.495,
                kP = 0.0,
                kI = 0.0,
                kD = 0.0;
        public static final int minPercentOut = 0,
                maxPercentOut = 1,
                maxVelocity = 300,
                maxAcceleration = 600;
    }
}
