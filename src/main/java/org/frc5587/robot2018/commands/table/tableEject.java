package org.frc5587.robot2018.commands.table;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Command;
import org.frc5587.robot2018.OI;
import org.frc5587.robot2018.Robot;
import org.frc5587.robot2018.subsystems.Table;

public class tableEject extends Command{
    Table table;
    @Override
    protected void initialize() {
        table = Robot.table;
    }

    @Override
    protected void execute() {
        if(OI.xb.getBButton()){
            table.setSpeed(Table.EjectDirection.RIGHT);
        }else if(OI.xb.getXButton()) {
            table.setSpeed(Table.EjectDirection.RIGHT);
        }else{
            table.setSpeed(Table.EjectDirection.OFF);
        }
    }

    @Override
    protected boolean isFinished() {
        // TODO: Make this return true when this Command no longer needs to run execute()
        return false;
    }

}