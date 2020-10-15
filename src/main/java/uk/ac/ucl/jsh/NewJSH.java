package uk.ac.ucl.jsh;

import java.util.Scanner;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class NewJSH {

    private static String currentDirectory = System.getProperty("user.dir");
    private ApplicationRunner app_runner = new ApplicationRunner();
    private Parser parser = new Parser();


    private void eval(String cmdline, OutputStream output) throws IOException {

        ArrayList<String> raw_commands = parser.parse(cmdline);

        for (String command : raw_commands) {
            ArrayList<String> info = parser.split_in2_tokens(command, currentDirectory);
            String appName = info.get(0);
            info.remove(0);
            switch (appName) {
                // execute different app_runner methods depending on app name.
            }
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
                    String prompt = currentDirectory + "> ";
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
