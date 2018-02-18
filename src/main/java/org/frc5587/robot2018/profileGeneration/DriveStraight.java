package org.frc5587.robot2018.profileGeneration;

import org.frc5587.lib.Pathgen;
import org.frc5587.robot2018.Robot;

import jaci.pathfinder.*;

public class DriveStraight{
	public DriveStraight(){
		Pathgen p = Robot.pathgen;
	    Waypoint[] points = {
	    	new Waypoint(0, 0, 0),
			new Waypoint(36, 0, Pathfinder.d2r(-45)),
			new Waypoint(72, -36, Pathfinder.d2r(-45))
	    };
	    p.createNew("DriveStraight", points);
	}
}