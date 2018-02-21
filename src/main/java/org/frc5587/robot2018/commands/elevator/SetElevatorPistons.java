package org.frc5587.robot2018.commands.elevator;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Command;
import org.frc5587.robot2018.Robot;

public class SetElevatorPistons extends Command {
    Value v;
    Timer t;

    public SetElevatorPistons(Value v) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        this.v = v;
        t = new Timer();
    }

    public void initialize() {
        Robot.elevator.triggerPistons(v);
        t.start();
    }

    public boolean isFinished(){
        return t.hasPeriodPassed(1.5);
    }

    public void end() {
    }

    public void interrupted() {
        end();
    }
}
