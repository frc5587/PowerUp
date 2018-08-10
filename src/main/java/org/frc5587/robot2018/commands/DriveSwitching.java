package org.frc5587.robot2018.commands;

import java.util.HashMap;

import org.frc5587.robot2018.OI;

import edu.wpi.first.wpilibj.command.Command;

public class DriveSwitching extends Command {
    HashMap<Boolean, Command> driveModes = new HashMap<>();
    boolean isArcade;

    public DriveSwitching() {
        // Use requires() here to declare subsystem dependencies
        // requires(Robot.exampleSubsystem);
        driveModes.put(true, new ArcadeDrive());
        driveModes.put(false, new CurveDrive());

    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        if(OI.xb.getBButtonPressed()) {
            System.out.println("Switching modes");
            driveModes.get(isArcade).cancel();
            isArcade = !isArcade;
            driveModes.get(isArcade).start();
        }
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