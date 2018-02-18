package org.frc5587.robot2018.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class LEDControl extends Subsystem {

    private I2C arduino = new I2C(I2C.Port.kMXP, 8);

    /**
     * Sends a char representing the colour for the LEDs to turn
     * The Arduino sketch only accepts the chars for yellow, red, and blue
     * @param aColor lowercase letter representing color to change the LEDs to
     */
    public void sendColor(DriverStation.Alliance aColor) { //Parameter Alliance Color
        switch (aColor) {
        case Blue:
            arduino.writeBulk(new byte[] { (byte) 'b' });
        case Red:
            arduino.writeBulk(new byte[] { (byte) 'r' });
        }
    }

    //arduino.writeBulk();

    /**
     * Send a value of LEDControl.Color with a float representing the elevator's current height from the ground over I2C
     * to an arduino to display the elevator's height on an addressable LED strip
     * @param aColor a LedControl.Color value that the arduino will turn the LED that is tracking the elevator's height
     * @param heightInInches a float describing the current height of the elevator in inches
     */
    public void sendColorWithHeight(Color aColor, float heightInInches) {
        byte[] heightArray = toByteArray((int) heightInInches);
        byte[] combinedArray = combineArrays(new byte[] { (byte) aColor.getChar() }, heightArray);
        arduino.writeBulk(combinedArray);
    }

    public void sendHeight(int height) {
        byte[] heightArray = toByteArray(height);
        byte[] combinedArray = combineArrays(new byte[] { (byte) 'e' }, heightArray);
        arduino.writeBulk(combinedArray);
    }

    public void sendCubeStatusWithColor(char cubeStatus, Color color) {
        byte[] cubeStatusToSend = toByteArray(cubeStatus);
        byte[] combinedArray = combineArrays(cubeStatusToSend, new byte[] { (byte) color.getChar() });
        arduino.writeBulk(combinedArray);
    }

    /**
     * Combines two arrays into one, such that all of the elements of a1 come before the elements of a2
     * @param a1 an array of type byte
     * @param a2 a different array of type byte
     * @return a combined array
     */
    private static byte[] combineArrays(byte[] a1, byte[] a2) {
        byte[] result = Arrays.copyOf(a1, a1.length + a2.length);
        System.arraycopy(a2, 0, result, a1.length, a2.length);
        return result;
    }

    /**
     * Converts a float to a byte array in little-endian order
     * @param f the float to convert to a byte array
     * @return the byte array describing the float
     */
    private static byte[] toByteArray(int i) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(i);
        return buffer.array();
    }

    public void initDefaultCommand() {
    }

    /**
     * Associates Color with a character, so a color to display on LEDs can be sent
     * to an arduino over I2C
     */
    public enum Color {
        RED('r'), BLUE('b'), YELLOW('y'), GREEN('g');

        private char asChar;

        Color(char colorChar) {
            this.asChar = colorChar;
        }

        char getChar() {
            return asChar;
        }
    }
}
