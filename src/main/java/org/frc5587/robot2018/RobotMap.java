/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc5587.robot2018;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

	public static class Grabber {
		public static final int[] EXPANDER_SOLENOID = { 1, 0 };
		public static int leftMotor = 1;
		public static int rightMotor = 2;
		// If you are using multiple modules, make sure to define both the port
		// number and the module. For example you with a rangefinder:
		// public static int rangefinderPort = 1;OID = {0, 1};
		public static final int LEFT = 6, RIGHT = 5;
		public static final int RECEIVER = 2;
	}

	public static class Drive {
		public static int leftMaster = 1;
		public static int rightMaster = 2;
		public static int leftSlave = 0;
		public static int rightSlave = 1;
	}

	public static class Camera {
		public static final int DRIVER_CAMERA = 0;
		public static final int GRABBER_CAMERA = 1;
	}

	public static class Elevator {
		public static final int ELEVATOR_TALON = 5;
		public static final int ELEVATOR_VICTORSPX = 6;
	}
	
	public static class Climber {
		public static int climberTalon = 9;
	}
}
