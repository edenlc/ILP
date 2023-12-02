package uk.ac.ed.inf;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.interfaces.LngLatHandling;

import java.lang.Math;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class LngLatHandler implements LngLatHandling {
    public LngLatHandler() {
    }

    /**
     * Calculates the distance between two LngLat points. Uses the formula defined in the ILP Spec.
     *
     * @param startPosition One of the two points
     * @param endPosition   the other of the two points
     * @return the distance between the two points
     */
    @Override
    public double distanceTo(LngLat startPosition, LngLat endPosition) {
        if (startPosition == null || endPosition == null) {
            return 0;
        }

        double distance;
        distance = Math.sqrt(Math.pow(startPosition.lng() - endPosition.lng(), 2) +
                Math.pow(startPosition.lat() - endPosition.lat(), 2));
        return distance;
    }

    /**
     * Checks if two points are within 0.00015 degrees of each other
     *
     * @param startPosition point 1
     * @param otherPosition point 2
     * @return whether or not two points are close
     */
    @Override
    public boolean isCloseTo(LngLat startPosition, LngLat otherPosition) {
        if (startPosition == null || otherPosition == null) {
            return false;
        }

        if (distanceTo(startPosition, otherPosition) < 0.00015) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * Checks if a point is in another region
     *
     * @param position the point to be checked
     * @param region   the region to be checked
     * @return whether or not the point is inside
     */
    @Override
    public boolean isInRegion(LngLat position, NamedRegion region) {
        /**
         * Start by checking that point is not above highest lat
         * then not below lowest lng
         * then that it is not to the right of the rightmost point.
         *
         * after that go edge by edge:
         * if position lies at max/min lat then it must be on a line or a vertice to be considered.
         * check that if position has same lat as both parts of point, it is online, else ignore.
         *
         * check the point that intersects between a line from -inf to inf lng with lat of position.
         * if position lies to the left of that point then it intersects, otherwise no.
         * if intersects off then it is good.
         */

        LngLat[] vertices = region.vertices();
        List<Double> lngs = Arrays.stream(vertices).map(LngLat::lng).toList();
        List<Double> lats = Arrays.stream(vertices).map(LngLat::lat).toList();

        //If above max, below min lat, or to the right of max lng then is outside
        if (position.lat() > Collections.max(lats) || position.lat() < Collections.min(lats) ||
                position.lng() > Collections.max(lngs)) {
            return false;
        }


        //If the point is on any of the vertices/eddges
        for (int i = 0; i < vertices.length - 2; i++) {
            if ((new Line(vertices[i], vertices[i + 1])).online(position)) {
                return true;
            }
        }
        int intersects = 0;
        //If not we now check intersects
        for (int i = 0; i < vertices.length - 1; i++) {
            if ((new Line(vertices[i], vertices[i + 1])).getLngForLatOnline(position) > position.lng()) {
                List<Double> points = Stream.of(vertices[i].lat(), vertices[i + 1].lat()).toList();
                //If inbetween those points then it must be on the edge
                if (position.lat() <= Collections.max(points) && position.lat() >= Collections.min(points)) {
                    intersects++;
                }
            }
        }

        return intersects % 2 == 1;
    }


    /**
     * Helper function to calculate the slope of a line
     *
     * @param vertex1 a points in the line
     * @param vertex2 another point in the line
     * @return the slope of the line.
     */
    private double calculateSlope(LngLat vertex1, LngLat vertex2) {
        return (vertex2.lat() - vertex1.lat()) / (vertex2.lng() - vertex1.lng());
    }

    /**
     * Helper function to Calculates if a point lies on (close to) a line
     *
     * @param point   the point to check
     * @param vertex1 end vertex of the line
     * @param vertex2 the other end vertex of the line
     * @return whether or not the point lies on the line
     */
    private boolean online(LngLat point, LngLat vertex1, LngLat vertex2) {
        double slope = calculateSlope(vertex1, vertex2);
        double c = vertex1.lat() - slope * vertex1.lng();

        //Calculates if the point's coordinates fit the equation of the line
        double pointLat = slope * point.lng() + c;

        LngLat point2 = new LngLat(point.lng(), pointLat);
        /** isCloseTo() accounts for distance tolerance **/
        return isCloseTo(point, point2);
    }


    /**
     * Gets the next position of the drone after moving at a specified angle.
     * Achieved via basic trigonometry - sin and cosine.
     *
     * @param startPosition The position we are moving from.
     * @param angle         The angle we are moving at.
     * @return The next position of the drone.
     */
    @Override
    public LngLat nextPosition(LngLat startPosition, double angle) {
        if (startPosition == null) {
            return null;
        }

        LngLat nextPos;
        double[] validAngles = new double[]{0.0, 22.5, 45.0, 67.5, 90.0, 112.5, 135.0, 157.5,
                180.0, 202.5, 225.0, 247.5, 270.0, 292.5, 315.0, 337.5};
        if (angle == 999) {
            nextPos = startPosition;
        }
        //here we use a stream to check if the angle parameter is valid.
        else if (Arrays.stream(validAngles).anyMatch(elem -> elem == angle)) {
            nextPos = calcNewPos(startPosition, angle);
        } else { //if angle is invalid then just hover.
            nextPos = startPosition;
        }
        return nextPos;
    }

    /**
     * Helper function to get calculate the new postion of the drone. See nextPosition() for details.
     * All params, return same as nextPosition().
     *
     * @param startPos
     * @param angle
     * @return
     */
    private LngLat calcNewPos(LngLat startPos, double angle) {
        LngLat newPos;
        newPos = new LngLat((Math.cos(Math.toRadians(angle)) * 0.00015) + startPos.lng(),
                (Math.sin(Math.toRadians(angle)) * 0.00015) + startPos.lat());
        return newPos;
    }
}