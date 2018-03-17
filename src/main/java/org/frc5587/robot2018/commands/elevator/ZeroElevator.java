package org.frc5587.robot2018.commands.elevator;

import edu.wpi.first.wpilibj.command.InstantCommand;
import org.frc5587.robot2018.Robot;
import org.frc5587.robot2018.subsystems.Elevator;

public class ZeroElevator extends InstantCommand {
    private Elevator elevator;

    public ZeroElevator() {
        this.setRunWhenDisabled(true);
        this.elevator = Robot.elevator;
    }

    /**
     * The initialize method is called just before the first time
     * this Command is run after being started.
     */
    @Override
    protected void initialize() {
        elevator.setEncoderPosition(0);
    }
}
