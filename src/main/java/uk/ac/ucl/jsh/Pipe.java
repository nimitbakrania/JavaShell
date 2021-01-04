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
    private IoRedirection redirect = new IoRedirection();

    /**
     * If a pipe symbol is one of the arguments in appArgs, then it causes the left and
     * right sides of the cmdline to be interpreted seperately, with the output of the left
     * side becoming the input for the right side.
     * 
     * @param in is the input stream for the left hand side of the pipe.
     * @param out is the output stream for the right hand side of the pipe.
     * @param app1 is the app name for the app on the left hand side of the pipe.
     * @param appArgs is the arguments for the app, including the pipe symbol and the whole right hand side.
     * 
     * @exception IOException if there are any issues with the Call.eval or IoRedirection.eval functions when they are called.
    */
    public void eval(InputStream in, OutputStream out, String app1, ArrayList<String> appArgs) throws IOException {
        File tempFile = File.createTempFile("temp", null);
        InputStream inp = new FileInputStream(tempFile);
        OutputStream outp = new FileOutputStream(tempFile);
        ArrayList<String> appArgs1 = new ArrayList<String>(appArgs.subList(0, appArgs.indexOf("|")));
        ArrayList<String> appArgs2 = new ArrayList<String>(appArgs.subList(appArgs.indexOf("|") + 2, appArgs.size()));
        if (appArgs1.contains(">") || appArgs1.contains("<")){
            redirect.eval(in, outp, app1, appArgs1);
        }
        else{
            call.eval(in, outp, app1, appArgs1);
        }
        outp.close();
        String app2 = appArgs.get(appArgs.indexOf("|") + 1);
        if (appArgs2.contains("|")){
            this.eval(inp, out, app2, appArgs2);
        }
        else if (appArgs2.contains(">") || appArgs2.contains("<")){
            redirect.eval(inp, out, app2, appArgs2);
        }
        else{
            call.eval(inp, out, app2, appArgs2);
        }
        inp.close();
        tempFile.delete();
    }
}

private class P1 extends Thread {
    private PipedOutputStream outp;
    private ArrayList<String> appArgs1;
    private String app1;
    private InputStream in;

    public P1(PipedOutputStream out, ArrayList<String> appArgs, String appname, InputStream inp) {
        this.outp = out;
        this.in = inp;
        this.appArgs1 = appArgs;
        this.app1 = appname;
    }

    public void run() {
        try {
            if (appArgs1.contains(">") || appArgs1.contains("<")) {
                redirect.eval(in, outp, app1, appArgs1);
            } else {
                call.eval(in, outp, app1, appArgs1);
            }
            outp.close();
            notify();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

private class P2 extends Thread {
    private PipedInputStream inp;
    private OutputStream out;
    private ArrayList<String> appArgs2;
    private String app2;

    public P2(PipedInputStream in, ArrayList<String> appArgs, String appname, OutputStream outp) {
        this.out = outp;
        this.inp = in;
        this.appArgs2 = appArgs;
        this.app2 = appname;
    }

    public void run() {
        try {
            if (appArgs2.contains("|")) {
                eval(inp, out, app2, appArgs2);
            } else if (appArgs2.contains(">") || appArgs2.contains("<")) {
                redirect.eval(inp, out, app2, appArgs2);
            } else {
                call.eval(inp, out, app2, appArgs2);
            }
            inp.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
public void eval(InputStream in, OutputStream out, String app1, ArrayList<String> appArgs) throws IOException {
    PipedOutputStream outp = new PipedOutputStream();
    PipedInputStream inp = new PipedInputStream(outp);
    ArrayList<String> appArgs1 = new ArrayList<String>(appArgs.subList(0, appArgs.indexOf("|")));
    ArrayList<String> appArgs2 = new ArrayList<String>(appArgs.subList(appArgs.indexOf("|") + 2, appArgs.size()));
    String app2 = appArgs.get(appArgs.indexOf("|") + 1);
    P1 t1 = new P1(outp, appArgs1, app1, in);
    P2 t2 = new P2(inp, appArgs2, app2, out);
    t1.run();
    t2.run();
    try {
        while (!t1.done()){
            t2.wait();
        }
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}

