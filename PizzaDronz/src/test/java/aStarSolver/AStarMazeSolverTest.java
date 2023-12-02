package aStarSolver;


import org.junit.Assert;
import org.junit.Test;
import uk.ac.ed.inf.aStarSolver.AStarMazeSolver;
import uk.ac.ed.inf.restHandler.CAreaReceiver;
import uk.ac.ed.inf.restHandler.NoFlyReceiver;
import uk.ac.ed.inf.restHandler.OrderReceiver;
import uk.ac.ed.inf.restHandler.RestaurantReciever;
import uk.ac.ed.inf.OrderValidator;
import uk.ac.ed.inf.ilp.data.LngLat;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;

import static org.junit.Assert.assertEquals;

public class AStarMazeSolverTest{

    NoFlyReceiver noFlyReceiver = new NoFlyReceiver();
    NamedRegion[] noFlyZones = noFlyReceiver.coordReciever("https://ilp-rest.azurewebsites.net");

    /**
    @Test
    public void checkCArea(){
        LngLat[] allPoints = {
                //Central area
                new LngLat(-3.188, 55.944),
                new LngLat(-3.187, 55.944),
                new LngLat(-3.186, 55.945),
                new LngLat(-3.188, 55.945),
                new LngLat(-3.187, 55.943),
                new LngLat(-3.186, 55.944)
        };


        for (LngLat point : allPoints){
            System.out.println("(" + point.lng() + "," + point.lat() + ")");
            Assert.assertTrue( AStarMazeSolver.checkInNoFlyZone(point, noFlyZones) );
        }
    }
    @Test
    public void checkGSArea(){
        LngLat[] allPoints = {
                // George Square Area
                new LngLat(-3.188097311019897, 55.94328811724263),
                new LngLat(-3.188681032575143, 55.943477740393744),
                new LngLat(-3.189578818321228, 55.94402412577528),
        };

        for (LngLat point : allPoints){
            Assert.assertTrue(AStarMazeSolver.checkInNoFlyZone(point, noFlyZones) );
        }
    }
    @Test
    public void checkEllis(){
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

        for (LngLat point : allPoints){
            //Assert.assertFalse( AStarMazeSolver.checkInNoFlyZone(point, noFlyZones) );
            Assert.assertFalse( AStarMazeSolver.checkInNoFlyZone(point, new NamedRegion[]{region}) );
        }
    }
    @Test
    public void checkBristo(){
        LngLat[] allPoints = {
                // Bristo Square Open Area
                new LngLat(-3.189543485641479, 55.94552313663306),
                new LngLat(-3.189382553100586, 55.94553214854692),
                new LngLat(-3.189259171485901, 55.94544803726933),
                new LngLat(-3.1892001628875732, 55.94533688994374),
                new LngLat(-3.189194798469543, 55.94519570234043),
                new LngLat(-3.189135789871216, 55.94511759833873),
                new LngLat(-3.188138008117676, 55.9452738061846),
                new LngLat(-3.1885510683059692, 55.946105902745614),
                new LngLat(-3.1895381212234497, 55.94555918427592),
                new LngLat(-3.189543485641479, 55.94552313663306)
        };

        for (LngLat point : allPoints){
            Assert.assertTrue( AStarMazeSolver.checkInNoFlyZone(point, noFlyZones) );
        }
    }
     */
    @Test
    public void checkFlighpath(){
        AStarMazeSolver solver = new AStarMazeSolver();

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

        LngLat testloc1 = new LngLat(-3.186874 - 0.00015*10, 55.944494 - 0.00015*30);
        LngLat testloc2 = new LngLat(-3.191097311019897, 55.94328811724263);
        LngLat halal_pizza = new LngLat(-3.185428203143916, 55.945846113595);
        LngLat civerinos = new LngLat(-3.1912869215011597, 55.945535152517735);
        LngLat dominos = new LngLat(-3.1838572025299072, 55.94449876875712);
        LngLat sorralella = new LngLat(-3.202541470527649, 55.943284737579376);
        LngLat trattoria = new LngLat(-3.1810810679852035, 	55.938910643735845);
        LngLat sodeberg = new LngLat(-3.1940174102783203, 	55.94390696616939);

        Assert.assertNotNull(solver.mazeSolver(sodeberg, centralArea, noFlyZones, ""));
    }

}