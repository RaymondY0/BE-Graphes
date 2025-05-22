package org.insa.graphs.algorithm.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.junit.BeforeClass;
import org.junit.Test;

public class LaunchJUnit {

    private static Graph graphInsa, graphToulouse;

    private static ArcInspector noFilterByLength, onlyCarsByLength, onlyCarsByTime, onlyPedestrianByTime;

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

        algoEmptyPathInsa = algorithm(new ShortestPathData(graphInsa,graphInsa.get(240),graphInsa.get(383),noFilterByLength));
        algoSingleNodePathToulouse = algorithm(new ShortestPathData(graphToulouse,graphToulouse.get(3167),graphToulouse.get(3167),noFilterByLength));

        algoShortPathInsaNoFilterByLength = algorithm(new ShortestPathData(graphInsa,graphInsa.get(272),graphInsa.get(310),noFilterByLength));
        algoShortPathInsaOnlyCarsByTime = algorithm(new ShortestPathData(graphInsa,graphInsa.get(53),graphInsa.get(63),onlyCarsByTime));
        algoLongPathInsaOnlyCarsByLength = algorithm(new ShortestPathData(graphInsa,graphInsa.get(221),graphInsa.get(361),onlyCarsByLength));
        algoLongPathInsaOnlyPedestrianByTime = algorithm(new ShortestPathData(graphInsa,graphInsa.get(200),graphInsa.get(805),onlyPedestrianByTime));

        algoShortPathToulouseOnlyCarsByLength = algorithm(new ShortestPathData(graphToulouse,graphToulouse.get(1357),graphToulouse.get(1882),onlyCarsByLength));
        algoShortPathToulouseOnlyPedestrianByTime = algorithm(new ShortestPathData(graphToulouse,graphToulouse.get(4533),graphToulouse.get(27040),onlyPedestrianByTime));
        algoLongPathToulouseNoFilterByLengh = algorithm(new ShortestPathData(graphToulouse,graphToulouse.get(639),graphToulouse.get(13700),noFilterByLength));
        algoLongPathToulouseOnlyCarsByTime = algorithm(new ShortestPathData(graphToulouse,graphToulouse.get(639),graphToulouse.get(13700),onlyCarsByTime));


    }

    public static ShortestPathSolution algorithm(ShortestPathData data){
        return (new DijkstraAlgorithm(data)).run();
    }

    @Test
    public void testValidPaths(){
        assertTrue(algoEmptyPathInsa.getPath().isValid());
        assertTrue(algoSingleNodePathToulouse.getPath().isValid());
        assertTrue(algoShortPathInsaNoFilterByLength.getPath().isValid());
        assertTrue(algoShortPathInsaOnlyCarsByTime.getPath().isValid());
        assertTrue(algoLongPathInsaOnlyCarsByLength.getPath().isValid());
        assertTrue(algoLongPathInsaOnlyPedestrianByTime.getPath().isValid());
        assertTrue(algoShortPathToulouseOnlyCarsByLength.getPath().isValid());
        assertTrue(algoShortPathToulouseOnlyPedestrianByTime.getPath().isValid());
        assertTrue(algoLongPathToulouseNoFilterByLengh.getPath().isValid());
        assertTrue(algoLongPathToulouseOnlyCarsByTime.getPath().isValid());
    }

    @Test
    public void testCostCalculated(){
        assertEquals(Double.parseDouble(algoSingleNodePathToulouse.toString().split(" ")[9].replace(",",".")),Double.parseDouble(String.format("%.4f",algoSingleNodePathToulouse.getPath().getLength()/1000).replace(",",".")),0.0001);
        assertEquals(Double.parseDouble(algoShortPathInsaNoFilterByLength.toString().split(" ")[9].replace(",",".")),Double.parseDouble(String.format("%.4f",algoShortPathInsaNoFilterByLength.getPath().getLength()/1000).replace(",",".")),0.0001);
        assertEquals(Double.parseDouble(algoShortPathInsaOnlyCarsByTime.toString().split(" ")[9].replace(",",".")),Double.parseDouble(String.format("%.4f",algoShortPathInsaOnlyCarsByTime.getPath().getMinimumTravelTime()/60).replace(",",".")),0.0001);
        assertEquals(Double.parseDouble(algoLongPathInsaOnlyCarsByLength.toString().split(" ")[9].replace(",",".")),Double.parseDouble(String.format("%.4f",algoLongPathInsaOnlyCarsByLength.getPath().getLength()/1000).replace(",",".")),0.0001);
        assertEquals(Double.parseDouble(algoLongPathInsaOnlyPedestrianByTime.toString().split(" ")[9].replace(",",".")),Double.parseDouble(String.format("%.4f",algoLongPathInsaOnlyPedestrianByTime.getPath().getTravelTime(5)/60).replace(",",".")),0.0001);
        assertEquals(Double.parseDouble(algoShortPathToulouseOnlyCarsByLength.toString().split(" ")[9].replace(",",".")),Double.parseDouble(String.format("%.4f",algoShortPathToulouseOnlyCarsByLength.getPath().getLength()/1000).replace(",",".")),0.0001);
        assertEquals(Double.parseDouble(algoShortPathToulouseOnlyPedestrianByTime.toString().split(" ")[9].replace(",",".")),Double.parseDouble(String.format("%.4f",algoShortPathToulouseOnlyPedestrianByTime.getPath().getTravelTime(5)/60).replace(",",".")),0.0001);
        assertEquals(Double.parseDouble(algoLongPathToulouseNoFilterByLengh.toString().split(" ")[9].replace(",",".")),Double.parseDouble(String.format("%.4f",algoLongPathToulouseNoFilterByLengh.getPath().getLength()/1000).replace(",",".")),0.0001);
        assertEquals(Double.parseDouble(algoLongPathToulouseOnlyCarsByTime.toString().split(" ")[9].replace(",",".")),Double.parseDouble(String.format("%.4f",algoLongPathToulouseOnlyCarsByTime.getPath().getMinimumTravelTime()/60).replace(",",".")),0.0001);
    }

} 