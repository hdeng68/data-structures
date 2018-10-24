public class PriorityNode implements Comparable<PriorityNode> {
    protected Long id;
    protected Long dest;
    protected double estimatedToDest;
    protected double distFromStart; //length of path from the start to this node
    protected double priority;
    protected PriorityNode parent;

  /*  public PriorityNode(PriorityNode parent, Long id) {
        this.parent = parent;
        this.id = id;
    } */

    public PriorityNode(GraphDB g, PriorityNode parent, long id, long dest) {
        this.id = id;
        this.dest = dest;
        this.parent = parent;
        estimatedToDest = g.distance(id, dest);
        if (parent == null) {
            distFromStart = 0;
        } else {
            distFromStart = parent.distFromStart + g.distance(parent.id, id);
        }
        priority = estimatedToDest + distFromStart;
    }

    @Override
    public int compareTo(PriorityNode wn) {
        if (this.priority > wn.priority) {
            return 1;
        } else if (this.priority < wn.priority) {
            return -1;
        } else {
            return 0;
        }
    }
}
