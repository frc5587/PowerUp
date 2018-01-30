package org.frc5587.robot2018.subsystems;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.command.Subsystem;

public class LEDControl extends Subsystem {

    private I2C arduino = new I2C(I2C.Port.kMXP, 8);

    /**
     * Sends a char representing the colour for the LEDs to turn
     * The Arduino sketch only accepts the chars for yellow, red, and blue
     * @param aColor lowercase letter representing color to change the LEDs to
     */
    public void sendColorChar(char aColor){ //Parameter Alliance Color
        byte[] color = {(byte) aColor};
        arduino.writeBulk(color);
    }

    /**
     * Sends a message to the Arduino telling it whether to turn off or on lights
     * @param turnOn boolean casted to byte that is sent to the Arduino
     */
    public void sendOnMessage(boolean turnOn) {
        arduino.writeBulk(new byte[] {toByte(turnOn)});
    }

    private byte toByte(boolean bool) {
        return (byte)(bool ? 1 : 0);
    }

    public void initDefaultCommand() { }
}

