package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;

public class User implements Serializable {
    private static final TETile TILE = Tileset.PLAYER;
    private static final long serialVersionUID = 887583838L;
    //number of lives?
    Position position;

    public User(){
    }

    public User(Position p) {
        this.position = p;
    }

    public static TETile getTile() {
        return TILE;
    }

    public int getX() {
        return position.x;
    }

    public int getY() {
        return position.y;
    }

    public Position getLeftPosition() {
        return new Position(position.x - 1, position.y);
    }

    public Position getRightPosition() {
        return new Position(position.x + 1, position.y);
    }

    public Position getUpPosition() {
        return new Position(position.x, position.y + 1);
    }

    public Position getDownPosition() {
        return new Position(position.x, position.y - 1);
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
