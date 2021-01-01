package uk.ac.ucl.jsh;

import java.io.OutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public interface Command {
    public void eval(InputStream cmdline, OutputStream output, String app, ArrayList<String> appArgs) throws IOException;
}
