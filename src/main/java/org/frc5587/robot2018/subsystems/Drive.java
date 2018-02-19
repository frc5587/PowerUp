/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc5587.robot2018.subsystems;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.frc5587.lib.TitanDrive;
import org.frc5587.lib.TitanDrive.DriveSignal;
import org.frc5587.robot2018.Constants;
import org.frc5587.robot2018.RobotMap;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.kauailabs.navx.frc.AHRS;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

/**
 * An example subsystem.  You can replace me with your own Subsystem.
 */
public class Drive extends Subsystem {

	TalonSRX leftMaster, rightMaster;
	VictorSPX leftSlave, rightSlave;
	TitanDrive driveHelper;
	AHRS navx;
	ADXRS450_Gyro gyro;
	MotionProfileStatus[] statuses = {new MotionProfileStatus(), new MotionProfileStatus()};

	public Drive(){
		driveHelper = new TitanDrive();

		try{
			navx = new AHRS(I2C.Port.kMXP);
		}
		catch(Exception e){
			e.printStackTrace();
		}

		try{
			gyro = new ADXRS450_Gyro(Port.kOnboardCS0);
		}
		catch(Exception e){
			e.printStackTrace();
		}

		//initialize Talons
		leftMaster = new TalonSRX(RobotMap.Drive.leftMaster);
		rightMaster = new TalonSRX(RobotMap.Drive.rightMaster);
		leftSlave = new VictorSPX(RobotMap.Drive.leftSlave);
		rightSlave = new VictorSPX(RobotMap.Drive.rightSlave);

		//invert right side
		rightMaster.setInverted(true);
		rightSlave.setInverted(true);

		leftMaster.setSensorPhase(true);
		rightMaster.setSensorPhase(true);

		//Set the slaves to mimic the masters
		leftSlave.follow(leftMaster);
		rightSlave.follow(rightMaster);


		//Enable Voltage Compensation
		rightMaster.configVoltageCompSaturation(Constants.Drive.kVCompSaturation, Constants.Drive.kTimeoutMs);
		rightMaster.enableVoltageCompensation(true);
		leftMaster.configVoltageCompSaturation(Constants.Drive.kVCompSaturation, Constants.Drive.kTimeoutMs);
		leftMaster.enableVoltageCompensation(true);

		leftMaster.configPeakOutputForward(Constants.Drive.maxPercentFw, Constants.Drive.kTimeoutMs);
		leftMaster.configPeakOutputReverse(-Constants.Drive.maxPercentBw, Constants.Drive.kTimeoutMs);
		rightMaster.configPeakOutputForward(Constants.Drive.maxPercentFw, Constants.Drive.kTimeoutMs);
		rightMaster.configPeakOutputReverse(-Constants.Drive.maxPercentBw, Constants.Drive.kTimeoutMs);
		leftSlave.configPeakOutputForward(Constants.Drive.maxPercentFw, Constants.Drive.kTimeoutMs);
		leftSlave.configPeakOutputReverse(-Constants.Drive.maxPercentBw, Constants.Drive.kTimeoutMs);
		rightSlave.configPeakOutputForward(Constants.Drive.maxPercentFw, Constants.Drive.kTimeoutMs);
		rightSlave.configPeakOutputReverse(-Constants.Drive.maxPercentBw, Constants.Drive.kTimeoutMs);

		fillPIDFSlot(0);
	}

	public class ProcessProfileRunnable implements java.lang.Runnable {
	    public void run(){
			leftMaster.processMotionProfileBuffer();
			rightMaster.processMotionProfileBuffer();
		}
	}

	public Notifier profileNotifer = new Notifier(new ProcessProfileRunnable());

