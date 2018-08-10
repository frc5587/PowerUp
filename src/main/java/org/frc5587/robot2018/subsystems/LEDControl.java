package org.frc5587.robot2018.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.command.Subsystem;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * LEDControl is the subsystem for coordinating communication with the Arduino
 * board over I2C (Inter-Integrated Circuit) in order to indirectly control the
 * RGB LED underlight and row of individually-addressable elevator RGB LEDs.
 */
public class LEDControl extends Subsystem {

    private I2C arduino = new I2C(I2C.Port.kMXP, 8);

    /**
     * Sends a char representing the colour for the LEDs to turn The Arduino sketch
     * only accepts the chars for yellow, red, and blue
     * 
     * @param aColor lowercase letter representing color to change the LEDs to
     */
    public void sendColor(DriverStation.Alliance aColor) { // Parameter Alliance Color
        byte[] colorByte;
        switch (aColor) {
        case Blue:
            colorByte = new byte[] { (byte) 'b' };
            break;
        case Red:
            colorByte = new byte[] { (byte) 'r' };
            break;
        default:
            colorByte = new byte[] { (byte) 'w' };
        }
        byte[] combinedArray = combineArrays(new byte[] { (byte) 'u' }, colorByte);
        arduino.writeBulk(combinedArray);
    }

    /**
     * Send a value of LEDControl.Color with a float representing the elevator's
     * current height from the ground over I2C to an arduino to display the
     * elevator's height on an addressable LED strip
     * 
     * @param aColor         a LedControl.Color value that the arduino will turn the
     *                       LED that is tracking the elevator's height
     * @param heightInInches a float describing the current height of the elevator
     *                       in inches
     */
    public void sendColorWithHeight(Color aColor, float heightInInches) {
        byte[] heightArray = toByteArray((int) heightInInches);
        byte[] combinedArray = combineArrays(new byte[] { (byte) aColor.getChar() }, heightArray);
        arduino.writeBulk(combinedArray);
    }

    /**
     * Send an array of bytes that describes the current height of the elevator for
     * the Arduino to use to coordinate the elevator LEDs
     * 
     * @param height the integer to send to the Arduino
     */
    public void sendHeight(int height) {
        byte[] heightArray = toByteArray(height);
        byte[] combinedArray = combineArrays(new byte[] { (byte) 'e' }, heightArray);
        arduino.writeBulk(combinedArray);
    }

    /**
     * Send a byte array that contains two characters, one decribing whether a cube
     * is currently inside the intake, and the other describing the color to set the
     * RGB LEDs to
     */
    public void sendCubeStatusWithColor(char cubeStatus, Color color) {
        byte[] cubeStatusToSend = toByteArray(cubeStatus);
        byte[] combinedArray = combineArrays(cubeStatusToSend, new byte[] { (byte) color.getChar() });
        arduino.writeBulk(combinedArray);
    }

    /**
     * Combines two arrays into one, such that all of the elements of a1 come before
     * the elements of a2
     * 
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
     * Converts a integer to a byte array in little-endian order
     * 
     * @param f the float to convert to a byte array
     * @return the byte array describing the float
     */
    private static byte[] toByteArray(int i) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(i);
        return buffer.array();
    }

    @Override
    protected void initDefaultCommand() {

    }

    /**
     * Associates Color with a character, so a color to display on LEDs can be sent
     * to an arduino over I2C
     */
    public static enum Color {
        RED('r'), BLUE('b'), YELLOW('y'), GREEN('g');

        private char asChar;

        /**
         * Default constructor for the Color enum
         * 
         * @param colorChar the character, as recognised by the Arduino, that
         *                  corresponds to a given colour
         */
        Color(char colorChar) {
            for (Color color : values()) {
                if (color.getChar() == colorChar) {
                    throw new IllegalArgumentException("The character \"" + colorChar
                            + "\" is already associated with another value in the LEDControl.Color enum. Please check the definition for the enum to resolve reptition.");
                }
            }
            this.asChar = colorChar;
        }

        /**
         * @return the color's associated character
         */
        public char getChar() {
            return asChar;
        }
    }
}
