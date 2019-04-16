package com.thesis.validator.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class ExternalProgramExecutor {

    public static String exec(String... args) {
        StringBuilder result = new StringBuilder();

        Process process = null;
        try {
            process = new ProcessBuilder(args).start();
            InputStream is = Objects.requireNonNull(process).getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            return String.valueOf(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
