import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    boolean querySuccess;

    public Rasterer() {
        querySuccess = false;
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        Map<String, Object> results = new HashMap<>();
        //retrieve information given in params map
        double queryULlon = params.get("ullon");
        double queryLRlon = params.get("lrlon");
        double queryULlat = params.get("ullat");
        double queryLRlat = params.get("lrlat");
        double height = params.get("h");
        double width = params.get("w");
        //check if querySuccess = false
        if (queryULlon > MapServer.ROOT_LRLON || queryULlat < MapServer.ROOT_LRLAT
                || queryLRlon < MapServer.ROOT_ULLON || queryLRlat > MapServer.ROOT_ULLAT) {
            querySuccess = false;
            return results;
        } else {
            querySuccess = true;
        }
        //calculate the user's desired lonDPP (degree lon per pixel)
        double querylonDPP = (queryLRlon - queryULlon) / width;
        //calculate an appropriate depth
        int depth = 0;
        Double currlonDPP = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / 256;
        while (currlonDPP > querylonDPP && depth < 7) {
            depth += 1;
            currlonDPP = currlonDPP / 2;
        }
        //tiles at the specified depth
        int tilesAtDepth = (int) Math.pow(2, depth);

        double totalX = Math.abs(MapServer.ROOT_LRLON - MapServer.ROOT_ULLON);
        double totalY = Math.abs(MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT);
        //upper left tile position
        double xtoUL = Math.abs(MapServer.ROOT_ULLON - queryULlon);
        double ytoUL = Math.abs(MapServer.ROOT_ULLAT - queryULlat);
        int xul = (int) ((xtoUL) / (totalX) * (tilesAtDepth));
        int yul = (int) (((ytoUL) / (totalY) * (tilesAtDepth)));
        //lower right tile position (plus one for each b/c want the end of the tile)
        double xtoLR = Math.abs(queryLRlon - MapServer.ROOT_ULLON);
        double ytoLR = Math.abs(MapServer.ROOT_ULLAT - queryLRlat);
        int xlr = (int) Math.floor(((xtoLR) / (totalX) * (tilesAtDepth) + 1));
        if (xlr > tilesAtDepth) {
            xlr = tilesAtDepth;
        }
        int ylr = (int) Math.floor(((ytoLR) / (totalY) * (tilesAtDepth) + 1));
        if (ylr > tilesAtDepth) {
            ylr = tilesAtDepth;
        }
        //dimensions of solution array
        int gridX = xlr - xul;
        int gridY = ylr - yul;
        //generate image file names
        String[][] grid = new String[gridY][gridX];
        for (int j = 0; j < gridY; j++) {
            for (int i = 0; i < gridX; i++) {
                if ((xul + i) > (tilesAtDepth - 1) || (yul + j) > (tilesAtDepth - 1)
                        || (xul + i) < 0 || (yul + j) < 0) {
                    grid[j][i] = null;
                } else {
                    grid[j][i] = "d" + depth + "_x" + (xul + i) + "_y" + (yul + j) + ".png";
                }
            }
        }
        //calculate raster dimensions
        double rasterULlon = MapServer.ROOT_ULLON + ((double) xul / tilesAtDepth) * (totalX);
        double rasterULlat = MapServer.ROOT_ULLAT - ((double) yul / tilesAtDepth) * (totalY);
        double rasterLRlon = MapServer.ROOT_ULLON + ((double) xlr / tilesAtDepth) * (totalX);
        double rasterLRlat = MapServer.ROOT_ULLAT - ((double) ylr / tilesAtDepth) * (totalY);
        //add results to results map
        results.put("raster_ul_lon", rasterULlon);
        results.put("raster_lr_lon", rasterLRlon);
        results.put("raster_ul_lat", rasterULlat);
        results.put("raster_lr_lat", rasterLRlat);
        results.put("render_grid", grid);
        results.put("depth", depth);
        results.put("query_success", querySuccess);

        return results;
    }

}
