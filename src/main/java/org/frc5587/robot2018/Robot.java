/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc5587.robot2018;

import org.frc5587.robot2018.commands.climber.Climb;
import org.frc5587.robot2018.commands.elevator.*;
import org.frc5587.robot2018.commands.drive.*;
import org.frc5587.robot2018.commands.*;
import org.frc5587.robot2018.commands.auto.*;
import org.frc5587.robot2018.profileGeneration.*;
import org.frc5587.robot2018.subsystems.*;
import org.frc5587.lib.Pathgen;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import openrio.powerup.MatchData;
import openrio.powerup.MatchData.OwnedSide;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {
	public static final Drive kDrive = new Drive();
	public static final Elevator elevator = new Elevator();
	public static final Compressor compressor = new Compressor();
	public static final LEDControl ledControl = new LEDControl();
	public static final Grabber grabber = new Grabber();
	public static final Climber climber = new Climber();

	public static final OI m_oi = new OI();

	public static final Pathgen pathgen = new Pathgen(30, .010, 60, 80, 100);

	public static CameraServer cameraServer;
	public static UsbCamera driverCamera, grabberCamera;
	public static CvSink driverCvSink, grabberCvSink;

	private SendableChooser<StartPosition> positionChooser;
	private SendableChooser<AutoModes> autoTargetChooser;
	OwnedSide nearSwitchSide = OwnedSide.UNKNOWN;
	OwnedSide scaleSide = OwnedSide.UNKNOWN;

	Command autonomousCommand;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		compressor.setClosedLoopControl(Constants.compressorEnabled);
		positionChooser = new SendableChooser<>();
		for (StartPosition pos : StartPosition.values()) {
			positionChooser.addObject(pos.name(), pos);
		}
		SmartDashboard.putData("Starting Position Chooser", positionChooser);

		autoTargetChooser = new SendableChooser<>();
		autoTargetChooser.addObject("Switch Only", AutoModes.SWITCH_ONLY);
		autoTargetChooser.addObject("Switch and Scale", AutoModes.SCALE_AND_SWITCH);
		SmartDashboard.putData("Target for Auto", autoTargetChooser);

		SmartDashboard.putData("Reset Drive Encoders", new ResetSensorPos());

		cameraServer = CameraServer.getInstance();
		driverCamera = cameraServer.startAutomaticCapture(RobotMap.Camera.DRIVER_CAMERA);
		grabberCamera = cameraServer.startAutomaticCapture(RobotMap.Camera.GRABBER_CAMERA);
		// driverCvSink = new CvSink("driverCamera");
		// driverCvSink.setSource(driverCamera);
		// driverCvSink.setEnabled(true);
		// grabberCvSink = new CvSink("grabberCamera");
		// grabberCvSink.setSource(grabberCamera);
		// grabberCvSink.setEnabled(true);
		cameraServer.startAutomaticCapture(driverCamera);

		new LEDElevatorHeight().start();
		new ResetElevator().start();
		//new CameraSwitching().start();
		SmartDashboard.putData("Start Compressor", new RunCompressor(true));
		SmartDashboard.putData("Stop Compressor", new RunCompressor(false));
		SmartDashboard.putData("Generate Profiles", new MPGenCommand());
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		System.out.println("Disabled starting. . .");
		kDrive.enableBrakeMode(false);
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		ledControl.sendColor(DriverStation.getInstance().getAlliance());
		nearSwitchSide = MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR);
		scaleSide = MatchData.getOwnedSide(MatchData.GameFeature.SCALE);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		System.out.println("Autonomous Starting...");
		kDrive.resetEncoders();

		StartPosition startPosition = positionChooser.getSelected();
		AutoModes target = autoTargetChooser.getSelected();
		switch (startPosition) {
		case LEFT:
			switch (target) {
			case SCALE_AND_SWITCH:
				if (scaleSide == OwnedSide.LEFT) {
					if(nearSwitchSide == OwnedSide.LEFT) {
						System.out.println("Scale and switch are close on left side");
						autonomousCommand = new TwoCubeLeft();
					} else {
						System.out.println("Only scale is close on left side");
						autonomousCommand = new LeftToLeftScale();
					}
				} else if (scaleSide != OwnedSide.LEFT && nearSwitchSide == OwnedSide.LEFT) {
					System.out.println("Switch is close on left side");
					autonomousCommand = new LeftToLeftSwitchOutside();
				} else { // TODO: Add check and execution for LeftToRightScaleFront()
					System.out.println("Neither switch nor scale are close, while starting on left");
					autonomousCommand = new GyroCompMPRunner("DriveStraight");
				}
				break;
			default:
				switch (nearSwitchSide) {
				case LEFT:
					System.out.println("Switch is close on left side");
					autonomousCommand = new LeftToLeftSwitchOutside();
					break;
				default:
					System.out.println("Switch is far away while we are starting on left");
					autonomousCommand = new GyroCompMPRunner("DriveStraight");
					break;
				}
				break;
			}
			break;
		case RIGHT:
			switch (target) {
			case SCALE_AND_SWITCH:
				if (scaleSide == OwnedSide.RIGHT) {
					System.out.println("Scale is close on right side");
					autonomousCommand = new RightToRightScale();
				} else if (scaleSide != OwnedSide.RIGHT && nearSwitchSide == OwnedSide.RIGHT) {
					System.out.println("Switch is close on right side");
					autonomousCommand = new LeftToLeftSwitchOutside();
				} else { // TODO: Add check and execution for RightToLeftScale()
					System.out.println("Neither switch nor scale are close, while starting on right");
					autonomousCommand = new GyroCompMPRunner("DriveStraight");
				}
				break;
			default:
				switch (nearSwitchSide) {
				case RIGHT:
					System.out.println("Switch is close on right side");
					autonomousCommand = new RightToRightSwitchOutside();
					break;
				default:
					System.out.println("Switch is far away while we are starting on right");
					autonomousCommand = new GyroCompMPRunner("DriveStraight");
					break;
				}
				break;
			}
			break;
		default: // Otherwise, assume centre...
			System.out.println(nearSwitchSide);
			switch (nearSwitchSide) {
			case LEFT:
				System.out.println("Switch is on left side");
				autonomousCommand = new CenterToLeftSwitchFront();
				break;
			case RIGHT:
				System.out.println("Switch is on right side");
				autonomousCommand = new CenterToRightSwitchFront();
				break;
			default:
				System.out.println("Switch is unknown");
				break;
			}
			break;
		}

		if (autonomousCommand != null) {
			autonomousCommand.start();
			System.out.println("Starting Command: " + autonomousCommand.toString());
		} else {
			System.out.println("Position of self and/or elements was unknown, so starting the drive straight profile");
			System.out.println("Autonomous Mode: " + target + "\nStarting Position: " + startPosition);
		}
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		System.out.println("Teleop starting... ");
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}

		new ControlGrabber().start();
		new ControlElevator().start();
		//new TestElevator().start();
		new Climb().start();
		new ArcadeDrive().start();
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}

	public enum StartPosition {
		LEFT, CENTER, RIGHT, TEST;
	}

	public enum AutoModes {
		SWITCH_ONLY, SCALE_AND_SWITCH;
	}
}
