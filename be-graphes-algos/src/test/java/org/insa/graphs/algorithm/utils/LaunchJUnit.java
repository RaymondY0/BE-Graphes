package org.insa.graphs.algorithm.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;

import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.GraphReader;
import org.junit.BeforeClass;
import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.junit.Test;
import java.util.List;
import org.insa.graphs.model.Arc;

public class LaunchJUnit {

    private static Graph graphInsa, graphToulouse;

    private static ArcInspector noFilterByLength, onlyCarsByLength, onlyCarsByTime, onlyPedestrianByTime;

    @SuppressWarnings("unused")
    private static ShortestPathData emptyPathInsa, singleNodePathToulouse, shortPathInsaNoFilterByLength, shortPathInsaOnlyCarsByTime,
    longPathInsaOnlyCarsByLength, longPathInsaOnlyPedestrianByTime, shortPathToulouseOnlyCarsByLength, 
    shortPathToulouseOnlyPedestrianByTime, longPathToulouseNoFilterByLengh, longPathToulouseOnlyCarsByTime;

    @SuppressWarnings("unused")
    private static ShortestPathSolution algoEmptyPathInsa, algoSingleNodePathToulouse, algoShortPathInsaNoFilterByLength, algoShortPathInsaOnlyCarsByTime,
    algoLongPathInsaOnlyCarsByLength, algoLongPathInsaOnlyPedestrianByTime, algoShortPathToulouseOnlyCarsByLength, 
    algoShortPathToulouseOnlyPedestrianByTime, algoLongPathToulouseNoFilterByLengh, algoLongPathToulouseOnlyCarsByTime;

    @BeforeClass
    public static void initAll() throws Exception{
        try (final GraphReader reader = new BinaryGraphReader(new DataInputStream(
                new BufferedInputStream(new FileInputStream("/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr"))))) {
            graphInsa = reader.read();
        }
        try (final GraphReader reader = new BinaryGraphReader(new DataInputStream(
                new BufferedInputStream(new FileInputStream("/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/toulouse.mapgr"))))) {
            graphToulouse = reader.read();
        }

        noFilterByLength = ArcInspectorFactory.getAllFilters().get(0);
        onlyCarsByLength = ArcInspectorFactory.getAllFilters().get(1);
        onlyCarsByTime = ArcInspectorFactory.getAllFilters().get(2);
        onlyPedestrianByTime = ArcInspectorFactory.getAllFilters().get(3);

        emptyPathInsa = new ShortestPathData(graphInsa,graphInsa.get(240),graphInsa.get(383),noFilterByLength);
        algoEmptyPathInsa = algorithm(emptyPathInsa);
        singleNodePathToulouse = new ShortestPathData(graphToulouse,graphToulouse.get(3167),graphToulouse.get(3167),noFilterByLength);
        algoSingleNodePathToulouse = algorithm(singleNodePathToulouse);

        shortPathInsaNoFilterByLength = new ShortestPathData(graphInsa,graphInsa.get(272),graphInsa.get(310),noFilterByLength);
        algoShortPathInsaNoFilterByLength = algorithm(shortPathInsaNoFilterByLength);
        shortPathInsaOnlyCarsByTime = new ShortestPathData(graphInsa,graphInsa.get(53),graphInsa.get(63),onlyCarsByTime);
        algoShortPathInsaOnlyCarsByTime = algorithm(shortPathInsaOnlyCarsByTime);
        longPathInsaOnlyCarsByLength = new ShortestPathData(graphInsa,graphInsa.get(221),graphInsa.get(361),onlyCarsByLength);
        algoLongPathInsaOnlyCarsByLength = algorithm(longPathInsaOnlyCarsByLength);
        longPathInsaOnlyPedestrianByTime = new ShortestPathData(graphInsa,graphInsa.get(200),graphInsa.get(805),onlyPedestrianByTime);
        algoLongPathInsaOnlyPedestrianByTime = algorithm(longPathInsaOnlyPedestrianByTime);

        shortPathToulouseOnlyCarsByLength = new ShortestPathData(graphToulouse,graphToulouse.get(1357),graphToulouse.get(1882),onlyCarsByLength);
        algoShortPathToulouseOnlyCarsByLength = algorithm(shortPathToulouseOnlyCarsByLength);
        shortPathToulouseOnlyPedestrianByTime = new ShortestPathData(graphToulouse,graphToulouse.get(4533),graphToulouse.get(27040),onlyPedestrianByTime);
        algoShortPathToulouseOnlyPedestrianByTime = algorithm(shortPathToulouseOnlyPedestrianByTime);
        longPathToulouseNoFilterByLengh = new ShortestPathData(graphToulouse,graphToulouse.get(639),graphToulouse.get(13700),noFilterByLength);
        algoLongPathToulouseNoFilterByLengh = algorithm(longPathToulouseNoFilterByLengh);
        longPathToulouseOnlyCarsByTime = new ShortestPathData(graphToulouse,graphToulouse.get(639),graphToulouse.get(13700),onlyCarsByTime);
        algoLongPathToulouseOnlyCarsByTime = algorithm(longPathToulouseOnlyCarsByTime);
    }

