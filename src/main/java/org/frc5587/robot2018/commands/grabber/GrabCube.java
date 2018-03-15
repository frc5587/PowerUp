package org.frc5587.robot2018.commands.grabber;

import org.frc5587.robot2018.Robot;
import org.frc5587.robot2018.subsystems.Grabber;
import org.frc5587.robot2018.subsystems.Grabber.MotorSpeed;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Command;

public class GrabCube extends Command{
    Timer t;
    Grabber grabber;

    public GrabCube(){
        grabber = Robot.grabber;
        requires(grabber);
        t = new Timer();
    }

    public void initialize(){
        grabber.setMotors(MotorSpeed.INTAKE);
        grabber.setPistons(Value.kReverse);
        t.start();
    }

    public boolean isFinished(){
        return t.hasPeriodPassed(3);
    }

    public void end(){
        grabber.setMotors(MotorSpeed.OFF);
        grabber.setPistons(Value.kForward);
    }

    public void interrupted(){
        end();
    }
}