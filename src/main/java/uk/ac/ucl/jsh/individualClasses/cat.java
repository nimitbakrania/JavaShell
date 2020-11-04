package uk.ac.ucl.jsh.individualClasses;

import uk.ac.ucl.jsh.core.JshCore;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class cat implements App {
    /*
    public void run(ArrayList<String> app_args, OutputStreamWriter writer, String curr_directory) throws IOException {
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
*/

    @Override
    public void run(JshCore core, ArrayList<String> appArgs, InputStream input, OutputStream output) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
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
            performCat(appArgs, writer, core);
        }

    }
    /*
        Command line arguments passed into method and a for-each loop loops through contents of the array.
        File is created using the filename passed through command line - if file exists, contents written to the OutputStream.
        It does this for each file, resulting in a concatenation of each file.
       
    */
    private void performCat(ArrayList<String> args, BufferedWriter writer, JshCore core) throws IOException {
        String currentDirectory = core.getCurrentDirectory().toString();
        Charset encoding = StandardCharsets.UTF_8;

        for (String arg : args) {
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
}

