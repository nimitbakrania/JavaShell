package uk.ac.ucl.jsh;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;



public class visitorApplication implements baseVisitor {
    public void visit(appVisitor.cd app){
        if (appArgs.isEmpty()) {
            // takes user to home directory when 'cd' is called alone
            Jsh.setCurrentDirectory(Jsh.getHomeDirectory());
        }
        else if (appArgs.size() > 1) {
            throw new RuntimeException("cd: too many arguments");
        }
        else {
            // takes user to directory specified in arg
            String currentDirectory = Jsh.getCurrentDirectory();
            String dirString = appArgs.get(0);
            File dir = new File(currentDirectory, dirString);
            
            if (!dir.exists() || !dir.isDirectory()) {
                throw new RuntimeException("cd: " + dirString + " is not an existing directory");
            }
    
            currentDirectory = dir.getCanonicalPath();
            Jsh.setCurrentDirectory(currentDirectory);
        }
    }
    public void visit(appVisitor.pwd app)
    {
        if(appArgs.isEmpty()){
            throw new RuntimeException("pwd: too many arguments");
        }
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        writer.write(core.getCurrentDirectory().toString());
        writer.write(System.getProperty("line.separator"));
        writer.flush();
    }
    public void visit(appVisitor.echo app)
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        boolean atLeastOnePrinted = !appArgs.isEmpty();
        // arguments printed with space between them, ensuring no space printed after last element
        int count = 0;
        for (String arg : appArgs) {
            writer.write(arg);
            if (count < appArgs.size() - 1) {
                writer.write(" ");
            }
            writer.flush();
            count++;
        }

        // newline only printed if there are arguments called on echo
        if (atLeastOnePrinted) {
            writer.write(System.getProperty("line.separator"));
            writer.flush();
        }
    }
    public void visit(appVisitor.head app);
    public void visit(appVisitor.tail app);

    public void visit(appVisitor.cat app){
        String currentDirectory = new String(app.directory);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        InputStream input = core.getInputStream();
        // decides whether stdin must be used when no args provided
        if (appArgs.isEmpty()) {
            if (input == null) {
                throw new RuntimeException("cat: missing arguments");
            }
            else {
                String line = new String(input.readAllBytes());
                writer.write(line);
                writer.flush();
            }
        }
        else {
            File currFile = new File(currentDirectory + File.separator + arg);

            if(currFile.isDirectory()){
                writer.write("cat: " + currFile.getName() + " is a directory");
                writer.write(System.getProperty("line.separator"));
                writer.flush();
                continue;
            }

            if (currFile.exists()) {
                Path filePath = Paths.get(currentDirectory + File.separator + arg);
                try (BufferedReader reader = Files.newBufferedReader(filePath, encoding)) {
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        writer.write(String.valueOf(line));
                        writer.write(System.getProperty("line.separator"));
                        writer.flush();
                    }
                }
            } else {
                throw new RuntimeException("cat: file does not exist");
            }
        }
    }

    public void visit(appVisitor.ls app)
    {
        String currentDirectory = core.getCurrentDirectory().toString();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        File currDir = validateArgs(appArgs, currentDirectory);
        writeOutput(currDir, writer);

        private void writeOutput(File currDir, BufferedWriter writer) throws IOException {
        try {
            File[] listOfFiles = currDir.listFiles();
            boolean atLeastOnePrinted = false;
            for (File file : listOfFiles) {
                if (!file.getName().startsWith(".")) {
                    writer.write(file.getName());
                    writer.write("\t");
                    writer.flush();
                    atLeastOnePrinted = true;
                }
            }
            if (atLeastOnePrinted) {
                writer.write(System.getProperty("line.separator"));
                writer.flush();
            }
        } catch (NullPointerException e) {
            throw new RuntimeException("ls: no such directory");
        }

        /*
        * Method returns current directory path if no argument specified or returns
        * specified path using if statements.
        */
        private File validateArgs(ArrayList<String> args, String currentDirectory) {
            File currDir;
            if (args.isEmpty()) {
                currDir = new File(currentDirectory);
            } else if (args.size() == 1) {
                currDir = new File(args.get(0));
            } else {
                throw new RuntimeException("ls: too many arguments");
            }
            return currDir;
    }
    

    public void visit(appVisitor.grep app);
}