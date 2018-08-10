/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc5587.robot2018.commands;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5587.robot2018.OI;
import org.frc5587.robot2018.Robot;
import org.frc5587.robot2018.subsystems.Drive;

/**
 * An example command.  You can replace me with your own command.
 */
public class ArcadeDrive extends Command {
	private static final double KID_THROTTLE_PERCENT = 0.5;
	private static final double KID_CURVE_PERCENT = 0.7;
	Drive kDrive;
	XboxController xb;
	boolean slowMode = false;
	public ArcadeDrive() {
		// Use requires() here to declare subsystem dependencies
		requires(Robot.kDrive);
		this.kDrive = Robot.kDrive;
		xb = OI.xb;
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {
		kDrive.enableBrakeMode(false);
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		// Xbox configuration
		// double throttle = -OI.xb.getY(Hand.kLeft);
		// double curve = OI.xb.getX(Hand.kLeft);

		double throttle = -OI.joystick.getY(Hand.kLeft);
		double curve = OI.joystick.getX(Hand.kLeft);

		if(xb.getBButtonPressed()){
			slowMode = !slowMode;
		}

		if(slowMode){
			if(!xb.getStickButton(Hand.kLeft)) {
				throttle *= KID_THROTTLE_PERCENT;
				curve *= KID_CURVE_PERCENT;
			}
			else{
				throttle = 0;
				curve = 0;
			}
		}

		kDrive.vbusArcade(throttle, curve);
		kDrive.sendDebugInfo();
		SmartDashboard.putBoolean("Drivetrain SlowMode", slowMode);
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
