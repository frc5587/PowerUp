package org.frc5587.robot2018.commands;

import org.frc5587.robot2018.OI;
import org.frc5587.robot2018.Robot;
import org.frc5587.lib.control.DeadbandXboxController;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;

public class CameraSwitching extends Command {
    DeadbandXboxController xb;
    Joystick joy;
    UsbCamera driverCamera, grabberCamera;
    VideoSink cameraSink;

    public CameraSwitching() {
        xb = OI.xb;
        joy = OI.joystick;
        driverCamera = Robot.driverCamera;
        grabberCamera = Robot.grabberCamera;
        cameraSink = Robot.cameraServer.getServer();
        this.setRunWhenDisabled(true);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {

    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        if (xb.getTrigger(Hand.kLeft) || joy.getRawButton(1)) {
            cameraSink.setSource(grabberCamera);
        } else {
            cameraSink.setSource(driverCamera);
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {

    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {

    }
}