    private static ShortestPathSolution algorithm(ShortestPathData data){
        return (new AStarAlgorithm(data)).run();  //mettre dijkstra ou A*
    }

    private static boolean arePathsEqual(Path path1, Path path2){  //les ifs sont là pour s'assurer que les paths ne sont pas nuls, dans le cas contraire on ne pourrait pas invoquer leurs méthodes.
        if(path1==null && path2==null)return true; 
        if(path1==null || path2==null)return false;
        boolean sameDestination = (path1.getDestination()==path2.getDestination());
        boolean sameOrigin = (path1.getOrigin()==path2.getOrigin());
        boolean sameArcs = true;
        List<Arc> arcs1 = path1.getArcs();
        List<Arc> arcs2 = path2.getArcs();
        for(int i=0;i<arcs1.size();i++){
            sameArcs = (sameArcs&&Float.compare(arcs1.get(i).getLength(),arcs2.get(i).getLength())==0);
            sameArcs = (sameArcs&&arcs1.get(i).getOrigin().equals(arcs2.get(i).getOrigin()));
            sameArcs = (sameArcs&&arcs1.get(i).getDestination().equals(arcs2.get(i).getDestination()));
        }
        return (sameDestination&&sameOrigin&&sameArcs);
    }

    @Test
    public void testValidPaths(){  //le if est là pour le cas single node car le path issue de la solution single node est "null" et donc on ne peut pas utiliser la méthode isValid.
        if(algoEmptyPathInsa.isFeasible())assertTrue(algoEmptyPathInsa.getPath().isValid());
        if(algoSingleNodePathToulouse.isFeasible())assertTrue(algoSingleNodePathToulouse.getPath().isValid());
        if(algoShortPathInsaNoFilterByLength.isFeasible())assertTrue(algoShortPathInsaNoFilterByLength.getPath().isValid());
        if(algoShortPathInsaOnlyCarsByTime.isFeasible())assertTrue(algoShortPathInsaOnlyCarsByTime.getPath().isValid());
        if(algoLongPathInsaOnlyCarsByLength.isFeasible())assertTrue(algoLongPathInsaOnlyCarsByLength.getPath().isValid());
        if(algoLongPathInsaOnlyPedestrianByTime.isFeasible())assertTrue(algoLongPathInsaOnlyPedestrianByTime.getPath().isValid());
        if(algoShortPathToulouseOnlyCarsByLength.isFeasible())assertTrue(algoShortPathToulouseOnlyCarsByLength.getPath().isValid());
        if(algoShortPathToulouseOnlyPedestrianByTime.isFeasible())assertTrue(algoShortPathToulouseOnlyPedestrianByTime.getPath().isValid());
        if(algoLongPathToulouseNoFilterByLengh.isFeasible())assertTrue(algoLongPathToulouseNoFilterByLengh.getPath().isValid());
        if(algoLongPathToulouseOnlyCarsByTime.isFeasible())assertTrue(algoLongPathToulouseOnlyCarsByTime.getPath().isValid());
    }

