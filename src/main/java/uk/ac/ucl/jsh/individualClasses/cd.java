package uk.ac.ucl.jsh.individualClasses;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import uk.ac.ucl.jsh.core.JshCore;

public class cd implements App {

    @Override
    public void run(JshCore core, ArrayList<String> appArgs) throws IOException {
        if (appArgs.isEmpty()) {
            core.setCurrentDirectory(core.getCurrentDirectory());
        }
        else if (appArgs.size()>1){
            throw new RuntimeException("cd: too many arguments");       
        }
        else{
            String curr_directory = core.getCurrentDirectory().toString();
            String dirString = appArgs.get(0);
            File dir = new File(curr_directory, dirString);
            if (!dir.exists() || !dir.isDirectory()) {
                throw new RuntimeException("cd: " + dirString + " is not an existing directory");
            }
            curr_directory = dir.getCanonicalPath();
            core.setCurrentDirectory(core.getCurrentDirectory());
        }
    }
}
