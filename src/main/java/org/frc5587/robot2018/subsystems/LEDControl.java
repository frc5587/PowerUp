package org.frc5587.robot2018.subsystems;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.command.Subsystem;

public class LEDControl extends Subsystem {

    private I2C arduino = new I2C(I2C.Port.kMXP, 8);

    public void sendAllianceColor(char Acolor){ //Parameter Alliance Color
        byte[] color = {(byte) Acolor};
        arduino.writeBulk(color);
        }

    public void initDefaultCommand() {
    }
}

