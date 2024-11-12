import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.IOException;
import java.util.List;


public class BackendTests {
    /**
     * Test that the backend can correctly retrieve values from the graph,
     * and that it can correctly parse a .dot file and insert those values into the graph.
     */
    @Test
    public void roleTest1() {
        GraphADT<String, Double> graph = new Graph_Placeholder();
        Backend backend = new Backend(graph);

        // Check if backend retrieves values from graph correctly.
        List<String> initialGraph = List.of("Union South", "Computer Sciences and Statistics", "Atmospheric, Oceanic and Space Sciences");
        Assertions.assertEquals(initialGraph, backend.getListOfAllLocations());
        // Test insertion of nodes.
        graph.insertNode("Memorial Union");
        Assertions.assertEquals(backend.getListOfAllLocations().get(3), "Memorial Union");
        // Test removal of nodes.
        graph.removeNode("Memorial Union");
        Assertions.assertEquals(initialGraph, backend.getListOfAllLocations());

        // Test the loading of graph data.
        // Can only test whether it correctly captures and inserts the first node on the first line.
        // Can implement a more rigorous test once a full Graph implementation is made.
        try {
            backend.loadGraphData("campus.dot");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(backend.getListOfAllLocations().get(3), "Memorial Union");
    }

    /**
     * Test the retrieval of the locations on shortest paths and the times of shortest paths.
     */
    @Test
    public void roleTest2() {
        GraphADT<String, Double> graph = new Graph_Placeholder();
        Backend backend = new Backend(graph);

        // Test that the locations on shortest path are retrieved from the graph correctly.
        List<String> path = backend.findLocationsOnShortestPath("Union South", "Computer Sciences and Statistics");
        Assertions.assertEquals(List.of("Union South", "Computer Sciences and Statistics"), path);
        // Test that the times on shortest path are retrieved from the graph correctly.
        List<Double> cost = backend.findTimesOnShortestPath("Union South", "Computer Sciences and Statistics");
        Assertions.assertEquals(List.of(1.0), cost);
    }

    /**
     * Test the computation of the ten closest destinations to a node.
     */
    @Test
    public void roleTest3() {
        GraphADT<String, Double> graph = new Graph_Placeholder();
        Backend backend = new Backend(graph);

        // Test that the backend correctly returns the closest nodes in the correct order (ascending).
        List<String> closest = backend.getTenClosestDestinations("Union South");
        Assertions.assertEquals(List.of("Computer Sciences and Statistics", "Atmospheric, Oceanic and Space Sciences"), closest);
    }
}
