package uk.ac.ed.inf;
import constant2.OrientationCode;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.interfaces.LngLatHandling;

import java.lang.Math;
import java.util.Arrays;

public class LngLatHandler implements LngLatHandling{
    public LngLatHandler(){}

    /**
     * Calculates the distance between two LngLat points. Uses the formula defined in the ILP Spec.
     * @param startPosition One of the two points
     * @param endPosition the other of the two points
     * @return the distance between the two points
     */
    @Override
    public double distanceTo(LngLat startPosition, LngLat endPosition) {
        if (startPosition == null || endPosition == null){
            return 0;
        }

        double distance;
        distance = Math.sqrt(Math.pow(startPosition.lng() - endPosition.lng(), 2) +
                             Math.pow(startPosition.lat() - endPosition.lat(), 2));
        return distance;
    }

    /**
     * Checks if two points are within 0.00015 degrees of each other
     * @param startPosition point 1
     * @param otherPosition point 2
     * @return whether or not two points are close
     */
    @Override
    public boolean isCloseTo(LngLat startPosition, LngLat otherPosition) {
        if (startPosition == null || otherPosition == null){
            return false;
        }

        if (distanceTo(startPosition, otherPosition) < 0.00015 ) {
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Checks if a point is in another region (within 0.00015 degrees of it)
     * @param position the point to be checked
     * @param region the region to be checked
     * @return whether or not the point is inside
     */
    @Override
    public boolean isInRegion(LngLat position, NamedRegion region) {
        if (position == null || region == null){
            return false;
        }

        /**
        Vertices[] in order top left, bottom left, bottom right, top right
        Assume each vertex connects to the ones next to it.
        **/
        int count = 0;

        // Checks intersection between each edge and the line  ((position), (infinity, position.lat))
        // If odd number returns true, else false.
        for (int i = 0; i < region.vertices().length; i++){
            if (intersect(position, region.vertices()[i], region.vertices()[(i+1) % region.vertices().length], true)){
                count++;

                //Checks that a point that is inline with an edge lies on it, else it must be outside the shape.
                if (orientation(region.vertices()[i], position, region.vertices()[(i+1) % region.vertices().length]) == OrientationCode.COLLINEAR){
                    return online(position, region.vertices()[i], region.vertices()[(i+1) % region.vertices().length]);
                }
            }
        }

        if (count % 2 == 1){
            return true;
        }
        else{

        }

        return false;
    }

    /**
     * Helper function to calculate the slope of a line
     * @param vertex1 a points in the line
     * @param vertex2 another point in the line
     * @return the slope of the line.
     */
    private double calculateSlope(LngLat vertex1, LngLat vertex2){
        return (vertex2.lat() - vertex1.lat())/(vertex2.lng() - vertex1.lng());
    }

    /**
     * Helper function to Calculates if a point lies on (close to) a line
     * @param point the point to check
     * @param vertex1 end vertex of the line
     * @param vertex2 the other end vertex of the line
     * @return whether or not the point lies on the line
     */
    private boolean online(LngLat point, LngLat vertex1, LngLat vertex2){
        double slope = calculateSlope(vertex1, vertex2);
        double c = vertex1.lat() - slope * vertex1.lng();

        //Calculates if the point's coordinates fit the equation of the line
        double pointLat = slope * point.lng() + c;

        LngLat point2 = new LngLat(point.lng(), pointLat);
        /** isCloseTo() accounts for distance tolerance **/
        return isCloseTo(point, point2);
        //return (pointLat <= (point.lat() + (1E-12)) && pointLat >= (point.lat() - (1E-12)));
    }

    /**
     * Helper function to calculate the orientation of the two lines formed between 3 points
     * @param point1
     * @param point2
     * @param point3
     * @return whether the lines are collinear, or rotate clockwise or anticlockwise,
     *         used an enum for maximum readability.
     */
    private OrientationCode orientation(LngLat point1, LngLat point2, LngLat point3){
        double dir = (point2.lat() - point1.lat()) * (point3.lng() - point2.lat()) -
                  (point2.lng() - point1.lng()) * (point3.lat() - point2.lat());
        if (dir == 0){
            return OrientationCode.COLLINEAR;
        }
        else if (dir < 0){
            return OrientationCode.ANTICLOCKWISE;
        }
        else{
            return OrientationCode.CLOCKWISE;
        }
    }

    /**
     * Calculates whether two lines intersect. Line 1 is the line formed between the point to check and the point with
     * the same latitude at longitudinal infinity. Line 2 is an edge of a polygon
     * Uses Orientation to check whether the lines are collinear, intersect, or have no intersection.
     * @param point1 The point inside the polygon
     * @param point2 End point 1 of an edge
     * @param point3 Endpoint 2 of an edge
     * @param positive whether we are checking positive or negative infinity. currently only used as positive but may
     *                 later be modified if need be.
     * @return whether they  intersect.
     */
    private boolean intersect(LngLat point1, LngLat point2, LngLat point3, boolean positive ){
        LngLat ptAtInf;
        //LngLat degrees stop at ±180, therefore ±181 is effectively infinity.
        if (positive == true) {
            ptAtInf = new LngLat(181, point1.lat());
        }
        else{
            ptAtInf = new LngLat(-181, point1.lat());
        }

        OrientationCode or1 = orientation(point1, ptAtInf, point2);
        OrientationCode or2 = orientation(point1, ptAtInf, point3);
        OrientationCode or3 = orientation(point2, point3, point1);
        OrientationCode or4 = orientation(point2, point3, ptAtInf);

         /** if the inf line and edge intersect **/
        if (or1 != or2 && or3 != or4){
            return true;
        }
        /** if point 2 is on the inf line **/
        else if (or1 == OrientationCode.COLLINEAR && online(point2, point1, ptAtInf)){
            return true;
        }
        /** if point 3 is on the inf line **/
        else if (or2 == OrientationCode.COLLINEAR && online(point3, point1, ptAtInf)){
            return true;
        }
        /** if point 1 is on the edge **/
        else if (or3 == OrientationCode.COLLINEAR && online(point1, point2, point3)){
            return true;
        }
        /** if point at inf is on the edge **/
        else if (or4 == OrientationCode.COLLINEAR && online(ptAtInf, point2, point3)){
            return true;
        }
        else{
            return false;
        }

    }

    /**
     * Gets the next position of the drone after moving at a specified angle.
     * Achieved via basic trigonometry - sin and cosine.
     * @param startPosition The position we are moving from.
     * @param angle The angle we are moving at.
     * @return The next position of the drone.
     */
    @Override
    public LngLat nextPosition(LngLat startPosition, double angle) {
        if (startPosition == null){
            return null;
        }

        LngLat nextPos;
        double[] validAngles = new double[]{0.0, 22.5, 45.0, 67.5, 90.0, 112.5, 135.0, 157.5,
                                            180.0, 202.5, 225.0, 247.5, 270.0, 292.5, 315.0, 337.5};
        if (angle == 999) {
            nextPos = startPosition;
        }
        //here we use a stream to check if the angle parameter is valid.
        else if (Arrays.stream(validAngles).anyMatch(elem -> elem == angle)){
            nextPos = calcNewPos(startPosition, angle);
        }
        else { //if angle is invalid then just hover.
            nextPos = startPosition;
        }
        return nextPos;
    }

    /**
     * Helper function to get calculate the new postion of the drone. See nextPosition() for details.
     * All params, return same as nextPosition().
     * @param startPos
     * @param angle
     * @return
     */
    private LngLat calcNewPos(LngLat startPos, double angle){
        LngLat newPos;
        newPos = new LngLat((Math.cos(Math.toRadians(angle)) * 0.00015) + startPos.lng(),
                (Math.sin(Math.toRadians(angle)) * 0.00015) + startPos.lat());
        return newPos;
    }


    /**
     *Just a method I used for some manual testing.
     */
    public static void main(String[] args){
        LngLatHandler handler = new LngLatHandler();

        LngLat position = new LngLat(0, 2);

        LngLat tl = new LngLat(-3, 3);
        LngLat bl = new LngLat(0, 0);
        LngLat br = new LngLat(3, 3);
        LngLat tr = new LngLat(0, 6);

        LngLat[] vertices = new LngLat[]{tl, bl, br, tr};

        NamedRegion region = new NamedRegion("Region", vertices);

        boolean isInside = handler.isInRegion(position, region);
        if (isInside){
            System.out.println("Inside! :)");
        }
        else{
            System.out.println("Not inside :/");
        }
    }


}

