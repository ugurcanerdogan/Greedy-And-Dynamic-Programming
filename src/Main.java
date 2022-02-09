import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    /**
     * Propogated {@link IOException} here
     * {@link #readFile} and {@link #writeOutput} methods should be called here
     * A {@link Scheduler} instance must be instantiated here
     */
    public static void main(String[] args) throws IOException {
        Assignment[] assignments = readFile(args[0]);   // Reading file and creating array
        Arrays.sort(assignments);                       // Sorting array
        Scheduler sch = new Scheduler(assignments);     // Creating schedule instance and instantiating arrays
        writeOutput("solution_dynamic.json", sch.scheduleDynamic());    // Outputs
        writeOutput("solution_greedy.json", sch.scheduleGreedy());      //
    }

    /**
     * @param filename json filename to read
     * @return Returns a list of {@link Assignment}s obtained by reading the given json file
     * @throws FileNotFoundException If the given file does not exist
     */
    private static Assignment[] readFile(String filename) throws FileNotFoundException {
        Assignment[] assignments = new Assignment[20];      // Creating temporary assignment array
        BufferedReader reader;
        reader = new BufferedReader(new FileReader(filename));      // Reading given input file
        Gson gson = new Gson();                                     // Creating gson object to read json format file
        assignments = gson.fromJson(reader, Assignment[].class);    // Creating array from gson format data

        return assignments;
    }


    /**
     * @param filename  json filename to write
     * @param arrayList a list of {@link Assignment}s to write into the file
     * @throws IOException If something goes wrong with file creation
     */
    private static void writeOutput(String filename, ArrayList<Assignment> arrayList) throws IOException {
        Gson gson = new GsonBuilder()       // Creating gson object to write json format file
                .setPrettyPrinting()
                .disableHtmlEscaping()
                .create();
        FileWriter writer = new FileWriter(filename);   // Creating filewriter object for write operations.
        writer.write(gson.toJson(arrayList));           // Writing output string to output file.
        writer.close();
    }
}
