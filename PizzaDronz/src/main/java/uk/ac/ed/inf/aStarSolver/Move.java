package uk.ac.ed.inf.aStarSolver;

import uk.ac.ed.inf.ilp.data.LngLat;

/**
 * Move class representing a move from one LngLat point to another
 */
public class Move {
    private String order = null;
    private double startLng, startLat, angle, endLng, endLat;

    public Move(LngLat startPos, double angle, LngLat endPos){
        this.startLng = startPos.lng();
        this.startLat = startPos.lat();
        this.angle = angle;
        this.endLng = endPos.lng();
        this.endLat = endPos.lat();
    }

    public void setOrder(String order){
        this.order = order;
    }
    public String getOrder(){
        if (order != null){
            return order;
        }
        else{
            return "";
        }
    }
    public double getStartLng(){
        return startLng;
    }
    public double getStartLat(){
        return startLat;
    }
    public double getAngle(){
        return angle;
    }
    public double getEndLng(){
        return endLng;
    }
    public double getEndLat(){
        return endLat;
    }

}
