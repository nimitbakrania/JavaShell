package uk.ac.ucl.jsh;

import java.io.OutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public interface Command {
    public void eval(InputStream cmdline, OutputStream output, String currentDirectory, String app, ArrayList<String> appArgs);
}
