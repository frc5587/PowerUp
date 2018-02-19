package org.frc5587.robot2018.commands.drive;

import org.frc5587.robot2018.Robot;
import org.frc5587.robot2018.subsystems.Drive;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class ResetSensorPos extends InstantCommand{

    Drive drive;

    public ResetSensorPos(){
        drive = Robot.kDrive;
        setRunWhenDisabled(true);
    }

    public void execute(){
        drive.resetEncoders();
    }
}