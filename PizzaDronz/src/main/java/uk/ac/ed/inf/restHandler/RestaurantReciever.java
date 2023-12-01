package uk.ac.ed.inf.restHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import uk.ac.ed.inf.ilp.data.Restaurant;

import java.io.IOException;
import java.net.URL;

public class RestaurantReciever extends BaseReceiver {
    public Restaurant[] restaurantReceiver(String url){
        //Used to recieve restuarant data from the rest server
        ObjectMapper mapper = baseReceiver(url);

        try {
            Restaurant[] orders = mapper.readValue(new URL(url + "/restaurants"), Restaurant[].class);
            return orders;
        } catch (IOException e) {
            System.err.println("Invalid URL");
            System.exit(2);
            throw new RuntimeException(e);
        }


    }
}
