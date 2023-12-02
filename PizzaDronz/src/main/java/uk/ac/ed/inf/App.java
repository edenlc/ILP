package uk.ac.ed.inf;

import uk.ac.ed.inf.aStarSolver.AStarMazeSolver;
import uk.ac.ed.inf.aStarSolver.Move;
import uk.ac.ed.inf.ilp.constant.OrderStatus;
import uk.ac.ed.inf.ilp.constant.OrderValidationCode;
import uk.ac.ed.inf.ilp.data.NamedRegion;
import uk.ac.ed.inf.ilp.data.Order;
import uk.ac.ed.inf.ilp.data.Restaurant;
import uk.ac.ed.inf.outPutters.DroneGeoGenerator;
import uk.ac.ed.inf.outPutters.OutPutDeliveries;
import uk.ac.ed.inf.outPutters.OutPutFlighpaths;
import uk.ac.ed.inf.restHandler.CAreaReceiver;
import uk.ac.ed.inf.restHandler.NoFlyReceiver;
import uk.ac.ed.inf.restHandler.OrderReceiver;
import uk.ac.ed.inf.restHandler.RestaurantReciever;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.*;

/**
 * Hello world!
 *
 */
public class App {
    /**
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
     */
    public static void main(String[] args){
        try {
            String date = args[0];
            String url = args[1];
            //Used to exit the system should it take longer than 60 seconds to execute


            //Set up instances for restRecievers and orderValidator
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
            //Initialise an empty list of moves, this will store all the generated moves for the day.
            List<Move> movesList = new ArrayList<>();
            //Initialise an empty Hashmap of flightpaths, this will store the flightpaths to and from each restaurant.
            HashMap<String, Move[]> flightMaps = new HashMap<>();

            //Generate a flightpath to and from each restuarant
            for (Restaurant restaurant : restaurants) {
                System.out.println("Finding path for " + restaurant.name());
                Move[] moves = AStarMazeSolver.mazeSolver(restaurant.location(), centralArea, noFlyZones, "");
                flightMaps.put(restaurant.name(), moves);

            }

            /**
             * For each order, checks if valid or not.
             * If valid:
             *   sets flighpath for order and stores in moveslist
             *   sets orderstatus to delivered
             * else:
             *   sets orderstatus to invalid
             */
            for (Order order : orders) {
                orderValidator.validateOrder(order, restaurants);

                if (order.getOrderValidationCode() == OrderValidationCode.NO_ERROR) {
                    order.setOrderStatus(OrderStatus.VALID_BUT_NOT_DELIVERED);
                    Restaurant restaurant = orderValidator.findRestaurant(order, restaurants);
                    Move[] moves = flightMaps.get(restaurant.name());
                    for (Move move : moves) {
                        //for each move add the current ordernumber
                        move.setOrder(order.getOrderNo());
                    }
                    movesList.addAll(Arrays.stream(moves).toList());
                    order.setOrderStatus(OrderStatus.DELIVERED);
                } else {
                    order.setOrderStatus(OrderStatus.INVALID);
                }
            }

            //sets moveslist to an array for producing output files
            Move[] movesArray = movesList.toArray(new Move[movesList.size()]);
            System.out.println("Number of moves total: " + movesArray.length);

            //generate output data.
            OutPutFlighpaths.generateFlighpaths(date, movesArray);
            DroneGeoGenerator.generateDroneData(date, movesArray);
            OutPutDeliveries.generateDeliveries(date, orders);
        } catch (ArrayIndexOutOfBoundsException e){
            System.err.println("No or Invalid Arguments Provided");
        }



    }
}
