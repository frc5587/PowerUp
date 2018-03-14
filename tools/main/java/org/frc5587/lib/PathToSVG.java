/**
 * Build: javac -cp Pathfinder-Java-1.8.jar:src/main/java tools/main/java/org/frc5587/lib/PathToSVG.java
 *   Run: java -Djava.library.path=pathfinderjava/shared/any64/ -cp Pathfinder-Java-1.8.jar:src/main/java:tools/main/java org.frc5587.lib.PathToSVG > asdf.html
 */
package org.frc5587.lib;

import org.frc5587.lib.Pathgen;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Trajectory.Segment;
import jaci.pathfinder.Waypoint;

public class PathToSVG {

  public static int BOARD_HEIGHT = 360;
  public static int BOARD_WIDTH = 648;

  public static String HTML_PROLOG =
    "<html>\n" +
    "  <head>\n" +
    "    <style type=\"text/css\">\n" +
    "      svg {\n" +
    "        width: " + BOARD_WIDTH + ";\n" +
    "        height: " + BOARD_HEIGHT + ";\n" +
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
    "    <svg>";

  public static String HTML_EPILOG =
    "    </svg>\n" +
    "  </body>\n" +
    "</html>";

  public static void drawLine(double x0, double y0, double x1, double y1) {
    drawLine(null, x0, y0, x1, y1);
  }

  public static void drawLine(String _class, double x0, double y0, double x1, double y1) {
    y0 = BOARD_HEIGHT - y0;
    y1 = BOARD_HEIGHT - y1;
    StringBuffer sb = new StringBuffer(1024);
    sb.append("      <polyline");
    if (_class != null) {
      sb.append(" class=\"" + _class + "\"");
    }
    sb.append(" points=\"" + x0 + "," + y0 + " " + x1 + "," + y1 + "\"/>");
    System.out.println(sb.toString());
  }

  public static void drawBox(double x, double y, double w, double h) {
    drawBox(null, x, y, w, h);
  }

  public static void drawBox(String _class, double x, double y, double w, double h) {
    drawLine(_class, x + 0, y + 0, x + w, y + 0);
    drawLine(_class, x + w, y + 0, x + w, y + h);
    drawLine(_class, x + w, y + h, x + 0, y + h);
    drawLine(_class, x + 0, y + h, x + 0, y + 0);
  }

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
    /* Draw game board */
    drawBox(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
    /* Draw bottom left switch */
    drawBox(140.000, 83.250, 56, 36);
    /* Draw top left switch */
    drawBox(140.000, 202.750, 56, 36);
    /* Draw bottom scale */
    drawBox(299.65, 71.57, 48, 36);
    /* Draw top scale */
    drawBox(299.65, 216.43, 48, 36);
    /* Draw main trajectory */
    for (int i = 0; i < t.segments.length - 1; i++) {
      Segment s = t.segments[i];
      Segment e = t.segments[i + 1];
      drawLine(s.x, s.y, e.x, e.y);
    }
    /* Draw trajectory left side */
    for (int i = 0; i < l.segments.length - 1; i++) {
      Segment s = l.segments[i];
      Segment e = l.segments[i + 1];
      drawLine("left", s.x, s.y, e.x, e.y);
    }
    /* Draw trajectory right side */
    for (int i = 0; i < r.segments.length - 1; i++) {
      Segment s = r.segments[i];
      Segment e = r.segments[i + 1];
      drawLine("right", s.x, s.y, e.x, e.y);
    }
    System.out.println(HTML_EPILOG);
  }

};
