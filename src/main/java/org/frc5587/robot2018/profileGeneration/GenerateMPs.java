package org.frc5587.robot2018.profileGeneration;

import org.frc5587.lib.Pathgen;
import org.frc5587.robot2018.Robot;

import jaci.pathfinder.*;

public class GenerateMPs{
	public GenerateMPs(){
		Pathgen p = Robot.pathgen;

		System.out.println("MP Generation Starting ...");
		
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
		p.createNew("LeftS",
		new Waypoint[]{
			new Waypoint(0, 0, 0),
			new Waypoint(36, 36, Pathfinder.d2r(70)),
			new Waypoint(60, 60, Pathfinder.d2r(0))
		}
		);

		p.createNew(
			"DriveStraight", 
			new Waypoint[]{
				new Waypoint(0, 0, 0),
				new Waypoint(36, 0, 0)
			}
		);

		p.createNew(
			"LeftStartLeftSwitch", 
			new Waypoint[]{
				new Waypoint(39, 268, Pathfinder.d2r(0)),
				new Waypoint(144, 268, Pathfinder.d2r(0)),
				new Waypoint(180, 235, Pathfinder.d2r(-89))
			}
		);

		p.createNew(
			"LeftStartLeftSwitchInside", 
			new Waypoint[]{
				new Waypoint(39, 268, Pathfinder.d2r(0)),
				new Waypoint(120, 217.25, Pathfinder.d2r(0)),
				new Waypoint(140, 217.25, Pathfinder.d2r(0))
			}
		);

		System.out.println("MP Generation Done");
	}
}