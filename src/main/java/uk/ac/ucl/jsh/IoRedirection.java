package uk.ac.ucl.jsh;
import java.io.OutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class IoRedirection implements Command {

    private Call call = new Call();
    
    public void eval(InputStream in, OutputStream output,  String app, ArrayList<String> appArgs) throws IOException {
        ArrayList<String> appArgs1 = appArgs;
        if (appArgs.contains("<")){
            int index1 = appArgs.indexOf("<");
            appArgs1 = new ArrayList<String>(appArgs.subList(0, index1));
            File inputFile = new File(Jsh.getCurrentDirectory(), appArgs.get(index1 + 1));
            in = new FileInputStream(inputFile);
            if (appArgs.contains(">")){
                int index2 = appArgs.indexOf(">");
                File outputFile = new File(Jsh.getCurrentDirectory(), appArgs.get(index2 + 1));
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
            File outputFile = new File(Jsh.getCurrentDirectory(), appArgs.get(index1 + 1));
            output = new FileOutputStream(outputFile);
            if (appArgs1.size() > index1 + 2){
                output.close();
                throw new RuntimeException("redirection: too many arguments after redirection symbol");
            }
        }
        if (appArgs1.contains(">") || appArgs1.contains("<")){
            throw new RuntimeException("redirection: too many redirection symbols");
        }
        call.eval(in, output, app, appArgs1);
        if (in != null){
            in.close();
        }
    }
}
