package uk.ac.ucl.jsh;

import java.util.Scanner;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class NewJSH {

    private static String currentDirectory = System.getProperty("user.dir");
    private App app = new App();
    private Parser parser = new Parser();


    private void eval2(String cmdline, OutputStream output) throws IOException {

        ArrayList<ArrayList<String>> commands = this.parser.parse(cmdline, currentDirectory);

        for (ArrayList<String> a_command : commands) {
            // a_command is app_args
            String name = a_command.get(0);
            a_command.remove(0);

            switch (name) {
                case "cd" : // create cd object and call dynamic dispatch method.
                case "ls" : // create ls object "  "
                case "pwd" : // create pwd object
                case "cat" : // create cat object
                case "echo" : // create echo object
                case "head" : //
                case "tail" : //
                case "grep" : //
                default : throw new RuntimeException(name + ": unknown application.");
            }
        }
    }


    private void eval(String cmdline, OutputStream output) throws IOException {

        ArrayList<ArrayList<String>> commands = this.parser.parse(cmdline, currentDirectory);
        this.app.exec(commands, new OutputStreamWriter(output), currentDirectory);

        
        OutputStreamWriter writer = new OutputStreamWriter(output);
        ArrayList<String> raw_commands = parser.parse(cmdline);

        for (String command : raw_commands) {
            ArrayList<String> app_args = parser.split_in2_tokens(command, currentDirectory);
            String app_name = app_args.get(0);
            app_args.remove(0);
            switch (app_name) {
                // execute different app_runner methods depending on app name.
                case "cd" : app_runner.cd(app_args); break;
                case "ls" : app_runner.ls(app_args, writer); break;
                case "pwd" : app_runner.pwd(writer); break;
                case "cat" : app_runner.cat(app_args, writer); break;
                case "echo" : app_runner.echo(app_args, writer); break;
                case "head" : app_runner.head(app_args, writer); break;
                case "tail" : app_runner.tail(app_args, writer); break;
                case "grep" : app_runner.grep(app_args, writer); break;
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
