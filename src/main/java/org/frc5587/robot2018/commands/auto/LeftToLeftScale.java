package org.frc5587.robot2018.commands.auto;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.frc5587.robot2018.commands.drive.GyroCompMPRunner;
import org.frc5587.robot2018.commands.elevator.ElevatorToSetpoint;
import org.frc5587.robot2018.commands.elevator.SetElevatorPistons;
import org.frc5587.robot2018.commands.grabber.ShootCube;
import org.frc5587.robot2018.subsystems.Elevator.HeightLevels;

public class LeftToLeftScale extends CommandGroup{
    public LeftToLeftScale(){
        addSequential(new SetElevatorPistons(Value.kReverse));
        addParallel(new ElevatorToSetpoint(HeightLevels.SWITCH));
        addSequential(new GyroCompMPRunner("LeftToLeftScale"), 5.5);
        addSequential(new ElevatorToSetpoint(HeightLevels.SCALE), 3);
        addSequential(new ShootCube());
        addSequential(new ElevatorToSetpoint(HeightLevels.SWITCH));
    }
}