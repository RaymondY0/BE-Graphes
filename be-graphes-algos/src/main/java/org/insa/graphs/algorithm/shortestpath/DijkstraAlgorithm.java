package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
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

        Label[] labels = new Label[nbNodes];

        for (int i=0;i<nbNodes;i++){
            labels[i] = new Label(graph.getNodes().get(i));
        }

        BinaryHeap<Label> lesCouts = new BinaryHeap<Label>();

        labels[data.getOrigin().getId()].setCost(0);  //set le cout de l'origin à 0
        labels[data.getOrigin().getId()].marked();  //marquage de l'origine

        lesCouts.insert(labels[data.getOrigin().getId()]);

        // Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());

        Node lastMarked = labels[data.getOrigin().getId()].getNode();

        while(!(labels[data.getDestination().getId()].getMark()) && !(lesCouts.isEmpty())){  //tant que le Node destination n'est pas marqué
            for(Arc arc : lastMarked.getSuccessors()){

                // Small test to check allowed roads...
                if (!data.isAllowed(arc)) {
                    continue;
                }

                if(labels[arc.getDestination().getId()].getMark()==false){
                    // Retrieve weight of the arc.
                    double w = data.getCost(arc);
                    double oldDistance = labels[arc.getDestination().getId()].getCost();
                    double newDistance = labels[lastMarked.getId()].getCost() + w;

                    if (Double.isInfinite(oldDistance)
                            && Double.isFinite(newDistance)) {
                        notifyNodeReached(arc.getDestination());
                        lesCouts.insert(labels[arc.getDestination().getId()]);
                    }

                    // Check if new distances would be better, if so update...
                    if (newDistance < oldDistance) {
                        lesCouts.remove(labels[arc.getDestination().getId()]);
                        labels[arc.getDestination().getId()].setCost(newDistance);
                        labels[arc.getDestination().getId()].setFather(arc);
                        lesCouts.insert(labels[arc.getDestination().getId()]);
                    }
                }
            }

            Label coutMin = lesCouts.deleteMin();
            labels[coutMin.getNode().getId()].marked();
            lastMarked = labels[coutMin.getNode().getId()].getNode();
            notifyNodeMarked(lastMarked);
        }

        if (!labels[data.getDestination().getId()].getMark()) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        } else {

            // The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());

            // Create the path from the array of predecessors...
            ArrayList<Arc> arcs = new ArrayList<>();
            Arc arc = labels[data.getDestination().getId()].getFather();
            while (arc != null) {
                arcs.add(arc);
                arc = labels[arc.getOrigin().getId()].getFather();
            }

            // Reverse the path...
            Collections.reverse(arcs);

            // Create the final solution.
            solution = new ShortestPathSolution(data, Status.OPTIMAL,
                    new Path(graph, arcs));
        }

        // when the algorithm terminates, return the solution that has been found
        return solution;
    }

}
