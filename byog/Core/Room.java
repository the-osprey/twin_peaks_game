package byog.Core;

import byog.TileEngine.TETile;

import java.io.Serializable;

public class Room implements Serializable {

    private static final long serialVersionUID = 83571587L;
    Position topLeft;
    Position topRight;
    Position bottomRight;
    Position bottomLeft;
    Position center;
    int width;
    int height;
    TETile tile;


    //create a room if you know all of the positions
    public Room(Position tl, Position tr, Position bl, Position br, int w, int h, TETile t) {
        this.topLeft = tl;
        this.topRight = tr;
        this.bottomLeft = bl;
        this.bottomRight = br;
        this.height = h;
        this.width = w;
        this.tile = t;
    }

    //create a room based on the bottom left position bl
    public Room(Position bl, int w, int h, TETile t) {
        if (w * h <= 4) {
            throw new IllegalArgumentException("Room must be bigger than 1 tile");
        }

        Position tr = new Position(bl.x + w - 1, bl.y + h - 1);
        Position br = new Position(bl.x + w - 1, bl.y);
        Position tl = new Position(bl.x, bl.y + h - 1);

        this.topLeft = tl;
        this.topRight = tr;
        this.bottomLeft = bl;
        this.bottomRight = br;
        this.width = w;
        this.height = h;
        this.tile = t;
    }

    //create a room based on the bottom right position b
    //if corner == 3, create room based on p = bottom right
    //else, create a room based on p = bottom left position
    // (if no corner value is given, use the method above)
    public Room(Position p, int w, int h, TETile t, int corner) {
        if (w * h <= 4) {
            throw new IllegalArgumentException("Room must be bigger than 1 tile");
        }
        Position br;
        Position tr;
        Position bl;
        Position tl;

        if (corner == 3) { //p = br
            br = p;
            tr = new Position(p.x, p.y + h - 1);
            bl = new Position(p.x - w + 1, p.y);
            tl = new Position(p.x - w + 1, p.y + h - 1);
        } else if (corner == 2) { //p = tr
            tr = p;
            br = new Position(p.x, p.y - h + 1);
            bl = new Position(p.x - w + 1, p.y - h + 1);
            tl = new Position(p.x - w + 1, p.y);
        } else { // p = bl
            bl = p;
            tr = new Position(bl.x + w - 1, bl.y + h - 1);
            br = new Position(bl.x + w - 1, bl.y);
            tl = new Position(bl.x, bl.y + h - 1);
        }


        this.topLeft = tl;
        this.topRight = tr;
        this.bottomLeft = bl;
        this.bottomRight = br;
        this.width = w;
        this.height = h;
        this.tile = t;
    }

    public Position center() {
        return new Position((int) Math.floor((this.bottomLeft.x + this.bottomRight.x) / 2),
                (int) Math.floor((this.bottomRight.y + this.topRight.y) / 2));
    }
}
