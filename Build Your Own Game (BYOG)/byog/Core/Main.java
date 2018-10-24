package byog.Core;

//import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;

/** This is the main entry point for the program. This class simply parses
 *  the command line inputs, and lets the byog.Core.Game class take over
 *  in either keyboard or input string mode.
 */
public class Main {
    //private static TERenderer ter = new TERenderer();
    public static void main(String[] args) {
        //ter.initialize(Game.WIDTH, Game.HEIGHT);
        if (args.length > 1) {
            System.out.println("Can only have one argument - the input string");
            System.exit(0);
        } else if (args.length == 1) {
            Game game = new Game();
            TETile[][] worldState = game.playWithInputString(args[0]);
            //game.ter.renderFrame(worldState);
            System.out.println(TETile.toString(worldState));
        } else {
            Game game = new Game();
            game.playWithKeyboard();
        }
    }
}

