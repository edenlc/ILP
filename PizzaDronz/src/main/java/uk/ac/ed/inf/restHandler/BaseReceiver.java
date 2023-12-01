package uk.ac.ed.inf.restHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.MalformedURLException;
import java.net.URL;

//Basereciever class that all other recievers inherit from.
public class BaseReceiver {
    //Creates an object mapper object and checks baseurl valid.s
    public static ObjectMapper baseReceiver(String baseurl){
        String url =baseurl;

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        try {
            var temp = new URL(url);
        } catch (MalformedURLException e) {
            System.err.println("Invalid URL");
            System.exit(2);
        }

        return mapper;
    }
}
