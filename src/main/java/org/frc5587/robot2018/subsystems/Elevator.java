package org.frc5587.robot2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.frc5587.robot2018.Constants;
import org.frc5587.robot2018.RobotMap;

/**
 * Elevator subsystem, which controls the height of the grabber and climber as
 * they are both mounted upon it, using a Talon SRX with encoder feedback and
 * optional additional motor controllers. The subsystem contains setter methods
 * for the elevator pistons and for the current height of the elevator, as well
 * as methods to print debug information and to assertain whether the elevator
 * is currently zeroed and zero it upon request. The height functions make use
 * of the enum HeightLevels, rather than raw encoder values,
 */
public class Elevator extends Subsystem {

    private DigitalInput hallEffect;
    private TalonSRX elevatorTalon;
    private VictorSPX elevatorVictorSPX;
    private DoubleSolenoid tiltDoubleSol;

    private double setpoint;
    private HeightLevels currentHeight;

    /**
     * Default constructor for the Elevator subsystem, which initiates all of the
     * objects using the ports provided as part of the RobotMap file
     */
    public Elevator() {
        // Initiate all of the parts of the elevator
        tiltDoubleSol = new DoubleSolenoid(RobotMap.Elevator.ELEVATOR_SOLENOID[0],
                RobotMap.Elevator.ELEVATOR_SOLENOID[1]);
        hallEffect = new DigitalInput(RobotMap.Elevator.HALL_EFFECT_SENSOR);
        elevatorTalon = new TalonSRX(RobotMap.Elevator.ELEVATOR_TALON);

        // Uncomment if using a second motor in a gearbox to control the elevator
        // elevatorVictorSPX = new VictorSPX(RobotMap.Elevator.ELEVATOR_VICTORSPX);
        // elevatorVictorSPX.follow(elevatorTalon);

        configureTalon();

        setpoint = getEncoderPosition();
        // Set currentHeight to height to be tracked to upon the start of operation
        currentHeight = HeightLevels.INTAKE;
    }

    /**
     * @return the currentHeight
     */
    public HeightLevels getHeightLevel() {
        return currentHeight;
    }

    /**
     * @param currentHeight the currentHeight to set
     */
    public void setCurrentHeight(HeightLevels currentHeight) {
        this.currentHeight = currentHeight;
    }

    /**
     * @param val the value to set the elevator solenoids to
     */
    public void triggerPistons(DoubleSolenoid.Value val) {
        tiltDoubleSol.set(val);
    }

    /**
     * Finds whether the elevator is currently zeroed by using a hall effect sensor
     * 
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
        elevatorTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,
                Constants.Elevator.kPIDLoopIdx, Constants.Elevator.kTimeoutMs);
        elevatorTalon.setSensorPhase(true);
        elevatorTalon.setInverted(false);
        // elevatorVictorSPX.setInverted(false);
        // Set relevant frame periods to be at least as fast as periodic rate
        elevatorTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, Constants.Elevator.kTimeoutMs);
        elevatorTalon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10,
                Constants.Elevator.kTimeoutMs);

        // set the peak and nominal outputs
        elevatorTalon.configNominalOutputForward(Constants.Elevator.minPercentOut, Constants.Elevator.kTimeoutMs);
        elevatorTalon.configNominalOutputReverse(-Constants.Elevator.minPercentOut, Constants.Elevator.kTimeoutMs);
        elevatorTalon.configPeakOutputForward(Constants.Elevator.maxPercentFw, Constants.Elevator.kTimeoutMs);
        elevatorTalon.configPeakOutputReverse(-Constants.Elevator.maxPercentBw, Constants.Elevator.kTimeoutMs);

        elevatorTalon.configPeakCurrentLimit(40, Constants.Elevator.kTimeoutMs);
        elevatorTalon.configPeakCurrentDuration(200, Constants.Elevator.kTimeoutMs);
        elevatorTalon.configContinuousCurrentLimit(35, Constants.Elevator.kTimeoutMs);

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
        // elevatorTalon.setSelectedSensorPosition(0, Constants.Elevator.kPIDLoopIdx,
        // Constants.Elevator.kTimeoutMs);
        elevatorTalon.configVoltageCompSaturation(Constants.Elevator.vCompSaturation, Constants.Elevator.kTimeoutMs);
    }

    /**
     * Sends information about the elevatorTalon's current status to SmartDashboard
     */
    public void sendDebugInfo() {
        double pos = elevatorTalon.getSelectedSensorPosition(0);
        double vel = elevatorTalon.getSelectedSensorVelocity(0);
        double voltage = elevatorTalon.getMotorOutputVoltage();
        double current = elevatorTalon.getOutputCurrent();
        SmartDashboard.putNumber("Elevator Height STU", pos);
        SmartDashboard.putNumber("Elevator Speed STU", vel);
        SmartDashboard.putNumber("Elevator Voltage", voltage);
        SmartDashboard.putNumber("Elevator Current", current);
    }

