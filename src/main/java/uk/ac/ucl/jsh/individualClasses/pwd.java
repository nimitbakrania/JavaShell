package uk.ac.ucl.jsh.individualClasses;

import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import java.util.ArrayList;

import uk.ac.ucl.jsh.core.JshCore;

public class pwd implements App {
    /*
    public void run(OutputStreamWriter writer, String curr_directory) throws IOException {

        writer.write(curr_directory);
        writer.write(System.getProperty("line.separator"));
        writer.flush();
    }
    */

    @Override
    public void run(JshCore core, ArrayList<String> appArgs, InputStream input, OutputStream output) throws IOException {
        if(appArgs.isEmpty()){
            throw new RuntimeException("pwd: too many arguments");
        }
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
        writer.write(core.getCurrentDirectory().toString());
        writer.write(System.getProperty("line.separator"));
        writer.flush();
    }
}
