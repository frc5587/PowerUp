/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc5587.robot2018.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import org.frc5587.robot2018.RobotMap;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

/**
 * An example subsystem.  You can replace me with your own Subsystem.
 */
public class Drive extends Subsystem {

	WPI_TalonSRX leftMaster, rightMaster, leftSlave, rightSlave;

	DifferentialDrive vbusDrive;

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
		//initialize Talons
		leftMaster = new WPI_TalonSRX(RobotMap.leftMaster);
		rightMaster = new WPI_TalonSRX(RobotMap.rightMaster);
		leftSlave = new WPI_TalonSRX(RobotMap.leftSlave);
		rightSlave = new WPI_TalonSRX(RobotMap.rightSlave);

		//Set the slaves to mimic the masters
		leftSlave.set(ControlMode.Follower, leftMaster.getDeviceID());
		rightSlave.set(ControlMode.Follower, rightMaster.getDeviceID());

		vbusDrive = new DifferentialDrive(leftMaster, rightMaster);
	}

	/**
	 * Send PIDF constants to master talons
	 * @param slotIdx Which slot to push values to
	 */
	private void pushPIDF(int slotIdx){
		leftMaster.config_kP(slotIdx, leftPIDs[0], 0);
		leftMaster.config_kI(slotIdx, leftPIDs[1], 0);
		leftMaster.config_kD(slotIdx, leftPIDs[2], 0);
		leftMaster.config_kF(slotIdx, leftPIDs[3], 0);

		rightMaster.config_kP(slotIdx, rightPIDs[0], 0);
		rightMaster.config_kI(slotIdx, rightPIDs[1], 0);
		rightMaster.config_kD(slotIdx, rightPIDs[2], 0);
		rightMaster.config_kF(slotIdx, rightPIDs[3], 0);
	}

	public void curvatureDrive(double throttle, double curve){
		vbusDrive.curvatureDrive(throttle, curve*Math.signum(throttle), Math.abs(throttle) <= .1);
	}

	public void arcadeDrive(double throttle, double curve){
		vbusDrive.arcadeDrive(throttle, curve, false);
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}
}
