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

    LEDControl leds;
    boolean direction = false;
    Alliance color = DriverStation.getInstance().getAlliance();


    // Called just before this Command runs the first time
    @Override
    protected void initialize(){
        leds = new LEDControl();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        char B = 'b', R = 'r';
        switch (color){
            case Red:
                    leds.sendAllianceColor(R);
                break;
            case Blue:
                leds.sendAllianceColor(B);
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

