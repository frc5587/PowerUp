package org.frc5587.robot2018.commands;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5587.robot2018.OI;
import org.frc5587.robot2018.Robot;
import org.frc5587.robot2018.commands.grabber.SetGrabberMotors;
import org.frc5587.robot2018.commands.grabber.TriggerGrabberPistons;
import org.frc5587.robot2018.subsystems.Elevator;
import org.frc5587.robot2018.subsystems.Grabber;
import org.frc5587.robot2018.subsystems.Grabber.MotorSpeed;

public class TestIntake extends Command{
    Elevator elevator;
    boolean pistonState = false;
    MotorSpeed speed = MotorSpeed.OFF;
    Grabber grabber;

    public TestIntake(){
        elevator = Robot.elevator;
        grabber = Robot.grabber;
    }
    protected void initialize(){
        
    }
    protected void execute(){
        if(OI.xb.getAButton()){
            speed = Grabber.MotorSpeed.INTAKE;
        }
        else if(OI.xb.getYButton()) {
            speed = Grabber.MotorSpeed.EJECT;
        }
        else{
            speed = Grabber.MotorSpeed.OFF;
        }
        grabber.setTalon(speed);

        if(OI.xb.getBumperPressed(Hand.kRight)){
            if(pistonState)
                new TriggerGrabberPistons(DoubleSolenoid.Value.kForward).start();
            else
                new TriggerGrabberPistons(DoubleSolenoid.Value.kReverse).start();
            pistonState = !pistonState;
        }

    }
    protected boolean isFinished(){
        return false;
    }
    protected void end() { }
    protected void interrupted() {
        end();
	}
}