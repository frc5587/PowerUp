package org.frc5587.robot2018.subsystems;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import org.frc5587.robot2018.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Grabber extends Subsystem {
    private TalonSRX leftMotor, rightMotor;
    private DoubleSolenoid expandSolenoid;
    public MotorSpeed currentSpeed;
    private DigitalInput breakBeam;

    public Grabber() {
        leftMotor = new TalonSRX(RobotMap.Grabber.LEFT);
        rightMotor = new TalonSRX(RobotMap.Grabber.RIGHT);
        rightMotor.setInverted(true);
        expandSolenoid = new DoubleSolenoid(RobotMap.Grabber.EXPANDER_SOLENOID[0],
                RobotMap.Grabber.EXPANDER_SOLENOID[1]);
        breakBeam = new DigitalInput(RobotMap.Grabber.RECEIVER);
        currentSpeed = MotorSpeed.OFF;
    }

    public void setMotors(MotorSpeed motorSpeed) {
        leftMotor.set(ControlMode.PercentOutput, motorSpeed.getLeft());
        rightMotor.set(ControlMode.PercentOutput, motorSpeed.getRight());
    }

    public void setPistons(DoubleSolenoid.Value valueToSet) {
        expandSolenoid.set(valueToSet);
    }

    public boolean pistonsOn() {
        return expandSolenoid.get() == Value.kForward;
    }

    public DoubleSolenoid.Value getPistonState() {
        return expandSolenoid.get();
    }

    public void initDefaultCommand() {
        //    setDefaultCommand(new MySpecialCommand());
    }

    public enum MotorSpeed {
        OFF           (new double[] { 0.0, 0.0 }), 
        INTAKE        (new double[] { -0.6, -0.6 }), 
        EJECT         (new double[] { 0.7, 0.7 }), 
        PASS_THROUGH  (new double[] { -0.7, -0.7 }), 
        RIGHT_ASSIST  (new double[] { -0.5, -0.7 }),
        LEFT_ASSIST   (new double[] { -0.7, -0.5 });

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
