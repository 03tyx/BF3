package JOJOLAND;

import java.util.*;

public class ThusSpokeRohanKishibe {
    
    private Graph<String, Integer> map;
    private List<String> locations;
    private String currentLocation;

    public ThusSpokeRohanKishibe(Graph<String, Integer> map, String currentLocation) {
        this.map = map;
        this.locations = map.getAllVertexObjects();
        this.currentLocation = currentLocation;
    }

    public void thusSpokeRohanKishibe() {
        System.out.println("List of locations: ");
        for (String location : locations) {
            System.out.printf("%s\n", location);
        }
        System.out.println("");

        List<String> destinations = promptLocationsToVisit();
        System.out.println("=".repeat(200));

        if (destinations != null) {
            List<String> shortestPath = findShortestPath(destinations);
            int totalDistance = calculateTotalDistance(shortestPath);

            System.out.println("Shortest Path:");
            printPath(shortestPath);
            System.out.println("Total Distance: " + totalDistance + " km");
        }

        System.out.println("=".repeat(200));
    }

    public List<String> promptLocationsToVisit() {
        Scanner sc = new Scanner(System.in);
        boolean validInput = false;
        List<String> destinations = new ArrayList<>();

        while (!validInput) {
            System.out.print("Enter the locations Rohan wants to visit (separated by commas): ");
            String input = sc.nextLine();
            String[] locationNames = input.split(",\\s*");
            destinations = new ArrayList<>(Arrays.asList(locationNames));

            // Check if all locations are valid
            boolean invalidLocation = false;
            for (String location : destinations) {
                if (!isValidLocation(location)) {
                    invalidLocation = true;
                    break;
                }
            }

            if (invalidLocation) {
                System.out.println("Invalid input. Please try again.");
            } else {
                validInput = true;
            }
        }

        destinations.add(currentLocation); // Add current location as a destination
        return destinations;
    }

    public boolean isValidLocation(String location) {
        return locations.contains(location);
    }

    public List<String> findShortestPath(List<String> destinations) {
        List<String> shortestPath = new ArrayList<>();
        Vertex<String, Integer> sourceVertex = map.getVertex(currentLocation);
        List<String> allDestinations = new ArrayList<>(destinations);

        // Calculate the shortest path using Dijkstra's algorithm
        while (!allDestinations.isEmpty()) {
            Vertex<String, Integer> destinationVertex = null;
            int minDistance = Integer.MAX_VALUE;

            for (String destination : allDestinations) {
                Vertex<String, Integer> vertex = map.getVertex(destination);
                int distance = dijkstra(sourceVertex, vertex);
                if (distance < minDistance) {
                    minDistance = distance;
                    destinationVertex = vertex;
                }
            }

            if (destinationVertex != null) {
                List<String> path = destinationVertex.shortestPath;
                if (path != null) {
                    shortestPath.addAll(path);
                }
                allDestinations.remove(destinationVertex.vertexInfo);
                sourceVertex = destinationVertex;
            }
        }

        // Add the remaining locations that were not included in the shortest path
        for (String location : destinations) {
            if (!shortestPath.contains(location)) {
                shortestPath.add(location);
            }
        }

        return shortestPath;
    }

    class VertexDistanceComparator implements Comparator<Vertex<String, Integer>> {
        @Override
        public int compare(Vertex<String, Integer> v1, Vertex<String, Integer> v2) {
            return Integer.compare(v1.getDistance(), v2.getDistance());
        }
    }

    private int dijkstra(Vertex<String, Integer> sourceVertex, Vertex<String, Integer> destinationVertex) {
        map.resetVertices();

        PriorityQueue<Vertex<String, Integer>> queue = new PriorityQueue<>(new VertexDistanceComparator());
        sourceVertex.distance = 0;
        queue.add(sourceVertex);

        while (!queue.isEmpty()) {
            Vertex<String, Integer> currentVertex = queue.poll();
            if (currentVertex.visited) {
                continue;
            }

            currentVertex.visited = true;
            if (currentVertex == destinationVertex) {
                return currentVertex.distance;
            }

            for (String neighbor : map.getNeighbours(currentVertex.vertexInfo)) {
                Vertex<String, Integer> neighborVertex = map.getVertex(neighbor);
                int distance = currentVertex.distance + map.getEdgeWeight(currentVertex.vertexInfo, neighbor);

                if (distance < neighborVertex.distance) {
                    neighborVertex.distance = distance;
                    List<String> shortestPaths = new ArrayList<>(currentVertex.shortestPath);
                    shortestPaths.add(currentVertex.vertexInfo);
                    neighborVertex.shortestPath = shortestPaths;
                    queue.add(neighborVertex);
                }
            }
        }

        return Integer.MAX_VALUE;
    }

    private int calculateTotalDistance(List<String> shortestPath) {
        int totalDistance = 0;
        for (int i = 0; i < shortestPath.size() - 1; i++) {
            String source = shortestPath.get(i);
            String destination = shortestPath.get(i + 1);
            int distance = map.getEdgeWeight(source, destination);
            totalDistance += distance;
        }
        return totalDistance;
    }

    private void printPath(List<String> path) {
        StringBuilder sb = new StringBuilder();
        for (String location : path) {
            sb.append(location);
            sb.append(" -> ");
        }
        // Remove -> from the last location
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 4);
        }
        System.out.println(sb.toString());
    }
}
