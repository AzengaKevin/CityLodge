package main.model.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import main.utils.AlertHelper;

/**
 *
 * Class for handling file operations
 *
 * @author guru
 */
public class FileHandler {

    public static final String FILE_NAME = "/export_data.txt";

    public FileHandler() {

    }

    public boolean writeToFile(String directory, String data) {

        String fileName = directory + FILE_NAME;

        PrintWriter writer = null;

        try {

            writer = new PrintWriter(
                    new BufferedWriter(
                            new FileWriter(
                                    new File(fileName), true
                            )
                    )
            );

            writer.write(data);

            return true;

        } catch (IOException ex) {

            System.err.println("IOException: " + ex.getLocalizedMessage());

            AlertHelper.error("Exporting Data", "IOException while writing to file\nCheck the console for more details");

        } finally {

            if (writer != null) {
                writer.close();
            }
        }

        return false;

    }

    public String readFronFile(String fileName) {

        BufferedReader reader = null;

        try {

            StringBuilder sBuilder = new StringBuilder();

            reader = new BufferedReader(
                    new FileReader(new File(fileName))
            );

            String line = reader.readLine();

            while (line != null) {
                sBuilder.append(line);
                sBuilder.append('\n');

                line = reader.readLine();
            }

            return sBuilder.toString().trim();

        } catch (IOException ex) {
            System.err.println("IOException: " + ex.getLocalizedMessage());
            AlertHelper.error("Importing Data", "IOException while writing to file\nCheck the console for more details");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    System.err.println("IOException: " + ex.getLocalizedMessage());
                }
            }
        }

        return null;

    }
}
