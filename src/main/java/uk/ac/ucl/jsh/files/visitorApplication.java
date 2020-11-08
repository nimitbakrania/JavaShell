package uk.ac.ucl.jsh.files;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class visitorApplication implements baseVisitor {

    public visitorApplication() { }

    public void visit(Visitable.Cd app) throws IOException{
        if (app.appArgs.isEmpty()) {
            throw new RuntimeException("cd: missing argument");
        } else if (app.appArgs.size() > 1) {
            throw new RuntimeException("cd: too many arguments");
        }
        String dirString = app.appArgs.get(0);
        File dir = new File(app.currentDirectory, dirString);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new RuntimeException("cd: " + dirString + " is not an existing directory");
        }
        app.currentDirectory = dir.getCanonicalPath();
    }

    public void visit(Visitable.Pwd app) throws IOException{

        if(app.appArgs.isEmpty()){
            throw new RuntimeException("pwd: too many arguments");
        }
        OutputStreamWriter writer = new OutputStreamWriter(app.output, StandardCharsets.UTF_8);
        writer.write(app.currentDirectory.toString());
        writer.write(System.getProperty("line.separator"));
        writer.flush();
    }


    public void visit(Visitable.Echo app) throws IOException{
        OutputStreamWriter writer = new OutputStreamWriter(app.output, StandardCharsets.UTF_8);
        boolean atLeastOnePrinted = false;
        for (String arg : app.appArgs) {
            writer.write(arg);
            writer.write(" ");
            writer.flush();
            atLeastOnePrinted = true;
        }
        if (atLeastOnePrinted) {
            writer.write(System.getProperty("line.separator"));
            writer.flush();
        }
    }

    public void visit(Visitable.Head app) throws IOException{
        OutputStreamWriter writer = new OutputStreamWriter(app.output, StandardCharsets.UTF_8);
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
        File headFile = new File(app.currentDirectory + File.separator + headArg);
        if (headFile.exists()) {
            Charset encoding = StandardCharsets.UTF_8;
            Path filePath = Paths.get((String) app.currentDirectory + File.separator + headArg);
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

    public void visit(Visitable.Tail app) throws IOException{
        OutputStreamWriter writer = new OutputStreamWriter(app.output, StandardCharsets.UTF_8);
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
        File tailFile = new File(app.currentDirectory + File.separator + tailArg);
        if (tailFile.exists()) {
            Charset encoding = StandardCharsets.UTF_8;
            Path filePath = Paths.get((String) app.currentDirectory + File.separator + tailArg);
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
                    writer.write(storage.get(i) + System.getProperty("line.separator"));
                    writer.flush();
                }
            } catch (IOException e) {
                throw new RuntimeException("tail: cannot open " + tailArg);
            }
        } else {
            throw new RuntimeException("tail: " + tailArg + " does not exist");
        }
    }

    public void visit(Visitable.Cat app) throws IOException{
        OutputStreamWriter writer = new OutputStreamWriter(app.output, StandardCharsets.UTF_8);
        if (app.appArgs.isEmpty()) {
            throw new RuntimeException("cat: missing arguments");
        } else {
            for (String arg : app.appArgs) {
                Charset encoding = StandardCharsets.UTF_8;
                File currFile = new File(app.currentDirectory + File.separator + arg);
                if (currFile.exists()) {
                    Path filePath = Paths.get(app.currentDirectory + File.separator + arg);
                    try (BufferedReader reader = Files.newBufferedReader(filePath, encoding)) {
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            writer.write(String.valueOf(line));
                            writer.write(System.getProperty("line.separator"));
                            writer.flush();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("cat: cannot open " + arg);
                    }
                } else {
                    throw new RuntimeException("cat: file does not exist");
                }
            }
        }
    }

    public void visit(Visitable.Ls app) throws IOException{
        OutputStreamWriter writer = new OutputStreamWriter(app.output, StandardCharsets.UTF_8);
        File currDir;
        if (app.appArgs.isEmpty()) {
            currDir = new File(app.currentDirectory);
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
    }

    public void visit(Visitable.Grep app) throws IOException{
        OutputStreamWriter writer = new OutputStreamWriter(app.output, StandardCharsets.UTF_8);
        if (app.appArgs.size() < 2) {
            throw new RuntimeException("grep: wrong number of arguments");
        }
        Pattern grepPattern = Pattern.compile(app.appArgs.get(0));
        int numOfFiles = app.appArgs.size() - 1;
        Path filePath;
        Path[] filePathArray = new Path[numOfFiles];
        Path currentDir = Paths.get(app.currentDirectory);
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
