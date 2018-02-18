package org.frc5587.robot2018.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import org.frc5587.robot2018.Robot;
import org.frc5587.robot2018.commands.TestElevator;
import org.frc5587.robot2018.subsystems.Elevator;

public class ElevatorToSetpoint extends Command {
    private double setPoint;
    private Elevator elevator;

    /**
     * 
     * @param setPoint Height to go to in inches
     */
    public ElevatorToSetpoint(double setPoint) {
        // Use requires() here to declare subsystem dependencies
        requires(Robot.elevator);
        this.setPoint = setPoint;
        elevator = Robot.elevator;
    }

    /**
     * The initialize method is called just before the first time
     * this Command is run after being started.
     */
    @Override
    protected void initialize() {
        System.out.println("Starting Elevator MotionMagic");
        elevator.createSetpoint(setPoint);
    }

    protected void execute() {
        elevator.sendDebugInfo();
        elevator.sendMotionMagicDebugInfo();
    }

    protected boolean isFinished() {
        return elevator.isDoneMoving();
    }

    /**
     * Called once when the command ended peacefully; that is it is called once
     * after {@link #isFinished()} returns true. This is where you may want to
     * wrap up loose ends, like shutting off a motor that was being used in the
     * command.
     */
    protected void end() {
        elevator.holdWithVoltage();
        new TestElevator().start();
        System.out.println("Elevator MotionMagic Finished");
    }

    /**
     * <p>
     * Called when the command ends because somebody called {@link #cancel()} or
     * another command shared the same requirements as this one, and booted it out. For example,
     * it is called when another command which requires one or more of the same
     * subsystems is scheduled to run.
     * </p><p>
     * This is where you may want to wrap up loose ends, like shutting off a motor that was being
     * used in the command.
     * </p><p>
     * Generally, it is useful to simply call the {@link #end()} method within this
     * method, as done here.
     * </p>
     */
    @Override
    protected void interrupted() {
        end();
    }
}
