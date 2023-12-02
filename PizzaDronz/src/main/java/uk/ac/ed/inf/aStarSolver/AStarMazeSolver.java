package uk.ac.ed.inf.aStarSolver;

import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;

import java.util.*;


import uk.ac.ed.inf.LngLatHandler;

public class AStarMazeSolver {

    private static final double[] DIRS = {0.0, 22.5, 45.0, 67.5, 90.0, 112.5, 135.0, 157.5,
                                          180.0, 202.5, 225.0, 247.5, 270.0, 292.5, 315.0, 337.5};

    // global defined variables for the search
    static PriorityQueue<Cell> openSet;     // frontier
    static HashSet<Cell> closedSet;         // visited
    static List<Cell> path;                 // resulting path
    static PriorityQueue<Cell> tempSet;     /** temporary queue used for adding only the best nodes to the frontier,
                                                reduces branching factor as not having to poll so many nodes,
                                                improving efficiency when pathfinding over long distances
                                            */

    /**
     * Function to find the shortest path between two points adhering to specific conditions
     * @param start start point (restaurant)
     * @param goal end point (delivery point)
     * @param centralArea central area NamedRegion
     * @param noFlyZones Array of noFlyZones
     * @return t/f whether or not a path was found
     */
    private static boolean findShortestPath(Cell start, Cell goal, NamedRegion centralArea,
                                           NamedRegion[] noFlyZones) {

        boolean leftCentralArea = false; //Used to remember whether drone has left central area for this set of moves - cant reeenter

        LngLatHandler handler = new LngLatHandler();

        //Add start node to frontier
        openSet.add(start);

        while (!openSet.isEmpty()){

            //get cell with best fscore, add to visited.
            Cell current = openSet.poll();
            closedSet.add(current);

            //check if close to destination (<0.00015 acceptable as stated in spec).
            if (handler.isCloseTo(current.postition, goal.postition)){
                path = new ArrayList<>();
                while (current != null){
                    path.add(current);
                    current = current.parent;
                }
                Collections.reverse(path);

                return true;
            }


            //If left central area set left to true
            if (!(handler.isInRegion(current.postition, centralArea))){
                leftCentralArea = true;
            }

            //Check the nodes at each branching direction
            for (double dir : DIRS){

                LngLat nextPos = round(handler.nextPosition(current.postition, dir));

                /**
                 * check new node against conditions:
                 *   not in no fly zone
                 *   not reentering central area after leaving
                 *   an identical instance is not already present in the closedset
                 */
                if (!(leftCentralArea && handler.isInRegion(nextPos, centralArea)) &&!checkLineInNoFly(current.postition, dir, noFlyZones)
                    && !closedSet.contains(new Cell(nextPos))){

                    //gscore for new node
                    double tentativeG = current.g + 0.00015;

                    //checks if node already exists at position
                    Cell existing_neighbor = findNeighbor(nextPos);

                    if(existing_neighbor != null){
                        // Check if this path is better than any previously generated path to the neighbor
                        if(tentativeG < existing_neighbor.g){
                            // update cost, parent information
                            existing_neighbor.parent = current;
                            existing_neighbor.angle = dir;
                            existing_neighbor.g = tentativeG;
                            existing_neighbor.h = handler.distanceTo(existing_neighbor.postition, goal.postition);
                            existing_neighbor.f = existing_neighbor.g + existing_neighbor.h;

                            System.out.println("Updating cell at position: (" + existing_neighbor.postition.lng() + ',' + existing_neighbor.postition.lat() + ')');
                        }
                    }
                    else{
                        //creates new cell at position
                        Cell neighbor = new Cell(nextPos);
                        neighbor.parent = current;
                        neighbor.angle = dir;
                        neighbor.g = tentativeG;
                        neighbor.h = handler.distanceTo(neighbor.postition, goal.postition);
                        neighbor.f = neighbor.g + neighbor.h;

                        tempSet.add(neighbor);
                    }
                }
            }

            if (!tempSet.isEmpty()){
                //add the best 3 nodes to the frontier
                for (int i = 0; i < 3; i++) {
                    if (tempSet.size() > 0){
                        Cell node = tempSet.poll();
                        if (node != null) {
                            openSet.add(node);
                        }
                    }
                }
                tempSet.clear();
            }

        }
        return false;
    }

