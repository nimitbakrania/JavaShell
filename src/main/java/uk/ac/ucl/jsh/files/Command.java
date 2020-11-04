package uk.ac.ucl.jsh.anirudh;

import java.io.OutputStream;
import java.io.InputStream;

public interface Command {

    public void eval(InputStream cmdline, OutputStream output);
    
}
