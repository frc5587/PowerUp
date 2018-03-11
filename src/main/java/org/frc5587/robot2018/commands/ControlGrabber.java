package org.frc5587.robot2018.commands;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;

import org.frc5587.lib.DeadbandXboxController;
import org.frc5587.robot2018.OI;
import org.frc5587.robot2018.Robot;
import org.frc5587.robot2018.subsystems.Grabber;
import org.frc5587.robot2018.subsystems.Grabber.MotorSpeed;

public class ControlGrabber extends Command {
    MotorSpeed speed = MotorSpeed.OFF;
    Grabber grabber;
    DeadbandXboxController xb;

    public ControlGrabber() {
        requires(Robot.grabber);
        grabber = Robot.grabber;
        xb = OI.xb;
    }

    protected void initialize() {

    }

    protected void execute() {
        // Grabber Intake and Eject bound to the Y axis of the right joystick
        if (xb.getYButton()) {
            System.out.println("Ejecting");
            speed = Grabber.MotorSpeed.EJECT;
        } else if (xb.getAButton()) {
            System.out.println("Intaking");
            speed = Grabber.MotorSpeed.INTAKE;
        } else {
            speed = Grabber.MotorSpeed.OFF;
        }
        grabber.setMotors(speed);

        // Piston bound to triggers (both do the same thing)
        if (xb.getTrigger(Hand.kRight)) {
            grabber.setPistons(DoubleSolenoid.Value.kReverse);
        } else {
            grabber.setPistons(DoubleSolenoid.Value.kForward);
        }
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
        end();
    }
}
