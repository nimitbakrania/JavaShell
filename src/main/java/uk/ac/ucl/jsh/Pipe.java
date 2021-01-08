package uk.ac.ucl.jsh;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;

import java.io.IOException;

public class Pipe implements Command {
    private Call call = new Call();
    private IoRedirection redirect = new IoRedirection();

    /**
     * If a pipe symbol is one of the arguments in appArgs, then it causes the left
     * and right sides of the cmdline to be interpreted seperately, with the output
     * of the left side becoming the input for the right side.
     * 
     * @param in      is the input stream for the left hand side of the pipe.
     * @param out     is the output stream for the right hand side of the pipe.
     * @param app1    is the app name for the app on the left hand side of the pipe.
     * @param appArgs is the arguments for the app, including the pipe symbol and
     *                the whole right hand side.
     * 
     * @exception IOException if there are any issues with the Call.eval or
     *                        IoRedirection.eval functions when they are called.
     */
    public void eval(InputStream in, OutputStream out, String app1, ArrayList<String> appArgs) throws IOException {
        PipedOutputStream outp = new PipedOutputStream();
        PipedInputStream inp = new PipedInputStream();
        outp.connect(inp);
        ArrayList<String> appArgs1 = new ArrayList<String>(appArgs.subList(0, appArgs.indexOf("|")));
        ArrayList<String> appArgs2 = new ArrayList<String>(appArgs.subList(appArgs.indexOf("|") + 2, appArgs.size()));
        String app2 = appArgs.get(appArgs.indexOf("|") + 1);
        evalThread(in, outp, app1, appArgs1);
        if (appArgs2.contains("|")){
            eval(inp, out, app2, appArgs2);    
        }
        else{
            evalThread(inp, out, app2, appArgs2);
        }
        
    }

    private void evalThread(InputStream in, OutputStream outp, String app1, ArrayList<String> appArgs1) throws IOException{
        new Thread(){
            public void run(){
                try{
                    if (appArgs1.contains(">") || appArgs1.contains("<")) {
                        redirect.eval(in, outp, app1, appArgs1);
                    } else {
                        call.eval(in, outp, app1, appArgs1);
                    }
                    outp.close();
                }
                catch (IOException e){
                    throw new RuntimeException(e);
                }
            }
        }.start();
    }     
}
