package byog.Core;

import byog.TileEngine.TETile;

public class Hall extends Room {
    String type;
    //type


    public Hall(Position p, int w, int h, TETile t, int corner, String type) {
        super(p, w, h, t, corner);
        this.type = type;
    }

    public boolean isVert() {
        if (type.contains("v")) {
            return true;
        }
        return false;
    }

    public boolean isHor() {
        if (type.contains("h")) {
            return true;
        }
        return false;
    }
}
