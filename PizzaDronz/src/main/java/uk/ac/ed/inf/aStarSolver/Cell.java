package uk.ac.ed.inf.aStarSolver;

import uk.ac.ed.inf.ilp.data.LngLat;

import java.util.Objects;

public class Cell {

    LngLat postition;   // cell position


    double f, g, h, angle;    /** A* algorithm value parameters
                                  fscore = g + h
                                  gscore = distance from start point
                                  h      = distance to goal
                                */
    Cell parent;    // parent record: come from

    public Cell(LngLat position) {
        this.postition = position;
        parent = null;
        f = 0;
        g = 0;
        h = 0;
    }

    @Override
    public int hashCode(){
        return Objects.hash(postition.lat(), postition.lng());
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }

        if(obj == null || getClass() != obj.getClass()){
            return false;
        }

        Cell other = (Cell)obj;
        return this.postition.lng() == other.postition.lng() && this.postition.lat() == other.postition.lat();

    }

}
