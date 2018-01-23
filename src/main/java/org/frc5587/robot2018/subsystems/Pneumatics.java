package org.frc5587.robot2018.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.frc5587.robot2018.RobotMap;


/**
 *
 */
public class Pneumatics extends Subsystem
{
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private Compressor comp;

    public Pneumatics()
    {
        comp = new Compressor(0);
    }

    public void initDefaultCommand()
    {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }

    public void enableClosedLoop(boolean enabled)
    {
        comp.setClosedLoopControl(enabled);
    }

    public void setSolenoid(DoubleSolenoid s, Value v)
    {
        s.set(v);
    }
}
