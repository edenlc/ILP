package uk.ac.ed.inf.restHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.ac.ed.inf.ilp.data.NamedRegion;

import java.io.IOException;
import java.net.URL;

public class NoFlyReceiver extends BaseReceiver {
    public NamedRegion[] coordReciever(String url){
        //recieves noflyzone data from the rest server
        ObjectMapper mapper = baseReceiver(url);

        try {
            NamedRegion[] coords = mapper.readValue(new URL(url + "/noFlyZones"), NamedRegion[].class);
            return coords;
        } catch (IOException e) {
            //throw new RuntimeException(e);
            System.err.println("Invalid URL");
            System.exit(2);
            throw new RuntimeException(e);
        }
    }
}
