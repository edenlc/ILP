package uk.ac.ed.inf.restHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.ac.ed.inf.ilp.data.NamedRegion;

import java.io.IOException;
import java.net.URL;

public class CAreaReceiver extends BaseReceiver {
    public NamedRegion coordReciever(String url) {
        //Recieves central area data from the rest server
        ObjectMapper mapper = baseReceiver(url);

        try {
            NamedRegion coords = mapper.readValue(new URL(url + "/centralArea"), NamedRegion.class);
            return coords;
        } catch (IOException e) {
            System.err.println("Invalid URL");
            System.exit(2);
            throw new RuntimeException(e);
        }
    }
}

