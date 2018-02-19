package org.frc5587.robot2018.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.frc5587.robot2018.RobotMap;

public class Table extends Subsystem {
    TalonSRX tableTalon;
    public void initDefaultCommand() {
        tableTalon = new TalonSRX(RobotMap.Table.tableTalon);

        // TODO: Set the default command, if any, for a subsystem here. Example:
        //    setDefaultCommand(new MySpecialCommand());
    }
    public enum EjectDirection{
         LEFT(.5),
         RIGHT(-.5),
         OFF(0);

         private double speed;

         EjectDirection(double speed){this.speed = speed;}

         public double getSpeed(){
             return speed;
         }
    }
    public void setSpeed(EjectDirection direction){
        double speed = direction.getSpeed();
        tableTalon.set(ControlMode.PercentOutput, speed);
    }
}

