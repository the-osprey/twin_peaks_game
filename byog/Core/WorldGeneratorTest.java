package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

// NOTE: This class was used in early world generation stages; it neither useful nor up to date, but
// we kept this for documentation's sake
public class WorldGeneratorTest {
    public static void main(String[] args) {

        // Initialize tile rendering engine
        TERenderer ter = new TERenderer();
        //ter.initialize(WorldGenerator.WIDTH, WorldGenerator.HEIGHT);
        ter.initialize(WorldGenerator.WIDTH, WorldGenerator.HEIGHT + 2, 0, 0);

        // Initialize tiles
        TETile[][] world = new TETile[WorldGenerator.WIDTH][WorldGenerator.HEIGHT];
        for (int x = 0; x < WorldGenerator.WIDTH; x += 1) {
            for (int y = 0; y < WorldGenerator.HEIGHT; y += 1) {
                world[x][y] = Tileset.TREE;
            }
        }
        // Add what you want to draw here:

        long seed = 514427116958644690L;
        WorldGenerator ourWorld = new WorldGenerator(seed);
        //ourWorld.initializer();
        ourWorld.createRandomRooms();
        ourWorld.createRandomHalls();
        ourWorld.drawHallWalls();

        // Draw world on screen
        ter.renderFrame(ourWorld.getWorld());
    }
}
