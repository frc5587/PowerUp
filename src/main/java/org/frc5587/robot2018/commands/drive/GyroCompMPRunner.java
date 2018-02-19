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
    String name = "Unknown Name";
    double initialHeading = 0;

    public GyroCompMPRunner(String pathName){
        this(Pathgen.getTrajectoryFromFile(pathName));
        name = pathName;
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

    @Override
    public String toString(){
        return this.getClass().getName() + ": " + name;
    }

    public void initialize(){
        lEncoderFollower.configureEncoder(drive.getLeftPosition(), Constants.Drive.stuPerRev, Constants.Drive.wheelDiameter);
        rEncoderFollower.configureEncoder(drive.getRightPosition(), Constants.Drive.stuPerRev, Constants.Drive.wheelDiameter);

        lEncoderFollower.configurePIDVA(
            Constants.Drive.pathfinderPIDVALeft[0], 
            Constants.Drive.pathfinderPIDVALeft[1], 
            Constants.Drive.pathfinderPIDVALeft[2], 
            Constants.Drive.pathfinderPIDVALeft[3], 
            Constants.Drive.pathfinderPIDVALeft[4]
        );

        rEncoderFollower.configurePIDVA(
            Constants.Drive.pathfinderPIDVARight[0], 
            Constants.Drive.pathfinderPIDVARight[1], 
            Constants.Drive.pathfinderPIDVARight[2], 
            Constants.Drive.pathfinderPIDVARight[3], 
            Constants.Drive.pathfinderPIDVARight[4]
        );

        initialHeading = drive.getHeading();

        drive.enableBrakeMode(true);

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
                SmartDashboard.putNumber("Left Expected Pos", lEncoderFollower.getSegment().position * Constants.Drive.stuPerInch);
                SmartDashboard.putNumber("Left Expected Vel", lEncoderFollower.getSegment().velocity * Constants.Drive.stuPerInch / 10f);
                SmartDashboard.putNumber("Left Pos Error", lEncoderFollower.getSegment().position - drive.getLeftPosition() / (double)Constants.Drive.stuPerInch);
                SmartDashboard.putNumber("Left Vel Error", lEncoderFollower.getSegment().velocity - drive.getLeftVelocity() / (double)Constants.Drive.stuPerInch * 10);
            }
            if(!rEncoderFollower.isFinished()){
                SmartDashboard.putNumber("Right Expected Pos", rEncoderFollower.getSegment().position * Constants.Drive.stuPerInch);
                SmartDashboard.putNumber("Right Expected Vel", rEncoderFollower.getSegment().velocity * Constants.Drive.stuPerInch / 10f);
                SmartDashboard.putNumber("Right Pos Error", rEncoderFollower.getSegment().position  - drive.getRightPosition() / (double)Constants.Drive.stuPerInch);
                SmartDashboard.putNumber("Left Vel Error", rEncoderFollower.getSegment().velocity - drive.getRightVelocity() / (double)Constants.Drive.stuPerInch * 10);
            }

            double left = lEncoderFollower.calculate(drive.getLeftPosition());
            double right = rEncoderFollower.calculate(drive.getRightPosition());

            double gyroHeading = drive.getHeading();
            double desiredHeading = Pathfinder.r2d(lEncoderFollower.getHeading());
            double angleDifference = Pathfinder.boundHalfDegrees(initialHeading + desiredHeading - gyroHeading);

            SmartDashboard.putNumber("Gyro Heading", gyroHeading);
            SmartDashboard.putNumber("Desired Heading", desiredHeading);
            SmartDashboard.putNumber("Angle Difference", angleDifference);

            double turn = Constants.Drive.gyrokP * angleDifference;            

            left += turn;
            right -= turn;

            //System.out.println("Left: " + left + " Right: " + right );

            drive.vbusLR(left, right);
        }
    }
}