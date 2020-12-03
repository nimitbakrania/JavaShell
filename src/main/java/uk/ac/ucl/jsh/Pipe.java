package uk.ac.ucl.jsh;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Pipe implements Command{
    private Call call = new Call();

    public void eval(InputStream in, OutputStream out, String currentDirectory, String app1, ArrayList<String> appArgs) throws IOException {
        File tempFile = File.createTempFile("temp", null);
        InputStream inp = new FileInputStream(tempFile);
        OutputStream outp = new FileOutputStream(tempFile);
        ArrayList<String> appArgs1 = new ArrayList<String>(appArgs.subList(0, appArgs.indexOf("|")));
        ArrayList<String> appArgs2 = new ArrayList<String>(appArgs.subList(appArgs.indexOf("|") + 2, appArgs.size()));
        call.eval(in, outp, currentDirectory, app1, appArgs1);
        outp.close();
        String app2 = appArgs.get(appArgs.indexOf("|") + 1);
        if (appArgs2.contains("|")){
            this.eval(inp, out, Jsh.getCurrentDirectory(), app2, appArgs2);
        }
        else{
            call.eval(inp, out, Jsh.getCurrentDirectory(), app2, appArgs2);
            inp.close();
        }
        tempFile.delete();
    }
}
