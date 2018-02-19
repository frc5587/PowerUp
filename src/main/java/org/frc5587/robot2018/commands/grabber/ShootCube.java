package org.frc5587.robot2018.commands.grabber;

import org.frc5587.robot2018.Robot;
import org.frc5587.robot2018.subsystems.Grabber;
import org.frc5587.robot2018.subsystems.Grabber.MotorSpeed;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class ShootCube extends Command{
    Timer t;
    Grabber grabber;

    public ShootCube(){
        grabber = Robot.grabber;
        requires(grabber);
        t = new Timer();
    }

    public void initialize(){
        grabber.setTalon(MotorSpeed.EJECT);
        t.start();
    }

    public boolean isFinished(){
        return t.hasPeriodPassed(.3);
    }

    public void end(){
        grabber.setTalon(MotorSpeed.OFF);
    }
    
    public void interrupted(){
        end();
    }
}