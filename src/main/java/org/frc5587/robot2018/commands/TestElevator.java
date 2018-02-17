package org.frc5587.robot2018.commands;

import org.frc5587.robot2018.OI;
import org.frc5587.robot2018.Robot;
import org.frc5587.robot2018.subsystems.Elevator;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;

public class TestElevator extends Command {
    private Elevator elevator;
    private boolean elevatorPistonsOn = false;

    public TestElevator() {
        elevator = Robot.elevator;
        requires(elevator);
    }

    protected void initialize() {

    }

    protected void execute() {
        // Control elevator movement with bumpers
        // TODO: Cycle positions of elevator using Motion Magic
        if (OI.xb.getBumper(Hand.kLeft)) {
            elevator.setPower(-1);
        } else if (OI.xb.getBumper(Hand.kRight)) {
            elevator.setPower(1);
        } else {
            elevator.stop();
        }

        // Toggle position of the elevator pistons using the start button
        if (OI.xb.getStartButtonPressed()) {
            if (elevatorPistonsOn) {
                elevator.triggerPistons(true);
            } else {
                elevator.triggerPistons(false);
            }
            elevatorPistonsOn = !elevatorPistonsOn;
        }
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
        elevator.stop();
    }

    protected void interrupted() {
        end();
    }
}