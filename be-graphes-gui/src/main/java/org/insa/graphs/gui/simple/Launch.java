package org.insa.graphs.gui.simple;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.nio.file.Files;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.insa.graphs.algorithm.ArcInspector;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.gui.drawing.Drawing;
import org.insa.graphs.gui.drawing.components.BasicDrawing;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.model.io.PathReader;

public class Launch {

    /**
     * Create a new Drawing inside a JFrame an return it.
     *
     * @return The created drawing.
     * @throws Exception if something wrong happens when creating the graph.
     */


    public static Drawing createDrawing() throws Exception {
        BasicDrawing basicDrawing = new BasicDrawing();
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("BE Graphes - Launch");
                frame.setLayout(new BorderLayout());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                frame.setSize(new Dimension(800, 600));
                frame.setContentPane(basicDrawing);
                frame.validate();
            }
        });
        return basicDrawing;
    }

    public static void main(String[] args) throws Exception {

        // visit these directory to see the list of available files on commetud.
        final String mapName =
                "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr";
        final String pathName =
                "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Paths/path_fr31insa_rangueil_r2.path";

        final Graph graph;
        final Path path;

        // create a graph reader
        try (final GraphReader reader = new BinaryGraphReader(new DataInputStream(
                new BufferedInputStream(new FileInputStream(mapName))))) {

            // TODO: read the graph
            graph = reader.read();
        }

        // create the drawing
        final Drawing drawing = createDrawing();

        // TODO: draw the graph on the drawing
        drawing.drawGraph(graph);

        // TODO: create a path reader
        try (final PathReader pathReader = new BinaryPathReader(new DataInputStream(new FileInputStream(pathName)))) {

            // TODO: read the path
            path = pathReader.readPath(graph);
        }

        // TODO: draw the path on the drawing
        drawing.drawPath(path);
        // création de data pour la graphe
        ArcInspector arcI = ArcInspectorFactory.getAllFilters().get(0);
        //chemin inexistant 
        ShortestPathData data = new ShortestPathData(graph,graph.get(609),graph.get(1000),arcI);
        //chemin normal
        ShortestPathData data1 = new ShortestPathData(graph,path.getOrigin(),path.getDestination(),arcI);
        //chemin de longueur nulle
        ShortestPathData data2 = new ShortestPathData(graph,path.getOrigin(),path.getOrigin(),arcI);
        //chemin long 
        ShortestPathData data3 = new ShortestPathData(graph,graph.get(839),graph.get(227),arcI);

        DijkstraAlgorithm dijstra = new DijkstraAlgorithm(data);

        ShortestPathSolution solution = dijstra.run();

        Path pathS = solution.getPath();
        //Si le path n'est pas valide
        if(!pathS.isValid()){
            System.out.println("Le path n'est pas valide !");
        }

        //Bellmanford

    }

}
