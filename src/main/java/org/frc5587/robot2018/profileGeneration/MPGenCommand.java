package org.frc5587.robot2018.profileGeneration;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class MPGenCommand extends InstantCommand{

    public MPGenCommand(){
        setRunWhenDisabled(true);
    }

    public void execute(){
        new GenerateMPs();
    }
}