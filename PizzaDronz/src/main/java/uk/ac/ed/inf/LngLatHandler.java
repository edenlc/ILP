package uk.ac.ed.inf;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.interfaces.LngLatHandling;

import java.lang.Math;
import java.util.Arrays;

public class LngLatHandler implements LngLatHandling{
    @Override
    public double distanceTo(LngLat startPosition, LngLat endPosition) {
        double distance;
        distance = Math.sqrt(Math.pow(startPosition.lng() - endPosition.lng(), 2) +
                             Math.pow(startPosition.lat() - endPosition.lat(), 2));
        return distance;
    }

    @Override
    public boolean isCloseTo(LngLat startPosition, LngLat otherPosition) {
        if (distanceTo(startPosition, otherPosition) < 0.00015 ) {
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean isInRegion(LngLat position, NamedRegion region) {
        return false;
    }

    @Override
    public LngLat nextPosition(LngLat startPosition, double angle) {
        LngLat nextPos;
        double[] validAngles = new double[]{0.0, 22.5, 45.0, 67.5, 90.0, 112.5, 135.0, 157.5,
                                            180.0, 202.5, 247.5, 270.0, 292.5, 315.0, 337.5};

        if (angle == 999) {
            nextPos = startPosition;
        }
        else if (Arrays.stream(validAngles).anyMatch(elem -> elem == angle)){
            nextPos = calcNewPos(startPosition, angle);
        }
        else {
            
        }


        /**
        else {
            for (int i = 0; i < 360; i += 22.5){
                if angle == i
            }
        }
        else if (angle == 0.0){
            nextPos = calcNewPos(startPosition, angle);
        }
        else if (angle == 22.5){
            nextPos = calcNewPos(startPosition, angle);
        }
        else if (angle == 45) {
        }
        else if (angle == 67.5){
        }
        else if (angle == 90.0){
        }
        else if (angle == 112.5){
        }
        else if (angle == 135.0){
        }
        else if (angle == 157.5){
        }
        else if (angle == 180.0){
        }
        else if (angle == 202.5) {
        }
        else if (angle == 225.0){
        }
        else if (angle == 247.5){
        }
        else if (angle == 270.0){
        }
        else if (angle == 292.5){
        }
        else if (angle == 315.0){
        }
        else if (angle == 337.5){
        }
        else {
        }
         **/
        return nextPos;
    }

    private LngLat calcNewPos(LngLat startPos, double angle){
        LngLat newPos;
        newPos = new LngLat(Math.sin(Math.toRadians(angle) * 0.00015), Math.cos(Math.toRadians(angle)) * 0.00015);
        return newPos;
    }


}

