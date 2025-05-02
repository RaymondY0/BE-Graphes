package org.insa.graphs.algorithm.shortestpath;

import java.lang.reflect.Array;

import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Arc;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {

        // retrieve data from the input problem (getInputData() is inherited from the
        // parent class ShortestPathAlgorithm)
        final ShortestPathData data = getInputData();

        // variable that will contain the solution of the shortest path problem
        ShortestPathSolution solution = null;

        // TODO: implement the Dijkstra algorithm
        Graph graph = data.getGraph();
        final int nbNodes = graph.size();

        Node origin = data.getOrigin();
        Node destination = data.getDestination();

        Label[] labels = new Label[nbNodes];
        BinaryHeap<Double> lesCouts = new BinaryHeap();
        lesCouts.insert((double)0);
        for (int i=0;i<nbNodes;i++){
            Array.set(labels,i,graph.getNodes().get(i));
        }
        for (int i=0;i<nbNodes-1;i++){
            lesCouts.insert(Double.POSITIVE_INFINITY);
        }
        int indexDestination = -1; //index du Node destination dans labels
        int indexOrigin = -1;  //index du Node origin dans labels
        int i = 0;
        while (indexDestination == -1 && indexOrigin == -1 && i<nbNodes){
            Node node = ((Label)Array.get(labels,i)).getNode();
            if(node==origin){
                indexOrigin = i;
            } else if(node==destination){
                indexDestination = i;
            }
            i++;
        }
        ((Label)Array.get(labels,indexOrigin)).setCost(0);  //set le cout de l'origin à 0
        ((Label)Array.get(labels,indexOrigin)).marked();  //marquage de l'origine
        Node lastMarked = ((Label)Array.get(labels,indexOrigin)).getNode();
        int lastIndex = indexOrigin;

        i = 0;
        while(!(((Label)Array.get(labels,indexDestination)).getMark())&&i<nbNodes){  //tant que le Node destination n'est pas marqué
            for(Arc arc : lastMarked.getSuccessors()){

                // Small test to check allowed roads...
                if (!data.isAllowed(arc)) {
                    continue;
                }

                Node destinArc = arc.getDestination();  //le Node destinataire de l'arc
                int index = -1;  //index du Node considéré dans labels
                int j=0;
                while (index==-1&&j<nbNodes){
                    if(((Label)Array.get(labels,i)).getNode()==destinArc){
                        index = j;
                    }
                    j++;
                }

                // Retrieve weight of the arc.
                double w = data.getCost(arc);
                double oldDistance = ((Label)Array.get(labels,index)).getCost();
                double newDistance = ((Label)Array.get(labels,lastIndex)).getCost() + w;

                if (Double.isInfinite(oldDistance)
                        && Double.isFinite(newDistance)) {
                    notifyNodeReached(arc.getDestination());
                    ((Label)Array.get(labels,index)).setCost(newDistance);
                    ((Label)Array.get(labels,index)).setFather(arc);
                }

                // Check if new distances would be better, if so update...
                if (newDistance < oldDistance) {
                    
                }
            }
        }

        // when the algorithm terminates, return the solution that has been found
        return solution;
    }

}
