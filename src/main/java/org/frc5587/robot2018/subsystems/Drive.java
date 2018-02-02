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
import org.frc5587.robot2018.commands.CurveDrive;

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

	double kMaxVelocity = 0.0;

	//PIDF Constants
	double[] leftPIDs = {
		0.0,	//kP
		0.0,	//kI
		0.0,	//kD
		0.0 	//kF
	};

	double[] rightPIDs = {
		0.0,	//kP
		0.0,	//kI
		0.0,	//kD
		0.0		//kF
	};

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
		//rightMaster.configVoltageCompSaturation(12.0, 0);
		//rightMaster.enableVoltageCompensation(true);
		//leftMaster.configVoltageCompSaturation(12.0, 0);
		//leftMaster.enableVoltageCompensation(true);

		rightMaster.configPeakOutputForward(1, 0);
		rightMaster.configPeakOutputReverse(-1, 0);
		rightSlave.configPeakOutputForward(1, 0);
		rightSlave.configPeakOutputReverse(-1, 0);
	}

	/**
	 * Send PIDF constants to master talons
	 * @param slotIdx Which slot to push values to
	 */
	private void fillPIDFSlot(int slotIdx){
		leftMaster.config_kP(slotIdx, leftPIDs[0], 0);
		leftMaster.config_kI(slotIdx, leftPIDs[1], 0);
		leftMaster.config_kD(slotIdx, leftPIDs[2], 0);
		leftMaster.config_kF(slotIdx, leftPIDs[3], 0);

		rightMaster.config_kP(slotIdx, rightPIDs[0], 0);
		rightMaster.config_kI(slotIdx, rightPIDs[1], 0);
		rightMaster.config_kD(slotIdx, rightPIDs[2], 0);
		rightMaster.config_kF(slotIdx, rightPIDs[3], 0);
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
		System.out.println(d.right);
	}

	public void velocityCurve(double throttle, double curve, boolean isQuickTurn){
		DriveSignal d = driveHelper.curvatureDrive(throttle, curve, isQuickTurn);
		
		leftMaster.set(ControlMode.Velocity, d.left * kMaxVelocity);
		rightMaster.set(ControlMode.Velocity, d.right * kMaxVelocity);
	}

	public void velocityArcade(double throttle, double turn){
		DriveSignal d = driveHelper.arcadeDrive(throttle, turn);

		leftMaster.set(ControlMode.Velocity, d.left * kMaxVelocity);
		rightMaster.set(ControlMode.Velocity, d.right * kMaxVelocity);
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
