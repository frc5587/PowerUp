/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc5587.robot2018.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import org.frc5587.lib.TitanDrive;
import org.frc5587.lib.TitanDrive.DriveSignal;
import org.frc5587.robot2018.Constants;
import org.frc5587.robot2018.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

/**
 * An example subsystem.  You can replace me with your own Subsystem.
 */
public class Drive extends Subsystem {

	TalonSRX leftMaster, rightMaster;
	VictorSPX leftSlave, rightSlave;
	TitanDrive driveHelper;

	public Drive(){
		driveHelper = new TitanDrive();
		//initialize Talons
		leftMaster = new TalonSRX(RobotMap.Drive.leftMaster);
		rightMaster = new TalonSRX(RobotMap.Drive.rightMaster);
		leftSlave = new VictorSPX(RobotMap.Drive.leftSlave);
		rightSlave = new VictorSPX(RobotMap.Drive.rightSlave);

		//invert right side
		rightMaster.setInverted(true);
		rightSlave.setInverted(true);

		//Set the slaves to mimic the masters
		leftSlave.follow(leftMaster);
		rightSlave.follow(rightMaster);

		//Enable Voltage Compensation
		rightMaster.configVoltageCompSaturation(Constants.Drive.kVCompSaturation, Constants.Drive.kTimeoutMs);
		rightMaster.enableVoltageCompensation(true);
		leftMaster.configVoltageCompSaturation(Constants.Drive.kVCompSaturation, Constants.Drive.kTimeoutMs);
		leftMaster.enableVoltageCompensation(true);
	}

	/**
	 * Send PIDF constants to master talons
	 * @param slotIdx Which slot to push values to
	 */
	private void fillPIDFSlot(){
		leftMaster.config_kP(0, Constants.Drive.leftPIDs[0], Constants.Drive.kTimeoutMs);
		leftMaster.config_kP(0, Constants.Drive.leftPIDs[2], Constants.Drive.kTimeoutMs);
		leftMaster.config_kP(0, Constants.Drive.leftPIDs[3], Constants.Drive.kTimeoutMs);
		leftMaster.config_kP(0, Constants.Drive.leftPIDs[4], Constants.Drive.kTimeoutMs);

		rightMaster.config_kP(0, Constants.Drive.rightPIDs[0], Constants.Drive.kTimeoutMs);
		rightMaster.config_kI(0, Constants.Drive.rightPIDs[1], Constants.Drive.kTimeoutMs);
		rightMaster.config_kD(0, Constants.Drive.rightPIDs[2], Constants.Drive.kTimeoutMs);
		rightMaster.config_kF(0, Constants.Drive.rightPIDs[3], Constants.Drive.kTimeoutMs);
	}

	public void vbusCurve(double throttle, double curve, boolean isQuickTurn){
		DriveSignal d = driveHelper.curvatureDrive(throttle, curve, isQuickTurn);
		
		leftMaster.set(ControlMode.PercentOutput, d.left);
		rightMaster.set(ControlMode.PercentOutput, d.right);
	}

	public void vbusArcade(double throttle, double turn){
		DriveSignal d = driveHelper.arcadeDrive(throttle, turn);
		
		leftMaster.set(ControlMode.PercentOutput, d.left);
		rightMaster.set(ControlMode.PercentOutput, d.right);
	}

	public void velocityCurve(double throttle, double curve, boolean isQuickTurn){
		DriveSignal d = driveHelper.curvatureDrive(throttle, curve, isQuickTurn);
		
		leftMaster.set(ControlMode.Velocity, d.left * Constants.Drive.kMaxVelocity);
		rightMaster.set(ControlMode.Velocity, d.right * Constants.Drive.kMaxVelocity);
	}

	public void velocityArcade(double throttle, double turn){
		DriveSignal d = driveHelper.arcadeDrive(throttle, turn);

		leftMaster.set(ControlMode.Velocity, d.left * Constants.Drive.kMaxVelocity);
		rightMaster.set(ControlMode.Velocity, d.right * Constants.Drive.kMaxVelocity);
	}

	public double getLeftPosition(){
		return leftMaster.getSelectedSensorPosition(0);
	}
	public double getRightPosition(){
		return rightMaster.getSelectedSensorPosition(0);
	}
	public double getLeftVelocity(){
		return leftMaster.getSelectedSensorPosition(0);
	}
	public double getRightVelocity(){
		return rightMaster.getSelectedSensorPosition(0);
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
		//setDefaultCommand(new CurveDrive());
	}
}
