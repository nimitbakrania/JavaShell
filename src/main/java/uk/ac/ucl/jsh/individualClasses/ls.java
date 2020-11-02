package uk.ac.ucl.jsh.individualClasses;
import uk.ac.ucl.jsh.core.JshCore;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class ls implements App {

    @Override
    public void run(JshCore core, ArrayList<String> appArgs, InputStream input, OutputStream output)
            throws IOException {
        String currentDirectory = core.getCurrentDirectory().toString();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        File currDir = validateArgs(appArgs, currentDirectory);
        writeOutput(currDir, writer);

    }
    /*
     * Method gets array of files in specified directory and writes names of files
     * not beginning with '.' to OutputStream.
     */
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
}
