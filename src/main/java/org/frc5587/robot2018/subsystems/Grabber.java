package org.frc5587.robot2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.frc5587.robot2018.RobotMap;


public class Grabber extends Subsystem {
    private TalonSRX leftTalon, rightTalon;
    private DoubleSolenoid expandSolenoid;

    public Grabber() {
        leftTalon = new TalonSRX(RobotMap.Grabber.LEFT_TALON);
        leftTalon.setInverted(true);
        rightTalon = new TalonSRX(RobotMap.Grabber.RIGHT_TALON);
        expandSolenoid = new DoubleSolenoid(RobotMap.Grabber.EXPANDER_SOLENOID[0], RobotMap.Grabber.EXPANDER_SOLENOID[1]);
    }

    public void setTalon(MotorSpeed motorSpeed) {
        leftTalon.set(ControlMode.PercentOutput, motorSpeed.getLeft());
        rightTalon.set(ControlMode.PercentOutput, motorSpeed.getRight());
    }

    public void setPistons(DoubleSolenoid.Value valueToSet) {
        expandSolenoid.set(valueToSet);
    }

    public void initDefaultCommand() {
        // TODO: Set the default command, if any, for a subsystem here. Example:
        //    setDefaultCommand(new MySpecialCommand());
    }

    public boolean pistonsOn() {
        return expandSolenoid.get() == Value.kForward;
    }

    public DoubleSolenoid.Value getPistonState() {
        return expandSolenoid.get();
    }

    public enum MotorSpeed {
        OFF            (new double[]{0.0, 0.0}),
        INTAKE         (new double[]{-0.5, -0.5}),
        EJECT          (new double[]{0.5, 0.5}),
        PASS_THROUGH   (new double[]{-0.1, -0.1}),
        RIGHT_ASSIST   (new double[]{-0.5, -0.7}),
        LEFT_ASSIST    (new double[]{-0.7, -0.5});

        private double[] speeds;

        MotorSpeed(double[] speeds) {
            this.speeds = speeds;
        }

        public double getLeft() {
            return speeds[0];
        }

        public double getRight() {
            return speeds[1];
        }
    }
}

