package org.frc5587.robot2018.profileGeneration;

import org.frc5587.lib.Pathgen;
import org.frc5587.robot2018.Robot;

import jaci.pathfinder.*;

public class GenerateMPs{
	public GenerateMPs(){
		Pathgen p = Robot.pathgen;

		System.out.println("MP Generation Starting ...");

		p.createNew(
			"DriveStraight", 
			new Waypoint[]{
				new Waypoint(0, 0, 0),
				new Waypoint(36, 0, 0)
			}
		);

		p.createNew(
			"LeftStartLeftSwitchOutside", 
			new Waypoint[]{
				new Waypoint(39, 268, Pathfinder.d2r(0)),
				new Waypoint(144, 268, Pathfinder.d2r(0)),
				new Waypoint(180, 230, Pathfinder.d2r(-89))
			}
		);

		p.createNew(
			"LeftStartLeftSwitchInside", 
			new Waypoint[]{
				new Waypoint(39, 268, Pathfinder.d2r(0)),
				new Waypoint(120, 200, Pathfinder.d2r(0)),
				new Waypoint(140, 200, Pathfinder.d2r(0))
			}
		);

		p.createNew(
			"RightStartRightSwitchOutside", 
			new Waypoint[]{
				new Waypoint(39, 46.5, Pathfinder.d2r(0)),
				new Waypoint(144, 46.5, Pathfinder.d2r(0)),
				new Waypoint(168, 88, Pathfinder.d2r(89))
			}
		);

		p.createNew(
			"RightStartRightSwitchInside", 
			new Waypoint[]{
				new Waypoint(39, 46.5, Pathfinder.d2r(0)),
				new Waypoint(120, 114, Pathfinder.d2r(0)),
				new Waypoint(140, 114, Pathfinder.d2r(0))
			}
		);

		p.createNew(
			"LeftToLeftScale", 
			new Waypoint[]{
				new Waypoint(39, 268, 0),
				new Waypoint(300, 274, Pathfinder.d2r(0)),
				new Waypoint(324, 258, Pathfinder.d2r(-89))
			}
		);

		System.out.println("MP Generation Done");
	}
}