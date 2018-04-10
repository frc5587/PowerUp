/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc5587.robot2018.commands;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;
import org.frc5587.robot2018.OI;
import org.frc5587.robot2018.Robot;
import org.frc5587.robot2018.subsystems.Drive;
import org.frc5587.lib.DeadbandXboxController;

/**
 * An example command.  You can replace me with your own command.
 */
public class ArcadeDrive extends Command {
	private static final double KID_THROTTLE_PERCENT = 0.3;
	private static final double KID_CURVE_PERCENT = 0.5;
	Drive kDrive;
	DeadbandXboxController xb;
	boolean kidControlOn;

	public ArcadeDrive() {
		// Use requires() here to declare subsystem dependencies
		requires(Robot.kDrive);
		this.kDrive = Robot.kDrive;
		this.xb = OI.xb;
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		kDrive.enableBrakeMode(false);
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		double throttle, curve;
		if(kidControlOn) {
			throttle = KID_THROTTLE_PERCENT * -OI.joystick.getY(Hand.kLeft);
			curve = KID_CURVE_PERCENT * OI.joystick.getX(Hand.kLeft);
			if(xb.getStartButtonPressed()) {
				kidControlOn = false;
			}
		} else {
			// Default mode
			throttle = -OI.joystick.getY(Hand.kLeft);
			curve = OI.joystick.getX(Hand.kLeft);
			if(xb.getStartButtonPressed()) {
				kidControlOn = true;
			}
		}
		// double throttle = -OI.xb.getY(Hand.kLeft);
		// double curve = OI.xb.getX(Hand.kLeft);
		kDrive.vbusArcade(throttle, curve);
		kDrive.sendDebugInfo();
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}
}
