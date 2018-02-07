package org.frc5587.robot2018.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import org.frc5587.robot2018.Robot;


/**
 * PURPOSE
 *
 * @author Brendan Doney
 * @version 1.0
 * @since 2/1/18
 */
public class InitiateMotionMagic extends Command {
    private double setPoint;

    public InitiateMotionMagic(double setPoint) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.elevator);
        this.setPoint = setPoint;
    }


    /**
     * The initialize method is called just before the first time
     * this Command is run after being started.
     */
    @Override
    protected void initialize() {
        Robot.elevator.createSetpoint(setPoint);
    }

    protected void execute(){
        Robot.elevator.sendDebugInfo();
    }

    protected boolean isFinished(){
        return false;//Robot.elevator.isDone();
    }

    /**
     * Called once when the command ended peacefully; that is it is called once
     * after {@link #isFinished()} returns true. This is where you may want to
     * wrap up loose ends, like shutting off a motor that was being used in the
     * command.
     */
    protected void end() {
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
