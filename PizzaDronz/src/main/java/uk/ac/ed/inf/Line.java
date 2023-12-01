package uk.ac.ed.inf;

import uk.ac.ed.inf.ilp.data.LngLat;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

//Helper class for LngLat handling for a line
public class Line {
    private LngLat point1, point2;
    private double slope, c;  //y=mx+c

    /**
     * Constructor. Uses basic equations for a linear line to calculate slop and c
     * @param endpoint1 a point on the line
     * @param endpoint2 another point on the line
     */
    public Line(LngLat endpoint1, LngLat endpoint2){
        this.point1 = endpoint1;
        this.point2 = endpoint2;
        this.slope = (endpoint2.lat() - endpoint1.lat())/(endpoint2.lng() - endpoint1.lng());
        this.c = endpoint1.lat() - slope * endpoint1.lng();
    }

    public LngLat point1(){return this.point1;}

    public LngLat point2(){return this.point2;}

    public double slope(){return this.slope;}

    /**
     * Gets the longitude for a given latitude on the line
     * assume line stretches to inf
     * @param point the point to check, only the latitude is taken into account
     * @return longitutde
     */
    public double getLngForLatOnline(LngLat point){

        //if horizontal line then we just return point1 on the line's lng.
        //Not technically correct always but is what is needed for the application of the function
        if (slope == 0) {
            return point1.lng();
        }
        // if vertical line then lng always the same
        else if(slope == Double.POSITIVE_INFINITY || slope == Double.NEGATIVE_INFINITY){
            return point1.lng();
        }
        else{
            //get the actual equation. x = (y-c)/slope
            return (point.lat() - c) / slope;
        }
    }

    //Checks if a given point is on the line
    public boolean online(LngLat point){
        if (slope == 0){
            //If colinear horizontally
            if (point.lat() == point1.lat() && point.lat() == point2.lat()){
                List<Double> points = Stream.of(point1.lng(), point2.lng()).toList();
                //Check if lies directly on line
                return point.lng() <= Collections.max(points) && point.lng() >= Collections.min(points);
            }
            else return false;

        }
        else if(slope == Double.POSITIVE_INFINITY || slope == Double.NEGATIVE_INFINITY){
            //If colinear verically
            if (point.lng() == point1.lng() && point.lng() == point2.lng()){
                List<Double> points = Stream.of(point1.lat(), point2.lat()).toList();
                return point.lat() <= Collections.max(points) && point.lat() >= Collections.min(points);
            }
            else return false;
        }
        else if(Double.isNaN(slope)){
            //Nan if point1 == point2, in that case check if point == point1 (== point2)
            return point.lat() == point1.lat() && point.lng() == point1.lng();
        }
        else{
            //Use getlngforlat... to get the lng for the point if it was on the line
            double lng = getLngForLatOnline(point);
            //if the lng is equal to the point's lng it is on the line, else no.
            return lng == point.lng();
        }
    }
}
