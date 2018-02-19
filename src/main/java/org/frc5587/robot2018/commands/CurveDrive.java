/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc5587.robot2018.commands;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.frc5587.robot2018.OI;
import org.frc5587.robot2018.Robot;
import org.frc5587.robot2018.subsystems.Drive;

/**
 * An example command.  You can replace me with your own command.
 */
public class CurveDrive extends Command {
	Drive kDrive;
	public CurveDrive() {
		// Use requires() here to declare subsystem dependencies
		requires(Robot.kDrive);
		this.kDrive = Robot.kDrive;
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		double throttle = OI.xb.getY(Hand.kLeft);
		double curve = -OI.xb.getX(Hand.kRight);
		boolean isQuickTurn = OI.xb.getBumper(Hand.kRight);
		SmartDashboard.putNumber("throttle", throttle);
		SmartDashboard.putNumber("curve", curve);
		kDrive.vbusCurve(throttle, curve, isQuickTurn);
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
