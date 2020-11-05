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

    public void visit(appVisitor.echo app){
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(app.output, StandardCharsets.UTF_8));
        boolean atLeastOnePrinted = !app.appArgs.isEmpty();
        int count = 0;
        for (String arg : app.appArgs) {
            writer.write(arg);
            if (count < appArgs.size() - 1) {
                writer.write(" ");
            }
            writer.flush();
            count++;
        }
        if (atLeastOnePrinted) {
            writer.write(System.getProperty("line.separator"));
            writer.flush();
        }
    }

    public void visit(appVisitor.head app){
        if (app.appArgs.isEmpty()) {
            throw new RuntimeException("head: missing arguments");
        }
        if (app.appArgs.size() != 1 && app.appArgs.size() != 3) {
            throw new RuntimeException("head: wrong arguments");
        }
        if (app.appArgs.size() == 3 && !app.appArgs.get(0).equals("-n")) {
            throw new RuntimeException("head: wrong argument " + app.appArgs.get(0));
        }
        int headLines = 10;
        String headArg;
        if (app.appArgs.size() == 3) {
            try {
                headLines = Integer.parseInt(app.appArgs.get(1));
            } catch (Exception e) {
                throw new RuntimeException("head: wrong argument " + app.appArgs.get(1));
            }
            headArg = app.appArgs.get(2);
        } else {
            headArg = app.appArgs.get(0);
        }
        File headFile = new File(app.directory + File.separator + headArg);
        if (headFile.exists()) {
            Charset encoding = StandardCharsets.UTF_8;
            Path filePath = Paths.get((String) app.directory + File.separator + headArg);
            try (BufferedReader reader = Files.newBufferedReader(filePath, encoding)) {
                for (int i = 0; i < headLines; i++) {
                    String line = null;
                    if ((line = reader.readLine()) != null) {
                        writer.write(line);
                        writer.write(System.getProperty("line.separator"));
                        writer.flush();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("head: cannot open " + headArg);
            }
        } else {
            throw new RuntimeException("head: " + headArg + " does not exist");
        }
    }

    public void visit(appVisitor.tail app);

    public void visit(appVisitor.cat app){
        String currentDirectory = new String(app.directory)
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(app.output, StandardCharsets.UTF_8));
        InputStream input = app.input;
        // decides whether stdin must be used when no args provided
        if (app.appArgs.isEmpty()) {
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

    public void visit(appVisitor.ls app);

    public void visit(appVisitor.grep app){
        if (app.appArgs.size() < 2) {
            throw new RuntimeException("grep: wrong number of arguments");
        }
        Pattern grepPattern = Pattern.compile(app.appArgs.get(0));
        int numOfFiles = app.appArgs.size() - 1;
        Path filePath;
        Path[] filePathArray = new Path[numOfFiles];
        Path currentDir = Paths.get(app.directory);
        for (int i = 0; i < numOfFiles; i++) {
            filePath = currentDir.resolve(app.appArgs.get(i + 1));
            if (Files.notExists(filePath) || Files.isDirectory(filePath) ||
                    !Files.exists(filePath) || !Files.isReadable(filePath)) {
                throw new RuntimeException("grep: wrong file argument");
            }
            filePathArray[i] = filePath;
        }
        for (int j = 0; j < filePathArray.length; j++) {
            Charset encoding = StandardCharsets.UTF_8;
            try (BufferedReader reader = Files.newBufferedReader(filePathArray[j], encoding)) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    Matcher matcher = grepPattern.matcher(line);
                    if (matcher.find()) {
                        if (numOfFiles > 1) {
                            writer.write(app.appArgs.get(j+1));
                            writer.write(":");
                        }
                        writer.write(line);
                        writer.write(System.getProperty("line.separator"));
                        writer.flush();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("grep: cannot open " + app.appArgs.get(j + 1));
            }
        }
    }

}