package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private int width;
    private int height;
    private int round;
    private Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        rand = new Random(seed);
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }

    public String generateRandomString(int n) {
        char[] randomString = new char[n];
        for (int i = 0; i < n; i ++) {
            randomString[i] = CHARACTERS[rand.nextInt(26)];
        }
        return new String(randomString);
    }

    public void drawFrame(String s) {
        StdDraw.clear();
        StdDraw.clear(Color.black);
        Font font = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(width/2.0, height/2.0, s);

        if (!gameOver) {
            Font smallFont = new Font("Monaco", Font.BOLD, 15);
            StdDraw.setFont(smallFont);
            StdDraw.line(0, height - 2, width, height - 2);
            StdDraw.text(3, height - 1, "Round: " + round);
            if (playerTurn) {
                StdDraw.text(width / 2.0, height - 1, "Type!");
            } else {
                StdDraw.text(width / 2.0, height - 1, "Watch!");
            }
            StdDraw.text(width - 5, height - 1, ENCOURAGEMENT[round % ENCOURAGEMENT.length]);
        }

        StdDraw.show();
    }

    public void flashSequence(String letters) {
        for (int i = 0; i < letters.length(); i++) {
            drawFrame(letters.substring(i, i+ 1));
            StdDraw.pause(1000);
            drawFrame("");
            StdDraw.pause(500);
        }
    }

    public String solicitNCharsInput(int n) {
        String input = "";
        while (input.length() < n) {
            if (!StdDraw.hasNextKeyTyped()) {
                continue;
            }
            input += String.valueOf(StdDraw.nextKeyTyped());
            drawFrame(input);
        }
        StdDraw.pause(500);
        return input;
    }

    public void startGame() {
        drawFrame("Get ready!");
        StdDraw.pause(2000);
        round = 1;
        gameOver = false;
        playerTurn = false;
        while (!gameOver) {
            playerTurn = false;
            drawFrame("Round: " + round);

            String roundstring = generateRandomString(round);
            flashSequence(roundstring);

            playerTurn = true;
            String userstring = solicitNCharsInput(round);
            if (!userstring.equals(roundstring)) {
                drawFrame("Game Over! You made it to round: " + round);
                gameOver = true;
            } else {
                drawFrame("Good job!");
                StdDraw.pause(1500);
                round += 1;
            }
        }
    }

}
