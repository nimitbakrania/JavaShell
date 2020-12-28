package uk.ac.ucl.jsh;

import java.util.Scanner;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;


public class Jsh {

    private static String currentDirectory = System.getProperty("user.dir");
    final private static String homeDirectory = System.getProperty("user.dir");

    public static void eval(String cmdline, OutputStream output) throws IOException {
        
        OurParser parser = new OurParser();
        Call call = new Call();
        Pipe pipe = new Pipe();
        IoRedirection redirect = new IoRedirection();
        ArrayList<ArrayList<String>> lines = parser.parse(cmdline, currentDirectory);
        for (ArrayList<String> line : lines) {
            String appName = line.get(0);
            ArrayList<String> appArgs = new ArrayList<String>(line.subList(1, line.size()));
            if (appArgs.contains("|")){
                pipe.eval(null, output, currentDirectory, appName, appArgs);
            }
            else if (appArgs.contains(">") || appArgs.contains("<")){
                redirect.eval(null, output, currentDirectory, appName, appArgs);
            }
            else{
                call.eval(null, output, currentDirectory, appName, appArgs);
            }
        }
    }

    public static String getHomeDirectory() {
        return homeDirectory;
    }


    public static String getCurrentDirectory() {
        return currentDirectory;
    }
    
    public static void setCurrentDirectory(String dir) {
        Jsh.currentDirectory = dir;
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            if (args.length != 2) {
                System.out.println("jsh: wrong number of arguments");
                return;
            }
            if (!args[0].equals("-c")) {
                System.out.println("jsh: " + args[0] + ": unexpected arguments");
            }
            try {
                eval(args[1], System.out);
            } catch (Exception e) {
                System.err.println("jsh: " + e.getMessage());
            }
        } else {
            Scanner input = new Scanner(System.in);
            try {
                while (true) {
                    String prompt = currentDirectory + "> ";
                    System.out.print(prompt);
                    try {
                        String cmdline = input.nextLine();
                        eval(cmdline, System.out);
                    } catch (Exception e) {
                        System.err.println("jsh: " + e.getMessage());
                    }
                }
            } finally {
                input.close();
            }
        }
    }
}
