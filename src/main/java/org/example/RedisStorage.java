package org.example;

import java.io.*;

public class RedisStorage {

    public static void createFile(String fileName, String text) {

        try (FileWriter writer = new FileWriter(fileName, false)) {
            // запись всей строки
            writer.write(text);
            // запись по символам
//            writer.append('E');

            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static String readFile(String fileName) {
        String text = "";
        try (FileReader reader = new FileReader(fileName)) {
            int c;
            while ((c = reader.read()) != -1) {
                text += (char) c;
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return text;
    }
}
