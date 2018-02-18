package org.frc5587.robot2018.commands;

import org.frc5587.robot2018.OI;
import org.frc5587.robot2018.Robot;
import org.frc5587.robot2018.subsystems.Elevator;
import org.frc5587.robot2018.subsystems.Elevator.HeightLevels;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;

public class TestElevator extends Command {
    private Elevator elevator;
    private boolean elevatorPistonsOn = false;

    public TestElevator() {
        requires(Robot.elevator);
        elevator = Robot.elevator;
    }

    protected void initialize() {
        
    }

    protected void execute() {
        // Control elevator movement with bumpers
        if (OI.xb.getBumperPressed(Hand.kLeft)) {
            elevator.setMotors(HeightLevels.getPreviousValue(elevator.getHeightLevel()));
        } else if (OI.xb.getBumperPressed(Hand.kRight)) {
            elevator.setMotors(HeightLevels.getNextValue(elevator.getHeightLevel()));
        } else {
            elevator.holdWithVoltage();
        }

        // if (OI.xb.getBumper(Hand.kLeft)) {
        //     elevator.setPower(-1);
        // } else if (OI.xb.getBumper(Hand.kRight)) {
        //     elevator.setPower(1);
        // } else {
        //     elevator.holdWithVoltage();
        // }

        // Toggle position of the elevator pistons using the start button
        if (OI.xb.getStartButtonPressed()) {
            if (elevatorPistonsOn) {
                elevator.triggerPistons(Value.kForward);
            } else {
                elevator.triggerPistons(Value.kReverse);
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