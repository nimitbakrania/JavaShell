package uk.ac.ucl.jsh;

import java.io.OutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Call implements Command {

    private visitorApplication visitor = new visitorApplication();

    public void eval(InputStream in, OutputStream out, String currentDirectory, String app, ArrayList<String> appArgs) {
        Factory factory = new Factory();
        UnsafeDecorator application = factory.mkApplication(in, out, currentDirectory, app, appArgs);
        application.accept(visitor);
    }

    public void eval2(InputStream in, OutputStream out, String currentDirectory, String app, ArrayList<String> appArgs)
            throws IOException {
        if(appArgs.contains(">") && appArgs.contains("<"))
        {   
            ArrayList<String> al = new ArrayList<String>();
            int index = appArgs.indexOf(">");
            for(int i=0; i<index; i++)
            {
                al.add(appArgs.get(i));
            }
            String file1 = appArgs.get(index+1);
            File file = new File(Jsh.getCurrentDirectory() + System.getProperty("file.separator") + file1);
            try {
                OutputStream output = new FileOutputStream(file);
                eval(in, output, currentDirectory, app, al);
                out.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException("IO redirection: file not found.");
            }
        }
        else if(appArgs.contains(">") && !appArgs.contains("<")){
            ArrayList<String> al = new ArrayList<String>();
            int index = appArgs.indexOf(">");
            for(int i=0; i<index; i++)
            {
                al.add(appArgs.get(i));
            }
            String file1 = appArgs.get(index+1);
            File file = new File(Jsh.getCurrentDirectory() + System.getProperty("file.separator") + file1);
            try {
                OutputStream output = new FileOutputStream(file);
                eval(in, output, currentDirectory, app, al);
                out.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException("IO redirection: file not found.");
            }
        }
        else if(appArgs.contains("<") && !appArgs.contains(">")){
            ArrayList<String> al = new ArrayList<String>();
            int index = appArgs.indexOf("<");
            for(int i=index; i<appArgs.size(); i++)
            {
                al.add(appArgs.get(i));
            }
            String file1 = appArgs.get(0);
            File file = new File(Jsh.getCurrentDirectory() + System.getProperty("file.separator") + file1);
            try {
                OutputStream output = new FileOutputStream(file);
                eval(in, output, currentDirectory, app, al);
                out.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException("IO redirection: file not found.");
            }
        }
        else{
            //
        }
    }
    
}
