package uk.ac.ucl.jsh;

import java.util.Scanner;
import uk.ac.ucl.jsh.core.JshCore;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class NewJSH {

    private Parser parser = new Parser();
    private JshCore core = new JshCore();


    private void eval(String cmdline, OutputStream output) throws IOException {

        Factory factory = new Factory();
        ArrayList<ArrayList<String>> commands = this.parser.parse(cmdline, this.core.getCurrentDirectory());

        for (ArrayList<String> aCommand : commands) {
            String name = aCommand.get(0);
            aCommand.remove(0);
            App app = factory.mkApplication(name);
            app.run(this.core, aCommand);
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
