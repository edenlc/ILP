package uk.ac.ed.inf.outPutters;

import java.io.File;
import uk.ac.ed.inf.aStarSolver.Move;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class OutPutFlighpaths {
    public static void generateFlighpaths(String date, Move[] moves){

        try {

            //Create resultsfiles folder in parent directory of the project
            String currentDirectoryPath = System.getProperty("user.dir");
            File currentDirectory = new File(currentDirectoryPath);
            File resultsFilesDirectory = new File(currentDirectory, "resultsFiles");

            if (!resultsFilesDirectory.exists()) {
                if (resultsFilesDirectory.mkdir()) {
                    System.out.println("Created 'resultsFiles' directory.");
                } else {
                    System.err.println("Failed to create 'resultsFiles' directory.");
                }
            }
            else{
                System.out.println("'resultsFiles' directory already exists");
            }

            //Write flightpaths to file
            ObjectMapper objectMapper = new ObjectMapper();

            File outputFile = new File(resultsFilesDirectory.getAbsolutePath(), "flightpath-" + date + ".json");

            objectMapper.writeValue(outputFile, moves);

        } catch (IOException e){
            System.err.println("Failed to create outputfile.");
            System.exit(0);
        }


    }
}
