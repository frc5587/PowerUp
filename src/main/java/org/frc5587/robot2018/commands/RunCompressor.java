package org.frc5587.robot2018.commands;

import org.frc5587.robot2018.Robot;
import edu.wpi.first.wpilibj.command.InstantCommand;

public class RunCompressor extends InstantCommand{
    boolean on;

    public RunCompressor(boolean on){
        setRunWhenDisabled(true);
        this.on = on;
    }

    public void execute(){
        Robot.compressor.setClosedLoopControl(on);
    }
}