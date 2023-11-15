package restHandler;

import com.google.gson.Gson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import uk.ac.ed.inf.ilp.data.Restaurant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;

public class restaurantReciever {
    public static Restaurant[] restReciever(String url){
        ObjectMapper mapper = new ObjectMapper();

        try {
            var temp = new URL(url);
        } catch (Exception x) {
            System.err.println("The URL is invalid: " + x);
            System.exit(2);
        }

        try {
            Restaurant[] restaurants = mapper.readValue(new URL(url + "restaurants"), Restaurant[].class);
            return restaurants;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

}
