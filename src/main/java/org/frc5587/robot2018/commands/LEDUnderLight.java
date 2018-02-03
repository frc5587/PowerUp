package org.frc5587.robot2018.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.*;
import edu.wpi.first.wpilibj.command.Command;
import org.frc5587.robot2018.Robot;
import org.frc5587.robot2018.subsystems.LEDControl;

/**
 *
 */
public class LEDUnderLight extends Command{

    private Alliance color = DriverStation.getInstance().getAlliance();

    public LEDUnderLight() {
        requires(Robot.ledControl);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() { }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        switch (color){
            case Red:
                Robot.ledControl.sendColor(LEDControl.Color.RED);
                break;
            case Blue:
                Robot.ledControl.sendColor(LEDControl.Color.BLUE);
                break;
            default:
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
        end();
    }
}

