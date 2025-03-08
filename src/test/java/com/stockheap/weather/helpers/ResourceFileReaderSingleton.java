package com.stockheap.weather.helpers;

import org.springframework.data.repository.init.ResourceReader;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResourceFileReaderSingleton {

    private final static ResourceFileReaderSingleton instance = new ResourceFileReaderSingleton();
    private ResourceFileReaderSingleton() {
    }

    public static ResourceFileReaderSingleton getInstance()
    {
        return instance;
    }

    public String readFile(String fileIn) throws Exception
    {
        Path resourceDirectory = Paths.get("src","test","java","resources");
        File file = new File(resourceDirectory.toAbsolutePath()+ "/" + fileIn);
        return readFilLocal(file);

    }

    private String readFilLocal( File file) {


        if (file == null || !file.exists()) {
            System.out.println("File not found!");
            return "";
        }

        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }


}
