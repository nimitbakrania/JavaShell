package uk.ac.ucl.jsh.files;

import java.io.OutputStream;
import java.io.InputStream;

public interface Command {

    public void eval(InputStream cmdline, OutputStream output, String currentDirectory, String app, ArrayList<String> appArgs);
    
}
