package fr.laptoff.civilisationplot.Managers;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {

    public static void createFile(File file){
        if (file.exists())
            return;

        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //This method erase the text into the file et write the new text
    public static void register(File file, String text){
        if (!file.exists())
            return;

        try {
            FileWriter writer = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
