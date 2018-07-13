// Initial psudocode, so there's definitely some java that needs to be fixed!
package byog.Core;

import byog.TileEngine.TERenderer;
import edu.princeton.cs.introcs.StdAudio;
import edu.princeton.cs.introcs.StdDraw;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;
import java.util.Random;

/*
 * The menu class handles the initial selections the user makes when they open the game.
 * It runs the graphics and handles the inputs our program needs to create our world,
 * such as the seed number. It also allows for loading saved games. It handles both
 * mouse clicks and simple keyboard buttons.
 */
public class Menu implements Serializable {
    private static final long serialVersionUID = 8591724812274129L;
    static boolean gameRunning;
    private static int width = 1200;
    private static int height = 576;
    private static int midWidth = width / 2;
    private static int midHeight = height / 2;


    Menu() {
        // Create instance of main menu
        StdDraw.setCanvasSize(width, height);
        Font font = new Font("Garamond", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, width);
        StdDraw.setYscale(0, height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }

    // CALL THIS METHOD TO RUN MENU AND GAME!
    public static void runGame(Game g) {
        gameRunning = true;
        drawMainMenu();
        menuUserSelection(g);
    }
    ///NOTE FROM JEN: I modified the below methods to take in a Game g
    // that way I could set and store variables like the world in Game g

    // Draw the first menu that users see
    private static void drawMainMenu() {
        playSound("laura_theme.au");
//        StdAudio.play("/resources/laura_theme.au");
        StdDraw.clear(Color.decode("#15284B"));
        StdDraw.picture(midWidth, midHeight, "/resources/intro.png");
        Font headerFont = new Font("URW Gothic L", Font.BOLD, 40);
        Font subHeaderFont = new Font("URW Gothic L", Font.BOLD, 25);
        StdDraw.setFont(headerFont);
        StdDraw.setPenColor(71,165,78);
        StdDraw.text(midWidth, midHeight + 200, "Escape from ONE EYED JACK'S"); //        StdDraw.text(midWidth, midHeight + 120, "Escape from ONE EYED JACK'S");
        StdDraw.setFont(subHeaderFont);
        StdDraw.setPenRadius(.008);
        StdDraw.filledRectangle(midWidth, midHeight + 64, midWidth/6, 28);
        StdDraw.filledRectangle(midWidth, midHeight + 64 - 28*2 - 12, midWidth/6, 28);
        StdDraw.filledRectangle(midWidth, midHeight + 64 - 28*4 - 24, midWidth/6, 28);
        StdDraw.filledRectangle(midWidth, midHeight + 64 - 28*6 - 36, midWidth/6, 28);

        StdDraw.setPenColor(249,233,233);
        StdDraw.text(midWidth, midHeight + 64, "New Game (N)");
        StdDraw.text(midWidth, midHeight + 64 - 28*2 - 12, "Quit Game (Q)");
        StdDraw.text(midWidth, midHeight + 64 - 28*4 - 24, "Load Game (L)");
        StdDraw.text(midWidth, midHeight + 64 - 28*6 - 36, "Credits");


        // Old menu sizes saved for reference
//        StdDraw.rectangle(midWidth, midHeight - 16, midWidth/2, 28);
//        StdDraw.rectangle(midWidth, midHeight - 5.5 * 16, midWidth/2, 28);
//        StdDraw.rectangle(midWidth, midHeight - 10 * 16, midWidth/2, 28);
//        StdDraw.text(midWidth, midHeight - 16, "New Game (N)");
//        StdDraw.text(midWidth, midHeight - 5.5 * 16, "Quit Game (Q)");
//        StdDraw.text(midWidth, midHeight - 10 * 16, "Load Game (L)");

        StdDraw.show();

    }

    // When the user clicks around.
    private static void clickMenu(Game g){
        if (StdDraw.mouseX() < 600 + 150 && StdDraw.mouseX() > 600 - 150){
            if (StdDraw.mouseY() > midHeight + 64 - 28 && StdDraw.mouseY() < midHeight + 64 + 28){
                drawSeedMenu(g);
            }else  if (StdDraw.mouseY() > midHeight + 64 - 28*2 - 12 - 28 && StdDraw.mouseY() < midHeight + 64 - 28*2 - 12 + 28){
                System.exit(0);
            }else  if (StdDraw.mouseY() > midHeight + 64 - 28*4 - 24-28 && StdDraw.mouseY() < midHeight + 64 - 28*4 - 24 + 28){
                Game loadGame = (Game) Game.loadGame();
                long seed = loadGame.seed;
                loadGame.loads += 1;
                loadGame.loading = true;
                Menu.generateGame(loadGame.seed, loadGame);
            }
        }
    }

    // Method to read what user inputs in main menu (and check if it's valid)
    private static void menuUserSelection(Game g) {
        boolean hasntPressedOption = true;

        while (hasntPressedOption) {
            if (StdDraw.isMousePressed()){
                clickMenu(g);
            }

            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            //System.out.print(key);
            if (key == 'n' || key == 'N') {
                drawSeedMenu(g);
                hasntPressedOption = false;
            }
            if (key == 'q' || key == 'Q') {
                System.exit(0);
            }
            if (key == 'l' || key == 'L') {
                Game loadGame = (Game) Game.loadGame();
                if (loadGame.isTopRoom) {
                    loadGame.loads += 1;
                    loadGame.loading = true;
                    Menu.generateGame(loadGame.otherRoomSeed, loadGame);
                } else {
                    //System.out.print(seed);
                    loadGame.loads += 1;
                    loadGame.loading = true;
                    //System.out.println(loadGame.loads);
                    Menu.generateGame(loadGame.seed, loadGame);
                }
            }
        }
    }

    // If S is pressed, we go to the Seed sub-menu
    private static void drawSeedMenu(Game g) {
        // Clear frame to draw menu (quirk of StdDraw)
        StdDraw.clear();
        StdDraw.clear(Color.decode("#15284B"));
        StdDraw.picture(midWidth, midHeight, "/resources/newgame.png");

        Font headerFont = new Font("URW Gothic L", Font.BOLD, 40);
        Font subHeaderFont = new Font("URW Gothic L", Font.BOLD, 25);
        StdDraw.setFont(headerFont);
        StdDraw.setPenColor(71,165,78);
        StdDraw.text(midWidth, midHeight + 200, "Type in your seed (an integer):");

        StdDraw.filledRectangle(midWidth, midHeight + 80, midWidth/4, 28);
        StdDraw.filledRectangle(midWidth, midHeight - 10, midWidth/4, 28);

        StdDraw.setPenColor(249,233,233);
        StdDraw.setFont(subHeaderFont);
        StdDraw.text(midWidth, midHeight + 80, "Your seed: ");
        StdDraw.text(midWidth, midHeight - 10, "Press 'S' to start");


        StdDraw.show();

        seedMenuSelection(g);
    }

    // Read user inputs for seed menu
    private static void seedMenuSelection(Game g) {
        String input = "";
        boolean hasntPressedS = true;
        while (hasntPressedS) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            char key = StdDraw.nextKeyTyped();
            if ((key == 's' || key == 'S') && !input.equals("")) {
                // add take in input
                g.seed = Long.parseLong(input);
                g.otherRoomSeed = Long.parseLong(input) + 1;
                generateGame(Long.parseLong(input), g);
            }
            if (Character.isDigit(key)) {
                input += key;

                // Clunky because I repeat code, but easiest way to do it :(
                StdDraw.clear();
                StdDraw.clear(Color.decode("#15284B"));

                Font headerFont = new Font("URW Gothic L", Font.BOLD, 40);
                Font subHeaderFont = new Font("URW Gothic L", Font.BOLD, 25);
                StdDraw.picture(midWidth, midHeight, "/resources/newgame.png");
                StdDraw.setFont(headerFont);
                StdDraw.setPenColor(71,165,78);
                StdDraw.text(midWidth, midHeight + 200, "Type in your seed (an integer):");
                StdDraw.setFont(subHeaderFont);

                StdDraw.filledRectangle(midWidth, midHeight + 80, midWidth/4, 28);
                StdDraw.filledRectangle(midWidth, midHeight - 10, midWidth/4, 28);

                StdDraw.setPenColor(249,233,233);
                StdDraw.text(midWidth, midHeight + 80, "Your seed: "  + input);
                StdDraw.text(midWidth, midHeight - 10, "Press 'S' to start");


                StdDraw.show();
            }
        }
    }

    //added this to test world generation
    //we should think about where to implement this? Idk if in this class or world gen
    static void generateGame(long seed, Game g) {
        // Add what you want to draw here:
//        StdAudio.close();
//        StdAudio.play("/laura_theme.wav");

        StdDraw.clear();
        TERenderer ter = new TERenderer();
        ter.initialize(WorldGenerator.WIDTH, WorldGenerator.HEIGHT + 2, 0, 0);

        WorldGenerator ourWorld = new WorldGenerator(seed);
        g.setUserWorld(ourWorld);
        g.setTer(ter);
        //ourWorld.initializer();
        ourWorld.createRandomRooms();
        ourWorld.createRandomHalls();
        ourWorld.drawHallWalls();
        ourWorld.placeStairs();
//        ourWorld.placeKey();
        ter.renderFrame(ourWorld.getWorld());

        g.setRand(new Random(seed));
        // Draw world on screen
        g.playGame(g);
        //return(ourWorld.getWorld());
    }

    public static synchronized void playSound(final String url) {
        new Thread(new Runnable() {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                            Main.class.getResourceAsStream("/resources/" + url));
                    clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }
}
