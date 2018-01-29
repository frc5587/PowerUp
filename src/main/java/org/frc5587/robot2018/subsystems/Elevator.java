package org.frc5587.robot2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.frc5587.robot2018.RobotMap;

public class Elevator extends Subsystem {

    private Counter hallEffect;
    private TalonSRX elevatorController;
    private DoubleSolenoid tiltDoubleSol;

    public Elevator() {
        tiltDoubleSol = new DoubleSolenoid(RobotMap.ELEVATOR_SOLENOID[0], RobotMap.ELEVATOR_SOLENOID[1]);
        hallEffect = new Counter(RobotMap.HALL_EFFECT_SENSOR);
        hallEffect.setUpDownCounterMode();
        elevatorController = new TalonSRX(RobotMap.ELEVATOR_MOTOR);
    }
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void triggerPistons(boolean enable) {
        if(enable)
            tiltDoubleSol.set(DoubleSolenoid.Value.kForward);
        else
            tiltDoubleSol.set(DoubleSolenoid.Value.kOff);
    }

    public void startCalibration() {
        hallEffect.reset();
        elevatorController.set(ControlMode.PercentOutput, -0.01);
    }

    @Override
    public void initDefaultCommand() {
        // TODO: Set the default command, if any, for a subsystem here. Example:
        //    setDefaultCommand(new MySpecialCommand());
    }

    public int getHallEffect() {
        return hallEffect.get();
    }

    public void stopMotor() {
        elevatorController.set(ControlMode.PercentOutput, 0.0);
    }
}

