package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.Serializable;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

public class Game implements Serializable, MouseListener {

    public static final int WIDTH = 70;
    public static final int HEIGHT = 55;
    private static final char[] MOVEMENTS = "wWaAsSdD".toCharArray();
    private static final long serialVersionUID = 7281996L;
    private static TERenderer ter;
    private static Random rand;
    WorldGenerator userWorld;
    WorldGenerator topWorld;
    User user;
    Enemy enemy;
    long seed = 0;
    long otherRoomSeed;
    boolean isTopRoom = false;
    boolean hasUsedStairs = false;

    //Added loading so didn't have to break destroy inputstring method, which plays old version of game
    int loads;
    boolean loading;
    private boolean isPlaying;


    public Game() {
        isPlaying = true;
        seed = seed;
        otherRoomSeed = seed + 1;
        loads = 0;
        loading = false;
        // user;
    }

    public Game(boolean isPlaying) {
        playWithKeyboard();
    }

    public Game(long seed, WorldGenerator userWorld) {
        rand = new Random(seed);
        this.userWorld = userWorld;
    }

    //@SOURCE: modified from the example SaveDemo code
    private static void saveGame(Game g) {
        File f = new File("./saves/save1.txt");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }

            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(g);
            os.close();
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    //@Source: modified from the example code from SaveDemo
    public static Game loadGame() {
        File f = new File("./saves/save1.txt");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                Game loadGame = (Game) os.readObject();
                os.close();
                return loadGame;
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }

