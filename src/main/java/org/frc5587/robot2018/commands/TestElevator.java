package org.frc5587.robot2018.commands;

import org.frc5587.robot2018.OI;
import org.frc5587.robot2018.Robot;
import org.frc5587.robot2018.subsystems.Elevator;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TestElevator extends Command{
    Elevator elevator;
    public TestElevator(){
        elevator = Robot.elevator;
    }
    protected void initialize(){

    }
    protected void execute(){
        if(OI.xb.getAButton()){
            elevator.setPower(.5);
        }
        else if(OI.xb.getBButton()){
            elevator.setPower(-.5);
        }
        SmartDashboard.putNumber("Encoder Position Native", elevator.getEncoderPosition());
        SmartDashboard.putNumber("Encoder Velocity Native", elevator.getEncoderVelocity());
        SmartDashboard.putNumber("Elevator Height Inches", elevator.getElevatorHeightIn());
    }
    protected boolean isFinished(){
        return false;
    }
    protected void end(){
        elevator.stop();
    }
    protected void interrupted() {
        end();
    }
}