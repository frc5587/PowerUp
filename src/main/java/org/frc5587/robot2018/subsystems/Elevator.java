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
import org.frc5587.robot2018.commands.elevator.ElevatorToSetpoint;

public class Elevator extends Subsystem {

    private DigitalInput hallEffect;
    private TalonSRX elevatorTalon;
    private DoubleSolenoid tiltDoubleSol;

    private double setpoint;
    private HeightLevels currentHeight;

    public Elevator() {
        System.out.println("Elevator starting... ");
        tiltDoubleSol = new DoubleSolenoid(RobotMap.Elevator.ELEVATOR_SOLENOID[0], RobotMap.Elevator.ELEVATOR_SOLENOID[1]);
        hallEffect = new DigitalInput(RobotMap.Elevator.HALL_EFFECT_SENSOR);
        elevatorTalon = new TalonSRX(RobotMap.Elevator.ELEVATOR_TALON);
        configureTalon();

        setpoint = getEncoderPosition();
        // Robot starts a intake height by default, although it doesn't particularly matter in operation
        currentHeight = HeightLevels.INTAKE;
        System.out.println("Elevator done starting... ");
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public HeightLevels getHeightLevel() {
        return currentHeight;
    }

    public void setMotors(HeightLevels newLevel) {
        currentHeight = newLevel;
        currentHeight.startCommand();
    }

    public void triggerPistons(DoubleSolenoid.Value val) {
        tiltDoubleSol.set(val);
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
        //elevatorTalon.setSelectedSensorPosition(0, Constants.Elevator.kPIDLoopIdx, Constants.Elevator.kTimeoutMs);
    }

    /**
     * Sends information about the elevatorTalon's current status to SmartDashboard
     */
    public void sendDebugInfo(){
        double pos = elevatorTalon.getSelectedSensorPosition(0);
        double vel = elevatorTalon.getSelectedSensorVelocity(0);
        double voltage = elevatorTalon.getMotorOutputVoltage();
        double current = elevatorTalon.getOutputCurrent();
        SmartDashboard.putNumber("Elevator Height STU", pos);
        SmartDashboard.putNumber("Elevator Speed STU", vel);
        SmartDashboard.putNumber("Elevator Voltage", voltage);
        SmartDashboard.putNumber("Elevator Current", current);
    }

    public void sendMotionMagicDebugInfo(){
        double pos = elevatorTalon.getActiveTrajectoryPosition();
        double vel = elevatorTalon.getActiveTrajectoryVelocity();
        SmartDashboard.putNumber("MM position", pos);
        SmartDashboard.putNumber("MM velocity", vel);
    }

    /**
     * Sends information about the elevatorTalon's current status to SmartDashboard
     */
    public void sendInfo(){
        SmartDashboard.putNumber("Elevator Height Inches", getElevatorHeightIn());
    }

    /**
     * Starts Motion Magic on elevatorTalon for a given setpoint
     * @param targetPos the setpoint to use Motion Magic with in inches
     */
    public void createSetpoint(double targetPos) {
        setpoint = inchesToEncoder(targetPos);
        System.out.println(setpoint);
        elevatorTalon.set(ControlMode.MotionMagic, setpoint);
    }
    
    /**
     * Sets the elevatorTalon to brake mode
     */
    public void stopMotor() {
        elevatorTalon.set(ControlMode.PercentOutput, 0.0);
        elevatorTalon.setNeutralMode(NeutralMode.Brake);
    }

    public void holdWithVoltage(){
        elevatorTalon.set(ControlMode.PercentOutput, 0.2);
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

    public void resetEncoderPosition() {
        elevatorTalon.setSelectedSensorPosition(Constants.Elevator.hallHeight, Constants.Elevator.kPIDLoopIdx, Constants.Elevator.kTimeoutMs);
    }

    /**
     * Returns how many inches from the ground the elevator currently is
     * @return elevator's current height in inches
     */
    public double getElevatorHeightIn() {
        return getEncoderPosition() / (double)Constants.Elevator.stuPerInch;
    }

    public static int inchesToEncoder(double inches) {
        return (int)(inches * Constants.Elevator.stuPerInch);
    }

    /**
     * Approximates whether or not the elevatorTalon is done going to a Magic Motion setpoint
     * @return the talon's current progress in tracking to a Magic Motion setpoint
     */
    public boolean isDoneMoving(){
        SmartDashboard.putNumber("Is done moving", Math.abs(getEncoderPosition() - setpoint ));
        return Math.abs(getEncoderPosition() - setpoint ) <= Constants.Elevator.kDeadband;
    }

    public void setPower(double percent){
        if(percent < -1.0 || percent > 1.0) {
            throw new Error("Percentage for the elevatorTalon not within the range -1.0 < x < 1.0");
        }
        elevatorTalon.set(ControlMode.PercentOutput, percent);
    }

    public void stop(){
        elevatorTalon.set(ControlMode.PercentOutput, 0);
    }

    @Override
    public void initDefaultCommand() {
    }

    public enum HeightLevels {
        // The order that the values are presented is the order the bumpers will cycle through
        INTAKE  (Constants.Elevator.intakeHeight),
        SWITCH  (Constants.Elevator.switchHeight),
        SCALE   (Constants.Elevator.scaleHeight);
        

        private double height;
 
        HeightLevels(double height) {
            this.height = height;
        }

        public void startCommand() {
            new ElevatorToSetpoint(height).start();
        }

        public double getHeight() {
            return height;
        }

        public static HeightLevels getNextValue(HeightLevels previousValue) {
            return getValueForStep(previousValue, 1);
        }

        public static HeightLevels getPreviousValue(HeightLevels previousValue) {
            return getValueForStep(previousValue, -1);
        }

        public static HeightLevels getValueForStep(HeightLevels previousValue, int step) {
            int i;
            HeightLevels[] allValues = HeightLevels.values();
            for (i = 0; i < allValues.length; i++) {
                if (allValues[i].equals(previousValue)) {
                    break;
                }
            }

            i += step; // Change to reflect next desired element in array
            if (i >= allValues.length || i < 0) {
                return previousValue;
            } else {
                return allValues[i];
            }
        }
    }
}
