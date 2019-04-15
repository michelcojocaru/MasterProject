package com.thesis.validator.config;

import com.thesis.validator.file.ExternalProgramExecutor;

public class ClocInstaller {

    private static String OS = System.getProperty("os.name").toLowerCase();

    public static boolean installCloc(){

        if (isWindows()) {
            System.out.println("This is Windows");
        } else if (isMac()) {
            System.out.println("This is Mac");
            ExternalProgramExecutor.exec( "brew" ,"install" ,"cloc");
        } else if (isUnix()) {
            System.out.println("This is Unix or Linux");
            ExternalProgramExecutor.exec( "apt-get" ,"install" ,"perl");
            ExternalProgramExecutor.exec( "apt-get" ,"install" ,"cloc");
        } else if (isSolaris()) {
            System.out.println("This is Solaris");
        } else {
            System.out.println("Your OS is not support!!");
            return false;
        }
        return true;
    }

    public static boolean isWindows() {
        return (OS.contains("win"));
    }

    public static boolean isMac() {
        return (OS.contains("mac"));
    }

    public static boolean isUnix() {
        return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
    }

    public static boolean isSolaris() {
        return (OS.contains("sunos"));
    }
}
