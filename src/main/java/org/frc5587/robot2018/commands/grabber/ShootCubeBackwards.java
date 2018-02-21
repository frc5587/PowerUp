package org.frc5587.robot2018.commands.grabber;

import org.frc5587.robot2018.Robot;
import org.frc5587.robot2018.subsystems.Grabber;
import org.frc5587.robot2018.subsystems.Grabber.MotorSpeed;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class ShootCubeBackwards extends Command{
    Timer t;
    Grabber grabber;

    public ShootCubeBackwards(){
        grabber = Robot.grabber;
        requires(grabber);
        t = new Timer();
    }

    public void initialize(){
        grabber.setTalon(MotorSpeed.PASS_THROUGH);
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