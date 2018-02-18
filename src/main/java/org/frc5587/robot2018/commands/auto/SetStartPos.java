/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.frc5587.robot2018.commands.auto;

import edu.wpi.first.wpilibj.command.InstantCommand;
import org.frc5587.robot2018.Robot;
import org.frc5587.robot2018.Robot.StartPosition;


public class SetStartPos extends InstantCommand {
    StartPosition pos;

    public SetStartPos(StartPosition pos) {
        this.pos = pos;
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        Robot.startPos = pos;
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
