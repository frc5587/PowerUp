package org.frc5587.robot2018.commands;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5587.lib.DeadbandXboxController;
import org.frc5587.robot2018.OI;
import org.frc5587.robot2018.Robot;
import org.frc5587.robot2018.subsystems.Grabber;
import org.frc5587.robot2018.subsystems.Grabber.MotorSpeed;

public class ControlGrabber extends Command {
    MotorSpeed speed = MotorSpeed.OFF;
    Grabber grabber;
    DeadbandXboxController xb;
    Joystick db;
    boolean kidMode = false;

    public ControlGrabber() {
        requires(Robot.grabber);
        grabber = Robot.grabber;
        xb = OI.xb;
        db = OI.DriverStationButtons;
    }

    protected void initialize() {

    }

    protected void execute() {
        // Grabber Intake and Eject bound to the Y axis of the right joystick for control from Xbox controller

        if(xb.getXButtonPressed()) {
            kidMode = !kidMode;
        }


        if(!kidMode) {
            if (xb.getYButton()) {
                speed = Grabber.MotorSpeed.EJECT;
            } else if (xb.getAButton()) {
                speed = Grabber.MotorSpeed.INTAKE;
            } else {
                speed = Grabber.MotorSpeed.OFF;
            }
        }
        else{
            if (db.getRawButton(1)) {
                speed = Grabber.MotorSpeed.EJECT;
            } else if (db.getRawButton(2)) {
                speed = Grabber.MotorSpeed.INTAKE;
            } else {
                speed = Grabber.MotorSpeed.OFF;
            }
        }
        grabber.setMotors(speed);



        // Piston bound to triggers (both do the same thing)
        if (xb.getTrigger(Hand.kRight)) {
            grabber.setPistons(DoubleSolenoid.Value.kReverse);
        } else {
            grabber.setPistons(DoubleSolenoid.Value.kForward);
        }
        SmartDashboard.putBoolean("Grabber KidMode", kidMode);
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
