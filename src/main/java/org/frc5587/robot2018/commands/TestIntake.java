//package org.frc5587.robot2018.commands;
//
//import edu.wpi.first.wpilibj.DoubleSolenoid;
//import edu.wpi.first.wpilibj.command.Command;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import org.frc5587.robot2018.OI;
//import org.frc5587.robot2018.Robot;
//import org.frc5587.robot2018.commands.grabber.SetGrabberMotors;
//import org.frc5587.robot2018.commands.grabber.TriggerGrabberPistons;
//import org.frc5587.robot2018.subsystems.Elevator;
//import org.frc5587.robot2018.subsystems.Grabber;
//
//public class TestIntake extends Command{
//    Elevator elevator;
//    boolean pistonState = false;
//
//    public TestIntake(){
//        elevator = Robot.elevator;
//    }
//    protected void initialize(){
//
//    }
//    protected void execute(){
//        if(OI.xb.getAButtonPressed()){
//            new SetGrabberMotors(Grabber.MotorSpeed.INTAKE).start();
//        }
//        else if(OI.xb.getXButtonPressed()) {
//            new SetGrabberMotors(Grabber.MotorSpeed.EJECT).start();
//        }
//        else if(OI.xb.getAButtonReleased() || OI.xb.getXButtonReleased()) {
//            new SetGrabberMotors(Grabber.MotorSpeed.OFF).start();
//        }
//
//        if(OI.xb.getBButtonPressed()){
//            if(pistonState)
//                new TriggerGrabberPistons(DoubleSolenoid.Value.kForward).start();
//            else
//                new TriggerGrabberPistons(DoubleSolenoid.Value.kReverse).start();
//            pistonState = !pistonState;
//        }
//
//    }
//    protected boolean isFinished(){
//        return false;
//    }
//    protected void end() { }
//    protected void interrupted() {
//        end();
//	}
//}