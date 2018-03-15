package org.frc5587.robot2018.commands.auto;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.frc5587.robot2018.commands.drive.GyroCompMPRunner;
import org.frc5587.robot2018.commands.elevator.ElevatorToSetpoint;
import org.frc5587.robot2018.commands.elevator.SetElevatorPistons;
import org.frc5587.robot2018.commands.grabber.ShootCube;
import org.frc5587.robot2018.subsystems.Elevator.HeightLevels;

public class RightToRightScale extends CommandGroup {
    public RightToRightScale() {
        addParallel(new ElevatorToSetpoint(HeightLevels.SWITCH));
        addSequential(new SetElevatorPistons(Value.kReverse));
        addSequential(new GyroCompMPRunner("RightToRightScale1/3"), 5.5);
        addSequential(new ElevatorToSetpoint(HeightLevels.SCALE), 3);
        addSequential(new GyroCompMPRunner("RightToRightScale2/3"), 2.5);
        addSequential(new ShootCube());
        addSequential(new GyroCompMPRunner("RightToRightScale3/3", true), 2.5);
        addSequential(new ElevatorToSetpoint(HeightLevels.SWITCH));
    }
}