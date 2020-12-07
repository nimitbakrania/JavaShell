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
    
    public void eval(InputStream in, OutputStream output, String currentDirectory, String app, ArrayList<String> appArgs) throws IOException {
        ArrayList<String> appArgs1 = appArgs;
        if (appArgs.contains("<")){
            int index1 = appArgs.indexOf("<");
            appArgs1 = new ArrayList<String>(appArgs.subList(0, index1));
            File inputFile = new File(currentDirectory, appArgs.get(index1 + 1));
            in = new FileInputStream(inputFile);
            if (appArgs.contains(">")){
                int index2 = appArgs.indexOf(">");
                File outputFile = new File(currentDirectory, appArgs.get(index2 + 1));
                output = new FileOutputStream(outputFile);
                if (appArgs1.size() > index2 + 2){
                    in.close();
                    output.close();
                    throw new RuntimeException("redirection: too many arguments after redirection symbol");
                }
            }
            else{
                if (appArgs1.size() > index1 + 2){
                    in.close();
                    throw new RuntimeException("redirection: too many arguments after redirection symbol");
                }
            }
        }
        else if(appArgs.contains(">")){
            int index1 = appArgs.indexOf(">");
            appArgs1 = new ArrayList<String>(appArgs.subList(0, index1));
            File outputFile = new File(currentDirectory, appArgs.get(index1 + 1));
            output = new FileOutputStream(outputFile);
            if (appArgs1.size() > index1 + 2){
                output.close();
                throw new RuntimeException("redirection: too many arguments after redirection symbol");
            }
        }
        if (appArgs1.contains(">") || appArgs1.contains("<")){
            throw new RuntimeException("redirection: too many redirection symbols");
        }
        call.eval(in, output, currentDirectory, app, appArgs1);
        in.close();
        output.close();
    }
    
    /*
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
    */
}
