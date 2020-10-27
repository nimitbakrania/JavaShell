package uk.ac.ucl.jsh.App;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import Currinterface;

public abstract class AbstractA implements funcInterface {

    Currinterface jshCore;
    private InputStream inputStream;
    private OutputStream outputStream;
    private OutputStreamWriter outputStreamWriter;

    AbstractA(Currinterface jshCore) {
        this.jshCore = jshCore;

        //Input stream is default to be Sys.in
        //Output stream is default to be Sys.out unless otherwise defined
        //Err output stream is default to be Sys.err
        inputStream = jshCore.getInputStream();
        outputStream = jshCore.getOutputStream();

        outputStreamWriter = new OutputStreamWriter(outputStream);
    }

    void writeOutputStream(String content) throws RuntimeException {
        if (content.isEmpty()) {
            return;
        }

        try {
            outputStreamWriter.write(content);
            outputStreamWriter.flush();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    //not sure if correct- currently working on 
    void writeOutputStreamLn(String content) {
        writeOutputStream(content + jshCore.getLineSeparator());
    }
    
    OutputStream getRawOutputStream() {
        return outputStream;
    }
    InputStream getRawInputStream() { return inputStream; }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
        outputStreamWriter = new OutputStreamWriter(outputStream);
    }

    public abstract void run() throws RuntimeException;
    public abstract void setArgs(String[] args) throws RuntimeException;
}