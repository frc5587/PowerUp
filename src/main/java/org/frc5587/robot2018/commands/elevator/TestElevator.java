package org.frc5587.robot2018.commands;

import org.frc5587.robot2018.OI;
import org.frc5587.robot2018.Robot;
import org.frc5587.robot2018.subsystems.Elevator;

import edu.wpi.first.wpilibj.GenericHID.Hand;
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
        if(OI.xb.getBumper(Hand.kLeft)){
            elevator.setPower(-.3);
        }
        else if(OI.xb.getBumper(Hand.kRight)){
            elevator.setPower(.3);
        }
        else{
            elevator.stop();
        }

        elevator.sendInfo();
        elevator.sendDebugInfo();
        elevator.sendMotionMagicDebugInfo();
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