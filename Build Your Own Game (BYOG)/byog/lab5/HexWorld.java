package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    /** adds hexagon w/ specific tiles, size s, starting Position p, into a TETile[][] world */
    private static void addHexagon(TETile[][] world, TETile tile, int s) {
        /** iterates through the x-size of each row, adding by two each time until reaching 2*size*/
        int xPos = 10;
        int yPostop = 0;
        int yPosbot = s*2 - 1;

        for (int j = s; j <= (3*s -2); j += 2) {
            //creates bottom-most and top-most row first,
            //then as j increases, fills in the rest of the rows,
            // incrementing by one on both the top and bottom yIndex
            addRow(yPostop, j, xPos, world, tile);
            addRow(yPosbot, j, xPos, world, tile);

            //sets p to a new position (one up and one to the left)
            yPostop += 1;
            yPosbot -= 1;
            xPos -= 1;
        }
    }
    /**called by addHexagon, adds a specific row of a hexagon at a time*/
    private static void addRow(int yIndex, int xSize, int xStartIndex, TETile[][] world, TETile tile) {
        for (int i = xStartIndex; i < xStartIndex + xSize; i++) {
            world[i][yIndex] = tile;
        }
    }

    /** the Position class serves to keep track of the starting position
     * of a new hexagon or a new row
     */

    /** private class Position{
     private int x;
     private int y;

     //can create a new Position object w/ a specific xPos, yPos
     private Position(int xPos, int yPos){
     x = xPos;
     y = yPos;
     }
     }
     */

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(60, 30);

        TETile[][] world = new TETile[60][30];
        for (int x = 0; x < 60; x += 1) {
            for (int y = 0; y < 30; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        addHexagon(world, Tileset.HUG, 9);

        ter.renderFrame(world);
    }
}
