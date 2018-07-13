package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class WorldGenerator implements Serializable {
    static final int WIDTH = 70;
    static final int HEIGHT = 55;
    private static final long serialVersionUID = 1212121212L;
    private static long SEED;
    private static Random RANDOM;
    private ArrayList<Room> ROOMLIST;
    private ArrayList<Position> PASSABLETILELIST;
    private TETile[][] world;


    public WorldGenerator(long seed) {
        // Initialize tile rendering engine

        // Initialize tiles
        world = new TETile[WorldGenerator.WIDTH][WorldGenerator.HEIGHT];
        for (int x = 0; x < WorldGenerator.WIDTH; x += 1) {
            for (int y = 0; y < WorldGenerator.HEIGHT; y += 1) {
                world[x][y] = Tileset.TREE;
            }
        }


        this.SEED = seed;
        RANDOM = new Random(SEED);
        ROOMLIST = new ArrayList<Room>();
        PASSABLETILELIST = new ArrayList<>();
    }

    //modified from the RandomWorldDemo java file
    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(3);
        switch (tileNum) {
            case 0:
                return Tileset.WALL;
            case 1:
                return Tileset.FLOWER;
            case 2:
                return Tileset.TREE;
            case 3:
                return Tileset.MOUNTAIN;
            case 4:
                return Tileset.SAND;
            default:
                return Tileset.NOTHING;
        }
    }

    public static boolean noOverLap(ArrayList<Room> rooms, Room newRoom) {
        for (Room other : rooms) {
            if (!(newRoom.bottomRight.y > other.topRight.y
                    || newRoom.topLeft.y < other.bottomRight.y
                    || newRoom.topLeft.x > other.bottomRight.x
                    || newRoom.bottomRight.x < other.topLeft.x)) {
                //System.out.println(3);
                return false;
            }
        }
        return true;
    }

    public TETile[][] getWorld() {
        return world;
    }

    public ArrayList<Position> getPASSABLETILELIST() {
        return PASSABLETILELIST;
    }

    public ArrayList<Room> getROOMLIST() {
        return ROOMLIST;
    }

    public TETile[][] initializer() {
        world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        return world;
    }

    public void drawRoom(Room r) {
        Position p = r.bottomLeft;
        int height = r.height;
        int width = r.width;
        TETile t = r.tile;

        drawRow(p.x, p.y, width, t);
        for (int i = 1; i < height - 1; i++) {
            drawMiddleRow(p.x, p.y + i, width, t);
        }
        drawRow(p.x, p.y + height - 1, width, t);
    }

    // creates a row full of tile t
    // eg draws ####
    public void drawRow(int x, int y, int width, TETile t) {
        for (int i = x; i < x + width; i++) {
            world[i][y] = t;
        }
    }

    public void drawMiddleRow(int x, int y, int width, TETile t) {
        world[x][y] = t;
        for (int i = x + 1; i < x + width - 1; i++) {
            world[i][y] = Tileset.FLOOR;
        }
        world[x + width - 1][y] = t;
    }

    public void createRandomRooms() {
        int xPos;
        int yPos;
        int height;
        int width;
        // Change for autograder
        int numRooms = RANDOM.nextInt(5) + 15;

        TETile t = Tileset.WALL;

        for (int i = 0; i < numRooms; i++) {
            //create a random room (random w, h, and bottom left position)
            height = RANDOM.nextInt(6) + 6;
            width = RANDOM.nextInt(6) + 6;
            xPos = RANDOM.nextInt(WIDTH - width);
            yPos = RANDOM.nextInt(HEIGHT - height);
            Position bl = new Position(xPos, yPos);
            Room r = new Room(bl, width, height, t);

            //check if that room overlaps with other rooms in array
            if (r.bottomRight.x > WorldGenerator.WIDTH || r.topRight.y > WorldGenerator.HEIGHT) {
                i -= 1;
                //do nothing
            } else if (noOverLap(ROOMLIST, r)) {
                drawRoom(r);
                ROOMLIST.add(r);
            } else {
                i -= 1;
            }
        }
    }

    // @SOURCE used some
    private void drawHorizontalHall(int xStart, int xEnd, int y) {
        int newX = xStart;
        for (int i = 0; i < xEnd - xStart; i++) {
            world[newX][y] = Tileset.FLOOR;
            Position pos = new Position(newX, y);
            PASSABLETILELIST.add(pos);
            newX += 1;
        }
    }

    private void drawVerticalHall(int yStart, int yEnd, int x) {
        int newY = yStart;
        for (int i = 0; i < yEnd - yStart; i++) {
            world[x][newY] = Tileset.FLOOR;
            Position pos = new Position(x, newY);
            PASSABLETILELIST.add(pos);
            newY += 1;
        }
    }

    public void createRandomHalls() {
        /* How this works:
         * 1) Iterate through each room
         * 2) Find center, draw horizontal hall of length x
         * between other room's center(no walls, just  one line--draw them later)
         * 3) Find center of next room, draw vertical wall of y distance between each room */
        for (int i = 0; i < ROOMLIST.size(); i++) {
            // Draw horizontal hall first
            int xStart = Math.min(ROOMLIST.get(i).center().x,
                    ROOMLIST.get((i + 1) % ROOMLIST.size()).center().x);
            int xEnd = Math.max(ROOMLIST.get(i).center().x,
                    ROOMLIST.get((i + 1) % ROOMLIST.size()).center().x);
            int y = ROOMLIST.get(i).center().y;
            drawHorizontalHall(xStart, xEnd, y);

            // Now draw connecting vertical hall
            int yStart = Math.min(ROOMLIST.get((i + 1) % ROOMLIST.size()).center().y,
                    ROOMLIST.get(i).center().y);
            int yEnd = Math.max(ROOMLIST.get((i + 1) % ROOMLIST.size()).center().y,
                    ROOMLIST.get(i).center().y);
            int x = ROOMLIST.get((i + 1) % ROOMLIST.size()).center().x;
            drawVerticalHall(yStart, yEnd, x);
        }
    }

    public void drawHallWalls() {
        // To simplify our hall drawing method (e.g. dealing with ugly corners),
        // We draw all walls AFTER halls without walls are generated
        for (Room r : ROOMLIST) {
            // Add rooms to passable tile list
            int startX = r.bottomLeft.x;
            //System.out.println(r.bottomLeft.x);
            int endX = r.bottomRight.x;
            int startY = r.bottomLeft.y;
            int endY = r.topLeft.y;
            for (int x = startX + 1; x < endX - 1; x++) {
                for (int y = startY + 1; y < endY - 1; y++) {
                    Position pos = new Position(x, y);
                    PASSABLETILELIST.add(pos);
                }
            }
        }
        // Now draw all the walls
        for (int x = 1; x < WIDTH - 1; x++) {
            for (int y = 1; y < HEIGHT - 1; y++) {
                // Look at all 4 adjacent tiles to current one (so all (x+-1, y+-1))
                // Then draw if adjacent tile is nothing
                // (or whichever tile we set to be nothing tile).
                int yUp = y + 1;
                int yDown = y - 1;
                int xLeft = x - 1;
                int xRight = x + 1;
                if (world[x][y].description().equals("floor")) {
                    if (world[x][yUp].description().equals("tree")) {
                        world[x][yUp] = Tileset.WALL;
                    }
                    if (world[x][yDown].description().equals("tree")) {
                        world[x][yDown] = Tileset.WALL;
                    }
                    if (world[xRight][y].description().equals("tree")) {
                        world[xRight][y] = Tileset.WALL;
                    }
                    if (world[xLeft][y].description().equals("tree")) {
                        world[xLeft][y] = Tileset.WALL;
                    }
                }
            }
        }
    }

    void placeStairs() {
        ArrayList<Position> passTiles = getPASSABLETILELIST();
        //Random random = new Random(SEED);
        //int tries = random.nextInt(passTiles.size());
        for (int i = 0; i < passTiles.size(); i++) {
            int x = passTiles.get(i).x;
            int y = passTiles.get(i).y;
            if (world[x][y].description().equals("floor") &&
                    world[x+1][y].description().equals("floor") &&
                    world[x+1][y+1].description().equals("floor") &&
                    world[x+1][y-1].description().equals("floor") &&
                    world[x-1][y].description().equals("floor") &&
                    world[x-1][y+1].description().equals("floor") &&
                    world[x-1][y-1].description().equals("floor") &&
                    world[x][y+1].description().equals("floor") &&
                    world[x-1][y+1].description().equals("floor") &&
                    world[x+1][y+1].description().equals("floor") &&
                    world[x-1][y-1].description().equals("floor") &&
                    world[x+1][y-1].description().equals("floor")) {
                world[x][y] = Tileset.UNLOCKED_DOOR;
                Position pos = new Position(x, y);
                PASSABLETILELIST.add(pos);
                break;
            }
        }
    }

//    void placeKey() {
//        ArrayList<Position> passTiles = getPASSABLETILELIST();
//        //Random random = new Random(SEED);
//        //int tries = random.nextInt(passTiles.size());
//        for (int i = 0; i < passTiles.size(); i++) {
//            int x = passTiles.get(i).x;
//            int y = passTiles.get(i).y;
//            if (world[x][y].description().equals("floor") &&
//                    world[x+1][y].description().equals("floor") &&
//                    world[x+1][y+1].description().equals("floor") &&
//                    world[x+1][y-1].description().equals("floor") &&
//                    world[x-1][y].description().equals("floor") &&
//                    world[x-1][y+1].description().equals("floor") &&
//                    world[x-1][y-1].description().equals("floor") &&
//                    world[x][y+1].description().equals("floor") &&
//                    world[x-1][y+1].description().equals("floor") &&
//                    world[x+1][y+1].description().equals("floor") &&
//                    world[x-1][y-1].description().equals("floor") &&
//                    world[x+1][y-1].description().equals("floor")) {
//                world[x][y] = Tileset.KEY;
//                Position pos = new Position(x, y);
//                PASSABLETILELIST.add(pos);
//                break;
//            }
//        }
//    }
}
