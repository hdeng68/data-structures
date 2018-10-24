package byog.Core;

import org.junit.Test;
import org.junit.Assert;

import byog.TileEngine.TETile;

public class TestGame {

    @Test
    public void Testplaywithinputstring() {
        Game game = new Game();
        game.ter.initialize(SproulSpace.WIDTH, SproulSpace.HEIGHT);
        TETile[][] a = game.playWithInputString("123");
        TETile[][] b = game.playWithInputString("123");
        Assert.assertArrayEquals(a, b);
    }
}
