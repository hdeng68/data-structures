package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;
import java.util.Stack;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

import java.awt.Font;
import java.awt.Color;
import java.util.Random;


public class Game {
    TERenderer ter = new TERenderer();
    Random random = new Random();
    public static final int WIDTH = 60;
    public static final int HEIGHT = 29;
    protected SproulSpace saved;
    protected String seedString = "";

    //@source: https://www.geeksforgeeks.org/serialization-in-java/
    public void deserialize() {
        try {
            saved = null;
            // Reading the object from a file
            FileInputStream file = new FileInputStream(SproulSpace.filename);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            saved = (SproulSpace) in.readObject();

            in.close();
            file.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        } catch (ClassNotFoundException ex) {
            System.out.println("SproulSpace class not found");
            ex.printStackTrace();
            return;
        }
    }

    public void drawStartFrame() {
        StdDraw.clear();
        StdDraw.clear(Color.darkGray);
        Font titleFont = new Font("Monaco", Font.BOLD, 60);
        StdDraw.setFont(titleFont);
        StdDraw.setPenColor(Color.GREEN);
        StdDraw.text(WIDTH / 2.0, 23, "ESCAPE SPROUL!");
        Font verysmolFont = new Font("Times New Roman", Font.PLAIN, 18);
        StdDraw.setFont(verysmolFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2.0, 19, "Retrieve the key to success from Josh Hug,");
        StdDraw.text(WIDTH / 2.0, 17.5, "get 3 internship offers from clubs that are flyering "
                + "(beware, they can lower morale instead!!),");
        StdDraw.text(WIDTH / 2.0, 16, "keep morale greater than 0 by petting puppers "
                + "(+1 morale per pupper),");
        StdDraw.text(WIDTH / 2.0, 14.5, "and make it out of Sproul through Sather Gate!");
        Font smolFont = new Font("Comic Sans", Font.BOLD, 24);
        StdDraw.setFont(smolFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2.0, 9, "New Game (N)");
        StdDraw.text(WIDTH / 2.0, 7.5, "Load Saved Game (L)");
        StdDraw.text(WIDTH / 2.0, 6, "Quit (Q)");
        StdDraw.picture(10, 7, "sproulplaza.jpg", 15, 10, 5.0);
        StdDraw.picture(WIDTH - 10, 7, "sproulplaza.jpg", 15, 10, -5.0);
        StdDraw.show();
    }

    public void drawSeedPrompt(String s) {
        StdDraw.clear();
        StdDraw.clear(Color.darkGray);

        Font smolFont = new Font("Monaco", Font.BOLD, 24);
        StdDraw.setFont(smolFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2, 18, "Enter any number!");
        Font verysmolFont = new Font("Monaco", Font.PLAIN, 18);
        StdDraw.setFont(verysmolFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2, 15, "(Press 'S' when finished)");
        StdDraw.setFont(smolFont);
        StdDraw.setPenColor(Color.green);
        StdDraw.text(WIDTH / 2, 12, s);

        StdDraw.show();
    }

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        ter.initialize(WIDTH, HEIGHT);
        drawStartFrame();
        while (!StdDraw.hasNextKeyTyped()) {
            continue;
        }
        char key = StdDraw.nextKeyTyped();
        if (key == 'N' || key == 'n') {
            drawSeedPrompt(seedString);
            Long seed = solicitSeedInput();
            ter.initialize(Game.WIDTH, Game.HEIGHT, 0, -2);

            SproulSpace.random = new Random(seed);
            Room.random = new Random(seed);
            random = new Random(seed);
            SproulSpace s = new SproulSpace();

            TETile[][] worldState = buildGame(s);
            s.drawGeneralFrame(worldState);
            s.solicitMoveInput(s, worldState);
        }
        if (key == 'L' || key == 'l') {
            deserialize();
            ter.initialize(Game.WIDTH, Game.HEIGHT, 0, -2);
            SproulSpace old = saved;
            TETile[][] worldState = old.sproul;
            old.drawGeneralFrame(worldState);
            old.solicitMoveInput(old, worldState);
        }
        if (key == 'Q' || key == 'q') {
            System.exit(0);
        }

    }

    public Long solicitSeedInput() {
        Long seed;
        Stack<Character> seedStack = new Stack<>();

        while (!StdDraw.hasNextKeyTyped()) {
            drawSeedPrompt(seedString);
        }
        char key = StdDraw.nextKeyTyped();
        while (key != 's' && key != 'S') {
            seedStack.push(key);
            seedString += Character.toString(key);
            while (!StdDraw.hasNextKeyTyped()) {
                drawSeedPrompt(seedString);
            }
            key = StdDraw.nextKeyTyped();
        }

        StringBuilder strNum = new StringBuilder();
        while (!seedStack.empty()) {
            strNum.append(seedStack.pop());
        }
        strNum.reverse();
        seed = Long.parseLong(strNum.toString());
        return seed;
    }

    public TETile[][] playWithInputString(String input) {
        Long seed;

        int seedLength = 0;
        char first = input.charAt(0);
        int startMoves = 0;
        SproulSpace s;

        if (first == 'L' || first == 'l') {
            deserialize();
            //ter.initialize(Game.WIDTH, Game.HEIGHT, 0, 0);
            s = saved;
            startMoves = 1;
        } else {
            if (first != 'N' && first != 'n') {
                seed = Long.parseLong(input);
            } else {
                for (int i = 1; input.charAt(i) != 'S' && input.charAt(i) != 's'; i++) {
                    seedLength += 1;
                }
                int[] seedarray = new int[seedLength];
                for (int i = 1; input.charAt(i) != 'S' && input.charAt(i) != 's'; i++) {
                    char c = input.charAt(i);
                    startMoves = i + 2;
                    seedarray[i - 1] = Character.getNumericValue(c);
                }
                StringBuilder strNum = new StringBuilder();
                for (int num : seedarray) {
                    strNum.append(num);
                }
                seed = Long.parseLong(strNum.toString());
            }

            SproulSpace.random = new Random(seed);
            Room.random = new Random(seed);
            random = new Random(seed);
            s = new SproulSpace();
            s.sproul = buildGame(s);
        }

        for (int i = startMoves; i < input.length(); i++) {
            char key = input.charAt(i);
            if (key == ':') {
                char next = input.charAt(i + 1);
                if (next == 'Q' || next == 'q') {
                    s.serialize(s);
                    break;
                }
            } else {
                s.movePlayer(s, s.sproul, key, false);
            }
        }
        //Font font = new Font("Monaco", Font.BOLD, 14);
        //StdDraw.setFont(font);
        //s.renderFrame(s.sproul);
        return s.sproul;
    }

    public TETile[][] buildGame(SproulSpace s) {

        //build rooms in s
        for (int i = 0; i < 25; i++) {
            Room r = new Room(s);
            while (!r.built) {
                r = new Room(s);
            }
            s.rooms.add(i, r);
        }

        //add hallways to s
        s.addHallways();

        //add characters to rooms
        for (int i = 0; i < 20; i++) {
            s.placeInRoom(s, Tileset.CONSULTANT);
        }

        for (int i = 0; i < 10; i++) {
            s.placeInRoom(s, Tileset.PUPPER);
        }

        s.placeInRoom(s, Tileset.PLAYER);
        s.placeInRoom(s, Tileset.HUG);
        s.placeInRoom(s, Tileset.SGATE);

        //return the TETile 2-D array
        return s.sproul;
    }
}
