package org.frc5587.robot2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5587.robot2018.Constants;
import org.frc5587.robot2018.RobotMap;

public class Elevator extends Subsystem {

    private DigitalInput hallEffect;
    private TalonSRX elevatorTalon;
    private DoubleSolenoid tiltDoubleSol;

    private double setpoint = 0;

    public Elevator() {
        System.out.println("Elevator starting... ");
        tiltDoubleSol = new DoubleSolenoid(RobotMap.ELEVATOR_SOLENOID[0], RobotMap.ELEVATOR_SOLENOID[1]);
        hallEffect = new DigitalInput(RobotMap.HALL_EFFECT_SENSOR);
        System.out.println("Elevator done starting... ");
        elevatorTalon = new TalonSRX(RobotMap.ELEVATOR_TALON);
        configureTalon();
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void triggerPistons(boolean enable) {
        if(enable)
            tiltDoubleSol.set(DoubleSolenoid.Value.kForward);
        else
            tiltDoubleSol.set(DoubleSolenoid.Value.kOff);
    }

    public boolean isZeroed() {
        return hallEffect.get();
    }

    private void configureTalon() {
        // Choose sensor type
        elevatorTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
        elevatorTalon.setSensorPhase(true);
        elevatorTalon.setInverted(false);

        // Set relevant frame periods to be at least as fast as periodic rate
        elevatorTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.kTimeoutMs);
        elevatorTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.kTimeoutMs);

        // set the peak and nominal outputs
        elevatorTalon.configNominalOutputForward(Constants.elevatorMinPercentOut, Constants.kTimeoutMs);
        elevatorTalon.configNominalOutputReverse(-Constants.elevatorMinPercentOut, Constants.kTimeoutMs);
        elevatorTalon.configPeakOutputForward(Constants.elevatorMaxPercentOut, Constants.kTimeoutMs);
        elevatorTalon.configPeakOutputReverse(-Constants.elevatorMaxPercentOut, Constants.kTimeoutMs);

        // set closed loop gains in set slot
        elevatorTalon.selectProfileSlot(Constants.kSlotIdx, Constants.kPIDLoopIdx);
        elevatorTalon.config_kF(0, Constants.elevatorkF, Constants.kTimeoutMs);
        elevatorTalon.config_kP(0, Constants.elevatorkP, Constants.kTimeoutMs);
        elevatorTalon.config_kI(0, Constants.elevatorkI, Constants.kTimeoutMs);
        elevatorTalon.config_kD(0, Constants.elevatorkD, Constants.kTimeoutMs);
        // set acceleration and vcruise velocity - see documentation
        elevatorTalon.configMotionCruiseVelocity(Constants.elevatorMaxVelocity, Constants.kTimeoutMs);
        elevatorTalon.configMotionAcceleration(Constants.elevatorMaxAcceleration, Constants.kTimeoutMs);
        // zero the sensor
        elevatorTalon.setSelectedSensorPosition(0, Constants.kPIDLoopIdx, Constants.kTimeoutMs);
    }

    public void sendDebugInfo(){
        double pos = elevatorTalon.getSelectedSensorPosition(0);
        double vel = elevatorTalon.getSelectedSensorVelocity(0);
        double voltage = elevatorTalon.getMotorOutputVoltage();
        SmartDashboard.putNumber("Elevator Height", pos);
        SmartDashboard.putNumber("Elevator Speed", vel);
        SmartDashboard.putNumber("Elevator Voltage", voltage);
    }

    public void createSetpoint(double targetPos) {
        setpoint = targetPos;
        elevatorTalon.set(ControlMode.MotionMagic, targetPos);
    }

    public boolean isDone(){
        return (elevatorTalon.getSelectedSensorPosition(0) - setpoint ) <= Constants.kDeadband;
    }

    @Override
    public void initDefaultCommand() {
        // TODO: Set the default command, if any, for a subsystem here. Example:
        //    setDefaultCommand(new MySpecialCommand());
    }
}

