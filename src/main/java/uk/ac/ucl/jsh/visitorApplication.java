package uk.ac.ucl.jsh;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.*;



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


    public void visit(Visitable.Echo app) throws IOException {

        OutputStreamWriter writer = new OutputStreamWriter(app.output, StandardCharsets.UTF_8);
        Stream<String> args = app.appArgs.stream();
        
        args.forEach(arg -> echoPrint(writer, arg));

        if (args.count() > 0) {
            writer.write(System.getProperty("line.separator"));
            writer.flush();
        }
    }

    // Auxiliary method for ECHO to print arg onto outputstream.
    private void echoPrint(OutputStreamWriter writer, String arg) { //JULIAN COMMENT - The reason the program made you add the try/except is because you forgot to throw IOException

        try {
            writer.write(arg);
            writer.write(" ");
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("echo: unable to print args.");
        }       
    }

    public void visit(Visitable.Head app) throws IOException {

        OutputStreamWriter writer = new OutputStreamWriter(app.output, StandardCharsets.UTF_8);
        if (app.appArgs.isEmpty()) {
            throw new RuntimeException("head: missing arguments");
        }
        if (app.appArgs.size() > 3) {
            throw new RuntimeException("head: wrong arguments");
        }
        if (app.appArgs.size() == 3 && !app.appArgs.get(0).equals("-n")) {
            throw new RuntimeException("head: wrong argument " + app.appArgs.get(0));
        }
        int headLines = 10;
        String headArg = "";
        if (app.appArgs.size() == 2 || app.appArgs.size() == 3) {
            try {
                headLines = Integer.parseInt(app.appArgs.get(1));
            } catch (Exception e) {
                throw new RuntimeException("head: wrong argument " + app.appArgs.get(1));
            }
            if (app.appArgs.size() == 3){
                headArg = app.appArgs.get(2);
            }
        } else {
            headArg = app.appArgs.get(0);
        }
        if (app.appArgs.size() == 2 && app.appArgs.get(0).equals("-n")) {
            BufferedReader standardInputBuffer = new BufferedReader(new InputStreamReader(System.in));
            for (int i = 0; i < headLines; i++) {
                String line = null;
                if ((line = standardInputBuffer.readLine()) != null) {
                    writer.write(line);
                    writer.write(System.getProperty("line.separator"));
                    writer.flush();
                }
            }
        }
        else if(app.appArgs.size() == 3){
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
        else{
            throw new RuntimeException("Arguments are in wrong order");
        }
    }

    public void visit(Visitable.Tail app) throws IOException {
        
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

    public void visit(Visitable.Ls app) throws IOException {

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
            Stream<File> streamOfFiles = Stream.of(currDir.listFiles()).filter(f -> !f.getName().startsWith("."));
            streamOfFiles.forEach(f -> lsWriteFile(writer, f));
            
            if (streamOfFiles.count() > 0) {
                writer.write(System.getProperty("line.separator"));
                writer.flush();
            }
        } catch (NullPointerException e) {
            throw new RuntimeException("ls: no such directory");
        }
    }

    // Auxiliary method for LS to print argument f onto outputstream.
    private void lsWriteFile(OutputStreamWriter writer, File f) {

        try {
            writer.write(f.getName());
            writer.write("\t");
            writer.flush();
        }
        catch (IOException e) {
            throw new RuntimeException("ls: unable to write files.");
        }
    }
    
    public void visit(Visitable.Grep app) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(app.output, StandardCharsets.UTF_8);
        if (app.appArgs.isEmpty()){
            throw new RuntimeException("grep: wrong number of arguments");
        }
        // Need to create cases for nothing in files and * in files
        Pattern grepPattern = Pattern.compile(app.appArgs.get(0));
        if (app.appArgs.size() == 1) { //Case for no argument for files, finds in standard input
            BufferedReader standardInputBuffer = new BufferedReader(new InputStreamReader(System.in));
            String line = null;
            while ((line = standardInputBuffer.readLine()) != null) {
                Matcher matcher = grepPattern.matcher(line);
                if (matcher.find()) {
                    writer.write(line);
                    writer.write(System.getProperty("line.separator"));
                    writer.flush();
                }
            }
        }
        else{
            File currDir = new File(app.currentDirectory);
            int numOfFiles = app.appArgs.size() - 1;
            Path filePath;
            if (app.appArgs.get(1) == "*") {
                numOfFiles = currDir.listFiles().length - 1;
            }
            Path[] filePathArray = new Path[numOfFiles];
            Path currentDir = Paths.get(app.currentDirectory);
            if (app.appArgs.get(1) == "*") {
                for (int i = 0; i < numOfFiles; i++) {
                    filePath = currentDir.resolve(currDir.listFiles()[i + 1].toPath());
                    if (Files.notExists(filePath) || !Files.exists(filePath) || !Files.isReadable(filePath)) {
                        throw new RuntimeException("grep: wrong file argument");
                    }
                    if (!Files.isDirectory(filePath)) {
                        filePathArray[i] = filePath;
                    }
                }
            }
            else{
                for (int i = 0; i < numOfFiles; i++) {
                    filePath = currentDir.resolve(app.appArgs.get(i + 1));
                    if (Files.notExists(filePath) || Files.isDirectory(filePath) || !Files.exists(filePath)
                            || !Files.isReadable(filePath)) {
                        throw new RuntimeException("grep: wrong file argument");
                    }
                    filePathArray[i] = filePath;
                }
            }
            for (int j = 0; j < filePathArray.length; j++) {
                Charset encoding = StandardCharsets.UTF_8;
                try (BufferedReader reader = Files.newBufferedReader(filePathArray[j], encoding)) {
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        Matcher matcher = grepPattern.matcher(line);
                        if (matcher.find()) {
                            if (numOfFiles > 1) {
                                writer.write(app.appArgs.get(j + 1));
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
    
    public void visit(Visitable.Find app) throws IOException{
        OutputStreamWriter writer = new OutputStreamWriter(app.output, StandardCharsets.UTF_8);
        Path rootDirectory;
        Pattern findPattern = Pattern.compile(app.appArgs.get(app.appArgs.size() - 1));
        if (app.appArgs.size() != 2 || app.appArgs.size() != 3){
            throw new RuntimeException("Find: Wrong number of arguments");
        }
        if (app.appArgs.get(app.appArgs.size() - 2) != "-name"){
            throw new RuntimeException("Find: Wrong arguments");
        }
        if (app.appArgs.size() == 2){
            rootDirectory = Paths.get(app.currentDirectory);
        }
        else{
            rootDirectory = Paths.get(app.appArgs.get(2));
        }
        findRecurse(writer, rootDirectory, findPattern);
    }

    private void findRecurse(OutputStreamWriter writer, Path currDirectory, Pattern findPattern) throws IOException{
        File[] listOfFiles = currDirectory.toFile().listFiles();
        for (File currFile : listOfFiles){
            Matcher matcher = findPattern.matcher(currFile.getName());
            if (currFile.isDirectory()){
                findRecurse(writer, currFile.toPath(), findPattern);
            }
            else if (matcher.find()){
                writer.write(currFile.getPath());
                writer.write(System.getProperty("line.separator"));
            }
        }
    }

    /* This function assumes you cant have multiple different options. Eg 1,2,3 OR 5-6,7-8 OR -5,6-. Not 
       1,2,4-5.
    */
    public void visit(Visitable.Cut app) {

        if (app.appArgs.isEmpty()) {
            throw new RuntimeException("cut: not enough arguments.");
        }
        System.out.println(app.appArgs.get(0));
        if (!app.appArgs.get(0).equals("-b")) { // cut -b 1,2,3 Dockerfile
            throw new RuntimeException("cut: incorrect option input " + app.appArgs.get(0));
        }

        if (app.appArgs.size() > 3) {
            throw new RuntimeException("cut: too many arguments.");
        }

        if (app.appArgs.size() < 3) {
            throw new RuntimeException("cut: too few arguments.");
        }

        Charset charset = StandardCharsets.UTF_8;
        OutputStreamWriter writer = new OutputStreamWriter(app.output, charset);
        String[] args = app.appArgs.get(1).split(",");

        for (int i = 0; i != args.length; ++i) {
            if (!Pattern.matches("[0-9]*-*[0-9]*", args[i])) {
                // Check arguments are 1, 4-7, -5 or 6-.
                throw new RuntimeException("cut: invalid arguments.");
            }
        }

        File file = new File(app.currentDirectory + File.separator + app.appArgs.get(2));
        Path filePath = Paths.get(app.currentDirectory + File.separator + app.appArgs.get(2));

        if (file.exists()) {
            if (!args[0].contains("-")) {
                try (BufferedReader reader = Files.newBufferedReader(filePath, charset)) {
                    // Reads in file line by line. Extracts relevant bytes from bytes. Outputs relevant bytes and continues.
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        byte[] bytes = line.getBytes(charset);
                        byte[] bytesToPrint = new byte[args.length];
                            
                        for (int i = 0; i != args.length; ++i) {
                            // bytes is offset by 1. Eg first byte is at index 0 in bytes. So -1 to get first byte.
                            bytesToPrint[i] = bytes[Integer.parseInt(args[i]) - 1];
                        }
                            
                        writer.write(new String(bytesToPrint, charset));
                        writer.write(System.getProperty("line.separator"));
                        writer.flush();
                    }
                } catch (IOException e) {
                    throw new RuntimeException("cut: cannot open " + app.appArgs.get(2));
                }
            }
            else {
                if (args[0].length() == 3) {
                    // option is of format [0-9]-[0-9].
                    // calculate length of bytesToPrint
                    int length = 0;
                    for (int i = 0; i != args.length; ++i) {
                        length += (Character.getNumericValue(args[i].charAt(2)) - Character.getNumericValue(args[i].charAt(0)));
                    }
    
                    try (BufferedReader reader = Files.newBufferedReader(filePath, charset)) {
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            byte[] bytes = line.getBytes(charset);
                            byte[] bytesToPrint = new byte[length];
                            int counter = 0;
                            for (int i = 0; i != args.length; ++i) {
                                // for each interval in args
                                for (int j = Character.getNumericValue(args[i].charAt(0)) - 1; j != Character.getNumericValue(args[i].charAt(2)); ++j) {
                                    // loop from start to end of interval
                                    bytesToPrint[counter++] = bytes[j];
                                }
                            }
                            writer.write(new String(bytesToPrint, charset));
                            writer.write(System.getProperty("line.separator"));
                            writer.flush();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("cut: cannot open " + app.appArgs.get(2));
                    }
                    } 
                else {
                    // options are of the form -3,6-...
                    ArrayList<Integer> to = new ArrayList<>();
                    ArrayList<Integer> from = new ArrayList<>();
                    for (int i = 0; i != args.length; ++i) {
                        if (args[i].charAt(0) == '-') {
                            // of the form -[0-9].
                            to.add(Character.getNumericValue(args[i].charAt(1)));
                        }
                        else {
                            from.add(Character.getNumericValue(args[i].charAt(0)));
                        }
                    }
    
                    try (BufferedReader reader = Files.newBufferedReader(filePath, charset)) {
                        String line = null;
                        while ((line = reader.readLine()) != null) {
                            byte[] bytes = line.getBytes(charset);
                            byte[] bytesToPrint = new byte[bytes.length];         // Assume at most we print all bytes in line.
                            
                            int counter = 0;
                            for (int i = 0; i != to.size(); ++i) {
                                // Extract bytes to. Eg -5 will get first 4 bytes
                                for (int j = 0; j != to.get(i); ++j) {
                                    // dont offset this since it starts at same point. 
                                    bytesToPrint[counter++] = bytes[j];
                                }
                            }
    
                            for (int i = 0; i != from.size(); ++i) {
                                // Extract bytes from. Eg 5- will get bytes 5 to end.
                                for (int j = from.get(i); j != bytes.length; ++j) {
                                    // offset this since 5th byte would be at index 4.
                                    bytesToPrint[counter++] = bytes[j-1];
                                }
                            }
    
                            writer.write(new String(bytesToPrint, charset));
                            writer.write(System.getProperty("line.separator"));
                            writer.flush();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("cut: cannot open " + app.appArgs.get(2));
                    }
                }
            } 
        }
    }
}
