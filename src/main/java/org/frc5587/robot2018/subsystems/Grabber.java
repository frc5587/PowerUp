package org.frc5587.robot2018.subsystems;
import org.frc5587.robot2018.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Grabber extends Subsystem {
    private VictorSP leftMotor, rightMotor;
    private DoubleSolenoid expandSolenoid;
    public MotorSpeed currentSpeed;
    private DigitalInput breakBeam;

    public Grabber() {
        leftMotor = new VictorSP(RobotMap.Grabber.LEFT);
        rightMotor = new VictorSP(RobotMap.Grabber.RIGHT);
        rightMotor.setInverted(true);
        expandSolenoid = new DoubleSolenoid(RobotMap.Grabber.EXPANDER_SOLENOID[0],
                RobotMap.Grabber.EXPANDER_SOLENOID[1]);
        breakBeam = new DigitalInput(RobotMap.Grabber.RECEIVER);
        currentSpeed = MotorSpeed.OFF;
    }

    public void setMotors(MotorSpeed motorSpeed) {
        leftMotor.set(-motorSpeed.getLeft());
        rightMotor.set(-motorSpeed.getRight());
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

    public boolean hasCube() {
        return breakBeam.get();
    }

    public void initDefaultCommand() {
        //    setDefaultCommand(new MySpecialCommand());
    }

    public enum MotorSpeed {
        OFF           (new double[] { 0.0, 0.0 }), 
        INTAKE        (new double[] { -0.45, -0.45 }), 
        EJECT         (new double[] { 0.7, 0.7 }), 
        PASS_THROUGH  (new double[] { -0.5, -0.5 }), 
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
