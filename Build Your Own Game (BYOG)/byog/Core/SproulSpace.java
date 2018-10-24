package byog.Core;

import java.awt.Font;
import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Random;
import java.util.ArrayList;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

public class SproulSpace implements Serializable {
    protected static final int WIDTH = 60;
    protected static final int HEIGHT = 25;
    private static final int WORLDHEIGHT = 29;
    protected TETile[][] sproul;
    protected ArrayList<Room> rooms = new ArrayList<>(25);
    protected static Random random = new Random();

    private boolean gameOver = false;
    private int morale = 5;
    private int internships = 0;
    private boolean hasKey = false;
    private int playerX;
    private int playerY;
    protected static String filename = "savedSproulSpace.txt";

    protected SproulSpace() {
        sproul = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                sproul[x][y] = Tileset.GRASS;
            }
        }
    }

    public void addHallways() {
        for (Room r : rooms) {
            if (r.posX < WIDTH / 2 && r.posY < HEIGHT / 2) {
                drawUp(r);
                drawRight(r);
            } else if (r.posX < WIDTH / 2 && r.posY > HEIGHT / 2) {
                drawDown(r);
                drawRight(r);
            } else if (r.posX > WIDTH / 2 && r.posY > HEIGHT / 2) {
                drawDown(r);
                drawLeft(r);
            } else {
                drawUp(r);
                drawLeft(r);
            }
        }
        fixExceptions();
    }

    public void drawUp(Room r) {
        int startPosX = random.nextInt(r.rwidth - 2) + r.posX;
        int startPosY = r.posY + r.rlength - 1;
        for (int i = startPosY; i < HEIGHT; i++) {
            if (i + 1 == HEIGHT) {
                sproul[startPosX][i] = Tileset.TREE;
                sproul[startPosX + 1][i] = Tileset.TREE;
                sproul[startPosX + 2][i] = Tileset.TREE;
            } else if (sproul[startPosX + 1][i] == Tileset.CONCRETE) {
                break;
            } else {
                sproul[startPosX][i] = Tileset.TREE;
                sproul[startPosX + 1][i] = Tileset.CONCRETE;
                sproul[startPosX + 2][i] = Tileset.TREE;
            }
        }
    }

    public void drawDown(Room r) {
        int startPosX = random.nextInt(r.rwidth - 2) + r.posX;
        int startPosY = r.posY;
        for (int i = startPosY; i >= 0; i--) {
            if (i == 0) {
                sproul[startPosX][i] = Tileset.TREE;
                sproul[startPosX + 1][i] = Tileset.TREE;
                sproul[startPosX + 2][i] = Tileset.TREE;
            } else if (sproul[startPosX + 1][i] == Tileset.CONCRETE) {
                break;
            } else {
                sproul[startPosX][i] = Tileset.TREE;
                sproul[startPosX + 1][i] = Tileset.CONCRETE;
                sproul[startPosX + 2][i] = Tileset.TREE;
            }
        }
    }

    public void drawRight(Room r) {
        int startPosX = r.posX + r.rwidth - 1;
        int startPosY = random.nextInt(r.rlength - 2) + r.posY;
        for (int i = startPosX; i < WIDTH; i++) {
            if (i + 1 == WIDTH) {
                sproul[i][startPosY] = Tileset.TREE;
                sproul[i][startPosY + 1] = Tileset.TREE;
                sproul[i][startPosY + 2] = Tileset.TREE;
            } else if (sproul[i][startPosY + 1] == Tileset.CONCRETE) {
                break;
            } else {
                sproul[i][startPosY] = Tileset.TREE;
                sproul[i][startPosY + 1] = Tileset.CONCRETE;
                sproul[i][startPosY + 2] = Tileset.TREE;
            }
        }
    }

    public void drawLeft(Room r) {
        int startPosX = r.posX;
        int startPosY = random.nextInt(r.rlength - 2) + r.posY;
        for (int i = startPosX; i >= 0; i--) {
            if (i == 0) {
                sproul[i][startPosY] = Tileset.TREE;
                sproul[i][startPosY + 1] = Tileset.TREE;
                sproul[i][startPosY + 2] = Tileset.TREE;
            } else if (sproul[i][startPosY + 1] == Tileset.CONCRETE) {
                break;
            } else {
                sproul[i][startPosY] = Tileset.TREE;
                sproul[i][startPosY + 1] = Tileset.CONCRETE;
                sproul[i][startPosY + 2] = Tileset.TREE;
            }
        }
    }

    public void fixExceptions() {
        for (int i = 0; i < SproulSpace.WIDTH - 1; i++) {
            for (int j = 0; j < SproulSpace.HEIGHT - 1; j++) {
                if ((sproul[i][j] == Tileset.CONCRETE
                        && sproul[i + 1][j + 1] == Tileset.CONCRETE
                        && sproul[i + 1][j] == Tileset.TREE
                        && sproul[i][j + 1] == Tileset.TREE)
                        || (sproul[i][j] == Tileset.TREE
                        && sproul[i + 1][j + 1] == Tileset.TREE
                        && sproul[i + 1][j] == Tileset.CONCRETE
                        && sproul[i][j + 1] == Tileset.CONCRETE)) {
                    sproul[i][j] = Tileset.CONCRETE;
                    sproul[i + 1][j + 1] = Tileset.CONCRETE;
                    sproul[i + 1][j] = Tileset.CONCRETE;
                    sproul[i][j + 1] = Tileset.CONCRETE;
                }
            }
        }

        for (int i = 0; i < SproulSpace.WIDTH - 1; i++) {
            for (int j = 0; j < SproulSpace.HEIGHT - 1; j++) {
                if (sproul[i][j] == Tileset.CONCRETE
                        && sproul[i + 1][j + 1] == Tileset.GRASS) {
                    sproul[i + 1][j + 1] = Tileset.TREE;
                } else if (sproul[i][j] == Tileset.GRASS
                        && sproul[i + 1][j + 1] == Tileset.CONCRETE) {
                    sproul[i][j] = Tileset.TREE;
                } else if (sproul[i + 1][j] == Tileset.CONCRETE
                        && sproul[i][j + 1] == Tileset.GRASS) {
                    sproul[i][j + 1] = Tileset.TREE;
                } else if (sproul[i + 1][j] == Tileset.GRASS
                        && sproul[i][j + 1] == Tileset.CONCRETE) {
                    sproul[i + 1][j] = Tileset.TREE;
                }
            }
        }

        for (int i = 0; i < SproulSpace.WIDTH; i++) {
            for (int j = 0; j < SproulSpace.HEIGHT - 2; j++) {
                if (sproul[i][j] == Tileset.CONCRETE
                        && sproul[i][j + 1] == Tileset.TREE
                        && sproul[i][j + 2] == Tileset.CONCRETE) {
                    sproul[i][j + 1] = Tileset.CONCRETE;
                }
            }
        }
        for (int j = 0; j < SproulSpace.HEIGHT; j++) {
            for (int i = 0; i < SproulSpace.WIDTH - 2; i++) {
                if (sproul[i][j] == Tileset.CONCRETE
                        && sproul[i + 1][j] == Tileset.TREE
                        && sproul[i + 2][j] == Tileset.CONCRETE) {
                    sproul[i + 1][j] = Tileset.CONCRETE;
                }
            }
        }
        fixExtraTreeRows();
    }

    public void fixExtraTreeRows() {
        for (int i = 1; i < SproulSpace.WIDTH - 1; i++) {
            for (int j = 1; j < SproulSpace.HEIGHT - 1; j++) {
                if (sproul[i][j] == Tileset.TREE
                        && sproul[i - 1][j - 1] != Tileset.CONCRETE
                        && sproul[i - 1][j] != Tileset.CONCRETE
                        && sproul[i - 1][j + 1] != Tileset.CONCRETE
                        && sproul[i][j - 1] != Tileset.CONCRETE
                        && sproul[i][j + 1] != Tileset.CONCRETE
                        && sproul[i + 1][j - 1] != Tileset.CONCRETE
                        && sproul[i + 1][j] != Tileset.CONCRETE
                        && sproul[i + 1][j + 1] != Tileset.CONCRETE) {
                    sproul[i][j] = Tileset.GRASS;
                }
            }
        }
        for (int j = 1; j < SproulSpace.HEIGHT - 1; j++) {
            if (sproul[0][j] == Tileset.TREE
                    && sproul[0][j - 1] != Tileset.CONCRETE
                    && sproul[0][j + 1] != Tileset.CONCRETE
                    && sproul[1][j - 1] != Tileset.CONCRETE
                    && sproul[1][j] != Tileset.CONCRETE
                    && sproul[1][j + 1] != Tileset.CONCRETE) {
                sproul[0][j] = Tileset.GRASS;
            }
        }
        for (int j = 1; j < SproulSpace.HEIGHT - 1; j++) {
            if (sproul[WIDTH - 1][j] == Tileset.TREE
                    && sproul[WIDTH - 1][j - 1] != Tileset.CONCRETE
                    && sproul[WIDTH - 1][j + 1] != Tileset.CONCRETE
                    && sproul[WIDTH - 2][j - 1] != Tileset.CONCRETE
                    && sproul[WIDTH - 2][j] != Tileset.CONCRETE
                    && sproul[WIDTH - 2][j + 1] != Tileset.CONCRETE) {
                sproul[WIDTH - 1][j] = Tileset.GRASS;
            }
        }
        for (int i = 1; i < SproulSpace.WIDTH - 1; i++) {
            if (sproul[i][0] == Tileset.TREE
                    && sproul[i - 1][0] != Tileset.CONCRETE
                    && sproul[i + 1][0] != Tileset.CONCRETE
                    && sproul[i - 1][1] != Tileset.CONCRETE
                    && sproul[i][1] != Tileset.CONCRETE
                    && sproul[i + 1][1] != Tileset.CONCRETE) {
                sproul[i][0] = Tileset.GRASS;
            }
        }
        for (int i = 1; i < SproulSpace.WIDTH - 1; i++) {
            if (sproul[i][HEIGHT - 1] == Tileset.TREE
                    && sproul[i - 1][HEIGHT - 1] != Tileset.CONCRETE
                    && sproul[i + 1][HEIGHT - 1] != Tileset.CONCRETE
                    && sproul[i - 1][HEIGHT - 2] != Tileset.CONCRETE
                    && sproul[i][HEIGHT - 2] != Tileset.CONCRETE
                    && sproul[i + 1][HEIGHT - 2] != Tileset.CONCRETE) {
                sproul[i][HEIGHT - 1] = Tileset.GRASS;
            }
        }
    }

    public void movePlayer(SproulSpace s, TETile[][] worldState, char key, boolean render) {
        if ((key == 'w' || key == 'W')
                && !worldState[playerX][playerY + 1].equals(Tileset.TREE)) {
            TETile next = worldState[playerX][playerY + 1];
            checkSpecialEncounters(next);
            worldState[playerX][playerY + 1] = Tileset.PLAYER;
            worldState[playerX][playerY] = Tileset.CONCRETE;
            playerY++;
            if (render) {
                renderFrame(worldState);
            }
        } else if ((key == 's' || key == 'S')
                && !worldState[playerX][playerY - 1].equals(Tileset.TREE)) {
            TETile next = worldState[playerX][playerY - 1];
            checkSpecialEncounters(next);
            worldState[playerX][playerY - 1] = Tileset.PLAYER;
            worldState[playerX][playerY] = Tileset.CONCRETE;
            playerY--;
            if (render) {
                renderFrame(worldState);
            }
        } else if ((key == 'a' || key == 'A')
                && !worldState[playerX - 1][playerY].equals(Tileset.TREE)) {
            TETile next = worldState[playerX - 1][playerY];
            checkSpecialEncounters(next);
            worldState[playerX - 1][playerY] = Tileset.PLAYER;
            worldState[playerX][playerY] = Tileset.CONCRETE;
            playerX--;
            if (render) {
                renderFrame(worldState);
            }
        } else if ((key == 'd' || key == 'D')
                && !worldState[playerX + 1][playerY].equals(Tileset.TREE)) {
            TETile next = worldState[playerX + 1][playerY];
            checkSpecialEncounters(next);
            worldState[playerX + 1][playerY] = Tileset.PLAYER;
            worldState[playerX][playerY] = Tileset.CONCRETE;
            playerX++;
            if (render) {
                renderFrame(worldState);
            }
        } else {
            if (render) {
                solicitMoveInput(s, worldState);
            }
        }
    }

    public void checkSpecialEncounters(TETile t) {
        if (t.equals(Tileset.HUG)) {
            hasKey = true;
        } else if (t.equals(Tileset.PUPPER)) {
            if (morale < 5) {
                morale++;
            }
        } else if (t.equals(Tileset.SGATE)) {
            if (!hasKey || morale <= 0 || internships < 3) {
                gameOver = true;
            } else {
                drawWinFrame();
            }
        } else if (t.equals(Tileset.CONSULTANT)) {
            int luck = random.nextInt(4);
            if (luck == 0 || luck == 1 || luck == 2) {
                morale--;
            } else {
                internships++;
            }
        }
    }

    public void placeInRoom(SproulSpace s, TETile t) {
        int roomNumber = random.nextInt(25);
        Room room = s.rooms.get(roomNumber);
        int playerXPos = random.nextInt(room.rwidth - 2) + room.posX + 1;
        int playerYPos = random.nextInt(room.rlength - 2) + room.posY + 1;
        if (s.sproul[playerXPos][playerYPos] == Tileset.CONCRETE) {
            s.sproul[playerXPos][playerYPos] = t;
            if (t == Tileset.PLAYER) {
                playerX = playerXPos;
                playerY = playerYPos;
            }
        } else {
            placeInRoom(s, t);
        }
    }


    public void renderFrame(TETile[][] worldState) {
        int numXTiles = worldState.length;
        int numYTiles = worldState[0].length;
        for (int x = 0; x < numXTiles; x += 1) {
            for (int y = 0; y < numYTiles; y += 1) {
                if (worldState[x][y] == null) {
                    throw new IllegalArgumentException("Tile at position x=" + x + ", y=" + y
                            + " is null.");
                }
                worldState[x][y].draw(x, y);
            }
        }
    }

    public void solicitMoveInput(SproulSpace s, TETile[][] worldState) {
        if (s.morale == 0) {
            gameOver = true;
        }
        while (!StdDraw.hasNextKeyTyped()) {
            drawGeneralFrame(worldState);
        }
        char key = StdDraw.nextKeyTyped();
        if (key != ':') {
            s.movePlayer(s, worldState, key, true);
            drawGeneralFrame(worldState);
            solicitMoveInput(s, worldState);
        } else {
            while (!StdDraw.hasNextKeyTyped()) {
                drawGeneralFrame(worldState);
            }
            if (StdDraw.nextKeyTyped() == 'q') {
                serialize(s);
                System.exit(0);
            }
        }
    }

    public void drawGeneralFrame(TETile[][] worldState) {
        StdDraw.clear();
        StdDraw.clear(Color.darkGray);

        if (!gameOver) {
            Font font = new Font("Monaco", Font.BOLD, 14);
            StdDraw.setPenColor(Color.white);
            StdDraw.setFont(font);
            if (StdDraw.mouseX() <= (WIDTH - 1) && StdDraw.mouseX() >= 0
                    && StdDraw.mouseY() <= (HEIGHT - 1) && StdDraw.mouseY() >= 0) {
                StdDraw.textLeft(1, WORLDHEIGHT - 1,
                        worldState[(int) StdDraw.mouseX()][(int) StdDraw.mouseY()].description());
            }
            StdDraw.textRight(WIDTH - 1, WORLDHEIGHT - 1, "Morale: " + morale);
            StdDraw.textRight(WIDTH - 1, WORLDHEIGHT - 2, "Internships Offers: " + internships);
            StdDraw.textRight(WIDTH - 1, WORLDHEIGHT - 3,
                    hasKey ? "You have the key to success!" : "You're missing the key to success.");
            StdDraw.setPenColor(Color.black);
            StdDraw.line(0, WORLDHEIGHT - 3.75, WIDTH, WORLDHEIGHT - 3.75);
            renderFrame(worldState);
            StdDraw.show();
        } else {
            drawLoseFrame();
        }

    }

    public void drawWinFrame() {
        StdDraw.clear();
        StdDraw.clear(Color.darkGray);
        Font bigFont = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2.0, 18, "YOU WON!!!");
        Font smolFont = new Font("Comic Sans", Font.PLAIN, 24);
        StdDraw.setFont(smolFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2.0, 14.5, "You successfully made it out "
                + "of Sproul with at least 3 internships");
        StdDraw.text(WIDTH / 2.0, 13, "and the key to success!");
        Font verysmolFont = new Font("Times New Roman", Font.PLAIN, 18);
        StdDraw.setFont(verysmolFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2.0, 10, "Press (Q) to exit");
        StdDraw.show();
        solicitEndInput();
    }

    public void drawLoseFrame() {
        Font bigFont = new Font("Monaco", Font.BOLD, 50);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2.0, 17, "You lost 😞");
        Font smolFont = new Font("Comic Sans", Font.PLAIN, 24);
        StdDraw.setFont(smolFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2.0, 13.5, "Better luck next time.");
        Font verysmolFont = new Font("Times New Roman", Font.PLAIN, 18);
        StdDraw.setFont(verysmolFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(WIDTH / 2.0, 11, "Press (Q) to exit");
        StdDraw.show();
        solicitEndInput();
    }

    public void solicitEndInput() {
        while (!StdDraw.hasNextKeyTyped()) {
            continue;
        }
        char key = StdDraw.nextKeyTyped();
        if (key == 'Q' || key == 'q') {
            System.exit(0);
        }
    }

    //@source: https://www.geeksforgeeks.org/serialization-in-java/
    public void serialize(SproulSpace s) {
        try {
            //Saving of object in a file
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Method for serialization of object
            out.writeObject(s);

            out.close();
            file.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