        //@Source: modified from the example code from SaveDemo
        /* In the case no World has been saved yet, we return a new one. */
        return new Game(true);
    }

    private static String mouseHoversOver(double x, double y, TETile[][] world) {
        String tileType;
        int mouseXPos = (int) Math.floor(x); //Casting acts like floor func on double
        int mouseYPos = (int) Math.floor(y);
        if (mouseXPos >= WorldGenerator.WIDTH
                || mouseYPos >= WorldGenerator.HEIGHT) {
            return "HUD";
        } else {
            tileType = world[mouseXPos][mouseYPos].description();
            return tileType;
        }
    }

    public void setUserWorld(WorldGenerator userWorld) {
        this.userWorld = userWorld;
    }

    public void setTopWorld(WorldGenerator world) {
        this.topWorld = topWorld;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTer(TERenderer ter) {
        this.ter = ter;
    }

    public void setRand(Random rand) {
        this.rand = rand;
    }

    /**
     * The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */

    // this method runs the game using the input passed in,
    // and return a 2D tile representation of the world that would have been
    // drawn if the same inputs had been given to playWithKeyboard().
    public TETile[][] playWithInputString(String input) {
        boolean hadQuit = false;
        input = input.toLowerCase();
        // User wants to start new game; press q, l or n
        if (input.charAt(0) == 'q') {
            return null;
        }
        if (input.charAt(0) == 'n') {
            String seedAndMoves = input.substring(1);
            String[] split = seedAndMoves.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)");
            String seed1 = split[0];
            String movesAndEndKey = split[1].substring(1);
            // Generate our world and Game
            userWorld = new WorldGenerator(Long.parseLong(seed1));
            Game g = new Game();
            g.seed = Long.parseLong(seed1);
            // Create world tiles
            userWorld.createRandomRooms();
            userWorld.createRandomHalls();
            userWorld.drawHallWalls();
            // Place user tile
            rand = new Random(Long.parseLong(seed1));
            g.user = placeUser();
            g.setUserWorld(userWorld);
            userWorld.getWorld()[g.user.getX()][g.user.getY()] = g.user.getTile();
            for (int i = 0; i < movesAndEndKey.length(); i++) {
                String next = movesAndEndKey.substring(i, i + 1);
                if (next.equals("q") || next.equals("Q")) {
                    saveGame(g);
                    break;
                } else if (String.valueOf(next).equals("W") || String.valueOf(next).equals("w")
                        || String.valueOf(next).equals("A") || String.valueOf(next).equals("a")
                        || String.valueOf(next).equals("S") || String.valueOf(next).equals("s")
                        || String.valueOf(next).equals("D") || String.valueOf(next).equals("d")) {
                    movePlayer(String.valueOf(next), g);
                }
            }
            return userWorld.getWorld();
        }
        if (input.charAt(0) == 'l') {
            String moves = input.substring(1);

            Game loadGame = (Game) Game.loadGame();
            long seed2 = loadGame.seed;
            //System.out.println(seed);
            userWorld = new WorldGenerator(seed2);
            loadGame.setUserWorld(userWorld);
            userWorld.createRandomRooms();
            userWorld.createRandomHalls();
            userWorld.drawHallWalls();
            loadGame.setRand(new Random(seed2));
            userWorld.getWorld()[loadGame.user.getX()]
                    [loadGame.user.getY()] = loadGame.user.getTile();
            for (int i = 0; i < moves.length(); i++) {
                String next = moves.substring(i, i + 1);
                if (next.equals("q") || next.equals("Q")) {
                    saveGame(loadGame);
                    break;
                } else if (String.valueOf(next).equals("W") || String.valueOf(next).equals("w")
                        || String.valueOf(next).equals("A") || String.valueOf(next).equals("a")
                        || String.valueOf(next).equals("S") || String.valueOf(next).equals("s")
                        || String.valueOf(next).equals("D") || String.valueOf(next).equals("d")) {
                    movePlayer(String.valueOf(next), loadGame);
                }
            }
            return userWorld.getWorld();
        }
        return null;
    }

    /**
     * Method used for playing a fresh game.
     * The game should start from the main menu.
     */
    public void playWithKeyboard() {
        // need to read input.
        Menu newMenu = new Menu();

        //This will draw a blank screen for now.
        Game newGame = new Game();
        newMenu.runGame(newGame);
    }

    // Need to add player (aka a tile) into PLAYABLETILE area
    // and then implement movement in PLAYGAME method!
    // NOTE FROM JEN: I put playGame here because
    // it made more sense to me that the game would playing the game rather then the menu
    // Notice how I don't pass in anything
    // Instead, games now have a WorldGenerator object that can be acessed anywhere
    // and the worldGen object has the list of rooms, TETiles[][]world,
    // I set the worldGen object in generateGame in the menu class
    public void playGame(Game g) {
        isPlaying = true;
        if (!g.loading) {
            g.user = placeUser();
            g.enemy = placeEnemy();
            //System.out.println(g.user.position.x);
            //System.out.println(g.user.position.y);
            userWorld.getWorld()[g.user.getX()][g.user.getY()] = g.user.getTile();
        } else {
            userWorld.getWorld()[g.user.getX()][g.user.getY()] = g.user.getTile();
            g.loading = false;
        }

        while (isPlaying) {
            ter.renderFrame(g.userWorld.getWorld());

            // Draw HUD Mouse Hovering Output
            Font worldFont = StdDraw.getFont();
            Font aHUDFont = new Font("Garamond", Font.LAYOUT_LEFT_TO_RIGHT, 15);
            StdDraw.setFont(aHUDFont);
            StdDraw.setPenColor(Color.white);

            String aHUDTile = mouseHoversOver(StdDraw.mouseX(),
                    StdDraw.mouseY(), g.userWorld.getWorld());
            StdDraw.text(3, 56, "Tile: " + aHUDTile);
            StdDraw.show();
            StdDraw.setFont(worldFont);

            if (StdDraw.isMousePressed()) {
                clickMove();
            }

            //take in key inputs to either quit or move the player
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            if (key == 'q' || key == 'Q') {
                saveGame(g);
                isPlaying = false;
                System.exit(0);
            } else if (String.valueOf(key).equals("W") || String.valueOf(key).equals("w")
                    || String.valueOf(key).equals("A") || String.valueOf(key).equals("a")
                    || String.valueOf(key).equals("S") || String.valueOf(key).equals("s")
                    || String.valueOf(key).equals("D") || String.valueOf(key).equals("d")) {
                //(Arrays.asList(MOVEMENTS).contains(key)){
                g.movePlayer(String.valueOf(key), g);
                ter.renderFrame(g.userWorld.getWorld());
            }

            //game.update(mouseX, mouseY);

            StdDraw.clear();
            StdDraw.clear(Color.decode("#15284B"));
        }
    }

    // moves the player tile to the next position,
    // and updates the current players position to be a floor tile
    private void movePlayer(String keyPress, Game g) {
        Position prev = g.user.position;
        Position next = g.user.position;
        if (keyPress.equals("W") || keyPress.equals("w")) {
            //move up if the position to the left is a passableTile
            if (userWorld.getWorld()[g.user.getUpPosition().x][g.user.getUpPosition().y].description().equals("unlocked door")) {
                if (!g.isTopRoom) {
                    g.isTopRoom = true;
                    g.hasUsedStairs = true;
                    Menu.generateGame(g.otherRoomSeed, g);
                } else {
                    g.isTopRoom = false;
                    Menu.generateGame(g.seed, g);
                }
            }
            if (validMove(g.user.getUpPosition())) {
                next = g.user.getUpPosition();
            }
        } else if (keyPress.equals("A") || keyPress.equals("a")) {
            if (userWorld.getWorld()[g.user.getLeftPosition().x][g.user.getLeftPosition().y].description().equals("unlocked door")) {
                if (!g.isTopRoom) {
                    g.isTopRoom = true;
                    g.hasUsedStairs = true;
                    Menu.generateGame(g.otherRoomSeed, g);
                } else {
                    g.isTopRoom = false;
                    Menu.generateGame(g.seed, g);
                }
            }
            //move left if position up is a passable tile
            if (validMove(g.user.getLeftPosition())) {
                next = g.user.getLeftPosition();
            }
        } else if (keyPress.equals("S") || keyPress.equals("s")) {
            if (userWorld.getWorld()[g.user.getDownPosition().x][g.user.getDownPosition().y].description().equals("unlocked door")) {
                if (!g.isTopRoom) {
                    g.isTopRoom = true;
                    g.hasUsedStairs = true;
                    Menu.generateGame(g.otherRoomSeed, g);
                } else {
                    g.isTopRoom = false;
                    Menu.generateGame(g.seed, g);
                }
            }
            //move down
            if (validMove(g.user.getDownPosition())) {
                next = g.user.getDownPosition();
            }
        } else { //if (keyPress.equals("D") || keyPress.equals('d')){
            //move right
            if (userWorld.getWorld()[g.user.getRightPosition().x][g.user.getRightPosition().y].description().equals("unlocked door")) {
                if (!g.isTopRoom) {
                    g.hasUsedStairs = true;
                    g.isTopRoom = true;
                    Menu.generateGame(g.otherRoomSeed, g);
                } else {
                    g.isTopRoom = false;
                    Menu.generateGame(g.seed, g);
                }
            }
            if (validMove(g.user.getRightPosition())) {
                next = g.user.getRightPosition();
            }
        }
        g.user.setPosition(next);
        g.userWorld.getWorld()[prev.x][prev.y] = Tileset.FLOOR;
        g.userWorld.getWorld()[next.x][next.y] = g.user.getTile();
    }

    public boolean validMove(Position p) {
        if (userWorld.getWorld()[p.x][p.y].description().equals("unlocked door")) {
            //System.out.println("this works! yuss");
        }
        if (userWorld.getWorld().length < p.x || userWorld.getWorld()[0].length < p.y) {
            return false;
        }
        if (userWorld.getWorld()[p.x][p.y].description().equals("floor")) {
            return true;
        }
        return false;
    }

    private User placeUser() {
        ArrayList<Position> numPassTiles = userWorld.getPASSABLETILELIST();
        int size = numPassTiles.size();
        int randomTileIndex = rand.nextInt(size);
        return new User(userWorld.getPASSABLETILELIST().get(randomTileIndex));
    }

    private Enemy placeEnemy() {
        ArrayList<Position> numPassTiles = userWorld.getPASSABLETILELIST();
        int size = numPassTiles.size();
        int randomTileIndex = rand.nextInt(size);
        return new Enemy(userWorld.getPASSABLETILELIST().get(randomTileIndex));
    }

    public void clickMove() {
        if (isPlaying == false) {
            return;
        }
        int mouseXPos = (int) Math.floor(StdDraw.mouseX()); //Casting acts like floor func on double
        int mouseYPos = (int) Math.floor(StdDraw.mouseY());
        Position old = user.position;
        Position press = new Position(mouseXPos, mouseYPos);
        if (validMove(press)) {
            user.setPosition(press);
            userWorld.getWorld()[old.x][old.y] = Tileset.FLOOR;
            userWorld.getWorld()[press.x][press.y] = user.getTile();
        }

    }

    //mouseReleased happens whenever the user lets go of the mouse button
    @Override
    public void mouseReleased(MouseEvent e) {
        //when the user lets go of the button, send the mouse coordinates to the variables.
        int mouseX = e.getX();
        int mouseY = e.getY();
        if (isPlaying) {
            Position old = user.position;
            Position press = new Position(mouseX, mouseY);
            if (validMove(press)) {
                user.setPosition(press);
                userWorld.getWorld()[old.x][old.y] = Tileset.FLOOR;
                userWorld.getWorld()[press.x][press.y] = user.getTile();
            }
        }

    }

    public void mouseClicked(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        if (isPlaying) {
            Position old = user.position;
            Position press = new Position(mouseX, mouseY);
            if (validMove(press)) {
                user.setPosition(press);
                userWorld.getWorld()[old.x][old.y] = Tileset.FLOOR;
                userWorld.getWorld()[press.x][press.y] = user.getTile();
            }
        }
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        if (isPlaying) {
            Position old = user.position;
            Position press = new Position(mouseX, mouseY);
            if (validMove(press)) {
                user.setPosition(press);
                userWorld.getWorld()[old.x][old.y] = Tileset.FLOOR;
                userWorld.getWorld()[press.x][press.y] = user.getTile();
            }
        }
    }

    public void mouseExited(MouseEvent e) {

    }
}
