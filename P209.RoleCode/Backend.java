import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Backend implements BackendInterface {

    private final GraphADT<String, Double> graph;

    /**
     * Public Constructor
     *
     * @param graph object to store the backend's graph data
     */
    public Backend(GraphADT<String,Double> graph) {
        this.graph = graph;
    }
    /**
     * Loads graph data from a dot file.  If a graph was previously loaded, this
     * method should first delete the contents (nodes and edges) of the existing
     * graph before loading a new one.
     *
     * @param filename the path to a dot file to read graph data from
     * @throws IOException if there was any problem reading from this file
     */
    @Override
    public void loadGraphData(String filename) throws IOException {
        // Clear the graph if it has any previous data.
        for (String node : graph.getAllNodes()) {
            graph.removeNode(node);
        }
        Scanner scnr;
        try {
            // Open the .dot file with a Scanner.
            scnr = new Scanner(new File(filename));
        } catch (FileNotFoundException e) {
            throw new IOException("No such file found.");
        }
        // Skip first line of .dot file.
        scnr.nextLine();
        // Declaration of temp variables.
        String line, nodeFrom, nodeTo;
        Double edgeWeight;
        // Beautiful regex that finds the three parts of each line we want: node from, node to, and edge weight.
        Pattern regex = Pattern.compile("((?<=\")[\\w .,\\-'&()]*(?=\"))|((?<=\\[seconds=)[0-9.]*(?=\\]))");
        // Iterate over each line of .dot file.
        while (scnr.hasNext()) {
            line = scnr.nextLine();
            // Skip last line of .dot file.
            if (line.contains("}")) {
                continue;
            }
            // Create a Matcher for the current line using our regex Pattern.
            Matcher matcher = regex.matcher(line);
            // Match the part in "" before ->
            if (!matcher.find()) throw new IOException("Couldn't find first \"\" match.");
            nodeFrom = matcher.group(1);
            // Match the part in "" after ->
            if (!matcher.find()) throw new IOException("Couldn't find second \"\" match.");
            nodeTo = matcher.group(1);
            // Match the [seconds=] property.
            if (!matcher.find()) throw new IOException("Couldn't find [seconds=] match in:\n" + line);
            edgeWeight = Double.parseDouble(matcher.group(2));

            // Insert nodes and edges into graph if they don't already exist.
            if (!graph.containsNode(nodeFrom)) graph.insertNode(nodeFrom);
            if (!graph.containsNode(nodeTo)) graph.insertNode(nodeTo);
            if (!graph.containsEdge(nodeFrom, nodeTo)) graph.insertEdge(nodeFrom, nodeTo, edgeWeight);
        }
    }

    /**
     * Returns a list of all locations (node data) available in the graph.
     *
     * @return list of all location names
     */
    @Override
    public List<String> getListOfAllLocations() {
        return graph.getAllNodes();
    }

    /**
     * Return the sequence of locations along the shortest path from
     * startLocation to endLocation, or an empty list if no such path exists.
     *
     * @param startLocation the start location of the path
     * @param endLocation   the end location of the path
     * @return a list with the nodes along the shortest path from startLocation
     * to endLocation, or an empty list if no such path exists
     */
    @Override
    public List<String> findLocationsOnShortestPath(String startLocation, String endLocation) {
        try {
            return graph.shortestPathData(startLocation, endLocation);
        } catch (NoSuchElementException e) {
            return List.of();
        }
    }

    /**
     * Return the walking times in seconds between each two nodes on the
     * shortest path from startLocation to endLocation, or an empty list of no
     * such path exists.
     *
     * @param startLocation the start location of the path
     * @param endLocation   the end location of the path
     * @return a list with the walking times in seconds between two nodes along
     * the shortest path from startLocation to endLocation, or an empty
     * list if no such path exists
     */
    @Override
    public List<Double> findTimesOnShortestPath(String startLocation, String endLocation) {
        List<String> path;
        ArrayList<Double> costs = new ArrayList<>();
        try {
            path = graph.shortestPathData(startLocation, endLocation);
        } catch (NoSuchElementException e) {
            return List.of();
        }
        for (int i = 0; i < path.size()-1; i++) {
            costs.add(graph.getEdge(path.get(i), path.get(i+1)));
        }
        return costs;
    }

    /**
     * Returns a list of the ten closest destinations that can be reached most
     * quickly when starting from the specified startLocation.
     *
     * @param startLocation the location to find the closest destinations from
     * @return the ten closest destinations from the specified startLocation
     * @throws NoSuchElementException if startLocation does not exist, or if
     *                                there are no other locations that can be reached from there
     */
    @Override
    public List<String> getTenClosestDestinations(String startLocation) throws NoSuchElementException {
        // If the graph doesn't have node "startLocation" throw error.
        if (!graph.containsNode(startLocation)) throw new NoSuchElementException("Graph does not contain that location.");
        ArrayList<Map.Entry<String, Double>> list = new ArrayList<>();
        // Get the list of all nodes in graph and remove the node we are searching from.
        List<String> nodes = graph.getAllNodes();
        nodes.remove(startLocation);
        // Loop through each node and calculate its shortest path cost from startLocation.
        for (String node : nodes) {
            try {
                // Add each (node, cost) pair to list as an Entry.
                list.add(Map.entry(node, graph.shortestPathCost(startLocation, node)));
            } catch (NoSuchElementException e) {
            }
        }
        // Sort the list by the costs (ascending).
        list.sort(Map.Entry.comparingByValue());
        List<String> tenClosest = new ArrayList<>();
        // Create a list from the first 10 nodes of the sorted entry list.
        for (int i = 0; i < list.size(); i++) {
            // Break out of the loop if we've added ten nodes.
            if (i > 9) {
                break;
            }
            tenClosest.add(list.get(i).getKey());
        }
        return tenClosest;
    }
}