    /**
     * Checks if given position is in a noflyzone
     * @param position position as a LngLat obj
     * @param zones array of noFlyZones as NamedRegion objs
     * @return t/f
     */
    private static boolean checkInNoFlyZone(LngLat position, NamedRegion[] zones){
        LngLatHandler noFlyHandler = new LngLatHandler();;
        for (NamedRegion namedRegion : zones){
            if (noFlyHandler.isInRegion(position, namedRegion)){
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if a move intersects a noflyzone
     * checks at 1/10ths of a line
     * @param p1 the start position
     * @param angle the angle of travel
     * @param zones all noflyzones
     * @return t/f
     */
    private static boolean checkLineInNoFly(LngLat p1, double angle, NamedRegion[] zones){
        double distance = 0.000015;

        for (int i = 1; i <= 10; i++){
            LngLat position = calcNewPos(p1, angle, distance * i);
            if (checkInNoFlyZone(position, zones)){
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates the new position after moving in a direction
     * @param startPos start position
     * @param angle angle of travel
     * @param distance the distance that is being travelled
     * @return new postion as a LngLat object
     */
    private static LngLat calcNewPos(LngLat startPos, double angle, double distance){
        LngLat newPos;
        newPos = new LngLat((Math.cos(Math.toRadians(angle)) * distance) + startPos.lng(),
                (Math.sin(Math.toRadians(angle)) * distance) + startPos.lat());
        return newPos;
    }


    /**
     * Helper function to find and return the neighbour cell.
     * Needed as priorityQueues in java cannot return specific elements.
     * Searches the frontier (openset) to check if a cell with the given position exists already.
     * @param position the position of the neighbour cell
     * @return a cell object for the neighbour, null if doesn't exist.
     */
    private static Cell findNeighbor(LngLat position){
        if(openSet.isEmpty()){
            return null;
        }

        Iterator<Cell> iterator = openSet.iterator();

        Cell find = null;
        while (iterator.hasNext()) {
            Cell next = iterator.next();
            if(next.postition.equals((new Cell(position)).postition)){
                find = next;
                break;
            }
        }
        return find;
    }

    /**
     * Helper function to round positions to the nearest 10^-12.
     * Helps account for rounding errors due to the use of trigonometric functions and doubles in java.
     * @param position the Position whos lng/lat need rounding
     * @return LngLat position with values rounded.
     */
    private static LngLat round(LngLat position){
        double lng = position.lng();
        double lat = position.lat();

        lng =  Math.round(lng * 1e12)/1e12;
        lat =  Math.round(lat * 1e12)/1e12;

        return new LngLat(lng, lat);
    }

    /**
     * Main function to find a path between two points including setup and all
     * @param restaurant restaurant for which delivery to be made to/from
     * @param centralArea central area NamedRegion object
     * @param noflyzones Array of NoFlyzones
     * @param orderno ordernumber for the moves. Currently, will always be an empty string but included for possible future modifications to the system.
     * @return an array of Move objects detailing the path, starting at appleton, to the restuaturant and back.
     *         If no path found, return an array containing a single move - hovering at appleton.
     */
    public static Move[] mazeSolver(LngLat restaurant, NamedRegion centralArea, NamedRegion[] noflyzones, String orderno){

        Cell start = new Cell(new LngLat(-3.186874, 55.944494));
        Cell goal = new Cell(restaurant);


        //Initialise queues and hashset. queues compare on fscore.
        openSet = new PriorityQueue<>(Comparator.comparingDouble(c -> c.f));
        closedSet = new HashSet<>();
        tempSet = new PriorityQueue<>(Comparator.comparingDouble(c -> c.f));

        boolean pathfound;

        if(findShortestPath(start, goal, centralArea, noflyzones)){
            System.out.println("Path count: " + path.size());
            System.out.println("Searched squares count: " + closedSet.size());
            pathfound = true;
        }
        else{
            System.out.println("No path found!");
            pathfound = false;
        }

        //If pathfound return path, else just hover at appleton
        if (pathfound){
            //arrays to store moves/full path
            List<Move> moves = new ArrayList<>();
            List<Move> fullpath = new ArrayList<>();

            //Converts path to a list of move objects
            for (int i = 0; i < path.size() - 2; i++) {
                moves.add(new Move(path.get(i).postition, path.get(i + 1).angle, path.get(i + 1).postition));
            }
            //Reverses for route back
            List<Move> movesReversed = new ArrayList<>();
            //reverse start and end lats for each, get opposite angle
            for (Move move : moves){
                LngLat startPos = new LngLat(move.getEndLng(), move.getEndLat());
                LngLat endPos = new LngLat(move.getStartLng(), move.getStartLat());
                double angle = (move.getAngle() + 180) % 360;

                Move newMove = new Move(startPos, angle, endPos);
                movesReversed.add(newMove);

            }
            Collections.reverse(movesReversed);


            LngLat closetogoal = new LngLat(movesReversed.get(0).getStartLng(), movesReversed.get(0).getStartLat());

            //go from appleton to restaurant, hover at restaurant, go back to appleton with same path, hover at appleton
            fullpath.addAll(moves);
            fullpath.add(new Move(closetogoal, 999, closetogoal));
            fullpath.addAll(movesReversed);
            fullpath.add(new Move(start.postition, 999, start.postition));

            Move[] fullPath = fullpath.toArray(new Move[fullpath.size()]);

            for (Move move : fullPath) {
                move.setOrder(orderno);
            }
            return fullPath;
        }
        else{
            Move hover[] =  new Move[]{new Move(start.postition, 999, start.postition)};
            return hover;
        }
    }
}
