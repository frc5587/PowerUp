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

		medPath.createNew( // To the a bit before the scale
			"LeftToLeftScale1_3", 
			new Waypoint[]{
				new Waypoint(0, 268, Pathfinder.d2r(0)), 
				new Waypoint(265, 297, Pathfinder.d2r(0)),
				new Waypoint(323.65, 258, Pathfinder.d2r(-91))
			}
		);

		slowPath.createNew( // Over the scale
			"LeftToLeftScale2_3", 
			new Waypoint[] { 
				new Waypoint(323.65, 258, Pathfinder.d2r(-91)),
				new Waypoint(323.65, 250, Pathfinder.d2r(-91))
			}
		);

		slowPath.createNew( // Back to a bit before the scale (MUST BE RUN BACKWARDS)
			"LeftToLeftScale3_3",
			new Waypoint[] {
				new Waypoint(323.65, 250, Pathfinder.d2r(-91)),
				new Waypoint(323.65, 258, Pathfinder.d2r(-91))
			}
		);
		
		medPath.createNew(
			"RightToRightScale1_3", 
			new Waypoint[] { 
				new Waypoint(39, 46.5, Pathfinder.d2r(0)),
				new Waypoint(265, 27.0, Pathfinder.d2r(0)),
				new Waypoint(323.65, 66.0, Pathfinder.d2r(-91))
			}
		);

		slowPath.createNew(
			"RightToRightScale2_3", 
			new Waypoint[] { 
				new Waypoint(323.65, 66.0, Pathfinder.d2r(-91)),
				new Waypoint(323.65, 74.0, Pathfinder.d2r(-91))
			}
		);

		slowPath.createNew(
			"RightToRightScale3_3", 
			new Waypoint[] { 
				new Waypoint(323.65, 74.0, Pathfinder.d2r(-91)),
				new Waypoint(323.65, 66.0, Pathfinder.d2r(-91))
			}
		);

		fastPath.createNew("LeftToLeftScaleBackwards", new Waypoint[] { 
			new Waypoint(0, 268, Pathfinder.d2r(0)), 
			new Waypoint(205, 278, Pathfinder.d2r(0)),
			new Waypoint(286, 264, Pathfinder.d2r(135))
		 });

		slowPath.createNew("LeftScaleBackwardsToLeftSwitchBack_Finish", new Waypoint[] {
			new Waypoint(286, 264, Pathfinder.d2r(135)), 
			new Waypoint(291, 259, Pathfinder.d2r(135))
		});

		slowPath.createNew("LeftScaleBackwardsToLeftSwitch", new Waypoint[] {
			new Waypoint(291, 259, Pathfinder.d2r(135)), 
			new Waypoint(286, 264, Pathfinder.d2r(0)),
			new Waypoint(200, 200, Pathfinder.d2r(0))
		});

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