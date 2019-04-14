package com.thesis.validator.file;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import java.io.File;
import java.nio.file.Paths;

public class GitRepoDownloader {

    public static boolean download(String url){
        String cloneDirectoryPath = System.getProperty("user.dir") + "/repo";
        try {
            //System.out.println("Cloning " + url + " into "+cloneDirectoryPath);
            Git.cloneRepository()
                    .setURI(url)
                    .setDirectory(Paths.get(cloneDirectoryPath).toFile())
                    .call();
            //System.out.println("Completed Cloning");
        } catch (GitAPIException e) {
            //System.out.println("Exception occurred while cloning repo");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }
}
