package com.thesis.validator.helpers;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

public class TextOperations {

    public static int countLines(String filePath){
        int lineCount = 0;
        Scanner s = null;
        try {
            File file = new File(filePath);
            s = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (Objects.requireNonNull(s).hasNext()) {
            String string = (String) s.nextLine();
            if (string != null) {
                if (!string.trim().startsWith("\n") &&
                    !string.trim().startsWith("\t") &&
                    !string.trim().startsWith("\r") &&
                    !string.trim().startsWith("\r\n") &&
                    !string.trim().isEmpty() &&
                    !string.trim().startsWith("//") &&
                    !string.trim().equals("{") &&
                    !string.trim().equals("}")) {
                        lineCount++;
                }
            }
        }
        return lineCount;
    }
}
