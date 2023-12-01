package uk.ac.ed.inf.outPutters;

import uk.ac.ed.inf.aStarSolver.Move;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

public class DroneGeoGenerator {
    public static void generateDroneData(String date, Move[] moves){

        // Create a GeoJSON feature collection with one LineString feature
        ObjectNode featureCollection = JsonNodeFactory.instance.objectNode();
        featureCollection.put("type", "FeatureCollection");

        ArrayNode features = JsonNodeFactory.instance.arrayNode();
        featureCollection.set("features", features);

        ObjectNode feature = JsonNodeFactory.instance.objectNode();
        feature.put("type", "Feature");

        ObjectNode geometry = JsonNodeFactory.instance.objectNode();
        geometry.put("type", "LineString");

        ObjectNode properties = JsonNodeFactory.instance.objectNode();
        properties.put("stroke", "#FF0000");
        feature.set("properties", properties);

        ArrayNode moveArray = JsonNodeFactory.instance.arrayNode();

        try {
            //Add the inital point
            //If inital point does not exist an due to no moves existing we need to handle this.
            //We still want to produce an output file, it will just be blank.
            ArrayNode initialPoint = JsonNodeFactory.instance.arrayNode();
            initialPoint.add(moves[0].getStartLng());
            initialPoint.add(moves[0].getStartLat());
            moveArray.add(initialPoint);
        }
        catch(ArrayIndexOutOfBoundsException e){
            System.out.println("No moves were generated, likely no orders for this date!");
        }
        finally {


            //Add all remaining points
            for (Move move : moves) {
                ArrayNode point = JsonNodeFactory.instance.arrayNode();
                point.add(move.getEndLng());
                point.add(move.getEndLat());
                moveArray.add(point);
            }
            geometry.set("coordinates", moveArray);

            feature.set("geometry", geometry);
            features.add(feature);


            try {
                //Create folder in parent directory of the project to store resultsFiles
                String currentDirectoryPath = System.getProperty("user.dir");
                File currentDirectory = new File(currentDirectoryPath).getParentFile();
                File resultsFilesDirectory = new File(currentDirectory, "resultsFiles");

                if (!resultsFilesDirectory.exists()) {
                    if (resultsFilesDirectory.mkdir()) {
                        System.out.println("Created 'resultsFiles' directory.");
                    } else {
                        System.err.println("Failed to create 'resultsFiles' directory.");
                    }
                } else {
                    System.out.println("'resultsFiles' directory already exists");
                }
                //Create file inside resultsfiles
                ObjectMapper objectMapper = new ObjectMapper();
                File outputFile = new File(resultsFilesDirectory.getAbsolutePath(), "drone-" + date + ".geojson");
                objectMapper.writeValue(outputFile, featureCollection);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}

