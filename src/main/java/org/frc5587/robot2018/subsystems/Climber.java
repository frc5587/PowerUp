package org.frc5587.robot2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.frc5587.robot2018.RobotMap;

/**
 * Climber subsystem, which consists of a simple one-way winch controlled by a
 * motor controller, enabling input to be issued to pull up the robot on the
 * standard Power Up bar
 */
public class Climber extends Subsystem {
    TalonSRX climberTalon;

    /**
     * Constructor for timer, which also configures the TalonSRX within the
     * constructor call
     */
    public Climber() {
        climberTalon = new TalonSRX(RobotMap.Climber.climberTalon);
        climberTalon.configContinuousCurrentLimit(50, 10);
        climberTalon.configPeakCurrentLimit(60, 10);
        climberTalon.configPeakCurrentDuration(200, 10);
    }

    @Override
    protected void initDefaultCommand() {
    }

    /**
     * Set the speed of the climber to a percentage using the TalonSRX percent ouput
     * 
     * @param percentSpeed the percentage output to set the climber's motor
     *                     controller to
     */
    public void setClimbSpeed(double percentSpeed) {
        climberTalon.set(ControlMode.PercentOutput, percentSpeed);
    }
}
