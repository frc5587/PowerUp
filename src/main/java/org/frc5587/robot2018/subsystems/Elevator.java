package org.frc5587.robot2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
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
        tiltDoubleSol = new DoubleSolenoid(RobotMap.Elevator.ELEVATOR_SOLENOID[0], RobotMap.Elevator.ELEVATOR_SOLENOID[1]);
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
            tiltDoubleSol.set(DoubleSolenoid.Value.kReverse);
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
        elevatorTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.Elevator.kPIDLoopIdx, Constants.Elevator.kTimeoutMs);
        elevatorTalon.setSensorPhase(true);
        elevatorTalon.setInverted(false);

        // Set relevant frame periods to be at least as fast as periodic rate
        elevatorTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.Elevator.kTimeoutMs);
        elevatorTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, Constants.Elevator.kTimeoutMs);

        // set the peak and nominal outputs
        elevatorTalon.configNominalOutputForward(Constants.Elevator.minPercentOut, Constants.Elevator.kTimeoutMs);
        elevatorTalon.configNominalOutputReverse(-Constants.Elevator.minPercentOut, Constants.Elevator.kTimeoutMs);
        elevatorTalon.configPeakOutputForward(Constants.Elevator.maxPercentFw, Constants.Elevator.kTimeoutMs);
        elevatorTalon.configPeakOutputReverse(-Constants.Elevator.maxPercentBw, Constants.Elevator.kTimeoutMs);

        // set closed loop gains in set slot
        elevatorTalon.selectProfileSlot(Constants.Elevator.kSlotIdx, Constants.Elevator.kPIDLoopIdx);
        elevatorTalon.config_kF(0, Constants.Elevator.kF, Constants.Elevator.kTimeoutMs);
        elevatorTalon.config_kP(0, Constants.Elevator.kP, Constants.Elevator.kTimeoutMs);
        elevatorTalon.config_kI(0, Constants.Elevator.kI, Constants.Elevator.kTimeoutMs);
        elevatorTalon.config_kD(0, Constants.Elevator.kD, Constants.Elevator.kTimeoutMs);
        // set acceleration and vcruise velocity - see documentation
        elevatorTalon.configMotionCruiseVelocity(Constants.Elevator.maxVelocity, Constants.Elevator.kTimeoutMs);
        elevatorTalon.configMotionAcceleration(Constants.Elevator.maxAcceleration, Constants.Elevator.kTimeoutMs);
        // zero the sensor
        elevatorTalon.setSelectedSensorPosition(0, Constants.Elevator.kPIDLoopIdx, Constants.Elevator.kTimeoutMs);
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
     * Sends information about the elevatorTalon's current status to SmartDashboard
     */
    public void sendInfo(){
        SmartDashboard.putNumber("Encoder Position Native", getEncoderPosition());
        SmartDashboard.putNumber("Encoder Velocity Native", getEncoderVelocity());
        SmartDashboard.putNumber("Elevator Height Inches", getElevatorHeightIn());
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
     * Sets the elevatorTalon to brake mode
     */
    public void stopMotor() {
        elevatorTalon.set(ControlMode.PercentOutput, 0.0);
        elevatorTalon.setNeutralMode(NeutralMode.Brake);
    }

    /**
     * Gets the current position of the elevatorTalon's encoder position
     * @return the current raw sensor units of elevatorTalon's encoder
     */
    public int getEncoderPosition() {
        return elevatorTalon.getSelectedSensorPosition(0);
    }

    /**
     * Gets the current velocity of the elevatorTalon's encoder position
     * @return the current raw sensor units of elevatorTalon's encoder per 100ms
     */
    public int getEncoderVelocity() {
        return elevatorTalon.getSelectedSensorVelocity(0);
    }

    public void resetEncoderPosition(int height) {
        elevatorTalon.setSelectedSensorPosition(height, Constants.Elevator.kPIDLoopIdx, Constants.Elevator.kTimeoutMs);
    }

    /**
     * Returns how many inches from the ground the elevator currently is
     * @return elevator's current height in inches
     */
    public float getElevatorHeightIn() {
        // 874 native encoder units per inch of movement
        return getEncoderPosition() / 874.0f;
    }

    public static int inchesToEncoder(float inches) {
        return (int)((double)inches * 874);
    }

    /**
     * Approximates whether or not the elevatorTalon is done going to a Magic Motion setpoint
     * @return the talon's current progress in tracking to a Magic Motion setpoint
     */
    public boolean isDoneMoving(){
        return (getEncoderPosition() - setpoint ) <= Constants.Elevator.kDeadband;
    }

    public void setPower(double percent){
        elevatorTalon.set(ControlMode.PercentOutput, percent);
    }

    public void stop(){
        elevatorTalon.set(ControlMode.PercentOutput, 0);
    }

    @Override
    public void initDefaultCommand() {
        // TODO: Set the default command, if any, for a subsystem here. Example:
        //    setDefaultCommand(new MySpecialCommand());
    }
}

