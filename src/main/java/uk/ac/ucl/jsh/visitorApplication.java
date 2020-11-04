package uk.ac.ucl.jsh;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;


public class visitorApplication implements baseVisitor {
    public void visit(appVisitor.cd app){
        //app function for cd
    }
    public void visit(appVisitor.pwd app);
    public void visit(appVisitor.echo app);
    public void visit(appVisitor.head app);
    public void visit(appVisitor.tail app);

    public void visit(appVisitor.cat app){
        String currentDirectory = new String(app.directory)
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(core.getOutputStream(), StandardCharsets.UTF_8));
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

    public void visit(appVisitor.ls app);
    public void visit(appVisitor.grep app);
}