package org.frc5587.robot2018.commands;

import org.frc5587.lib.DeadbandXboxController;
import org.frc5587.lib.MathHelper;
import org.frc5587.robot2018.Constants;
import org.frc5587.robot2018.OI;
import org.frc5587.robot2018.Robot;
import org.frc5587.robot2018.subsystems.Elevator;
import org.frc5587.robot2018.subsystems.Elevator.HeightLevels;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;

public class ControlElevator extends Command {
    private boolean elevatorPistonsOn = false, manualControlOn = false;
    private DeadbandXboxController xb;
    private Elevator elevator;

    public ControlElevator() {
        requires(Robot.elevator);
        elevator = Robot.elevator;
        xb = OI.xb;
    }

    protected void initialize() {

    }

    protected void execute() {
        if (!xb.getTrigger(Hand.kLeft)) {
            if (xb.getBackButtonPressed()) {
                elevator.goToHeight(HeightLevels.INTAKE);
            } else {
                if (manualControlOn) {
                    // Reset to hold to override old joystick output
                    elevator.holdWithVoltage();
                    manualControlOn = false;
                }

                // Control elevator movement with bumpers
                if (xb.getBumperPressed(Hand.kLeft)) {
                    elevator.goToHeight(HeightLevels.getPreviousValue(elevator.getHeightLevel()));
                } else if (xb.getBumperPressed(Hand.kRight)) {
                    elevator.goToHeight(HeightLevels.getNextValue(elevator.getHeightLevel()));
                } else if (elevator.isDoneMoving()) {
                    elevator.holdWithVoltage();
                }
            }
        } else {
            manualControlOn = true;
            elevator.setPower(MathHelper.limit(-xb.getY(Hand.kLeft) + Constants.Elevator.holdPercent, -1, 1));
        }

        // Toggle position of the elevator pistons using the start button
        if (xb.getStartButtonPressed()) {
            if (elevatorPistonsOn) {
                elevator.triggerPistons(Value.kForward);
            } else {
                elevator.triggerPistons(Value.kReverse);
            }
            elevatorPistonsOn = !elevatorPistonsOn;
        }

        elevator.sendDebugInfo();
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