package uk.ac.ed.inf.outPutters;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.ac.ed.inf.ilp.data.Order;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OutPutDeliveries {
    public static void generateDeliveries(String date, Order[] orders){

        try {
            //Create resultsfiles in parent directory of project
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

            //Output the deliveries. Uses the exportOrder class to achieve this
            ObjectMapper objectMapper = new ObjectMapper();

            File outputFile = new File(resultsFilesDirectory.getAbsolutePath(), "deliveries-" + date + ".json");

            List<exportOrder> exportOrdersList = new ArrayList<>();
            for (Order order: orders){
                exportOrdersList.add(new exportOrder(order));
            }
            exportOrder[] exportOrders = exportOrdersList.toArray(new exportOrder[exportOrdersList.size()]);

            objectMapper.writeValue(outputFile, exportOrders);

        } catch (IOException e){
            e.printStackTrace();
        }


    }
}