    @Test
    public void testCostCalculated(){  //le if est là pour le cas single node car le toString dans le cas non Feasible n'inclu pas le coût dans le message.
        if(algoSingleNodePathToulouse.isFeasible())assertEquals(Double.parseDouble(algoSingleNodePathToulouse.toString().split(" ")[9].replace(",",".")),Double.parseDouble(String.format("%.4f",algoSingleNodePathToulouse.getPath().getLength()/1000).replace(",",".")),0.0001);
        if(algoShortPathInsaNoFilterByLength.isFeasible())assertEquals(Double.parseDouble(algoShortPathInsaNoFilterByLength.toString().split(" ")[9].replace(",",".")),Double.parseDouble(String.format("%.4f",algoShortPathInsaNoFilterByLength.getPath().getLength()/1000).replace(",",".")),0.0001);
        if(algoShortPathInsaOnlyCarsByTime.isFeasible())assertEquals(Double.parseDouble(algoShortPathInsaOnlyCarsByTime.toString().split(" ")[9].replace(",",".")),Double.parseDouble(String.format("%.4f",algoShortPathInsaOnlyCarsByTime.getPath().getMinimumTravelTime()/60).replace(",",".")),0.0001);
        if(algoLongPathInsaOnlyCarsByLength.isFeasible())assertEquals(Double.parseDouble(algoLongPathInsaOnlyCarsByLength.toString().split(" ")[9].replace(",",".")),Double.parseDouble(String.format("%.4f",algoLongPathInsaOnlyCarsByLength.getPath().getLength()/1000).replace(",",".")),0.0001);
        if(algoLongPathInsaOnlyPedestrianByTime.isFeasible())assertEquals(Double.parseDouble(algoLongPathInsaOnlyPedestrianByTime.toString().split(" ")[9].replace(",",".")),Double.parseDouble(String.format("%.4f",algoLongPathInsaOnlyPedestrianByTime.getPath().getTravelTime(5)/60).replace(",",".")),0.0001);  //getTravelTime(5) car dans le filtre onlyPedestrianByTime la vitesse des voies est limité à 5.
        if(algoShortPathToulouseOnlyCarsByLength.isFeasible())assertEquals(Double.parseDouble(algoShortPathToulouseOnlyCarsByLength.toString().split(" ")[9].replace(",",".")),Double.parseDouble(String.format("%.4f",algoShortPathToulouseOnlyCarsByLength.getPath().getLength()/1000).replace(",",".")),0.0001);
        if(algoShortPathToulouseOnlyPedestrianByTime.isFeasible())assertEquals(Double.parseDouble(algoShortPathToulouseOnlyPedestrianByTime.toString().split(" ")[9].replace(",",".")),Double.parseDouble(String.format("%.4f",algoShortPathToulouseOnlyPedestrianByTime.getPath().getTravelTime(5)/60).replace(",",".")),0.0001);
        if(algoLongPathToulouseNoFilterByLengh.isFeasible())assertEquals(Double.parseDouble(algoLongPathToulouseNoFilterByLengh.toString().split(" ")[9].replace(",",".")),Double.parseDouble(String.format("%.4f",algoLongPathToulouseNoFilterByLengh.getPath().getLength()/1000).replace(",",".")),0.0001);
        if(algoLongPathToulouseOnlyCarsByTime.isFeasible())assertEquals(Double.parseDouble(algoLongPathToulouseOnlyCarsByTime.toString().split(" ")[9].replace(",",".")),Double.parseDouble(String.format("%.4f",algoLongPathToulouseOnlyCarsByTime.getPath().getMinimumTravelTime()/60).replace(",",".")),0.0001);
    }

    @Test
    public void testSamePathAsBellmanFord(){
        assertTrue(arePathsEqual((new BellmanFordAlgorithm(emptyPathInsa)).run().getPath(), algoEmptyPathInsa.getPath()));
        assertTrue(arePathsEqual((new BellmanFordAlgorithm(singleNodePathToulouse)).run().getPath(), algoSingleNodePathToulouse.getPath()));
        assertTrue(arePathsEqual((new BellmanFordAlgorithm(shortPathInsaNoFilterByLength)).run().getPath(), algoShortPathInsaNoFilterByLength.getPath()));
        assertTrue(arePathsEqual((new BellmanFordAlgorithm(shortPathInsaOnlyCarsByTime)).run().getPath(), algoShortPathInsaOnlyCarsByTime.getPath()));
        assertTrue(arePathsEqual((new BellmanFordAlgorithm(longPathInsaOnlyCarsByLength)).run().getPath(), algoLongPathInsaOnlyCarsByLength.getPath()));
        assertTrue(arePathsEqual((new BellmanFordAlgorithm(longPathInsaOnlyPedestrianByTime)).run().getPath(), algoLongPathInsaOnlyPedestrianByTime.getPath()));
        assertTrue(arePathsEqual((new BellmanFordAlgorithm(shortPathToulouseOnlyCarsByLength)).run().getPath(), algoShortPathToulouseOnlyCarsByLength.getPath()));
        assertTrue(arePathsEqual((new BellmanFordAlgorithm(shortPathToulouseOnlyPedestrianByTime)).run().getPath(), algoShortPathToulouseOnlyPedestrianByTime.getPath()));
        assertTrue(arePathsEqual((new BellmanFordAlgorithm(longPathToulouseNoFilterByLengh)).run().getPath(), algoLongPathToulouseNoFilterByLengh.getPath()));
        assertTrue(arePathsEqual((new BellmanFordAlgorithm(longPathToulouseOnlyCarsByTime)).run().getPath(), algoLongPathToulouseOnlyCarsByTime.getPath()));
    } 

} 