	public void resetMP(){
		leftMaster.clearMotionProfileHasUnderrun(Constants.Drive.kTimeoutMs);
		leftMaster.clearMotionProfileTrajectories();
		leftMaster.changeMotionControlFramePeriod(10);
		leftMaster.configMotionProfileTrajectoryPeriod(10, Constants.Drive.kTimeoutMs);
		leftMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.Drive.kTimeoutMs);
		leftMaster.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);


		rightMaster.clearMotionProfileHasUnderrun(Constants.Drive.kTimeoutMs);
		rightMaster.clearMotionProfileTrajectories();
		rightMaster.changeMotionControlFramePeriod(10);
		rightMaster.configMotionProfileTrajectoryPeriod(10, Constants.Drive.kTimeoutMs);
		rightMaster.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.Drive.kTimeoutMs);
		rightMaster.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);
	}

	public void updateStatus(){
		leftMaster.getMotionProfileStatus(statuses[0]);
		rightMaster.getMotionProfileStatus(statuses[1]);
	}

	public MotionProfileStatus[] getStatuses(){
		return statuses;
	}

	public boolean isMPReady(){
		boolean leftReady = getStatuses()[0].btmBufferCnt > Constants.Drive.minBufferCount;
		boolean rightReady = getStatuses()[0].btmBufferCnt > Constants.Drive.minBufferCount;
		return leftReady && rightReady;
	}

	public boolean isMPDone(){
		boolean leftDone = getStatuses()[0].isLast;
		boolean rightDone = getStatuses()[0].isLast;
		return leftDone && rightDone;
	}

	public void queuePoints(TrajectoryPoint[][] trajectories){
		for(TrajectoryPoint point : trajectories[0]){
			leftMaster.pushMotionProfileTrajectory(point);
		}
		for(TrajectoryPoint point : trajectories[1]){
			rightMaster.pushMotionProfileTrajectory(point);
		}
	}

	public void setProfileMode(SetValueMotionProfile mpMode){
		leftMaster.set(ControlMode.MotionProfile, mpMode.value);
		rightMaster.set(ControlMode.MotionProfile, mpMode.value);
	}

	/**
	 * Send PIDF constants to master talons
	 * @param slotIdx Which slot to push values to
	 */
	private void fillPIDFSlot(int slotIdx){
		leftMaster.config_kP(slotIdx, Constants.Drive.leftPIDs[0], 0);
		leftMaster.config_kI(slotIdx, Constants.Drive.leftPIDs[1], 0);
		leftMaster.config_kD(slotIdx, Constants.Drive.leftPIDs[2], 0);
		leftMaster.config_kF(slotIdx, Constants.Drive.leftPIDs[3], 0);

		rightMaster.config_kP(slotIdx, Constants.Drive.rightPIDs[0], 0);
		rightMaster.config_kI(slotIdx, Constants.Drive.rightPIDs[1], 0);
		rightMaster.config_kD(slotIdx, Constants.Drive.rightPIDs[2], 0);
		rightMaster.config_kF(slotIdx, Constants.Drive.rightPIDs[3], 0);
	}

	public void enableBrakeMode(boolean enabled){
		if(enabled){
			leftMaster.setNeutralMode(NeutralMode.Brake);
			rightMaster.setNeutralMode(NeutralMode.Brake);
			leftSlave.setNeutralMode(NeutralMode.Brake);
			rightSlave.setNeutralMode(NeutralMode.Brake);
		}
		else{
			leftMaster.setNeutralMode(NeutralMode.Coast);
			rightMaster.setNeutralMode(NeutralMode.Coast);
			leftSlave.setNeutralMode(NeutralMode.Coast);
			rightSlave.setNeutralMode(NeutralMode.Coast);
		}
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

	public void vbusLR(double left, double right){
		leftMaster.set(ControlMode.PercentOutput, left);
		rightMaster.set(ControlMode.PercentOutput, right);
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

	public void stop(){
		leftMaster.set(ControlMode.PercentOutput, 0.0);
		rightMaster.set(ControlMode.PercentOutput, 0.0);
	}

	public int getLeftPosition(){
		return leftMaster.getSelectedSensorPosition(0);
	}
	
	public int getRightPosition(){
		return rightMaster.getSelectedSensorPosition(0);
	}

	public int getLeftVelocity(){
		return leftMaster.getSelectedSensorVelocity(0);
	}

	public int getRightVelocity(){
		return rightMaster.getSelectedSensorVelocity(0);
	}

	public double getLeftVoltage(){
		return leftMaster.getMotorOutputVoltage();
	}

	public double getRightVoltage(){
		return rightMaster.getMotorOutputVoltage();
	}

	public double getHeading(){
		return gyro.getAngle();
	}

	public void resetEncoders(){
		leftMaster.setSelectedSensorPosition(0, 0, Constants.Drive.kTimeoutMs);
		rightMaster.setSelectedSensorPosition(0, 0, Constants.Drive.kTimeoutMs);
	}

	public void sendDebugInfo(){
		SmartDashboard.putNumber("Left Distance", getLeftPosition());
		SmartDashboard.putNumber("Right Distance", getRightPosition());
		SmartDashboard.putNumber("Left Velocity", getLeftVelocity());
		SmartDashboard.putNumber("Right Velocity", getRightVelocity());
		SmartDashboard.putNumber("Heading", getHeading());
	}

	public void sendMPDebugInfo(){
		SmartDashboard.putNumber("Left Expected Pos", leftMaster.getActiveTrajectoryPosition());
		SmartDashboard.putNumber("Right Expected Pos", rightMaster.getActiveTrajectoryPosition());
		SmartDashboard.putNumber("Left Expected Vel", leftMaster.getActiveTrajectoryVelocity());
		SmartDashboard.putNumber("Right Expected Vel", rightMaster.getActiveTrajectoryVelocity());
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
		//setDefaultCommand(new CurveDrive());
	}
}
