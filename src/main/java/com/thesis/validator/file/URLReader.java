package com.thesis.validator.file;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths;

public class URLReader {

    public static void copyURLToFile(URL url, File file) {

        try {
            InputStream input = url.openStream();
            if (file.exists()) {
                if (file.isDirectory())
                    throw new IOException("File '" + file + "' is a directory");

                if (!file.canWrite())
                    throw new IOException("File '" + file + "' cannot be written");
            } else {
                File parent = file.getParentFile();
                if ((parent != null) && (!parent.exists()) && (!parent.mkdirs())) {
                    throw new IOException("File '" + file + "' could not be created");
                }
            }

            FileOutputStream output = new FileOutputStream(file);

            byte[] buffer = new byte[4096];
            int n = 0;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }

            input.close();
            output.close();

            System.out.println("File '" + file + "' downloaded successfully!");
        }
        catch(IOException ioEx) {
            ioEx.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        String repoUrl = "https://github.com/javaee/cargotracker.git";
        String cloneDirectoryPath = System.getProperty("user.dir") + "/repo"; // Ex.in windows c:\\gitProjects\SpringBootMongoDbCRUD\
        try {
            System.out.println("Cloning "+repoUrl+" into "+repoUrl);
            Git.cloneRepository()
                    .setURI(repoUrl)
                    .setDirectory(Paths.get(cloneDirectoryPath).toFile())
                    .call();
            System.out.println("Completed Cloning");
        } catch (GitAPIException e) {
            System.out.println("Exception occurred while cloning repo");
            e.printStackTrace();
        }


//        //URL pointing to the file
//        String sUrl = "https://github.com/javaee/cargotracker";
//
//        URL url = new URL(sUrl);
//
//        //File where to be downloaded
//        File file = new File(System.getProperty("user.dir")+"/repo");
//
//        URLReader.copyURLToFile(url, file);
    }

}