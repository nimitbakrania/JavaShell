package uk.ac.ucl.jsh;
import java.io.OutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class IoRedirection implements Command {

    private Call call = new Call();

    @Override
    public void eval(InputStream in, OutputStream output, String currentDirectory, String app,
            ArrayList<String> appArgs) throws IOException {
        
        if(appArgs.contains("<") && appArgs.contains(">"))
        {   
            int index = appArgs.indexOf(">");
            int index2 = appArgs.indexOf("<");
            ArrayList<String> newapparg = new ArrayList<String>();
            for(int i=0; i< index2; i++){
                newapparg.add(appArgs.get(i));
            }
            System.out.println(newapparg);
            //System.out.println(file);
            //System.out.println(file.getAbsolutePath());
            try {
                File fileInput = new File(currentDirectory + File.separator + appArgs.get(index-1));
                File fileOutput = new File(currentDirectory + File.separator + appArgs.get(index+1));
                System.out.println(fileInput);
                System.out.println(fileInput.getAbsolutePath());
                System.out.println(fileOutput);
                System.out.println(fileOutput.getAbsolutePath());
                InputStream inp = new FileInputStream(fileInput);
                OutputStream out = new FileOutputStream(fileOutput);   
                System.out.println(out);        
                call.eval(inp, System.out, currentDirectory, app, newapparg);
                out.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException("IO redirection: file not found.");
            }
        }
        else if(appArgs.contains(">") && !appArgs.contains("<")){
            int index = appArgs.indexOf(">");
            ArrayList<String> newapparg = new ArrayList<String>();
            for(int i=0; i< index; i++){
                newapparg.add(appArgs.get(i));
            }
            System.out.println(newapparg);
            File file = new File(Jsh.getCurrentDirectory() + System.getProperty("file.separator") + appArgs.get(index+1));
            try {
                OutputStream out = new FileOutputStream(file);
                call.eval(in, out, currentDirectory, app, newapparg);
                out.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException("IO redirection: file not found.");
            }
        }
        else if(appArgs.contains("<") && !appArgs.contains(">")){
            System.out.println("here2");
            ArrayList<String> al = new ArrayList<String>();
            int index = appArgs.indexOf("<");
            for(int i=index; i<appArgs.size(); i++)
            {
                al.add(appArgs.get(i));
            }
            String file1 = appArgs.get(0);
            File file = new File(Jsh.getCurrentDirectory() + System.getProperty("file.separator") + file1);
            try {
                OutputStream out = new FileOutputStream(file);
                eval(in, out, currentDirectory, app, al);
                out.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException("IO redirection: file not found.");
            }
        }
    }
}
