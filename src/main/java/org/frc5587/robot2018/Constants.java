package org.frc5587.robot2018;

public class Constants {

    public static final class Drive {
        // set to zero to skip waiting for confirmation, set to nonzero to wait and report to DS if action fails
        public static final int kTimeoutMs = 10;

        public static final double kMaxVelocity = 0.0;

        public static final double kVCompSaturation = 12.0;

        //PIDF Constants
        public static final double[] leftPIDs = {
            0.0,	//kP
            0.0,	//kI
            0.0,	//kD
            0.0 	//kF
        };
        public static final double[] rightPIDs = {
            0.0,	//kP
            0.0,	//kI
            0.0,	//kD
            0.0		//kF
        };
    }
}
