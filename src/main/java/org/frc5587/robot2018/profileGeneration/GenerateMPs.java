package org.frc5587.robot2018.profileGeneration;

import org.frc5587.lib.Pathgen;
import org.frc5587.robot2018.Robot;

import jaci.pathfinder.*;

public class GenerateMPs{
	public GenerateMPs(){
		Pathgen p = Robot.pathgen;
		p.createNew(
			"TurnLeft", 
			new Waypoint[]{
				new Waypoint(0, 0, 0),
				new Waypoint(36, 12, Pathfinder.d2r(45))
			}
		);

		p.createNew(
			"TurnRight", 
			new Waypoint[]{
				new Waypoint(0, 0, 0),
				new Waypoint(36, -12, Pathfinder.d2r(-45))
			}
		);

	}
}