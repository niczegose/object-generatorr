package pl.kurs.java.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class CsvWriter {
    public static void save(String path, String stringToWrite) {
        try (PrintWriter writer = new PrintWriter(new File(path))) {
            writer.write(stringToWrite);
            System.out.println("CSV created");

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}