package org.frc5587.robot2018.commands.drive;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.TrajectoryPoint;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.frc5587.lib.Pathgen;
import org.frc5587.robot2018.Constants;
import org.frc5587.robot2018.Robot;
import org.frc5587.robot2018.subsystems.Drive;

import edu.wpi.first.wpilibj.command.Command;

public class MotionProfileFiller extends Command{
    Drive drive;
    String profileName;
    TrajectoryPoint[][] profiles;

    public MotionProfileFiller(String profileName, boolean zeroTalonDistance){
        this.profileName = profileName;
        drive = Robot.kDrive;

        profiles = deserializeProfile(profileName, zeroTalonDistance);
    }
    
    public void initialize(){
        drive.queuePoints(profiles);
        fillBuffer(true);
    }

    public void execute(){
        drive.updateStatus();
    }

    public boolean isFinished(){
        boolean finished = drive.getStatuses()[0].isLast && drive.getStatuses()[1].isLast;
        return finished;
    }

    public static TrajectoryPoint[][] deserializeProfile(String filename, boolean zeroTalonDistance){
        Pathgen p = Robot.pathgen;
        System.out.println("Loading...");
        Trajectory unmodified = p.getTrajectoryFromFile(filename);
        Trajectory leftTrajectory = p.getLeftSide(unmodified);
        Trajectory rightTrajectory = p.getRightSide(unmodified);
        System.out.println("Trajectory Modified.");
        TrajectoryPoint[] leftTalonPoints = p.convertToTalon(
            leftTrajectory, 
            0, 
            0, 
            Constants.Drive.stuPerInch,
            zeroTalonDistance
        );
        TrajectoryPoint[] rightTalonPoints = p.convertToTalon(
            rightTrajectory, 
            0, 
            0, 
            Constants.Drive.stuPerInch,
            zeroTalonDistance
        );
        System.out.println("Converted to talon");
        debugTalonToFile(leftTalonPoints, "Left");
        debugTalonToFile(rightTalonPoints, "Right");

        return new TrajectoryPoint[][]{
            leftTalonPoints, 
            rightTalonPoints
        };
    }

    public void fillBuffer(boolean enabled){
        if(enabled){
            drive.profileNotifer.startPeriodic(.005);
        }
        else{
            drive.profileNotifer.stop();
        }
    }

	private static void debugTalonToFile(TrajectoryPoint[] t, String fileNameAppended) {
		File myFile = new File("/home/lvuser/debugTalonTrajectory_" + fileNameAppended + ".csv");
        PrintWriter writer;
        try {
            writer = new PrintWriter(myFile);
            for(TrajectoryPoint pt : t){
                writer.printf(
                    "%f,%f,%d,%b \n",
                    pt.position,
                    pt.velocity,
                    pt.profileSlotSelect0,
                    pt.isLastPoint 
                );
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}