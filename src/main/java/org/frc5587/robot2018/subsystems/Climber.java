package org.frc5587.robot2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.frc5587.robot2018.RobotMap;

public class Climber extends Subsystem {
    TalonSRX climberTalon;

    public Climber() {
        climberTalon = new TalonSRX(RobotMap.Climber.climberTalon);
    }

    public void initDefaultCommand() { }

    public void setClimbSpeed(double climbSpeed) {
        climberTalon.set(ControlMode.PercentOutput, climbSpeed);
    }
}
