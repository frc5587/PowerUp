package org.frc5587.robot2018.commands.auto;

import org.frc5587.robot2018.commands.drive.GyroCompMPRunner;
import org.frc5587.robot2018.commands.elevator.ElevatorToSetpoint;
import org.frc5587.robot2018.commands.grabber.ShootCube;
import org.frc5587.robot2018.subsystems.Elevator.HeightLevels;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightStartRightSwitchInside extends CommandGroup{
    public RightStartRightSwitchInside() {
        addParallel(new ElevatorToSetpoint(HeightLevels.SWITCH));
        addSequential(new GyroCompMPRunner("RightStartRightSwitchInside"), 5);
        addSequential(new ShootCube());
    }
}