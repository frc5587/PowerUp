package org.frc5587.lib;

import org.frc5587.lib.Pathgen;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Trajectory.Segment;
import jaci.pathfinder.Waypoint;

public class PathToSVG {

  public static String HTML_PROLOG =
    "<html>\n" +
    "  <head>\n" +
    "    <style type=\"text/css\">\n" +
    "      svg {\n" +
    "        width: 1200;\n" +
    "        height: 1200;\n" +
    "      }\n" +
    "      polyline {\n" +
    "        stroke: grey;\n" +
    "        stroke-width: 1;\n" +
    "      }\n" +
    "      polyline.left {\n" +
    "        stroke: red;\n" +
    "      }\n" +
    "      polyline.right {\n" +
    "        stroke: blue;\n" +
    "      }\n" +
    "    </style>\n" +
    "  </head>\n" +
    "  <body>\n" +
    "    <svg>\n" +
    /* bottom left switch */
    "      <polyline points=\"140.000,83.250 196.000,83.250\"/>\n" +
    "      <polyline points=\"196.000,83.250 196.000,119.250\"/>\n" +
    "      <polyline points=\"196.000,119.250 140.000,119.250\"/>\n" +
    "      <polyline points=\"140.000,119.250 140.000,83.250\"/>\n" +
    /* top left switch */
    "      <polyline points=\"140.000,238.750 196.000,238.750\"/>\n" +
    "      <polyline points=\"196.000,238.750 196.000,202.750\"/>\n" +
    "      <polyline points=\"196.000,202.750 140.000,202.750\"/>\n" +
    "      <polyline points=\"140.000,202.750 140.000,238.750\"/>\n" +
    /* bottom scale */
    "      <polyline points=\"299.65,71.57 347.65,71.57\"/>\n" +
    "      <polyline points=\"347.65,71.57 347.65,107.57\"/>\n" +
    "      <polyline points=\"347.65,107.57 299.65,107.57\"/>\n" +
    "      <polyline points=\"299.65,107.57 299.65,71.57\"/>\n" +
    /* top scale */
    "      <polyline points=\"299.65,252.43 347.65,252.43\"/>\n" +
    "      <polyline points=\"347.65,252.43 347.65,216.43\"/>\n" +
    "      <polyline points=\"347.65,216.43 299.65,216.43\"/>\n" +
    "      <polyline points=\"299.65,216.43 299.65,252.43\"/>";

  public static String HTML_EPILOG =
    "    </svg>\n" +
    "  </body>\n" +
    "</html>";

  public static void main(String[] args) {
    Pathgen fastPath = new Pathgen(30, 0.010, 84, 80, 160);
    Trajectory t = fastPath.createTrajectory(new Waypoint[] {
      new Waypoint(39, 268, Pathfinder.d2r(0)),
      new Waypoint(144, 268, Pathfinder.d2r(0)),
      new Waypoint(180, 230, Pathfinder.d2r(-89))
    });
    Trajectory l = fastPath.getLeftSide(t);
    Trajectory r = fastPath.getRightSide(t);
    System.out.println(HTML_PROLOG);
    /* Draw main trajectory */
    for (int i = 0; i < t.segments.length - 1; i++) {
      Segment s = t.segments[i];
      Segment e = t.segments[i + 1];
      System.out.println("      <polyline points=\"" + s.x + "," + s.y + " " + e.x + "," + e.y + "\"/>");
    }
    /* Draw trajectory left side */
    for (int i = 0; i < l.segments.length - 1; i++) {
      Segment s = l.segments[i];
      Segment e = l.segments[i + 1];
      System.out.println("      <polyline class=\"left\" points=\"" + s.x + "," + s.y + " " + e.x + "," + e.y + "\"/>");
    }
    /* Draw trajectory right side */
    for (int i = 0; i < r.segments.length - 1; i++) {
      Segment s = r.segments[i];
      Segment e = r.segments[i + 1];
      System.out.println("      <polyline class=\"right\" points=\"" + s.x + "," + s.y + " " + e.x + "," + e.y + "\"/>");
    }
    System.out.println(HTML_EPILOG);
  }

};
