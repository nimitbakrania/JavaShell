package uk.ac.ucl.jsh.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class visitorApplication implements baseVisitor {

    public visitorApplication() { }

    public void visit(Visitable.Cd app) {

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

    public void visit(Visitable.Pwd app) {

        if(appArgs.isEmpty()){
            throw new RuntimeException("pwd: too many arguments");
        }
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        writer.write(core.getCurrentDirectory().toString());
        writer.write(System.getProperty("line.separator"));
        writer.flush();
    }

    public void visit(Visitable.Echo app) {

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

    public void visit(Visitable.Echo app) {

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

    public void visit(Visitable.Head app) {

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

    public void visit(Visitable.Tail app) {

        if (app.appArgs.isEmpty()) {
            throw new RuntimeException("tail: missing arguments");
        }
        if (app.appArgs.size() != 1 && app.appArgs.size() != 3) {
            throw new RuntimeException("tail: wrong arguments");
        }
        if (app.appArgs.size() == 3 && !app.appArgs.get(0).equals("-n")) {
            throw new RuntimeException("tail: wrong argument " + app.appArgs.get(0));
        }
        
        int tailLines = 10;
        String tailArg;
        if (app.appArgs.size() == 3) {
            try {
                tailLines = Integer.parseInt(app.appArgs.get(1));
            } catch (Exception e) {
                throw new RuntimeException("tail: wrong argument " + app.appArgs.get(1));
            }
            tailArg = app.appArgs.get(2);
        } else {
            tailArg = app.appArgs.get(0);
        }

        File tailFile = new File(currentDirectory + File.separator + tailArg);
        if (tailFile.exists()) {
            Charset encoding = StandardCharsets.UTF_8;
            Path filePath = Paths.get((String) currentDirectory + File.separator + tailArg);
            ArrayList<String> storage = new ArrayList<>();
            try (BufferedReader reader = Files.newBufferedReader(filePath, encoding)) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    storage.add(line);
                }
                int index = 0;
                if (tailLines > storage.size()) {
                    index = 0;
                } else {
                    index = storage.size() - tailLines;
                }
                for (int i = index; i < storage.size(); i++) {
                    app.output.write(storage.get(i) + System.getProperty("line.separator"));
                    app.output.flush();
                }            
            } catch (IOException e) {
                throw new RuntimeException("tail: cannot open " + tailArg);
            }
        } else {
            throw new RuntimeException("tail: " + tailArg + " does not exist");
        }
    }

    public void visit(Visitable.Cat app) {

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

    public void visit(Visitable.Ls app) {

        File currDir;
        if (app.appArgs.isEmpty()) {
            currDir = new File(currentDirectory);
        } else if (app.appArgs.size() == 1) {
            currDir = new File(app.appArgs.get(0));
        } else {
            throw new RuntimeException("ls: too many arguments");
        }

        try {
            File[] listOfFiles = currDir.listFiles();
            boolean atLeastOnePrinted = false;
            for (File file : listOfFiles) {
                if (!file.getName().startsWith(".")) {
                    app.output.write(file.getName());
                    app.output.write("\t");
                    app.output.flush();
                    atLeastOnePrinted = true;
                }
            }
            if (atLeastOnePrinted) {
                app.output.write(System.getProperty("line.separator"));
                app.output.flush();
            }
        } catch (NullPointerException e) {
            throw new RuntimeException("ls: no such directory");
        }
    }

    public void visit(Visitable.Grep app) {

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