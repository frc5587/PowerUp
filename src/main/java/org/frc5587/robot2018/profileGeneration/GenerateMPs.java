package org.frc5587.robot2018.profileGeneration;

import org.frc5587.lib.Pathgen;

import jaci.pathfinder.*;

public class GenerateMPs{
	public GenerateMPs(){
		Pathgen fastPath = new Pathgen(30, 0.010, 84, 80, 160);
		Pathgen medPath = new Pathgen(30, 0.010, 60, 80, 160);
		Pathgen slowPath = new Pathgen(30, 0.010, 36, 60, 120);

		System.out.println("MP Generation Starting ...");

		fastPath.createNew(
			"DriveStraight", 
			new Waypoint[]{
				new Waypoint(0, 0, 0),
				new Waypoint(120, 0, 0)
			}
		);

		fastPath.createNew(
			"LeftToLeftSwitchOutside", 
			new Waypoint[]{
				new Waypoint(39, 268, Pathfinder.d2r(0)),
				new Waypoint(144, 268, Pathfinder.d2r(0)),
				new Waypoint(180, 230, Pathfinder.d2r(-89))
			}
		);

		fastPath.createNew(
			"LeftToLeftSwitchFront", 
			new Waypoint[]{
				new Waypoint(39, 268, Pathfinder.d2r(0)),
				new Waypoint(120, 200, Pathfinder.d2r(0)),
				new Waypoint(140, 200, Pathfinder.d2r(0))
			}
		);

		fastPath.createNew(
			"RightToRightSwitchOutside", 
			new Waypoint[]{
				new Waypoint(39, 46.5, Pathfinder.d2r(0)),
				new Waypoint(144, 46.5, Pathfinder.d2r(0)),
				new Waypoint(180, 84.5, Pathfinder.d2r(89))
			}
		);

		fastPath.createNew(
			"RightToRightSwitchFront", 
			new Waypoint[]{
				new Waypoint(39, 46.5, Pathfinder.d2r(0)),
				new Waypoint(120, 114, Pathfinder.d2r(0)),
				new Waypoint(140, 114, Pathfinder.d2r(0))
			}
		);

		medPath.createNew(
			"LeftToLeftScale", 
			new Waypoint[]{
				new Waypoint(39, 268, 0),
				new Waypoint(210, 268, Pathfinder.d2r(0)),
				new Waypoint(285, 204, Pathfinder.d2r(-20))
			}
		);

		medPath.createNew(
			"RightToRightScale", 
			new Waypoint[] { 
				new Waypoint(39, 46.5, 0),
				new Waypoint(210, 46.5, Pathfinder.d2r(0)), 
				new Waypoint(285, 110.5, Pathfinder.d2r(-20))
			}
		);

		// fastPath.createNew(
		// 	"LeftToRightScale", 
		// 	new Waypoint[]{
		// 		new Waypoint(39, 268, Pathfinder.d2r(0)),

		// 	}
		// );

		fastPath.createNew(
			"LeftToLeftScaleBackwards", 
			new Waypoint[]{
				new Waypoint(0, 268, Pathfinder.d2r(180)),
				new Waypoint(171, 268, Pathfinder.d2r(180)),
				new Waypoint(270, 204, Pathfinder.d2r(200))
			}
		);

		slowPath.createNew(
			"LeftScaleBackwardsToLeftSwitchBack_Finish", 
			new Waypoint[]{
				new Waypoint(270, 204, Pathfinder.d2r(200)),
				new Waypoint(295, 204, Pathfinder.d2r(180))
			}
		);

		slowPath.createNew(
			"LeftScaleBackwardsToLeftSwitchBack", 
			new Waypoint[]{
				new Waypoint(295, 204, Pathfinder.d2r(180)),
				new Waypoint(220, 200, Pathfinder.d2r(180))
			}
		);

		medPath.createNew(
			"LeftToRightSwitchFront", 
			new Waypoint[]{
				new Waypoint(39, 268, Pathfinder.d2r(0)),
				new Waypoint(70, 200, Pathfinder.d2r(-89)),
				new Waypoint(65, 132, Pathfinder.d2r(-60)),
				new Waypoint(140, 110, Pathfinder.d2r(15))
			}
		);

		medPath.createNew(
			"CenterToLeftSwitchFront", 
			new Waypoint[]{
				new Waypoint(39, 12*11.5, Pathfinder.d2r(0)),
				new Waypoint(12*10, 12*18, Pathfinder.d2r(0)),
				new Waypoint(12*13.5, 12*18, Pathfinder.d2r(0))
			}
		);

		medPath.createNew(
			"CenterToRightSwitchFront", 
			new Waypoint[]{
				new Waypoint(39, 12*11.5, Pathfinder.d2r(0)),
				new Waypoint(12*10, 12*8.5, Pathfinder.d2r(0)),
				new Waypoint(12*13.5, 12*8.5, Pathfinder.d2r(0))
			}
		);

		System.out.println("MP Generation Done");
	}
}