    /**
     * Puts information about the state of Motion Magic with the elevator's
     * TalonSRX to Smart Dashboard for debugging
     */
    public void sendMotionMagicDebugInfo() {
        double pos = elevatorTalon.getActiveTrajectoryPosition();
        double vel = elevatorTalon.getActiveTrajectoryVelocity();
        SmartDashboard.putNumber("MM position", pos);
        SmartDashboard.putNumber("MM velocity", vel);
    }

    /**
     * Sends information about the elevatorTalon's current status to SmartDashboard
     */
    public void sendInfo() {
        SmartDashboard.putNumber("Elevator Height Inches", getElevatorHeightIn());
    }

    /**
     * Starts Motion Magic on elevatorTalon for a given setpoint
     * 
     * @param newLevel the setpoint to use Motion Magic with
     */
    public void goToHeight(HeightLevels newLevel) {
        currentHeight = newLevel;
        setpoint = inchesToEncoder(currentHeight.getHeight());
        System.out.println(setpoint);
        elevatorTalon.set(ControlMode.MotionMagic, setpoint);
        sendMotionMagicDebugInfo();
    }

    /**
     * Sets the elevatorTalon to brake mode
     */
    public void stopMotor() {
        elevatorTalon.set(ControlMode.PercentOutput, 0.0);
        elevatorTalon.setNeutralMode(NeutralMode.Brake);
    }

    /**
     * Hold the elevator with a constant voltage. If the elevator mechanism is
     * improved, it might be worth removing this method altogether to decrease power
     * consumption by a bit.
     */
    public void holdWithVoltage() {
        elevatorTalon.set(ControlMode.PercentOutput, Constants.Elevator.holdPercent);
    }

    /**
     * Gets the current position of the elevatorTalon's encoder position
     * 
     * @return the current raw sensor units of elevatorTalon's encoder
     */
    public int getEncoderPosition() {
        return elevatorTalon.getSelectedSensorPosition(0);
    }

    /**
     * Gets the current velocity of the elevatorTalon's encoder position
     * 
     * @return the current raw sensor units of elevatorTalon's encoder per 100ms
     */
    public int getEncoderVelocity() {
        return elevatorTalon.getSelectedSensorVelocity(0);
    }

    /**
     * Reset the encoder reading on the elevator to the minimum height on the
     * elevator. It is extremely important to note that this value is not zero, but
     * the encoder ticks from the bottom of a cube that is picked up
     */
    public void resetEncoderPosition() {
        elevatorTalon.setSelectedSensorPosition(Constants.Elevator.hallHeight, Constants.Elevator.kPIDLoopIdx,
                Constants.Elevator.kTimeoutMs);
    }

    /**
     * Manually sets the enocder position of the elevator's TalonSRX to a custom
     * value
     * 
     * @param val the encoder tick value to set the encoders to
     */
    public void setEncoderPosition(int val) {
        elevatorTalon.setSelectedSensorPosition(val, Constants.Elevator.kPIDLoopIdx, Constants.Elevator.kTimeoutMs);
    }

