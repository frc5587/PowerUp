package org.frc5587.robot2018;

public class Constants {

    // Which PID slot to pull gains from. Starting 2018, you can choose from 0,1,2 or 3.
    public static final int kSlotIdx = 0;
    // Talon SRX/Victor SPX will supported multiple (cascaded) PID loops. For now we just want the primary one
    public static final int kPIDLoopIdx = 0;
    // set to zero to skip waiting for confirmation, set to nonzero to wait and report to DS if action fails
    public static final int kTimeoutMs = 10;
    public static final double kDeadband = 10;

    // Elevator
    public static final double elevatorkF = 2.495,
            elevatorkP = 0.0,
            elevatorkI = 0.0,
            elevatorkD = 0.0;
    public static final int elevatorMinPercentOut = 0,
            elevatorMaxPercentOut = 1,
            elevatorMaxVelocity = 300,
            elevatorMaxAcceleration = 600;
}
