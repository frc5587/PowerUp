package org.frc5587.robot2018.commands.auto;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.CommandGroup;
import org.frc5587.robot2018.commands.drive.GyroCompMPRunner;
import org.frc5587.robot2018.commands.elevator.ElevatorToSetpoint;
import org.frc5587.robot2018.commands.elevator.SetElevatorPistons;
import org.frc5587.robot2018.commands.grabber.GrabCube;
import org.frc5587.robot2018.commands.grabber.ShootCube;
import org.frc5587.robot2018.commands.grabber.ShootCubeBackwards;
import org.frc5587.robot2018.subsystems.Elevator.HeightLevels;

/**
 * Paths go forwards instead of backwards
 * DO NOT USE without modifying profiles
 */
public class TwoCubeLeft extends CommandGroup{
    public TwoCubeLeft(){
        addParallel(new ElevatorToSetpoint(HeightLevels.SWITCH), 2);
        addSequential(new GyroCompMPRunner("LeftToLeftScaleBackwards", false), 6);
        addSequential(new SetElevatorPistons(Value.kReverse));
        addSequential(new ElevatorToSetpoint(HeightLevels.SCALE), 4);
        addSequential(new GyroCompMPRunner("LeftScaleBackwardsToLeftSwitchBack_Finish", true), 2);
        addSequential(new ShootCubeBackwards());
        addSequential(new ElevatorToSetpoint(HeightLevels.INTAKE), 4);
        addParallel(new GrabCube());
        addSequential(new GyroCompMPRunner("LeftScaleBackwardsToLeftSwitch", true), 3);
        addSequential(new ElevatorToSetpoint(HeightLevels.SWITCH), 2);
        addSequential(new ShootCube());
    }
}