/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc5587.robot2018;

import org.frc5587.robot2018.commands.auto.SetStartPos;
import org.frc5587.robot2018.commands.climber.Climb;
import org.frc5587.robot2018.commands.elevator.*;
import org.frc5587.robot2018.commands.drive.*;
import org.frc5587.robot2018.commands.*;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5587.lib.Pathgen;
import org.frc5587.robot2018.profileGeneration.DriveStraight;
import org.frc5587.robot2018.subsystems.Elevator.HeightLevels;
import org.frc5587.robot2018.subsystems.*;
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
	public static final Compressor compressor = new Compressor(RobotMap.COMPRESSOR);
	public static final LEDControl ledControl = new LEDControl();
	public static final Grabber grabber = new Grabber();
	public static final Climber climber = new Climber();

	public static final OI m_oi = new OI();
	public static final Pathgen pathgen = new Pathgen(24, .010, 50, 50, 50);

	public static StartPosition startPos;
	private SendableChooser<SetStartPos> positionChooser;
	CameraServer cam;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		compressor.setClosedLoopControl(Constants.compressorEnabled);
		positionChooser = new SendableChooser<>();
		for (StartPosition pos : StartPosition.values()) {
			positionChooser.addObject(pos.name(), new SetStartPos(pos));
		}
		SmartDashboard.putData("Starting Position Chooser", positionChooser);

		SmartDashboard.putData("Reset Drive Encoders", new ResetSensorPos());

		//cam = CameraServer.getInstance();
		//cam.startAutomaticCapture("LifeCam", 0);

		new LEDElevatorHeight().start();
		new ResetElevator().start();
		new DriveStraight();
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		System.out.println("Disabled starting. . .");
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
		ledControl.sendColor(DriverStation.getInstance().getAlliance());
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
		positionChooser.getSelected().start();
		OwnedSide nearSwitchSide = MatchData.getOwnedSide(MatchData.GameFeature.SWITCH_NEAR);

		switch (startPos) {
		case LEFT:
			if (nearSwitchSide == OwnedSide.LEFT) {
				System.out.println("Switch is close on left side");
			} else {
				System.out.println("Switch is far away while we are starting on left");
			}
			break;
		case RIGHT:
			if (nearSwitchSide == OwnedSide.RIGHT) {
				System.out.println("Switch is close on right side");
			} else {
				System.out.println("Switch is far away while we are starting on right");
			}
			break;
		case CENTER:
			if (nearSwitchSide == OwnedSide.LEFT) {
				System.out.println("Switch is on left side");
			} else {
				System.out.println("Switch is on right side");
			}
			break;
		default:
			new GyroCompMPRunner("DriveStraight").start();
			break;
		}
		//new MotionProfileFiller("DriveStraight", true).start();
		//new MotionProfileRunner().start();
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

		new TestIntake().start();
		new TestElevator().start();
		new Climb().start();
		new ArcadeDrive().start();
		new StopElevatorPistons().start();
		SmartDashboard.putData("switch height", new ElevatorToSetpoint(HeightLevels.SWITCH));
		SmartDashboard.putData("scale height", new ElevatorToSetpoint(HeightLevels.SCALE));
		SmartDashboard.putData("intake height", new ElevatorToSetpoint(HeightLevels.INTAKE));
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
}
