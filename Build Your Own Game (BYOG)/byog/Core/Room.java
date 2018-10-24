package byog.Core;
import java.io.Serializable;
import java.util.Random;

import byog.TileEngine.Tileset;
import byog.TileEngine.TERenderer;
public class Room implements Serializable {
    protected int posX;
    protected int posY;

    protected int rlength;
    protected int rwidth;
    protected static Random random = new Random();
    protected boolean built;

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(SproulSpace.WIDTH, SproulSpace.HEIGHT);

        SproulSpace s = new SproulSpace();

        for (int i = 0; i < 25; i++) {
            Room r = new Room(s);
            while (!r.built) {
                r = new Room(s);
            }
            s.rooms.add(i, r);
        }

        s.addHallways();

        ter.renderFrame(s.sproul);
    }

    public Room(SproulSpace s) {
        posX = random.nextInt(SproulSpace.WIDTH);
        posY = random.nextInt(SproulSpace.HEIGHT);
        rlength = random.nextInt(6) + 3;
        rwidth = random.nextInt(6) + 3;
        if (!hasSpace(s)) {
            built = false;
        } else {
            buildRoom(s);
            built = true;
        }
    }

    public void buildRoom(SproulSpace s) {
        this.buildBorder(s);
        this.buildInside(s);
    }

    public void buildBorder(SproulSpace s) {
        for (int i = posX; i < posX + rwidth; i++) {
            for (int j = posY; j < posY + rlength; j++) {
                s.sproul[i][j] = Tileset.TREE;
            }
        }
    }

    public void buildInside(SproulSpace s) {
        for (int i = posX + 1; i < posX + rwidth - 1; i++) {
            for (int j = posY + 1; j < posY + rlength - 1; j++) {
                s.sproul[i][j] = Tileset.CONCRETE;
            }
        }
    }

    public boolean hasSpace(SproulSpace s) {
        for (int i = posX; i < posX + rwidth; i++) {
            for (int j = posY; j < posY + rlength; j++) {
                if (i >= SproulSpace.WIDTH || j >= SproulSpace.HEIGHT
                        || s.sproul[i][j] != Tileset.GRASS) {
                    return false;
                }
            }
        }
        return true;
    }
}
