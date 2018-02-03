package org.frc5587.lib;

import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motion.TrajectoryPoint.TrajectoryDuration;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Trajectory.Segment;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.modifiers.TankModifier;
import java.io.File;
import java.io.IOException;

public class Pathgen{
    double treadWidth, dt, vMax, aMax, jMax;
    Trajectory.Config config;
    public static final String DIRECTORY = "/home/lvuser/";


    /**
     * Use to configure the path planner
     * ALL UNITS ARE IN INCHES AND SECONDS
     * @param treadWidth Drivetrain wheel seperation (empirically
     * finding this may work better than just measuring)
     * @param dt Time between loops for profile to be generated on
     * @param vMax Max velocity of drivetrain
     * @param aMax Max acceleration of drivetrain
     * @param jMax Max jerk of drivetrain 
     */
    public Pathgen(double treadWidth, double dt, double vMax, double aMax, double jMax){
        this.treadWidth = treadWidth;
        this.dt = dt;
        this.vMax = vMax;
        this.aMax = aMax;
        this.jMax = jMax;
        this.config = new Trajectory.Config(
            Trajectory.FitMethod.HERMITE_CUBIC, 
            Trajectory.Config.SAMPLES_HIGH, this.dt,
            this.vMax, 
            this.aMax, 
            this.jMax
        );

    }

    /**
     * Generate profiles with this
     * ALL UNITS ARE IN INCHES
     * @param filename Where the file is written to
     * @param points Array of waypoints, Units are in inches and radians
     */
    public void createNew(String filename, Waypoint[] points){
        Trajectory trajectory = Pathfinder.generate(points, config);
        File myFile = new File(DIRECTORY + filename + ".csv");
        Pathfinder.writeToCSV(myFile, trajectory);
        TankModifier modifier = new TankModifier(trajectory).modify(treadWidth);
    }
    
    public Trajectory getTrajectoryFromFile(String name){
        File myFile = new File(name + ".csv");
        Trajectory trajectory = Pathfinder.readFromCSV(myFile);
        return trajectory;
    }

    /**
     * Converts a Pathfinder generated motion profile to one to send to a talon.
     * Assumes that this profile is the modified one for the respective side
     * of the drivetrain.
     * @param t The trajectory for THAT SPECIFIC SIDE of the drivetrain
     * 
     */
    public TrajectoryPoint[] convertToTalon(
        Trajectory t,
        double currentPos, 
        int pidfSlot, 
        double nativeUnitsPerInch
    ){
        int length = t.length();
        TrajectoryPoint[] outProfile = new TrajectoryPoint[length];
        for(int i = 0; i < length; i++){
            Segment s = t.get(i);
            TrajectoryPoint point = outProfile[i];
            point.timeDur = TrajectoryDuration.Trajectory_Duration_0ms;
            point.profileSlotSelect0 = pidfSlot;
            point.position = (int)((s.position + currentPos)* nativeUnitsPerInch);
            point.velocity = (int)(s.velocity * nativeUnitsPerInch / 10);
            point.zeroPos = i == 0;
            point.isLastPoint = (i+1) == length;
        }
        return outProfile;
    }
}