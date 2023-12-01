package uk.ac.ed.inf.restHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.ac.ed.inf.ilp.data.Order;


import java.io.IOException;
import java.net.URL;

public class OrderReceiver extends BaseReceiver {
    public Order[] orderReceiver(String date, String url) {
        //used to receive the orders from the rest server
        ObjectMapper mapper = baseReceiver(url);

        try {
            Order[] orders = mapper.readValue(new URL(url + "/orders/" + date), Order[].class);
            return orders;
        } catch (IOException e) {
            System.err.println("Invalid URL and/or date");
            System.exit(2);
            throw new RuntimeException(e);
        }


    }
}
