package uk.ac.ucl.jsh;
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

/* This class is responsible for running the applications. It should receive
   a parsed input from Parser class and executes the relevant application.
*/
public class ApplicationRunner {
    
    private String curr_directory;         // Directory may be important in running some applications.

    /* Constructor for ApplicationRunner. Call this at the start so that the
       directory can be initialized.
    */
    public ApplicationRunner() {
        // Call this constructor when starting up jsh.
        curr_directory = System.getProperty("user.dir");
    }

    /* Changes the working directory to the one specified in app_Args.
       @params = app_args : Arraylist that contains the arguments passed to cd. Should either be a path
                 or a directory. If not throw an exception.
    */
    public void cd(ArrayList<String> app_args) throws IOException {
        if (app_args.isEmpty()) {
            throw new RuntimeException("cd: missing argument");
        } else if (app_args.size() > 1) {
            throw new RuntimeException("cd: too many arguments");
        }
        String dirString = app_args.get(0);
        File dir = new File(curr_directory, dirString);
        if (!dir.exists() || !dir.isDirectory()) {
            throw new RuntimeException("cd: " + dirString + " is not an existing directory");
        }
        curr_directory = dir.getCanonicalPath();
    }

    /* Prints the current working directory in jsh.*/
    public void pwd(OutputStreamWriter writer) throws IOException {
        writer.write(curr_directory);
        writer.write(System.getProperty("line.separator"));
        writer.flush();
    }

    /* Lists all files in the current directory or given argument.
       @params = app_args : arraylist containing directory to print files in or should be empty 
                            in the case where you want to print the current directory contents.
    */
    public void ls(ArrayList<String> app_args, OutputStreamWriter writer) throws IOException {
        File currDir;
        if (app_args.isEmpty()) {
            currDir = new File(curr_directory);
        } else if (app_args.size() == 1) {
            currDir = new File(app_args.get(0));
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

    /* Concatenates all contents of the files given as arguments and prints them in jsh.
       @params = app_args : contains files that need to be read.
    */
    public void cat(ArrayList<String> app_args, OutputStreamWriter writer) {
        if (app_args.isEmpty()) {
            throw new RuntimeException("cat: missing arguments");
        } else {
            for (String arg : app_args) {
                Charset encoding = StandardCharsets.UTF_8;
                File currFile = new File(curr_directory + File.separator + arg);
                if (currFile.exists()) {
                    Path filePath = Paths.get(curr_directory + File.separator + arg);
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

    /* Outputs whatever argument it is given. If output is directed towards a file it is written there else
       it is printed in jsh.
       @params = app_args : contains arguments to print. 
    */
    public void echo(ArrayList<String> app_args, OutputStreamWriter writer) throws IOException {
        boolean atLeastOnePrinted = false;
        for (String arg : app_args) {
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

    /* Outputs the first N lines in given file where N is specified in app_args. If output isnt redirected 
    it should be printed in jsh.
       @params = app_args : contains arguments to head. Should contain N and also the file to read.
    */
    public void head(ArrayList<String> app_args, OutputStreamWriter writer) {
        if (app_args.isEmpty()) {
            throw new RuntimeException("head: missing arguments");
        }
        if (app_args.size() != 1 && app_args.size() != 3) {                      
            throw new RuntimeException("head: wrong arguments");
        }
        if (app_args.size() == 3 && !app_args.get(0).equals("-n")) {
            throw new RuntimeException("head: wrong argument " + app_args.get(0));
        }
        int headLines = 10;
        String headArg;
        if (app_args.size() == 3) {
            try {
            headLines = Integer.parseInt(app_args.get(1));
            } catch (Exception e) {
            throw new RuntimeException("head: wrong argument " + app_args.get(1));
            }
            headArg = app_args.get(2);
        } else {
            headArg = app_args.get(0);
        }
        File headFile = new File(curr_directory + File.separator + headArg);
        if (headFile.exists()) {
            Charset encoding = StandardCharsets.UTF_8;
            Path filePath = Paths.get((String) curr_directory + File.separator + headArg);
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

    /* Outputs the last N lines in a given file where N is specified in app_args. If file is < N, print all lines
       without throwing an error. If output isnt redirected, print in jsh.
       @params = app_args : contains arguments to tail. Should contain N and also the file to read.
    */
    public void tail(ArrayList<String> app_args, OutputStreamWriter writer) {
        if (app_args.isEmpty()) {
            throw new RuntimeException("tail: missing arguments");
        }
        if (app_args.size() != 1 && app_args.size() != 3) {
            throw new RuntimeException("tail: wrong arguments");
        }
        if (app_args.size() == 3 && !app_args.get(0).equals("-n")) {
            throw new RuntimeException("tail: wrong argument " + app_args.get(0));
        }
        int tailLines = 10;
        String tailArg;
        if (app_args.size() == 3) {
            try {
                tailLines = Integer.parseInt(app_args.get(1));
            } catch (Exception e) {
                throw new RuntimeException("tail: wrong argument " + app_args.get(1));
            }
            tailArg = app_args.get(2);
        } else {
            tailArg = app_args.get(0);
        }
        File tailFile = new File(curr_directory + File.separator + tailArg);
        if (tailFile.exists()) {
            Charset encoding = StandardCharsets.UTF_8;
            Path filePath = Paths.get((String) curr_directory + File.separator + tailArg);
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

    /* Pattern matches the (CHECK IDK IF IT IS REGEX OR STRING) in a given file/directory. Outputs all the lines
       that contain the pattern and if the output isn't redircted, print in jsh.
       @params = app_args : contains arguments to grep. Should have a pattern and directory/file.
    */
    public void grep(ArrayList<String> app_args, OutputStreamWriter writer) {
        if (app_args.size() < 2) {
            throw new RuntimeException("grep: wrong number of arguments");
        }
        Pattern grepPattern = Pattern.compile(app_args.get(0));
        int numOfFiles = app_args.size() - 1;
        Path filePath;
        Path[] filePathArray = new Path[numOfFiles];
        Path currentDir = Paths.get(curr_directory);
        for (int i = 0; i < numOfFiles; i++) {
            filePath = currentDir.resolve(app_args.get(i + 1));
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
                            writer.write(app_args.get(j+1));
                            writer.write(":");
                        }
                        writer.write(line);
                        writer.write(System.getProperty("line.separator"));
                        writer.flush();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("grep: cannot open " + app_args.get(j + 1));
            }
      }
    } 
}
