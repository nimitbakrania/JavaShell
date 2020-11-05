package uk.ac.ucl.jsh.files;

import java.util.Scanner;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class NewJSH {

    private Parser parser = new Parser();
    private String currentDirectory = System.getProperty("user.dir");


    private void eval(String cmdline, OutputStream output) throws IOException {
        // first parse
        // check if its pipe or seq or call. for now assume its always call
        // use factory to make specific visitable.* application and reutrn it.
        // call accept on that obj.
        ArrayList<ArrayList<String>> lines = parser.parse(cmdline, currentDirectory);
        Call call = new Call();

        for (ArrayList<String> line : lines) {
            // we are assuming theyre all call.
            call.eval(line.get(0), line.subList(1, line.size());         // first argument is application name eg "cd", "grep" the second argument is appArgs.
        }

    }

    public static void main(String[] args) {
        NewJSH obj = new NewJSH();
        if (args.length > 0) {
            if (args.length != 2) {
                System.out.println("jsh: wrong number of arguments");
                return;
            }
            if (!args[0].equals("-c")) {
                System.out.println("jsh: " + args[0] + ": unexpected argument");
            }
            try {
                obj.eval(args[1], System.out);
            } catch (Exception e) {
                System.out.println("jsh: " + e.getMessage());
            }
        } else {
            Scanner input = new Scanner(System.in);
            try {
                while (true) {
                    String prompt = obj.core.getCurrentDirectory() + "> ";
                    System.out.print(prompt);
                    try {
                        String cmdline = input.nextLine();
                        obj.eval(cmdline, System.out);
                    } catch (Exception e) {
                        System.out.println("jsh: " + e.getMessage());
                    }
                }
            } finally {
                input.close();
            }
        }
    }
    
}
