package uk.ac.ed.inf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;


import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.*;

public class LngLatHandlerTest {

    @Test
    public void lltest(){
        LngLatHandler handler = new LngLatHandler();

        LngLat position = new LngLat(0, 2);
        LngLat tl = new LngLat(-3, 3);
        LngLat bl = new LngLat(0, 0);
        LngLat br = new LngLat(3, 3);
        LngLat tr = new LngLat(0, 6);

        LngLat[] vertices = new LngLat[]{tl, bl, br, tr};

        NamedRegion region = new NamedRegion("Region", vertices);

        assertEquals(handler.isInRegion(position, region), true);

        position = new LngLat(0, 0);

        //Check invalid angle
        assertEquals(handler.nextPosition(position, 15), position);

        //Check movement at angle 0.
        assertEquals(handler.nextPosition(position, 0).lng(), 0.00015, 1E-12);
        assertEquals(handler.nextPosition(position, 0).lat(), 0, 1E-12);

        //Check movement at angle 22.5.
        assertEquals(handler.nextPosition(position, 22.5).lng(), 0.0001385819299, 1E-12);
        assertEquals(handler.nextPosition(position, 22.5).lat(), 0.0000574025148, 1E-12);

        //Check movement at angle 45.
        assertEquals(handler.nextPosition(position, 45).lng(), 0.0001060660172, 1E-12);
        assertEquals(handler.nextPosition(position, 45).lat(), 0.0001060660172, 1E-12);

        //Check movement at angle 90
        assertEquals(handler.nextPosition(position, 90).lng(), 0, 1E-12);
        assertEquals(handler.nextPosition(position, 90).lat(), 0.00015, 1E-12);

        //Check movement at angle 135.
        assertEquals(handler.nextPosition(position, 135).lng(), -0.0001060660172, 1E-12);
        assertEquals(handler.nextPosition(position, 135).lat(), 0.0001060660172, 1E-12);

        //Check movement at angle 180.
        assertEquals(handler.nextPosition(position, 180).lng(), -0.00015, 1E-12);
        assertEquals(handler.nextPosition(position, 180).lat(), 0, 1E-12);


        //Check movement at angle 225.
        assertEquals(handler.nextPosition(position, 225).lng(), -0.0001060660172, 1E-12);
        assertEquals(handler.nextPosition(position, 225).lat(), -0.0001060660172, 1E-12);


        //Check movement at angle 270
        assertEquals(handler.nextPosition(position, 270).lng(), 0, 1E-12);
        assertEquals(handler.nextPosition(position, 270).lat(), -0.00015, 1E-12);

        //Check movement at angle 315.
        assertEquals(handler.nextPosition(position, 315).lng(), 0.0001060660172, 1E-12);
        assertEquals(handler.nextPosition(position, 315).lat(), -0.0001060660172, 1E-12);

    }
}
