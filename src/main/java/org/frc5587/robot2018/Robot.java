/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc5587.robot2018;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import org.frc5587.robot2018.commands.climber.Climb;
import org.frc5587.robot2018.commands.elevator.*;
import org.frc5587.robot2018.fieldInfo.FieldInfo;
import org.frc5587.robot2018.fieldInfo.FieldInfo.FieldObjects;
import org.frc5587.robot2018.fieldInfo.FieldInfo.OwnedSide;
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

	private static NetworkTableEntry matchStartedEntry;
	private static boolean matchStarted = false;

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
		SmartDashboard.putData("Reset Drive Encoders", new ResetSensorPos());
		SmartDashboard.putData("Zero Elevator", new ZeroElevator());

		NetworkTableInstance inst = NetworkTableInstance.getDefault();
		NetworkTable table = inst.getTable("dataTable");
		matchStartedEntry = table.getEntry("Match Started");
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
        matchStartedEntry.setBoolean(false);
    }

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		ledControl.sendColor(DriverStation.getInstance().getAlliance());
		FieldInfo.updateData();
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
        matchStartedEntry.setBoolean(true);

        kDrive.resetEncoders();

		OwnedSide nearSwitchSide = FieldInfo.getOwnedSide(FieldObjects.CLOSE_SWITCH);
		switch (positionChooser.getSelected()) {
		case LEFT:
			if (nearSwitchSide == OwnedSide.LEFT) {
				System.out.println("Switch is close on left side");
				autonomousCommand = new LeftToLeftSwitchOutside();
			} else if (nearSwitchSide == OwnedSide.RIGHT) {
				System.out.println("Switch is far away while we are starting on left");
				//autonomousCommand = new LeftToRightSwitchFront();
				autonomousCommand = new GyroCompMPRunner("DriveStraight");
			} else {
				System.out.println("Switch is unknown");
			}
			break;
		case RIGHT:
			if (nearSwitchSide == OwnedSide.RIGHT) {
				System.out.println("Switch is close on right side");
				autonomousCommand = new RightToRightSwitchOutside();
			} else if (nearSwitchSide == OwnedSide.LEFT) {
				System.out.println("Switch is far away while we are starting on right");
				autonomousCommand = new GyroCompMPRunner("DriveStraight");
			} else {
				System.out.println("Switch is unknown");
			}
			break;
		case CENTER:
			if (nearSwitchSide == OwnedSide.LEFT) {
				System.out.println("Switch is on left side");
				autonomousCommand = new CenterToLeftSwitchFront();
			} else if (nearSwitchSide == OwnedSide.RIGHT) {
				System.out.println("Switch is on right side");
				autonomousCommand = new CenterToRightSwitchFront();
			} else {
				System.out.println("Switch is unknown");
			}
			break;
		default:
			System.out.println("Just driving forward");
			autonomousCommand = new GyroCompMPRunner("DriveStraight");
			break;
		}
		if (autonomousCommand != null) {
			autonomousCommand.start();
			System.out.println("Starting Command: " + autonomousCommand.toString());
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
		LEFT, CENTER, RIGHT, FORWARD;
	}
}