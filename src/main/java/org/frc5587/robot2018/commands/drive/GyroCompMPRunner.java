package org.frc5587.robot2018.commands.drive;

import org.frc5587.lib.Pathgen;
import org.frc5587.robot2018.Constants;
import org.frc5587.robot2018.Robot;
import org.frc5587.robot2018.subsystems.Drive;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;

public class GyroCompMPRunner extends Command{

    Drive drive;
    Pathgen p;
    Trajectory trajectory;
    EncoderFollower lEncoderFollower, rEncoderFollower;
    Notifier looper;
    double initialHeading = 0;

    public GyroCompMPRunner(String pathName){
        this(Pathgen.getTrajectoryFromFile(pathName));
    }

    public GyroCompMPRunner(Trajectory t){
        drive = Robot.kDrive;
        p = Robot.pathgen;
        trajectory = t;
        lEncoderFollower = new EncoderFollower(p.getLeftSide(trajectory));
        rEncoderFollower = new EncoderFollower(p.getRightSide(trajectory));
        looper = new Notifier(new ProfileLooper());
        System.out.println("Trajectory length: " + trajectory.length());
    }

    public void initialize(){
        lEncoderFollower.configureEncoder(drive.getLeftPosition(), Constants.Drive.stuPerRev, Constants.Drive.wheelDiameter);
        rEncoderFollower.configureEncoder(drive.getRightPosition(), Constants.Drive.stuPerRev, Constants.Drive.wheelDiameter);

        lEncoderFollower.configurePIDVA(
            Constants.Drive.pathfinderPIDVA[0], 
            Constants.Drive.pathfinderPIDVA[1], 
            Constants.Drive.pathfinderPIDVA[2], 
            Constants.Drive.pathfinderPIDVA[3], 
            Constants.Drive.pathfinderPIDVA[4]
        );

        rEncoderFollower.configurePIDVA(
            Constants.Drive.pathfinderPIDVA[0], 
            Constants.Drive.pathfinderPIDVA[1], 
            Constants.Drive.pathfinderPIDVA[2], 
            Constants.Drive.pathfinderPIDVA[3], 
            Constants.Drive.pathfinderPIDVA[4]
        );

        initialHeading = drive.getHeading();

        looper.startPeriodic(.01);
    }

    public void execute(){
        drive.sendDebugInfo();
    }

    public boolean isFinished(){
        return false;
    }

    public void end(){
        looper.stop();
    }

    public void interrupted(){
        end();
    }

    public class ProfileLooper implements Runnable{
        @Override
        public void run() {
            if(!lEncoderFollower.isFinished()){
                SmartDashboard.putNumber("Left Expected Pos", lEncoderFollower.getSegment().position);
                SmartDashboard.putNumber("Left Expected Vel", lEncoderFollower.getSegment().velocity);
            }
            if(!rEncoderFollower.isFinished()){
                SmartDashboard.putNumber("Right Expected Pos", rEncoderFollower.getSegment().position);
                SmartDashboard.putNumber("Right Expected Vel", rEncoderFollower.getSegment().velocity);
            }

            double left = lEncoderFollower.calculate(drive.getLeftPosition());
            double right = rEncoderFollower.calculate(drive.getRightPosition());

            double gyroHeading = drive.getHeading();
            double desiredHeading = Pathfinder.r2d(lEncoderFollower.getHeading());

            double angleDifference = Pathfinder.boundHalfDegrees(initialHeading + desiredHeading - gyroHeading);
            double turn = Constants.Drive.gyrokP * angleDifference;            

            drive.vbusLR(left + turn, right - turn);
        }
    }
}