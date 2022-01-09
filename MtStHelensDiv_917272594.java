import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.*;


public class MtStHelensDiv_917272594 implements AIModule
{

    //Declaring DataStructures 
    private ArrayList<Node> nodePath;
    private PriorityQueue<Node> pq;
    private HashMap<Point, Double> hash;
    private ArrayList<Point> output;

    /// Creates the path to the goal.
    public List<Point> createPath(final TerrainMap map)
    {
        //initializing DataStructures
        nodePath = new ArrayList<Node>();
        pq = new PriorityQueue<Node>(100, new Node());
        hash = new HashMap<Point, Double>();
        hash.put(map.getStartPoint(), getHeuristic(map.getStartPoint(), map.getEndPoint()));
        pq.add(new Node(map.getStartPoint(), getHeuristic(map.getStartPoint(), map.getEndPoint()))); 
        Node endPoint = new Node();
        output = new ArrayList<Point>();

        //Main loop
        while (pq.size() > 0){
            Node nu = pq.remove();

            nodePath.add(nu);

            //Break if reaches endpoint
            if (nu.node.equals(map.getEndPoint())){
                endPoint = new Node(nu);
                break;
            }
            //calls important function
            getChild(nu, map);
        }

        //retrives actual path from each points parent
        //startign point has a null as its parent
        while(endPoint.parent != null){
            output.add(0,new Point(endPoint.parent.node)) ;
            endPoint = endPoint.parent;
        }
        //add the endpoint to the path
        output.add(map.getEndPoint()) ;

        // We're done!
        return output;
    }

    private void getChild(Node nu, final TerrainMap map){
        //initialize cost vars
        double cost = -1.0;
        double totalCost = -1.0;
        Point[] children = map.getNeighbors(nu.node);
        
        for (int i = 0; i < children.length; i++ ){
            //make neighbor point into Node class with cost
            Node v = new Node(children[i], getHeuristic(children[i], map.getEndPoint())+ map.getCost(nu.node, children[i]));
            // Adds / updates costs / parentents in hashmap
            // Adds them to PriorityQueue
            if (!nodePath.contains(v)){
                cost = v.cost;
                totalCost = hash.get(nu.node) + cost;

                if(hash.containsKey(v.node) && totalCost < hash.get(v.node)){
                    hash.put(v.node, totalCost);
                    v.parent = new Node(nu);
                    v.cost = hash.get(v.node);
                    pq.add(new Node(v));
                }
                else if(!hash.containsKey(v.node)){
                    hash.put(v.node, totalCost);
                    v.parent= new Node(nu);
                    v.cost = hash.get(v.node);
                    pq.add(new Node(v));
                }
               
            }
        }
    }
    //Heuristic for Divisive cost is simply Euclidean distance 
    //works on my Machine for both 500/500 maps and MTAFT
    private double getHeuristic(Point p1, Point p2){
        return (Math.sqrt(((p2.x - p1.x) * (p2.x - p1.x)) + ((p2.y - p1.y) * (p2.y - p1.y)))) ;
        //return 0.0;

    }
    // Class to represent a node in the graph 
    class Node implements Comparator<Node> { 
        public Point node; 
        public double cost; 
        public Node parent;
  
        public Node() 
        { 
        } 
        public Node(Node n) 
        { 
            this.node = n.node;
            this.cost = n.cost;
            if(n.parent != null){
                this.parent = new Node(n.parent);
            }
    }    
        public Node(Point node, double cost) 
        { 
            this.node = node; 
            this.cost = cost; 
        } 
        @Override
        public int compare(Node node1, Node node2) 
        { 
            if (node1.cost < node2.cost) 
                return -1; 
            if (node1.cost > node2.cost) 
                return 1; 
            return 0; 
        } 
    } 
}
