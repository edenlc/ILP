package uk.ac.ed.inf;

import org.junit.Assert;
import org.junit.Test;
import uk.ac.ed.inf.ilp.data.LngLat;

import static org.junit.Assert.*;

public class LineTest{

    @Test
    public void getLngTest(){
        LngLat point1 = new LngLat(1 , 0);
        LngLat point2 = new LngLat(0, 0);

        LngLat testpoint = new LngLat(0, 0);

        Line line = new Line(point1, point2);
        System.out.println(line.slope());
        System.out.println(line.getLngForLatOnline(testpoint));
    }

    @Test
    public void onlineTestSlopeNaNT(){
        LngLat point1 = new LngLat(0 , 0);
        LngLat point2 = new LngLat(0, 0);
        Line line = new Line(point1, point2);

        LngLat testPoint = new LngLat(0, 0);

        assertTrue(line.online(testPoint));
    }
    @Test
    public void onlineTestSlopeNaNF(){
        LngLat point1 = new LngLat(0 , 0);
        LngLat point2 = new LngLat(0, 0);
        Line line = new Line(point1, point2);

        LngLat testPoint = new LngLat(1, 0);

        assertFalse(line.online(testPoint));
    }
    @Test
    public void onlineTestSlopeInfT(){
        LngLat point1 = new LngLat(0 , 0);
        LngLat point2 = new LngLat(0, 0);
        Line line = new Line(point1, point2);

        LngLat testPoint = new LngLat(0, 0);

        assertTrue(line.online(testPoint));
    }



}