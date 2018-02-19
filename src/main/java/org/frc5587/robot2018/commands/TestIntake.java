package org.frc5587.robot2018.commands;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;
import org.frc5587.robot2018.OI;
import org.frc5587.robot2018.Robot;
import org.frc5587.robot2018.subsystems.Elevator;
import org.frc5587.robot2018.subsystems.Grabber;
import org.frc5587.robot2018.subsystems.Grabber.MotorSpeed;

public class TestIntake extends Command {
    Elevator elevator;
    MotorSpeed speed = MotorSpeed.OFF;
    Grabber grabber;

    public TestIntake() {
        requires(Robot.grabber);
        grabber = Robot.grabber;
    }

    protected void initialize() {

    }

    protected void execute() {
        // Grabber Intake and Eject bound to the Y axis of the right joystick
        if (OI.xb.getYButton()) {
            speed = Grabber.MotorSpeed.EJECT;
        } else if (OI.xb.getAButton()) {
            speed = Grabber.MotorSpeed.INTAKE;
        } else {
            speed = Grabber.MotorSpeed.OFF;
        }
        grabber.setTalon(speed);

        // Piston bound to triggers (both do the same thing)
        if (OI.xb.getTriggerAxis(Hand.kRight) > .05 || OI.xb.getTriggerAxis(Hand.kLeft) > .05) {
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