    /**
     * Returns how many inches from the ground the elevator currently is
     * 
     * @return elevator's current height in inches
     */
    public double getElevatorHeightIn() {
        return getEncoderPosition() / (double) Constants.Elevator.stuPerInch;
    }

    /**
     * Convert from inches to the encoder ticks value using the conversion factor
     * stored in Constants.java
     * 
     * @param inches the inches to convert to
     * @return the encoder value that corresponds to the inches
     */
    public static int inchesToEncoder(double inches) {
        return (int) (inches * Constants.Elevator.stuPerInch);
    }

    /**
     * Approximates whether or not the elevatorTalon is done going to a Magic Motion
     * setpoint
     * 
     * @return the talon's current progress in tracking to a Magic Motion setpoint
     */
    public boolean isDoneMoving() {
        SmartDashboard.putNumber("Is done moving", Math.abs(getEncoderPosition() - setpoint));
        return Math.abs(getEncoderPosition() - setpoint) <= Constants.Elevator.kDeadband;
    }

    /**
     * Set the percentage of percent output for the elevator's TalonSRX
     * 
     * @param percent the percent to set the motors to
     */
    public void setPower(double percent) {
        elevatorTalon.set(ControlMode.PercentOutput, percent);
    }

    /**
     * Sets the elevator TalonSRX to neutral output
     */
    public void stop() {
        elevatorTalon.neutralOutput();
    }

    @Override
    public void initDefaultCommand() {
    }

    /**
     * HeightLevels, an enum used to associate the various height levels of the
     * elevator, as found in the Constants.java file, and standard positions to use
     * throughout code. The enum itself is used to cycle between the values as well,
     * as there is a static method to get the value that is up or down in the order
     * of HeighLevels as they are intially declared.
     */
    public enum HeightLevels {
        // The order that the values are presented is the order the bumpers will cycle
        // through
        INTAKE(Constants.Elevator.intakeHeight), CARRY(Constants.Elevator.carryHeight),
        SWITCH(Constants.Elevator.switchHeight), CLIMB(Constants.Elevator.barHeight),
        SCALE(Constants.Elevator.scaleHeight);

        private double height;

        /**
         * The standard constructor for a height level constant
         * 
         * @param height the height level, in inches, to associate with the constant
         */
        HeightLevels(double height) {
            this.height = height;
        }

        /**
         * A getter for the value of a height level
         * 
         * @return the inches from the bottom of the lift of the height level
         */
        public double getHeight() {
            return height;
        }

        /**
         * A convenience method to get the next height level according to the order in
         * which they were declared.
         */
        public static HeightLevels getNextValue(HeightLevels previousValue) {
            return getValueForStep(previousValue, 1);
        }

        /**
         * A convenience method to get the previous height level according to the order
         * in which they were declared.
         */
        public static HeightLevels getPreviousValue(HeightLevels previousValue) {
            return getValueForStep(previousValue, -1);
        }

        /**
         * A method to get a value that is a specific number away from a given height
         * value. If the step value would result in an IndexOutOfBoundsException, the
         * function returns the value passed into it
         * 
         * @param previousValue the original height value to find the step based off of
         * @param step          the number of steps to make when finding the next
         *                      desired height level
         * @return the HeightLevel respresenting the found value, or the same value as
         *         previousValue, if the step counter ran out of bounds
         */
        public static HeightLevels getValueForStep(HeightLevels previousValue, int step) {
            int i;
            HeightLevels[] allValues = HeightLevels.values();
            // Find index of previousValue to track to
            for (i = 0; i < allValues.length; i++) {
                if (allValues[i].equals(previousValue)) {
                    break;
                }
            }

            // Add the steps, and confirm that the new index is not out of bounds
            i += step; // Change to reflect next desired element in array
            if (i >= allValues.length || i < 0) {
                // Return the previousValue is out of range
                return previousValue;
            } else {
                // Otherwise return the correct value
                return allValues[i];
            }
        }
    }
}
