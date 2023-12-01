package uk.ac.ed.inf;

import org.junit.Assert;
import org.junit.Test;


import uk.ac.ed.inf.restHandler.CAreaReceiver;
import uk.ac.ed.inf.restHandler.NoFlyReceiver;
import uk.ac.ed.inf.restHandler.OrderReceiver;
import uk.ac.ed.inf.restHandler.RestaurantReciever;
import uk.ac.ed.inf.ilp.data.*;

import static org.junit.Assert.*;

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

    @Test
    public void inRegionTest(){
        LngLatHandler handler = new LngLatHandler();

        double[][] coordinates = {
                {-3.1876927614212036, 55.94520696732767},
                {-3.187555968761444, 55.9449621408666},
                {-3.186981976032257, 55.94505676722831},
                {-3.1872327625751495, 55.94536993377657},
                {-3.1874459981918335, 55.9453361389472},
                {-3.1873735785484314, 55.94519344934259},
                {-3.1875935196876526, 55.94515665035927},
                {-3.187624365091324, 55.94521973430925},
                {-3.1876927614212036, 55.94520696732767},
                //{-3.189, 55.945}
        };

        // Create an array of LngLat objects
        LngLat[] lngLatArray = new LngLat[coordinates.length];
        for (int i = 0; i < coordinates.length; i++) {
            double lng = coordinates[i][0];
            double lat = coordinates[i][1];
            lngLatArray[i] = new LngLat(lng, lat);
        }

        double[][] coordinates2 = {
                {-3.1876927614212036, 55.94520696732767},
                {-3.187555968761444, 55.9449621408666},
                {-3.186981976032257, 55.94505676722831},
                {-3.1872327625751495, 55.94536993377657},
                {-3.1874459981918335, 55.9453361389472},
                {-3.1873735785484314, 55.94519344934259},
                {-3.1875935196876526, 55.94515665035927},
                {-3.187624365091324, 55.94521973430925},
                {-3.1876927614212036, 55.94520696732767}
        };

        // Create an array of LngLat objects
        LngLat[] lngLatArray2 = new LngLat[coordinates2.length];
        for (int i = 0; i < coordinates2.length; i++) {
            double lng = coordinates2[i][0];
            double lat = coordinates2[i][1];
            lngLatArray2[i] = new LngLat(lng, lat);
        }

        // Create a NamedRegion object for the provided region
        NamedRegion region = new NamedRegion("YourRegionName", lngLatArray2);

        for (LngLat point : lngLatArray){
            assertTrue(handler.isInRegion(point, region));
        }

    }

    @Test
    public void inregionbasic(){
        LngLatHandler handler = new LngLatHandler();

        LngLat[] vertices = {
                new LngLat(0, 1),
                new LngLat(-1, 0),
                new LngLat(0, -1),
                new LngLat(1, 0.1)
        };
        NamedRegion region = new NamedRegion("testRegion", vertices);

        LngLat position = new LngLat(0, 0.1);

        assertTrue(handler.isInRegion(position, region));
    }

    @Test
    public void checkEllis() {
        LngLatHandler handler = new LngLatHandler();
        LngLat[] allPoints = {
                // Dr Elsie Inglis Quadrangle
                new LngLat(-3.187281028691, 55.944559309763),
                new LngLat(-3.187419610621, 55.944616712278),
                new LngLat(-3.187569610621, 55.944616712278),
                new LngLat(-3.187708192551, 55.944674114793),
                new LngLat(-3.187858192551, 55.944674114793),
                new LngLat(-3.187996774481, 55.944731517308),
                new LngLat(-3.188146774481, 55.944731517308),
                new LngLat(-3.188285356411, 55.944788919823),
                new LngLat(-3.188435356411, 55.944788919823),
                new LngLat(-3.188573938341, 55.944846322338),
                new LngLat(-3.188723938341, 55.944846322338),
                new LngLat(-3.188862520271, 55.944903724853),
                new LngLat(-3.189012520271, 55.944903724853),
                new LngLat(-3.189151102201, 55.944961127368),
                new LngLat(-3.189301102201, 55.944961127368),
                new LngLat(-3.189439684131, 55.945018529883),
                new LngLat(-3.189589684131, 55.945018529883),
                new LngLat(-3.189728266061, 55.945075932398),
                new LngLat(-3.189878266061, 55.945075932398),
                new LngLat(-3.190016847991, 55.945133334913),
                new LngLat(-3.190166847991, 55.945133334913),
                new LngLat(-3.190305429921, 55.945190737428),
                new LngLat(-3.190455429921, 55.945190737428),
                new LngLat(-3.190594011851, 55.945248139943),
                new LngLat(-3.190732593781, 55.945305542458),
                new LngLat(-3.190871175711, 55.945362944973),
                new LngLat(-3.191009757641, 55.945420347488),
                new LngLat(-3.191148339571, 55.945477750003),
                new LngLat(-3.1912869215011597, 55.945535152517735),
                new LngLat(-3.1912869215011597, 55.945535152517735),
                new LngLat(-3.1912869215011597, 55.945535152517735),
                new LngLat(-3.191148339571, 55.945477750003),
                new LngLat(-3.191009757641, 55.945420347488),
                new LngLat(-3.190871175711, 55.945362944973),
                new LngLat(-3.190732593781, 55.945305542458),
                new LngLat(-3.190594011851, 55.945248139943),
                new LngLat(-3.190455429921, 55.945190737428),
                new LngLat(-3.190305429921, 55.945190737428),
                new LngLat(-3.190166847991, 55.945133334913),
                new LngLat(-3.190016847991, 55.945133334913),
                new LngLat(-3.189878266061, 55.945075932398),
                new LngLat(-3.189728266061, 55.945075932398),
                new LngLat(-3.189589684131, 55.945018529883),
                new LngLat(-3.189439684131, 55.945018529883),
                new LngLat(-3.189301102201, 55.944961127368),
                new LngLat(-3.189151102201, 55.944961127368),
                new LngLat(-3.189012520271, 55.944903724853),
                new LngLat(-3.188862520271, 55.944903724853),
                new LngLat(-3.188723938341, 55.944846322338),
                new LngLat(-3.188573938341, 55.944846322338),
                new LngLat(-3.188435356411, 55.944788919823),
                new LngLat(-3.188285356411, 55.944788919823),
                new LngLat(-3.188146774481, 55.944731517308),
                new LngLat(-3.187996774481, 55.944731517308),
                new LngLat(-3.187858192551, 55.944674114793),
                new LngLat(-3.187708192551, 55.944674114793),
                new LngLat(-3.187569610621, 55.944616712278),
                new LngLat(-3.187419610621, 55.944616712278),
                new LngLat(-3.187281028691, 55.944559309763),
                new LngLat(-3.187131028691, 55.944559309763),
                new LngLat(-3.186874, 55.944494)
        };

        LngLat[] vertices = {
                new LngLat(-3.1907182931900024, 55.94519570234043),
                new LngLat(-3.1906163692474365, 55.94498241796357),
                new LngLat(-3.1900262832641597, 55.94507554227258),
                new LngLat(-3.190133571624756, 55.94529783810495),
                new LngLat(-3.1907182931900024, 55.94519570234043)
        };

        NamedRegion region = new NamedRegion("area", vertices);

        for (LngLat point : allPoints) {
            //Assert.assertFalse( AStarMazeSolver.checkInNoFlyZone(point, noFlyZones) );
            Assert.assertFalse(handler.isInRegion(point, region));
        }
    }

    @Test
    public void checkisinregiontest(){
        String date = "2023-11-15";
        String url = "https://ilp-rest.azurewebsites.net";

        NoFlyReceiver noFlyReceiver = new NoFlyReceiver();
        CAreaReceiver cAreaReceiver = new CAreaReceiver();
        RestaurantReciever restaurantReciever = new RestaurantReciever();
        OrderReceiver orderReceiver = new OrderReceiver();

        OrderValidator orderValidator = new OrderValidator();

        //Get orders, noflyzones, etc...
        Order[] orders = orderReceiver.orderReceiver(date, url);
        NamedRegion[] noFlyZones = noFlyReceiver.coordReciever(url);
        NamedRegion centralArea = cAreaReceiver.coordReciever(url);
        Restaurant[] restaurants = restaurantReciever.restaurantReceiver(url);

        LngLatHandler handler = new LngLatHandler();

        LngLat point = new LngLat( -3.189124,55.943294);

        System.out.println(handler.isInRegion(point, centralArea));

        for (NamedRegion area : noFlyZones){
            System.out.println(area.name() + ":" + handler.isInRegion(point, area));
        }
    }
}
