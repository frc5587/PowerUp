package org.frc5587.robot2018.commands.auto;

import org.frc5587.robot2018.Constants;
import org.frc5587.robot2018.Robot;
import org.frc5587.robot2018.subsystems.Drive;

import edu.wpi.first.wpilibj.command.Command;

public class DriveVoltage extends Command{
    Drive drive;
    double voltage;

    public DriveVoltage(double voltage){
        this.voltage = voltage;
        drive = Robot.kDrive;
        requires(drive);
    }
    public void initialize(){
        drive.enableBrakeMode(true);
        drive.vbusLR(voltage/Constants.Drive.kVCompSaturation, voltage/Constants.Drive.kVCompSaturation);
    }

    public void execute(){
        System.out.println(drive.getLeftVoltage() + "," + drive.getRightVoltage() + "," + drive.getLeftVelocity() + "," + drive.getRightVelocity());
    }

    public boolean isFinished(){
        return false;
    }
    public void end(){
        drive.stop();
        drive.enableBrakeMode(false);
    }
    public void interrupted(){
        end();
    }
}