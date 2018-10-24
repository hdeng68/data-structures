import java.util.ArrayList;

public class Node implements Comparable<Node> {
    protected Long id;
    protected Double lat;
    protected Double lon;
    protected ArrayList<Node> neighbors = new ArrayList<>();
    protected Double calculatedDist;

    protected Node(Long id, Double lat, Double lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }

    public void addNeighbor(Node nd) {
        neighbors.add(nd);
    }

    public int compareTo(Node nd) {
        if (this.calculatedDist > nd.calculatedDist) {
            return 1;
        } else if (this.calculatedDist < nd.calculatedDist) {
            return -1;
        } else {
            return 0;
        }
    }
}
