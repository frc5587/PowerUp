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
        // tiltDoubleSol = new DoubleSolenoid(RobotMap.Elevator.ELEVATOR_SOLENOID[0], RobotMap.Elevator.ELEVATOR_SOLENOID[1]);
        hallEffect = new DigitalInput(RobotMap.Elevator.HALL_EFFECT_SENSOR);
        System.out.println("Elevator done starting... ");
        elevatorTalon = new TalonSRX(RobotMap.Elevator.ELEVATOR_TALON);
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

    /**
     * Finds whether the elevator is currently zeroed by using a hall effect sensor
     * @return whether the elevator is at the "zero" position
     */
    public boolean isZeroed() {
        return !hallEffect.get();
    }

    /**
     * Configures the elevatorTalon (no parameters)
     */
    private void configureTalon() {
        // Choose sensor type
        elevatorTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.ElevatorTalon.kPIDLoopIdx, Constants.ElevatorTalon.kTimeoutMs);
        elevatorTalon.setSensorPhase(true);
        elevatorTalon.setInverted(false);

        // Set relevant frame periods to be at least as fast as periodic rate
        elevatorTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.ElevatorTalon.kTimeoutMs);
        elevatorTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.ElevatorTalon.kTimeoutMs);

        // set the peak and nominal outputs
        elevatorTalon.configNominalOutputForward(Constants.ElevatorTalon.minPercentOut, Constants.ElevatorTalon.kTimeoutMs);
        elevatorTalon.configNominalOutputReverse(-Constants.ElevatorTalon.minPercentOut, Constants.ElevatorTalon.kTimeoutMs);
        elevatorTalon.configPeakOutputForward(Constants.ElevatorTalon.maxPercentOut, Constants.ElevatorTalon.kTimeoutMs);
        elevatorTalon.configPeakOutputReverse(-Constants.ElevatorTalon.maxPercentOut, Constants.ElevatorTalon.kTimeoutMs);

        // set closed loop gains in set slot
        elevatorTalon.selectProfileSlot(Constants.ElevatorTalon.kSlotIdx, Constants.ElevatorTalon.kPIDLoopIdx);
        elevatorTalon.config_kF(0, Constants.ElevatorTalon.kF, Constants.ElevatorTalon.kTimeoutMs);
        elevatorTalon.config_kP(0, Constants.ElevatorTalon.kP, Constants.ElevatorTalon.kTimeoutMs);
        elevatorTalon.config_kI(0, Constants.ElevatorTalon.kI, Constants.ElevatorTalon.kTimeoutMs);
        elevatorTalon.config_kD(0, Constants.ElevatorTalon.kD, Constants.ElevatorTalon.kTimeoutMs);
        // set acceleration and vcruise velocity - see documentation
        elevatorTalon.configMotionCruiseVelocity(Constants.ElevatorTalon.maxVelocity, Constants.ElevatorTalon.kTimeoutMs);
        elevatorTalon.configMotionAcceleration(Constants.ElevatorTalon.maxAcceleration, Constants.ElevatorTalon.kTimeoutMs);
        // zero the sensor
        elevatorTalon.setSelectedSensorPosition(0, Constants.ElevatorTalon.kPIDLoopIdx, Constants.ElevatorTalon.kTimeoutMs);
    }

    /**
     * Sends information about the elevatorTalon's current status to SmartDashboard
     */
    public void sendDebugInfo(){
        double pos = elevatorTalon.getSelectedSensorPosition(0);
        double vel = elevatorTalon.getSelectedSensorVelocity(0);
        double voltage = elevatorTalon.getMotorOutputVoltage();
        SmartDashboard.putNumber("Elevator Height", pos);
        SmartDashboard.putNumber("Elevator Speed", vel);
        SmartDashboard.putNumber("Elevator Voltage", voltage);
    }

    /**
     * Starts Motion Magic on elevatorTalon for a given setpoint
     * @param targetPos the setpoint to use Motion Magic with
     */
    public void createSetpoint(double targetPos) {
        setpoint = targetPos;
        elevatorTalon.set(ControlMode.MotionMagic, targetPos);
    }

    /**
     * Gets the current position of the elevatorTalon's encoder position
     * @return the current raw sensor units of elevatorTalon's encoder
     */
    public int getEncoderPosition() {
        return elevatorTalon.getSelectedSensorPosition(0);
    }

    public void resetEncoderPosition() {
        elevatorTalon.setSelectedSensorPosition(496, Constants.ElevatorTalon.kPIDLoopIdx, Constants.ElevatorTalon.kTimeoutMs);
    }

    /**
     * Returns how many inches from the ground the elevator currently is
     * @return elevator's current height in inches
     */
    public float getElevatorHeightIn() {
        // 874 native encoder units per inch of movement
        return getEncoderPosition() / 874.0f;
    }

    /**
     * Approximates whether or not the elevatorTalon is done going to a Magic Motion setpoint
     * @return the talon's current progress in tracking to a Magic Motion setpoint
     */
    public boolean isDoneMoving(){
        return (getEncoderPosition() - setpoint ) <= Constants.ElevatorTalon.kDeadband;
    }

    @Override
    public void initDefaultCommand() {
        // TODO: Set the default command, if any, for a subsystem here. Example:
        //    setDefaultCommand(new MySpecialCommand());
    }
}

