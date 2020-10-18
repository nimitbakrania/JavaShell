package uk.ac.ucl.jsh;

import java.util.Scanner;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class NewJSH {

    private static String currentDirectory = System.getProperty("user.dir");
    private ApplicationRunner app_runner = new ApplicationRunner();
    private Parser parser = new Parser();


    private void eval(String cmdline, OutputStream output) throws IOException {
        
        OutputStreamWriter writer = new OutputStreamWriter(output);
        ArrayList<String> raw_commands = parser.parse(cmdline);

        for (String command : raw_commands) {
            ArrayList<String> app_args = parser.split_in2_tokens(command, currentDirectory);
            String app_name = app_args.get(0);
            app_args.remove(0);
            switch (app_name) {
                // execute different app_runner methods depending on app name.
                case "cd" : app_runner.cd(app_args); break;
                case "ls" : app_runner.ls(app_args); break;
                case "pwd" : app_runner.pwd(); break;
                case "cat" : app_runner.cat(app_args); break;
                case "echo" : app_runner.echo(app_args); break;
                case "head" : app_runner.head(app_args); break;
                case "tail" : app_runner.tail(app_args); break;
                case "grep" : app_runner.grep(app_args); break;
                default : throw new RuntimeException(app_name + ": unknown application");
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
