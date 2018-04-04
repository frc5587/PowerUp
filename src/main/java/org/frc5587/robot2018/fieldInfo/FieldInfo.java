package org.frc5587.robot2018.fieldInfo;

import edu.wpi.first.wpilibj.DriverStation;

/**
 * FieldInfo2
 */
public class FieldInfo {
    private static String fmsString;

    public static enum FieldObjects {
        CLOSE_SWITCH, SCALE, FAR_SWITCH;

        FieldObject object;

        FieldObjects() {
            this.object = new FieldObject();
        }

        private FieldObject getObject() {
            return object;
        }
    }

    public static enum OwnedSide {
        LEFT, RIGHT, UNKNOWN;
    }

    public static OwnedSide getOwnedSide(FieldObjects f) {
        return f.getObject().getOwnedSide();
    }

    /**
     * @return the fmsString
     */
    public static String getFmsString() {
        return fmsString;
    }

    public static void updateData() {
        fmsString = DriverStation.getInstance().getGameSpecificMessage();
        parseFMSString();
    }

    private static void parseFMSString() {
        FieldObjects[] objects = FieldObjects.values();
        try {
            for (int i = 0; i < fmsString.length(); i++) {
                // fmsString is definitely not null if inside for loop
                char c = fmsString.charAt(i);

                // Default to UNKNOWN, otherwise assign based on character
                OwnedSide ownedSide = OwnedSide.UNKNOWN;
                if (c == 'L' || c == 'l') {
                    ownedSide = OwnedSide.LEFT;
                } else if (c == 'R' || c == 'r') {
                    ownedSide = OwnedSide.RIGHT;
                }

                // Assign corresponding object from objects
                objects[i].getObject().setOwnedSide(ownedSide);
            }
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException(String.format("fmsString is %d long, while there are %d FieldObjects", fmsString.length(), objects.length));
        } catch (NullPointerException e) {
            // fmsString is the only value that can result in a null value
            System.out.println("fmsString was null, so setting everything to UNKOWN for now");
            for(FieldObjects o : objects) {
                o.getObject().setOwnedSide(OwnedSide.UNKNOWN);
            }
            return;
        }
    }

    /**
    * FieldObject
    */
    private static class FieldObject {
        private OwnedSide ownedSide;

        /**
         * The side of the field object that is designated
         * for the alliance of the Driver Station
         */
        private FieldObject(OwnedSide ownedSide) {
            this.ownedSide = ownedSide;
        }

        private FieldObject() {
            this(OwnedSide.UNKNOWN);
        }

        /**
         * @return the ownedSide
         */
        private OwnedSide getOwnedSide() {
            return ownedSide;
        }

        /**
         * @param ownedSide the ownedSide to set
         */
        private void setOwnedSide(OwnedSide ownedSide) {
            this.ownedSide = ownedSide;
        }
    }
}